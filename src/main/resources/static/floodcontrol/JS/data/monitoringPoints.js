/* =================== 监测点数据 =================== */
/* 所有监测点数据，包含 floodRisk/droughtRisk */

// 导出所有监测点数据
// floodRisk / droughtRisk 取值统一为：extreme / high / medium / low / safe
const allMonitoringPoints = [
  // 长江流域及重要水库
  { name: '长江武汉站', value: [114.305429, 30.592849, 23.56], type: 'river', floodRisk: 'extreme', droughtRisk: 'low' },
  { name: '洞庭湖城陵矶站', value: [113.136554, 29.443755, 24.76], type: 'lake', floodRisk: 'high', droughtRisk: 'medium' },
  { name: '鄱阳湖九江站', value: [115.989639, 29.735502, 19.21], type: 'lake', floodRisk: 'medium', droughtRisk: 'medium' },
  { name: '三峡水库', value: [110.848368, 30.856651, 173.2], type: 'reservoir', floodRisk: 'high', droughtRisk: 'safe' },
  { name: '新安江水库', value: [119.014587, 29.531589, 105.8], type: 'reservoir', floodRisk: 'medium', droughtRisk: 'low' },

  // 黄河流域
  { name: '黄河郑州站', value: [113.625368, 34.746599, 12.34], type: 'river', floodRisk: 'high', droughtRisk: 'extreme' },
  { name: '黄河兰州站', value: [103.834303, 36.061101, 10.12], type: 'river', floodRisk: 'medium', droughtRisk: 'high' },
  { name: '黄河银川站', value: [106.258033, 38.468068, 9.80], type: 'river', floodRisk: 'low', droughtRisk: 'high' },

  // 珠江流域
  { name: '珠江广州站', value: [113.280637, 23.125178, 8.78], type: 'river', floodRisk: 'medium', droughtRisk: 'safe' },
  { name: '西江梧州站', value: [111.279115, 23.476963, 7.32], type: 'river', floodRisk: 'low', droughtRisk: 'safe' },

  // 华东地区
  { name: '太湖苏州站', value: [120.585316, 31.298845, 4.23], type: 'lake', floodRisk: 'low', droughtRisk: 'medium' },
  { name: '钱塘江杭州站', value: [120.15507, 30.274084, 5.67], type: 'river', floodRisk: 'medium', droughtRisk: 'low' },

  // 西北华北部分监测站（偏旱区）
  { name: '陕西西安监测站', value: [108.939761, 34.341568, 28], type: 'monitoring', floodRisk: 'safe', droughtRisk: 'high' },
  { name: '甘肃兰州监测站', value: [103.834303, 36.061101, 22], type: 'monitoring', floodRisk: 'safe', droughtRisk: 'medium' },
  { name: '宁夏银川监测站', value: [106.258033, 38.468068, 40], type: 'monitoring', floodRisk: 'low', droughtRisk: 'extreme' },
  { name: '新疆乌鲁木齐监测站', value: [87.616848, 43.825592, 12], type: 'monitoring', floodRisk: 'safe', droughtRisk: 'high' },

  // 华南与西南部分监测站（降雨充沛，旱情较轻）
  { name: '广西南宁监测站', value: [108.366543, 22.817002, 18], type: 'monitoring', floodRisk: 'medium', droughtRisk: 'safe' },
  { name: '云南昆明监测站', value: [102.712251, 25.040609, 15], type: 'monitoring', floodRisk: 'low', droughtRisk: 'low' }
];

// 导出监测点数据
export default allMonitoringPoints;
