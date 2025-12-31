option1 = {
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        y: "top",
        x: "center",
        itemWidth: 10,
        itemHeight: 10,
        textStyle: {
            color: "rgba(255,255,255,.6)"
        },
        data: ['长江', '黄河', '淮河', '珠江']
    },
    grid: {
        left: '0%',
        top: '30',
        right: '0%',
        bottom: '0',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: [],
        axisLine: {
            show: true,
            lineStyle: {
                color: "rgba(255,255,255,.1)"
            }
        },
        axisTick: { show: false },
        axisLabel: {
            textStyle: {
                color: 'rgba(255,255,255,.4)',
                fontSize: 12
            }
        }
    },
    yAxis: {
        type: 'value',
        name: '来水量 (m³/s)',
        nameTextStyle: {
            color: 'rgba(255,255,255,.6)'
        },
        splitNumber: 4,
        axisLine: { show: false },
        axisTick: { show: false },
        splitLine: {
            show: true,
            lineStyle: {
                color: 'rgba(255,255,255,0.05)'
            }
        },
        axisLabel: {
            textStyle: {
                color: "rgba(255,255,255,.4)",
                fontSize: 12
            }
        }
    },
    series: [
        {
            name: '长江',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 0,
            itemStyle: { normal: { color: "#5470c6" } },
            data: []
        },
        {
            name: '黄河',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 0,
            itemStyle: { normal: { color: "#ee6666" } },
            data: []
        },
        {
            name: '淮河',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 0,
            itemStyle: { normal: { color: "#fac858" } },
            data: []
        },
        {
            name: '珠江',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 0,
            itemStyle: { normal: { color: "#91cc75" } },
            data: []
        }
    ]
};


option2 = {
    legend: {
        orient: "vertical",
        y: "center",
        x: "left",
        itemWidth: 10,
        itemHeight: 10,
        textStyle: {
            color: "rgba(255,255,255,.6)"
        },
    },

    // tooltip: {
    //     trigger: 'item',
    //     formatter: "{b} : {c} ({d}%)"
    // },


    series: [{
        type: 'pie',
        radius: ['10%', '60%'],
        center: ['50%', '50%'],
        roseType:"area",
        // color: ['#ffe600', '#00c56c', '#0089ff'],
        data: [],
        label: {
            normal: {
                formatter: ['{c|{c}}', '{b|{b}}'].join('\n'),
                rich: {
                    c: {
                        color: 'rgb(241,246,104)',
                        fontSize: 16,
                        fontWeight: 'bold',
                        lineHeight: 5
                    },
                    b: {
                        color: 'rgb(98,137,169)',
                        fontSize: 12,
                        height: 40
                    },
                },
            }
        },

        itemStyle: {
            borderRadius: 5,
        }
    }]
};

