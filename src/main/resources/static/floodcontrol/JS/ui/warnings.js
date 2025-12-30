/* =================== 预警信息 =================== */
/* 左侧预警信息滚动 */

const defaultWarningMessages = [
  '【预警】太湖吴江站水位14.41m超警（警戒13.80m），仍在上涨，请注意防范！',
  '【预警】海河天津站水位15.67m超警（警戒15.00m），河道行洪压力增大。',
  '【预警】钱塘江杭州站水位17.17m超警（警戒16.50m），请加强堤防巡查。',
  '【预警】洪泽湖蒋坝站水位23.21m超警（警戒22.50m），沿湖低洼地带注意排涝。',
  '【预警】东江博罗站水位30.59m超警（警戒30.00m），下游城区注意防汛。',
  '【预警】三峡水库-土壤湿度32.0%，特旱风险突出，建议提前落实抗旱保供措施。',
  '【预警】丹江口水库-土壤湿度36.0%，特旱风险突出，建议加强水源调度与节水管理。',
  '【预警】洞庭湖城陵矶站-土壤湿度32.0%，特旱风险突出，注意农业灌溉保障。',
  '【预警】鄱阳湖湖口站-土壤湿度36.0%，特旱风险突出，关注供水与生态补水。',
  '【预警】小浪底水库-土壤湿度32.0%，特旱风险突出，关注旱情发展与用水需求。',
  '【预警】丰满水库-土壤湿度32.0%，特旱风险突出，注意蓄水保供与调度。',
  '【预警】河南郑州监测站湿度29.7%逼近预警线（30%），重旱风险加剧。',
  '【预警】新疆哈密监测站湿度29.6%显著下降，重旱风险加剧，建议落实节水措施。',
  '【灾情上报】广东省部分地区遭遇暴雨袭击，已造成12人受灾。',
  '【预案启动】启动防汛Ⅲ级应急响应。',
  '【提示】系统按整点自动更新站点数据与风险等级。'
];

function renderWarningMessages(warningScroll, messages) {
  if (!warningScroll) return;
  warningScroll.innerHTML = '';
  const fragment = document.createDocumentFragment();
  messages.forEach(text => {
    const item = document.createElement('div');
    item.className = 'warning-item';
    item.textContent = String(text);
    fragment.appendChild(item);
  });
  warningScroll.appendChild(fragment);
}

// 初始化左侧预警信息滚动
async function initWarningScroll() {
  const warningScroll = document.getElementById('warningScroll');
  if (!warningScroll) return;

  renderWarningMessages(warningScroll, defaultWarningMessages);

  if (warningScroll.__warningAutoScrollStarted) return;
  warningScroll.__warningAutoScrollStarted = true;

  let scrollPosition = 0;

  function scrollWarning() {
    scrollPosition += 1;
    if (scrollPosition > warningScroll.scrollHeight - warningScroll.clientHeight) {
      scrollPosition = 0;
    }
    warningScroll.scrollTop = scrollPosition;
    setTimeout(scrollWarning, 50);
  }

  scrollWarning();
}

// 导出函数
export { initWarningScroll };
