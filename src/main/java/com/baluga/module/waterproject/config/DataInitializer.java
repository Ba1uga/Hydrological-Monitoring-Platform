package com.baluga.module.waterproject.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.baluga.module.waterproject.entity.MajorRiver;
import com.baluga.module.waterproject.entity.WaterProject;
import com.baluga.module.waterproject.repository.MajorRiverRepository;
import com.baluga.module.waterproject.repository.WaterProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final WaterProjectRepository waterProjectRepository;
    private final MajorRiverRepository majorRiverRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        try {
            if (waterProjectRepository.count() == 0) {
                initWaterProjects();
            }
            if (majorRiverRepository.count() == 0) {
                initMajorRivers();
            }
        } catch (RuntimeException ignored) {
            return;
        }
    }

    private void initWaterProjects() throws Exception {
        List<WaterProject> projects = Arrays.asList(
            createProject(1L, "三峡水利枢纽", "大型枢纽", 
                Arrays.asList("大型枢纽", "水力发电", "防洪", "航运"),
                "湖北省宜昌市", "长江", "1994-2009", "393亿m³", "22500 MW",
                "185 m", "2309 m", "2039亿元", "1000亿kWh",
                "世界最大的水利枢纽工程，具有防洪、发电、航运、水资源利用等综合效益。大坝为混凝土重力坝，坝顶高程185米，正常蓄水位175米，防洪库容221.5亿立方米，总装机容量2250万千瓦，年发电量约1000亿千瓦时。",
                "三峡工程是中国水利史上的里程碑，彻底解决了长江中下游的防洪问题，年发电量相当于减少燃煤5000万吨，极大地改善了长江航运条件，综合效益显著。",
                "#0056b3", "三", 111.003, 30.822,
                "https://picsum.photos/id/1036/400/267", "https://picsum.photos/id/1036/800/533",
                393.0, 22500.0, 185.0, 2309.0, 15, 95),

            createProject(2L, "小浪底水库", "大型枢纽",
                Arrays.asList("大型枢纽", "防洪", "减淤", "发电"),
                "河南省洛阳市", "黄河", "1994-2001", "126.5亿m³", "1800 MW",
                "154 m", "1667 m", "347亿元", "51亿kWh",
                "黄河干流关键性控制工程，具有防洪、防凌、减淤、供水、灌溉、发电等综合效益。工程有效控制了黄河洪水，使黄河下游防洪标准由六十年一遇提高到千年一遇。",
                "小浪底水库是黄河治理的关键工程，有效缓解了黄河下游的防洪压力，改善了黄河的泥沙淤积状况，保障了黄河中下游的供水安全。",
                "#0056b3", "小", 112.367, 34.917,
                "https://picsum.photos/id/1043/400/267", "https://picsum.photos/id/1043/800/533",
                126.5, 1800.0, 154.0, 1667.0, 7, 85),

            createProject(3L, "南水北调中线工程", "供水工程",
                Arrays.asList("供水工程", "调水"),
                "湖北至北京沿线", "汉江、长江", "2003-2014", "调水95亿m³/年", "调水工程",
                "渠首176.6 m", "1432 km", "2013亿元", "0",
                "世界最大调水工程，从长江支流汉江中上游的丹江口水库引水，向河南、河北、北京、天津四省市供水，有效缓解了华北地区水资源短缺问题。",
                "南水北调工程是中国水资源配置的战略性工程，彻底改变了华北地区长期缺水的局面，保障了京津冀城市群的供水安全，促进了区域经济社会可持续发展。",
                "#0096ff", "南", 112.650, 32.650,
                "https://picsum.photos/id/1048/400/267", "https://picsum.photos/id/1048/800/533",
                95.0, 0.0, 176.6, 1432.0, 11, 90),

            createProject(4L, "丹江口水库", "大型枢纽",
                Arrays.asList("大型枢纽", "供水", "防洪", "发电"),
                "湖北省丹江口市", "汉江", "1958-1973", "290.5亿m³", "900 MW",
                "176.6 m", "2494 m", "10.8亿元", "38.3亿kWh",
                "南水北调中线工程水源地，汉江流域关键性控制工程，具有防洪、发电、灌溉、航运、养殖等综合效益。",
                "丹江口水库是南水北调中线工程的核心水源地，在防洪、发电、供水等方面发挥着重要作用，是保障京津冀地区供水安全的关键节点。",
                "#0056b3", "丹", 111.500, 32.533,
                "https://picsum.photos/id/1051/400/267", "https://picsum.photos/id/1051/800/533",
                290.5, 900.0, 176.6, 2494.0, 15, 80),

            createProject(5L, "白鹤滩水电站", "水力发电",
                Arrays.asList("水力发电", "大型枢纽", "清洁能源"),
                "四川省宁南县", "金沙江", "2013-2022", "206亿m³", "16000 MW",
                "289 m", "709 m", "1780亿元", "624亿kWh",
                "世界第二大水电站，装机规模仅次于三峡工程。采用双曲拱坝设计，是世界首座全坝采用低热水泥混凝土浇筑的特高拱坝，多项技术指标创世界纪录。",
                "白鹤滩水电站是中国'西电东送'骨干电源点，对优化能源结构、促进节能减排、实现'双碳'目标具有重要意义，代表了世界水电技术的最高水平。",
                "#4dabf7", "白", 102.967, 27.167,
                "https://picsum.photos/id/1060/400/267", "https://picsum.photos/id/1060/800/533",
                206.0, 16000.0, 289.0, 709.0, 9, 92),

            createProject(6L, "溪洛渡水电站", "水力发电",
                Arrays.asList("水力发电", "大型枢纽", "双曲拱坝"),
                "四川省雷波县", "金沙江", "2005-2015", "126.7亿m³", "13860 MW",
                "285.5 m", "698 m", "792亿元", "571亿kWh",
                "中国第二大、世界第三大水电站，双曲拱坝高度居世界第三位。工程以防洪、发电为主，兼有改善下游航运、拦沙等综合效益。",
                "溪洛渡水电站是'西电东送'中部通道的骨干电源点，对保障华东、华中地区电力供应，优化能源结构，促进长江流域防洪体系建设具有重要作用。",
                "#4dabf7", "溪", 103.583, 28.233,
                "https://picsum.photos/id/1063/400/267", "https://picsum.photos/id/1063/800/533",
                126.7, 13860.0, 285.5, 698.0, 10, 88),

            createProject(7L, "大藤峡水利枢纽", "大型枢纽",
                Arrays.asList("大型枢纽", "防洪", "发电", "航运"),
                "广西桂平市", "珠江", "2015-2023", "34.3亿m³", "1600 MW",
                "47.5 m", "1038 m", "357亿元", "61亿kWh",
                "珠江流域关键控制性工程，具有防洪、发电、航运、灌溉、水资源配置等综合效益，是粤港澳大湾区重要水安全保障工程。",
                "大藤峡水利枢纽是珠江流域防洪体系的重要组成部分，可有效调控西江洪水，保障粤港澳大湾区供水安全，改善西江航运条件，促进流域经济社会发展。",
                "#0056b3", "大", 109.900, 23.383,
                "https://picsum.photos/id/1069/400/267", "https://picsum.photos/id/1069/800/533",
                34.3, 1600.0, 47.5, 1038.0, 8, 75),

            createProject(8L, "葛洲坝水利枢纽", "大型枢纽",
                Arrays.asList("大型枢纽", "水力发电", "航运", "通航"),
                "湖北省宜昌市", "长江", "1970-1988", "15.8亿m³", "2715 MW",
                "47 m", "2595 m", "48.5亿元", "157亿kWh",
                "长江干流上第一座大型水电站，中国自行设计、施工、制造安装的大型水利枢纽工程。设有三座船闸，大大改善了长江航运条件。",
                "葛洲坝工程是中国水电建设的里程碑，为三峡工程建设积累了宝贵经验，提供了强大的技术支撑，被誉为'中国水电的摇篮'。",
                "#0056b3", "葛", 111.270, 30.740,
                "https://picsum.photos/id/1081/400/267", "https://picsum.photos/id/1081/800/533",
                15.8, 2715.0, 47.0, 2595.0, 18, 78),

            createProject(9L, "引滦入津工程", "供水工程",
                Arrays.asList("供水工程", "调水", "城市供水"),
                "河北省迁西县至天津", "滦河", "1982-1983", "调水10亿m³/年", "供水工程",
                "引水线路", "234 km", "11.3亿元", "0",
                "中国第一个大型跨流域调水工程，将滦河水引入天津，解决了天津市的严重缺水问题，被誉为'天津的生命线'。",
                "引滦入津工程从根本上解决了天津的城市供水问题，保障了天津经济社会发展和人民生活用水需求，是中国跨流域调水的成功范例。",
                "#0096ff", "引", 117.500, 40.000,
                "https://picsum.photos/id/1083/400/267", "https://picsum.photos/id/1083/800/533",
                10.0, 0.0, 0.0, 234.0, 1, 70),

            createProject(10L, "淮河入海水道工程", "防洪工程",
                Arrays.asList("防洪工程", "河道整治", "防洪减灾"),
                "江苏省淮安市", "淮河", "1999-2006", "泄洪2270 m³/s", "防洪工程",
                "堤防", "163.5 km", "41.2亿元", "0",
                "淮河流域防洪体系的骨干工程，使淮河有了独立的入海通道，结束了淮河800多年没有独立入海通道的历史，大大提高了淮河下游防洪标准。",
                "淮河入海水道工程是淮河流域防洪的关键性工程，有效解决了淮河洪水出路问题，保障了淮河下游2000多万人口和3000多万亩耕地的防洪安全。",
                "#ff9800", "淮", 119.150, 33.500,
                "https://picsum.photos/id/1086/400/267", "https://picsum.photos/id/1086/800/533",
                0.0, 0.0, 0.0, 163.5, 7, 72)
        );

        waterProjectRepository.saveAll(projects);
        System.out.println("水利工程数据初始化完成，共 " + projects.size() + " 条记录");
    }

    private WaterProject createProject(Long id, String name, String type, List<String> tags,
            String location, String river, String builtYear, String capacity, String power,
            String height, String length, String investment, String annualPower,
            String description, String importance, String color, String icon,
            Double lng, Double lat, String imgUrl, String largeImgUrl,
            Double chartCapacity, Double chartPower, Double chartHeight, Double chartLength,
            Integer chartYearRange, Integer chartImportance) throws Exception {
        
        WaterProject project = new WaterProject();
        project.setName(name);
        project.setType(type);
        project.setTags(objectMapper.writeValueAsString(tags));
        project.setLocation(location);
        project.setRiver(river);
        project.setBuiltYear(builtYear);
        project.setCapacity(capacity);
        project.setPower(power);
        project.setHeight(height);
        project.setLength(length);
        project.setInvestment(investment);
        project.setAnnualPower(annualPower);
        project.setDescription(description);
        project.setImportance(importance);
        project.setColor(color);
        project.setIcon(icon);
        project.setCoordinateLng(lng);
        project.setCoordinateLat(lat);
        project.setImgUrl(imgUrl);
        project.setLargeImgUrl(largeImgUrl);
        project.setChartCapacity(chartCapacity);
        project.setChartPower(chartPower);
        project.setChartHeight(chartHeight);
        project.setChartLength(chartLength);
        project.setChartYearRange(chartYearRange);
        project.setChartImportance(chartImportance);
        return project;
    }

    private void initMajorRivers() throws Exception {
        List<MajorRiver> rivers = Arrays.asList(
            createRiver("长江", new Double[][]{{91.1, 35.0}, {94.5, 33.5}, {100.0, 32.0}, {103.0, 30.0}, {104.5, 29.0}, {106.5, 28.0}, {108.0, 29.0}, {110.0, 30.0}, {112.0, 30.5}, {114.0, 30.0}, {116.0, 30.5}, {118.0, 31.5}, {120.0, 31.5}, {121.5, 31.0}}, "#00ff88", 4),
            createRiver("黄河", new Double[][]{{95.8, 36.0}, {97.5, 34.5}, {100.0, 36.0}, {102.0, 36.5}, {104.0, 36.0}, {106.5, 34.5}, {108.5, 34.0}, {110.0, 35.0}, {111.5, 36.5}, {112.5, 36.0}, {113.5, 35.0}, {114.5, 34.0}, {116.0, 33.0}, {117.5, 35.0}, {118.5, 37.5}, {119.5, 38.0}}, "#ffd700", 3),
            createRiver("珠江", new Double[][]{{104.0, 25.0}, {106.0, 24.0}, {108.0, 23.5}, {110.0, 22.5}, {112.0, 22.0}, {113.5, 23.0}, {115.0, 22.5}}, "#4dabf7", 3),
            createRiver("淮河", new Double[][]{{113.5, 32.0}, {115.0, 32.5}, {116.5, 32.0}, {118.0, 32.5}, {119.5, 33.5}, {120.5, 34.0}}, "#ff9800", 2),
            createRiver("汉江", new Double[][]{{107.5, 33.5}, {109.0, 32.5}, {110.5, 31.5}, {112.0, 30.5}, {113.0, 30.0}}, "#80d4ff", 2),
            createRiver("金沙江", new Double[][]{{91.0, 33.0}, {94.0, 30.0}, {97.0, 28.0}, {100.0, 26.5}, {102.5, 26.0}, {104.5, 26.5}}, "#9c27b0", 3),
            createRiver("滦河", new Double[][]{{115.5, 41.5}, {117.0, 40.5}, {118.5, 39.5}, {119.5, 39.0}}, "#0096ff", 2)
        );

        majorRiverRepository.saveAll(rivers);
        System.out.println("河流数据初始化完成，共 " + rivers.size() + " 条记录");
    }

    private MajorRiver createRiver(String name, Double[][] points, String color, Integer width) throws Exception {
        MajorRiver river = new MajorRiver();
        river.setName(name);
        river.setPoints(objectMapper.writeValueAsString(points));
        river.setColor(color);
        river.setWidth(width);
        return river;
    }
}