// 全国主要河流水文特征（用于柱状图悬浮提示）
// 注：以下数值为“约值”，用于可视化展示与对比（可按你的权威数据源进一步替换）
const riverHydro = {
    "长江": {
        length: "约6300 km",
        basin: "约180万 km²",
        annualRunoff: "约9600亿 m³/年",
        meanDischarge: "约30000 m³/s",
        tributaries: "嘉陵江、乌江、汉江、湘江、赣江等"
    },
    "黄河": {
        length: "约5464 km",
        basin: "约79.5万 km²",
        annualRunoff: "约580亿 m³/年",
        meanDischarge: "约1800 m³/s",
        tributaries: "渭河、汾河、洮河、湟水等"
    },
    "珠江": {
        length: "约2214 km（以西江为主干）",
        basin: "约45.3万 km²",
        annualRunoff: "约3300亿 m³/年",
        meanDischarge: "约10000 m³/s（口门附近量级）",
        tributaries: "西江、北江、东江等"
    },
    "黑龙江": {
        length: "约4440 km",
        basin: "约185万 km²",
        annualRunoff: "约（量级）数千亿 m³/年",
        meanDischarge: "约（量级）10000 m³/s",
        tributaries: "松花江、乌苏里江、结雅河等"
    },
    "松花江": {
        length: "约2308 km",
        basin: "约55.7万 km²",
        annualRunoff: "约（量级）数百亿~千亿 m³/年",
        meanDischarge: "约（量级）2000~5000 m³/s",
        tributaries: "嫩江、第二松花江等"
    },
    "雅鲁藏布江": {
        length: "约2840 km（境内为主）",
        basin: "约24万 km²（境内量级）",
        annualRunoff: "约（量级）千亿 m³/年",
        meanDischarge: "约（量级）5000~10000 m³/s",
        tributaries: "尼洋河、帕隆藏布等"
    },
    "澜沧江": {
        length: "约4350 km（全长，出境后称湄公河）",
        basin: "约81万 km²（全流域量级）",
        annualRunoff: "约（量级）数千亿 m³/年",
        meanDischarge: "约（量级）15000 m³/s（下游量级）",
        tributaries: "南捧河、南卡河、漾濞江等（境内）"
    },
    "淮河": {
        length: "约1000 km",
        basin: "约27万 km²",
        annualRunoff: "约（量级）数百亿 m³/年",
        meanDischarge: "约（量级）1000~3000 m³/s",
        tributaries: "颍河、涡河、洪泽湖水系等"
    }
};

option4 = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            lineStyle: {
                color: '#dddc6b'
            }
        }
    },
    grid: {
        left: '0',
        top: '30',
        right: '30',
        bottom: '25',
        containLabel: true
    },

    xAxis: {
        type: 'category',
        //  boundaryGap: false,
        axisTick:{show:false},
        axisLabel:  {
            interval: 0,
            rotate: 0,
            textStyle: {
                color: "rgba(255,255,255,.4)",
                fontSize:12
            },
        },
        axisLine: {
            lineStyle: {
                color: 'rgba(255,255,255,.1)'
            }
        },
        data: ['1000', '2000', '3000', '4000', '5000', '6000', '7000', '8000', '9000']

    },

    yAxis: [{
        type: 'value',
        axisTick: {show: false},
        splitNumber:3,
        axisLine: {show:false
        },
        axisLabel:  {
            show:false,
            textStyle: {
                color: "rgba(255,255,255,.3)",
                fontSize:12
            },
        },

        splitLine: {
            lineStyle: {
                color: 'rgba(255,255,255,.05)'
            }
        }
    }],
    series: [
        {
            name: '河流长度（km）',
            type: 'bar',
            barWidth: '20%',
            label: {
                normal: {
                    show: true,
                    position: 'top',
                    textStyle: { color: "rgba(255,255,255,.3)",fontSize:10}
                }
            },
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#6884d9'
                    }, {
                        offset: 1,
                        color: '#5470c6'
                    }]),
                    barBorderRadius: 11,
                }

            },
            data: [3000, 6000, 3000, 6000, 3000, 9000, 3000, 7000, 5000]


        },
    ]

};
// 实时异常TOP5 - 静态示例（可后续改为动态）
const anomalyData = [
    { rank:1, type:"超警戒", subtype:"水位超警", station:"长江汉口站", value:"+1.52m", color:"red" },
    { rank:2, type:"低水位", subtype:"流量异常", station:"黄河下游", value:"-30%", color:"orange" },
    { rank:3, type:"水质超标", subtype:"氨氮超标", station:"珠江广州段", value:"2.1mg/L", color:"yellow" },
    { rank:4, type:"流量异常", subtype:"枯水期", station:"松花江哈尔滨", value:"-45%", color:"gray" },
    { rank:5, type:"超警戒", subtype:"水位超警", station:"淮河蚌埠", value:"+0.8m", color:"red" }
];


