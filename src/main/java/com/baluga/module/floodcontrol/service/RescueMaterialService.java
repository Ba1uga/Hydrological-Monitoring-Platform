package com.baluga.module.floodcontrol.service;

import com.baluga.module.floodcontrol.pojo.RescueMaterial;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface RescueMaterialService extends IService<RescueMaterial> {

    /**
     * 获取防汛物资数据
     * @return 按分类分组的物资列表
     */
    List<Map<String, Object>> getFloodMaterials();

    /**
     * 获取抗旱物资数据
     * @return 按分类分组的物资列表
     */
    List<Map<String, Object>> getDroughtMaterials();

    /**
     * 获取指定分类的详细物资列表
     * @param mode 模式 (flood/drought)
     * @param categoryName 分类名称
     * @return 详细数据
     */
    Map<String, Object> getMaterialDetails(String mode, String categoryName);
}
