package com.baluga.module.floodcontrol.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baluga.module.floodcontrol.mapper.RescueMaterialCategoryMapper;
import com.baluga.module.floodcontrol.mapper.RescueMaterialMapper;
import com.baluga.module.floodcontrol.pojo.RescueMaterial;
import com.baluga.module.floodcontrol.pojo.RescueMaterialCategory;
import com.baluga.module.floodcontrol.service.RescueMaterialService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RescueMaterialServiceImpl extends ServiceImpl<RescueMaterialMapper, RescueMaterial> implements RescueMaterialService {

    private final RescueMaterialCategoryMapper categoryMapper;

    @Override
    public List<Map<String, Object>> getFloodMaterials() {
        return getMaterialsByMode("FLOOD");
    }

    @Override
    public List<Map<String, Object>> getDroughtMaterials() {
        return getMaterialsByMode("DROUGHT");
    }

    @Override
    @Cacheable(value = "materialDetails", key = "#mode + ':' + #categoryName", unless = "#result == null")
    public Map<String, Object> getMaterialDetails(String mode, String categoryName) {
        String modeType = "flood".equalsIgnoreCase(mode) ? "FLOOD" : "DROUGHT";
        
        // 1. 查询一级分类信息
        RescueMaterialCategory category = categoryMapper.selectOne(new LambdaQueryWrapper<RescueMaterialCategory>()
                .eq(RescueMaterialCategory::getModeType, modeType)
                .eq(RescueMaterialCategory::getName, categoryName)
                .eq(RescueMaterialCategory::getParentId, 0));
        
        if (category == null) {
            return null;
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", category.getId());
        result.put("title", category.getName());
        result.put("description", category.getDescription());
        result.put("updateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        result.put("status", "正常"); // 聚合状态，简化处理
        
        // 2. 查询该分类下的所有子分类
        List<RescueMaterialCategory> subCategories = categoryMapper.selectList(new LambdaQueryWrapper<RescueMaterialCategory>()
                .eq(RescueMaterialCategory::getModeType, modeType)
                .eq(RescueMaterialCategory::getParentId, category.getId()));
        
        List<Long> categoryIds = new ArrayList<>();
        // 包含一级分类本身（虽然一般物资挂在二级，但逻辑上可能挂在一级）
        categoryIds.add(category.getId()); 
        if (!subCategories.isEmpty()) {
            categoryIds.addAll(subCategories.stream().map(RescueMaterialCategory::getId).collect(Collectors.toList()));
        }
        
        // 3. 查询物资详情
        List<RescueMaterial> materials = this.list(new LambdaQueryWrapper<RescueMaterial>()
                .in(RescueMaterial::getCategoryId, categoryIds)
                .eq(RescueMaterial::getStatus, 1)); // 只查正常状态
        
        // 4. 组装物资列表 items (进行聚合去重)
        Map<String, Map<String, Object>> aggregatedItems = new LinkedHashMap<>();
        
        for (RescueMaterial m : materials) {
            String key = m.getName() + "##" + (m.getSpecification() == null ? "" : m.getSpecification());
            
            if (aggregatedItems.containsKey(key)) {
                Map<String, Object> item = aggregatedItems.get(key);
                BigDecimal currentVal = (BigDecimal) item.get("value");
                item.put("value", currentVal.add(m.getTotalQuantity()));
                
                // 聚合位置信息 (去重)
                String currentLoc = (String) item.get("location");
                String newLoc = m.getLocation();
                if (newLoc != null && !newLoc.isEmpty()) {
                    if (currentLoc == null || currentLoc.isEmpty()) {
                         item.put("location", newLoc);
                    } else if (!currentLoc.contains(newLoc)) {
                         item.put("location", currentLoc + ", " + newLoc);
                    }
                }
            } else {
                Map<String, Object> item = new HashMap<>();
                item.put("id", m.getId());
                item.put("name", m.getName());
                item.put("value", m.getTotalQuantity());
                item.put("unit", m.getUnit());
                item.put("spec", m.getSpecification());
                item.put("location", m.getLocation() == null ? "" : m.getLocation());
                aggregatedItems.put(key, item);
            }
        }
        
        List<Map<String, Object>> items = new ArrayList<>(aggregatedItems.values());
        
        result.put("items", items);
        
        // 计算总指标
        BigDecimal totalCount = materials.stream()
                .map(RescueMaterial::getTotalQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("totalCount", totalCount);
        
        return result;
    }

    private List<Map<String, Object>> getMaterialsByMode(String modeType) {
        // 1. 查询该模式下的一级分类
        List<RescueMaterialCategory> categories = categoryMapper.selectList(new LambdaQueryWrapper<RescueMaterialCategory>()
                .eq(RescueMaterialCategory::getModeType, modeType)
                .eq(RescueMaterialCategory::getParentId, 0));

        List<Map<String, Object>> result = new ArrayList<>();

        for (RescueMaterialCategory category : categories) {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("label", category.getName());
            categoryData.put("unit", getUnitForCategory(category.getName())); // 根据分类名称推断单位
            
            // 2. 查询该分类下的所有子分类ID
            List<RescueMaterialCategory> subCategories = categoryMapper.selectList(new LambdaQueryWrapper<RescueMaterialCategory>()
                    .eq(RescueMaterialCategory::getModeType, modeType)
                    .eq(RescueMaterialCategory::getParentId, category.getId()));
            
            List<Long> categoryIds = new ArrayList<>();
            categoryIds.add(category.getId());
            if (!subCategories.isEmpty()) {
                categoryIds.addAll(subCategories.stream().map(RescueMaterialCategory::getId).collect(Collectors.toList()));
            }

            // 3. 统计该分类下的物资总数
            List<RescueMaterial> materials = this.list(new LambdaQueryWrapper<RescueMaterial>()
                    .in(RescueMaterial::getCategoryId, categoryIds)
                    .eq(RescueMaterial::getStatus, 1)); // 只统计状态正常的

            BigDecimal totalValue = materials.stream()
                    .map(RescueMaterial::getTotalQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            categoryData.put("value", totalValue);
            
            // Calculate Progress (Availability Rate)
            int progress = 0;
            if (totalValue.compareTo(BigDecimal.ZERO) > 0) {
                // Calculate total available
                BigDecimal totalAvailable = materials.stream()
                        .map(RescueMaterial::getAvailableQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                progress = totalAvailable.divide(totalValue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).intValue();
            }
            categoryData.put("progress", progress);
            
            // Set Color based on category (keeping existing aesthetic)
            String color = getProgressColor(category.getName(), modeType);
            categoryData.put("progressColor", color);
            
            // 生成 Tooltip 内容 (聚合去重)
            Map<String, BigDecimal> tooltipMap = new LinkedHashMap<>();
            // 用于记录单位的辅助Map
            Map<String, String> unitMap = new HashMap<>();
            
            for (RescueMaterial m : materials) {
                String name = m.getName();
                tooltipMap.merge(name, m.getTotalQuantity(), BigDecimal::add);
                unitMap.putIfAbsent(name, m.getUnit());
            }

            String tooltip = tooltipMap.entrySet().stream()
                    .limit(3)
                    .map(entry -> entry.getKey() + entry.getValue().intValue() + unitMap.get(entry.getKey()))
                    .collect(Collectors.joining("、"));
            categoryData.put("tooltip", tooltip.isEmpty() ? "暂无明细" : tooltip);

            result.add(categoryData);
        }

        return result;
    }

    private String getUnitForCategory(String categoryName) {
        if (categoryName.contains("车") || categoryName.contains("泵")) return "台/辆";
        if (categoryName.contains("箱") || categoryName.contains("水")) return "箱/吨";
        return "件/套";
    }

    private String getProgressColor(String name, String mode) {
        if ("FLOOD".equals(mode)) {
            if (name.contains("救生") || name.contains("生活")) {
                return "var(--highlight-color)";
            } else if (name.contains("排水")) {
                return "#00e676";
            } else {
                return "#ff9800";
            }
        } else {
            if (name.contains("找水") || name.contains("生产")) {
                return "var(--highlight-color)";
            } else if (name.contains("节水")) {
                return "#00e676";
            } else {
                return "#ff9800";
            }
        }
    }
}