// 站点类型数量（用于顶部卡片 & “全国水文站点类型分布”环形饼图，确保两处数据一致）
let stationTypeData = [
    { name: '水位站', value: 450, color: '#00c2ff' },
    { name: '雨量站', value: 320, color: '#f0c725' },
    { name: '水质站', value: 210, color: '#e92b77' },
    { name: '闸坝站', value: 180, color: '#16f892' },
    { name: '潮位站', value: 140, color: '#8fa0b5' },
    // 如需在饼图中展示“其他”，可保留；顶部卡片不展示
    { name: '其他', value: 90, color: '#5470c6' }
];

function updateStationCards() {
    const selectorMap = {
        '水位站': '.count-waterlevel',
        '雨量站': '.count-rain',
        '水质站': '.count-quality',
        '闸坝站': '.count-gate',
        '潮位站': '.count-tide'
    };

    stationTypeData.forEach(d => {
        const sel = selectorMap[d.name];
        if (!sel) return;
        const $el = window.jQuery ? $(sel) : null;
        if ($el && $el.length) $el.text(d.value);
    });
}
function renderAnomalyList() {
    const ul = document.getElementById('anomaly-list');
    ul.innerHTML = '';
    anomalyData.forEach(item => {
        const li = document.createElement('li');
        li.innerHTML = `
            <span>${item.rank}</span>
            <span class="badge badge-${item.color}">${item.type}</span>
            <span>${item.subtype}</span>
            <span>${item.station}</span>
            <span class="text-${item.color}">${item.value}</span>
        `;
        ul.appendChild(li);
    });
}

// 水文站点分布（环形饼图）
function huaxing() {
    const el = document.getElementById('huaxing');
    const chart = echarts.getInstanceByDom(el) || echarts.init(el);
    const option = {
        tooltip: { trigger: 'item', formatter: '{b} : {c} ({d}%)' },
        legend: {
            orient: 'vertical',
            left: 'left',
            textStyle: { color: '#bad0e2' }
        },
        series: [{
            type: 'pie',
            radius: ['45%', '65%'],
            center: ['60%', '50%'],
            avoidLabelOverlap: false,
            label: { show: false },
            emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
            data: stationTypeData.map(d => ({ value: d.value, name: d.name, itemStyle: { color: d.color } }))
        }]
    };
    chart.setOption(option);
}

// 主要地区水流量（折线图）
function zhexian() {
    const el = document.getElementById('zhexian');
    const chart = echarts.getInstanceByDom(el) || echarts.init(el);
    const option = {
        tooltip: { trigger: 'axis' },
        legend: { top: '5%', textStyle: { color: '#bad0e2' } },
        grid: { left: '4%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['2020', '2021', '2022', '2023', '2024', '2025'],
            axisLine: { lineStyle: { color: 'rgba(255,255,255,0.2)' } },
            axisLabel: { color: '#bad0e2' }
        },
        yAxis: {
            type: 'value',
            name: '(m³/s)',
            axisLine: { show: false },
            splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } },
            axisLabel: { color: '#bad0e2' }
        },
        series: [
            { name: '上游流速', type: 'line', smooth: true, data: [120, 130, 110, 140, 125, 135], itemStyle: { color: '#f0c725' } },
            { name: '下游流速', type: 'line', smooth: true, data: [100, 105, 95, 115, 108, 110], itemStyle: { color: '#00c2ff' } },
            { name: '平均流速', type: 'line', smooth: true, data: [110, 118, 102, 128, 116, 122], itemStyle: { color: '#16f892' } }
        ]
    };
    chart.setOption(option);
}

// 初始化所有
$(function() {
    renderAnomalyList();
    huaxing();
    updateStationCards();
    zhexian();

    // 窗口resize时重绘ECharts
    window.addEventListener('resize', () => {
        echarts.getInstanceByDom(document.getElementById('huaxing'))?.resize();
        echarts.getInstanceByDom(document.getElementById('zhexian'))?.resize();
    });
});


// ================= ProvinceDashboard (frontend only) =================
// This module ONLY does frontend work:
// 1) receives province click (adcode/name)
// 2) calls backend API to get province-specific data
// 3) updates 4 modules:
//    - 水文站点类型分布 (#huaxing)
//    - 主要地区水流量（2020-2025） (#zhexian)
//    - 水质量分布情况 (#leida)
//    - 重点水利工程 (#echarts3)
//
// Backend API contract (recommended):
// GET /api/province/dashboard?adcode=xxxxxx
// Response JSON example:
// {
//   "stationType": [{"name":"水位站","value":123}, ...],
//   "flow": {"years":["2020","2021","2022","2023","2024","2025"], "series":[{"name":"上游流速","data":[...]}, ...]},
//   "waterQuality": {"years":["2023","2024","2025"], "indicators":[{"name":"溶解氧","max":10},...], "data":[{"name":"2023","value":[...]},...]},
//   "projects": [{"name":"大型枢纽","value":1385}, ...]
// }
(function () {
    const CHART_IDS = ['huaxing', 'zhexian', 'leida', 'echarts3'];
    const LOADING_DELAY_MS = 150;
    const LOADING_MIN_MS = 350;
    const CACHE_TTL_MS = 30 * 1000;
    const provinceCache = Object.create(null);
    let activeXhr = null;
    let activeRequestSeq = 0;
    let activeShowTimer = null;
    let loadingShownAt = 0;
    let loadingShownSeq = 0;

    function getChart(domId) {
        const el = document.getElementById(domId);
        if (!el) return null;
        return echarts.getInstanceByDom(el) || echarts.init(el);
    }

    function showAllLoading(text) {
        const opts = {
            text: text || '加载中...',
            color: '#ffffff',
            textColor: '#ffffff',
            maskColor: 'rgba(0,0,0,0.12)',
            fontSize: 14,
            showSpinner: true,
            spinnerRadius: 12,
            lineWidth: 2
        };

        CHART_IDS.forEach(id => {
            const c = getChart(id);
            if (c) c.showLoading('default', opts);
        });
    }
    function hideAllLoading() {
        CHART_IDS.forEach(id => {
            const c = getChart(id);
            if (c) c.hideLoading();
        });
    }

    function updateStationType(stationTypeArr) {
        if (!Array.isArray(stationTypeArr) || stationTypeArr.length === 0) return;

        // merge into stationTypeData (keep colors if possible)
        const colorMap = {};
        (stationTypeData || []).forEach(d => { colorMap[d.name] = d.color; });

        stationTypeData = stationTypeArr.map(d => ({
            name: d.name,
            value: Number(d.value || 0),
            color: d.color || colorMap[d.name] || '#5470c6'
        }));

        updateStationCards();
        huaxing();
    }

    function updateFlow(flowObj) {
        if (!flowObj || !Array.isArray(flowObj.years) || !Array.isArray(flowObj.series)) return;

        const chart = getChart('zhexian');
        if (!chart) return;

        const option = {
            xAxis: { data: flowObj.years },
            legend: { data: flowObj.series.map(s => s.name) },
            series: flowObj.series.map(s => ({
                name: s.name,
                type: 'line',
                smooth: true,
                data: s.data || []
            }))
        };

        chart.setOption(option, { notMerge: false, lazyUpdate: true });
    }

    function updateWaterQuality(wqObj) {
        if (!wqObj) return;
        const chart = getChart('leida');
        if (!chart) return;

        const oldOpt = chart.getOption ? (chart.getOption() || {}) : {};
        const oldSeries0 = (oldOpt.series && oldOpt.series[0]) ? oldOpt.series[0] : {};
        const oldDataArr = Array.isArray(oldSeries0.data) ? oldSeries0.data : [];

        const oldMap = {};
        oldDataArr.forEach(item => {
            if (!item) return;
            const key = (item.name !== undefined && item.name !== null) ? String(item.name) : '';
            if (key) oldMap[key] = item;
        });
        const template = oldDataArr.length ? oldDataArr[0] : {};

        const years = Array.isArray(wqObj.years) ? wqObj.years.map(y => String(y)) : [];
        const yearIndex = {};
        years.forEach((y, i) => { yearIndex[y] = i; });

        const incoming = Array.isArray(wqObj.data) ? wqObj.data : [];

        // 1) 先组装 series.data（保留你原来“按年份复用样式”的逻辑）
        const nextData = incoming.map(d => {
            const nm = (d && d.name !== undefined && d.name !== null) ? String(d.name) : '';
            const oldItem = (nm && oldMap[nm]) ? oldMap[nm] : {};

            const merged = window.jQuery
                ? $.extend(true, {}, template, oldItem)
                : Object.assign({}, template, oldItem);

            merged.name = nm;
            merged.value = Array.isArray(d && d.value) ? d.value : [];
            return merged;
        });

        if (years.length) {
            nextData.sort((a, b) => {
                const ia = yearIndex[a.name] !== undefined ? yearIndex[a.name] : 1e9;
                const ib = yearIndex[b.name] !== undefined ? yearIndex[b.name] : 1e9;
                return ia - ib;
            });
        }

        // 2) 关键：重新计算 radar.indicator[*].max，并加余量，避免顶满
        const indicators = Array.isArray(wqObj.indicators) ? wqObj.indicators : [];
        const dim = indicators.length || (nextData[0] && nextData[0].value ? nextData[0].value.length : 0);

        const maxByDim = new Array(dim).fill(0);
        nextData.forEach(row => {
            const arr = Array.isArray(row.value) ? row.value : [];
            for (let i = 0; i < dim; i++) {
                const v = Number(arr[i]);
                if (!Number.isNaN(v)) maxByDim[i] = Math.max(maxByDim[i], v);
            }
        });

        // 余量系数（你可以调成 1.1~1.5）
        const PAD = 1.25;

        const fixedIndicators = indicators.map((ind, i) => {
            const baseMax = Number(ind.max);
            const rawMax = Math.max(
                (Number.isFinite(baseMax) ? baseMax : 0),
                (maxByDim[i] || 0),
                1 // 防止全 0 时 max=0
            );

            let m = rawMax * PAD;

            // 让刻度更“好看”：大于10取整，小于10保留1位小数
            m = (m >= 10) ? Math.ceil(m) : Math.round(m * 10) / 10;

            return Object.assign({}, ind, { max: m });
        });

        const patch = {
            legend: { data: years },
            radar: { indicator: fixedIndicators },
            series: [{ data: nextData }]
        };

        if (!oldSeries0.type) patch.series[0].type = 'radar';
        chart.setOption(patch, { notMerge: false, lazyUpdate: true });
    }


    function updateProjects(projectArr) {
        if (!projectArr || projectArr.length === 0) {
            console.warn('水利工程数据为空');
            return;
        }

        const chart = getChart('echarts3');  // 确认你的图表 ID 是 echarts3（从 index.html 看应该是）
        if (!chart) {
            console.error('未找到 echarts3 实例');
            return;
        }

        // 关键：强制转为数字，并过滤掉 0 值（避免全0扇区看起来像坏了）
        const data = projectArr
            .map(item => ({
                name: item.name || '未知',
                value: Number(item.value) || 0
            }))
            .filter(item => item.value > 0);  // 只显示有值的，避免全0报错或空图

        if (data.length === 0) {
            console.warn('所有工程数量均为0，无有效数据展示');
            // 可选：显示提示文字
            chart.setOption({
                graphic: {
                    type: 'text',
                    left: 'center',
                    top: 'center',
                    style: {
                        text: '暂无数据',
                        font: '16px Microsoft YaHei',
                        fill: '#aaa'
                    }
                },
                series: []  // 清空
            });
            return;
        }

        console.log('水利工程最终数据:', data);  // 检查控制台！

        // 正确方式：只更新 data，不覆盖整个 series 配置
        chart.setOption({
            series: [{
                data: data
                // 不写 type、radius 等，让它继承原配置
            }]
        }, {
            notMerge: false,  // 合并更新
            lazyUpdate: true
        });
    }

    function applyProvinceData(data) {
        if (!data) return;
        updateStationType(data.stationType);
        updateFlow(data.flow);
        updateWaterQuality(data.waterQuality);
        updateProjects(data.projects);
    }

    function loadProvinceData(params) {
        const adcode = params && params.adcode ? String(params.adcode) : '';
        const name = params && params.name ? String(params.name) : '';
        if (!adcode) return;

        const now = Date.now();
        const cached = provinceCache[adcode];
        if (cached && cached.data && (now - cached.ts) < CACHE_TTL_MS) {
            applyProvinceData(cached.data);
            return;
        }

        activeRequestSeq += 1;
        const mySeq = activeRequestSeq;
        if (activeXhr && typeof activeXhr.abort === 'function') {
            try { activeXhr.abort(); } catch (e) {}
        }
        if (activeShowTimer) {
            clearTimeout(activeShowTimer);
            activeShowTimer = null;
        }
        hideAllLoading();
        loadingShownAt = 0;
        loadingShownSeq = 0;

        activeShowTimer = setTimeout(function () {
            if (mySeq !== activeRequestSeq) return;
            loadingShownSeq = mySeq;
            loadingShownAt = Date.now();
            showAllLoading(name ? (name + ' 加载中...') : '加载中...');
        }, LOADING_DELAY_MS);

        // You can change this endpoint to match your backend
        const url = (window.ProvinceDashboard && window.ProvinceDashboard.config && window.ProvinceDashboard.config.endpoint)
            ? window.ProvinceDashboard.config.endpoint
            : '/api/province/dashboard';

        activeXhr = $.ajax({
            url: url,
            method: 'GET',
            dataType: 'json',
            data: { adcode: adcode, name: name },
            timeout: 12000
        }).done(function (res) {
            if (mySeq !== activeRequestSeq) return;
            if (res) provinceCache[adcode] = { ts: Date.now(), data: res };

            const finish = function () {
                if (loadingShownSeq === mySeq) hideAllLoading();
                applyProvinceData(res);
            };

            if (loadingShownSeq !== mySeq) {
                if (activeShowTimer) {
                    clearTimeout(activeShowTimer);
                    activeShowTimer = null;
                }
                finish();
                return;
            }

            if (activeShowTimer) {
                clearTimeout(activeShowTimer);
                activeShowTimer = null;
            }

            const elapsed = Date.now() - (loadingShownAt || Date.now());
            const wait = Math.max(0, LOADING_MIN_MS - elapsed);
            if (wait) setTimeout(finish, wait);
            else finish();
        }).fail(function (xhr, status) {
            if (mySeq !== activeRequestSeq) return;
            if (status === 'abort') return;

            const finish = function () {
                if (loadingShownSeq === mySeq) hideAllLoading();
                console.warn('Province data load failed:', status, xhr && xhr.responseText);
            };

            if (loadingShownSeq !== mySeq) {
                if (activeShowTimer) {
                    clearTimeout(activeShowTimer);
                    activeShowTimer = null;
                }
                finish();
                return;
            }

            if (activeShowTimer) {
                clearTimeout(activeShowTimer);
                activeShowTimer = null;
            }

            const elapsed = Date.now() - (loadingShownAt || Date.now());
            const wait = Math.max(0, LOADING_MIN_MS - elapsed);
            if (wait) setTimeout(finish, wait);
            else finish();

            // Optional: keep current data, or inject a small hint in console
        });
    }

    // Public API
    window.ProvinceDashboard = window.ProvinceDashboard || {};
    window.ProvinceDashboard.config = window.ProvinceDashboard.config || {
        endpoint: '/api/province/dashboard'
    };
    window.ProvinceDashboard.loadProvinceData = loadProvinceData;
    window.ProvinceDashboard.applyProvinceData = applyProvinceData;
})();
