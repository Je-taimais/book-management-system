<template>
    <div class="dashboard-container">
        <div class="dashboard-welcome">
            <div class="welcome-content">
                <h1>欢迎使用图书管理系统</h1>
                <p>你好，{{ name }}，祝你有美好的一天！</p>
                <div class="welcome-actions">
                    <el-button type="primary" icon="el-icon-reading" @click="navigateTo('/bookmanage/bookinfo')">浏览图书</el-button>
                    <el-button type="success" icon="el-icon-document" @click="navigateTo('/bookmanage/borrow')">借阅管理</el-button>
                </div>
            </div>
            <div class="welcome-image">
                <i class="el-icon-collection"></i>
            </div>
        </div>

        <!-- 数据统计卡片 -->
        <el-row :gutter="24" class="data-overview">
            <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
                <el-card shadow="hover" class="data-card book-card">
                    <div class="card-content">
                        <div class="card-icon">
                            <i class="el-icon-reading"></i>
                        </div>
                        <div class="card-info">
                            <div class="card-title">图书总数</div>
                            <div class="card-value">{{ bookCount }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
                <el-card shadow="hover" class="data-card borrow-card">
                    <div class="card-content">
                        <div class="card-icon">
                            <i class="el-icon-document"></i>
                        </div>
                        <div class="card-info">
                            <div class="card-title">借阅总数</div>
                            <div class="card-value">{{ borrowCount }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
                <el-card shadow="hover" class="data-card user-card">
                    <div class="card-content">
                        <div class="card-icon">
                            <i class="el-icon-user"></i>
                        </div>
                        <div class="card-info">
                            <div class="card-title">用户总数</div>
                            <div class="card-value">{{ userCount }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
                <el-card shadow="hover" class="data-card type-card">
                    <div class="card-content">
                        <div class="card-icon">
                            <i class="el-icon-collection-tag"></i>
                        </div>
                        <div class="card-info">
                            <div class="card-title">图书类型</div>
                            <div class="card-value">{{ typeCount }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 统计图表区域 -->
        <el-row :gutter="24" class="chart-section">
            <el-col :span="24">
                <div class="section-header">
                    <h2 class="section-title">统计分析</h2>
                    <el-divider></el-divider>
                </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
                <el-card shadow="hover" class="chart-card">
                    <div slot="header" class="card-header">
                        <span>图书类型分布</span>
                    </div>
                    <div class="chart-container" ref="pieChartContainer"></div>
                </el-card>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
                <el-card shadow="hover" class="chart-card">
                    <div slot="header" class="card-header">
                        <span>借阅状态分布</span>
                    </div>
                    <div class="chart-container" ref="barChartContainer"></div>
                </el-card>
            </el-col>
            <el-col :xs="24" :sm="24" :md="24" :lg="8" :xl="8">
                <el-card shadow="hover" class="chart-card">
                    <div slot="header" class="card-header">
                        <span>高价图书排行</span>
                    </div>
                    <div class="chart-container" ref="lineChartContainer"></div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getCount as getBookCount } from "@/api/bookinfo";
import { getCount as getBorrowCount } from "@/api/borrow";
import { getCount as getUserCount } from "@/api/user";
import { getCount as getTypeCount } from "@/api/booktype";
import { getBookTypeDistribution, getBorrowStatusDistribution, getHighPriceBooks } from "@/api/dashboard";

export default {
    name: "Dashboard",
    data() {
        return {
            bookCount: 0,
            borrowCount: 0,
            userCount: 0,
            typeCount: 0,
            borrowRate: 0,
            customColors: [
                {color: '#f56c6c', percentage: 20},
                {color: '#e6a23c', percentage: 40},
                {color: '#5cb87a', percentage: 60},
                {color: '#1989fa', percentage: 80},
                {color: '#6f7ad3', percentage: 100}
            ],
            // 图表数据
            chartData: {
                pieData: [],
                barData: {
                    categories: [],
                    values: []
                },
                lineData: {
                    books: [],
                    values: []
                }
            },
            // 图表实例
            charts: {
                pieChart: null,
                barChart: null,
                lineChart: null
            },
            // 图表加载状态
            loading: {
                pie: true,
                bar: true,
                line: true
            }
        };
    },
    computed: {
        ...mapGetters(["id", "name", "roles"]),
    },
    mounted() {
        this.fetchData();
        this.$nextTick(() => {
            this.initCharts();
            this.fetchChartData();
        });
    },
    methods: {
        fetchData() {
            // 获取图书总数
            getBookCount().then(res => {
                this.bookCount = res;
            }).catch(() => {
                this.bookCount = 0;
            });

            // 获取借阅总数
            getBorrowCount().then(res => {
                this.borrowCount = res;
            }).catch(() => {
                this.borrowCount = 0;
            });

            // 获取用户总数
            getUserCount().then(res => {
                this.userCount = res;
            }).catch(() => {
                this.userCount = 0;
            });

            // 获取图书类型总数
            getTypeCount().then(res => {
                this.typeCount = res;
            }).catch(() => {
                this.typeCount = 0;
            });
            
            // 计算借阅率（作为展示，简单计算）
            setTimeout(() => {
                if (this.bookCount > 0) {
                    this.borrowRate = Math.round((this.borrowCount / this.bookCount) * 100);
                    if (this.borrowRate > 100) this.borrowRate = 100;
                }
            }, 500);
        },
        // 获取图表数据
        async fetchChartData() {
            try {
                // 获取图书类型分布数据
                const pieData = await getBookTypeDistribution();
                this.chartData.pieData = pieData;
                this.loading.pie = false;
                if (this.charts.pieChart) {
                    this.updatePieChart();
                }

                // 获取借阅状态分布
                const barData = await getBorrowStatusDistribution();
                this.chartData.barData = barData;
                this.loading.bar = false;
                if (this.charts.barChart) {
                    this.updateBarChart();
                }

                // 获取热门图书排行
                const lineData = await getHighPriceBooks();
                this.chartData.lineData = lineData;
                this.loading.line = false;
                if (this.charts.lineChart) {
                    this.updateLineChart();
                }
            } catch (error) {
                console.error('获取图表数据失败:', error);
            }
        },
        navigateTo(path) {
            this.$router.push(path);
        },
        // 初始化图表
        initCharts() {
            this.initPieChart();
            this.initBarChart();
            this.initLineChart();
        },
        // 初始化饼图
        initPieChart() {
            const pieChart = this.$echarts.init(this.$refs.pieChartContainer);
            this.charts.pieChart = pieChart;
            
            // 设置加载状态
            pieChart.showLoading();
            
            // 如果已经有数据，立即更新图表
            if (this.chartData.pieData.length > 0) {
                this.updatePieChart();
            }
            
            // 响应窗口大小变化
            window.addEventListener('resize', () => {
                pieChart.resize();
            });
        },
        // 更新饼图数据
        updatePieChart() {
            if (!this.charts.pieChart) return;
            
            const pieChart = this.charts.pieChart;
            // 隐藏加载状态
            pieChart.hideLoading();
            
            const option = {
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b}: {c} ({d}%)'
                },
                legend: {
                    type: 'scroll',
                    orient: 'horizontal',
                    bottom: 0,
                    left: 'center',
                    itemWidth: 10,
                    itemHeight: 10,
                    textStyle: {
                        fontSize: 12
                    },
                    data: this.chartData.pieData.map(item => item.name)
                },
                series: [
                    {
                        name: '图书类型',
                        type: 'pie',
                        radius: ['35%', '60%'],
                        center: ['50%', '45%'],
                        avoidLabelOverlap: false,
                        itemStyle: {
                            borderRadius: 8,
                            borderColor: '#fff',
                            borderWidth: 2
                        },
                        label: {
                            show: true,
                            position: 'outside',
                            formatter: '{b}: {c}本\n{d}%',
                            fontSize: 12,
                            fontWeight: 'bold'
                        },
                        labelLine: {
                            show: true,
                            length: 15,
                            length2: 10
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: 14,
                                fontWeight: 'bold'
                            },
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        },
                        data: this.chartData.pieData
                    }
                ],
                color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#6f7ad3', '#36cfc9', '#ff85c0']
            };
            pieChart.setOption(option);
        },
        // 初始化柱状图
        initBarChart() {
            const barChart = this.$echarts.init(this.$refs.barChartContainer);
            this.charts.barChart = barChart;
            
            // 设置加载状态
            barChart.showLoading();
            
            // 如果已经有数据，立即更新图表
            if (this.chartData.barData.categories.length > 0) {
                this.updateBarChart();
            }
            
            // 响应窗口大小变化
            window.addEventListener('resize', () => {
                barChart.resize();
            });
        },
        // 更新柱状图数据 - 借阅状态分布
        updateBarChart() {
            if (!this.charts.barChart) return;
            
            const barChart = this.charts.barChart;
            // 隐藏加载状态
            barChart.hideLoading();
            
            const option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        data: this.chartData.barData.categories,
                        axisTick: {
                            alignWithLabel: true
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '图书数量',
                        type: 'bar',
                        barWidth: '60%',
                        data: this.chartData.barData.values,
                        itemStyle: {
                            color: function(params) {
                                // 在馆图书为绿色，借出图书为橙色
                                const colors = ['#67C23A', '#E6A23C'];
                                return colors[params.dataIndex] || '#409EFF';
                            }
                        }
                    }
                ]
            };
            barChart.setOption(option);
        },
        // 初始化折线图
        initLineChart() {
            const lineChart = this.$echarts.init(this.$refs.lineChartContainer);
            this.charts.lineChart = lineChart;
            
            // 设置加载状态
            lineChart.showLoading();
            
            // 如果已经有数据，立即更新图表
            if (this.chartData.lineData.books.length > 0) {
                this.updateLineChart();
            }
            
            // 响应窗口大小变化
            window.addEventListener('resize', () => {
                lineChart.resize();
            });
        },
        // 更新排行榜图表 - 高价图书排行
        updateLineChart() {
            if (!this.charts.lineChart) return;
            
            const lineChart = this.charts.lineChart;
            // 隐藏加载状态
            lineChart.hideLoading();
            
            const option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    },
                    formatter: '{b}: {c} 元'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'value',
                    name: '价格(元)'
                },
                yAxis: {
                    type: 'category',
                    data: this.chartData.lineData.books,
                    axisLabel: {
                        width: 80,
                        overflow: 'truncate',
                        interval: 0
                    }
                },
                series: [{
                    name: '图书价格',
                    type: 'bar',
                    data: this.chartData.lineData.values,
                    itemStyle: {
                        color: new this.$echarts.graphic.LinearGradient(1, 0, 0, 0, [
                            { offset: 0, color: '#f56c6c' },
                            { offset: 0.5, color: '#f56c6c' },
                            { offset: 1, color: '#f78989' }
                        ])
                    },
                    label: {
                        show: true,
                        position: 'right',
                        formatter: '{c} 元'
                    }
                }]
            };
            lineChart.setOption(option);
        }
    }
};
</script>

<style lang="scss" scoped>
.dashboard-container {
    padding: 24px;
    background-color: #f5f7fa;
    min-height: calc(100vh - 50px);
}

.dashboard-welcome {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 32px;
    background: linear-gradient(to right, #1989fa, #5cbcff);
    border-radius: 8px;
    padding: 30px;
    color: #fff;
    box-shadow: 0 4px 12px rgba(25, 137, 250, 0.2);
    
    .welcome-content {
        flex: 1;
    }
    
    h1 {
        font-size: 28px;
        font-weight: 600;
        margin: 0 0 12px 0;
    }
    
    p {
        font-size: 16px;
        margin: 0 0 20px 0;
        opacity: 0.9;
    }
    
    .welcome-actions {
        margin-top: 20px;
    }
    
    .welcome-image {
        display: flex;
        align-items: center;
        justify-content: center;
        
        i {
            font-size: 120px;
            opacity: 0.2;
        }
    }
}

.section-container {
    margin-bottom: 32px;
}

.section-header {
    display: flex;
    flex-direction: column;
    margin-bottom: 16px;
    
    .section-title {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        margin: 0;
    }
    
    .el-divider {
        margin-top: 12px;
        margin-bottom: 0;
    }
}

.data-overview {
    margin-bottom: 32px;
}

.data-card {
    height: 120px;
    border-radius: 8px;
    overflow: hidden;
    transition: all 0.3s;
    
    &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
    }
    
    .card-content {
        display: flex;
        align-items: center;
        height: 100%;
        padding: 20px;
    }
    
    .card-icon {
        width: 64px;
        height: 64px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
        
        i {
            font-size: 30px;
            color: #fff;
        }
    }
    
    .card-info {
        flex: 1;
    }
    
    .card-title {
        font-size: 16px;
        color: #606266;
        margin-bottom: 10px;
    }
    
    .card-value {
        font-size: 30px;
        font-weight: bold;
        color: #303133;
    }
}

.book-card .card-icon {
    background: linear-gradient(135deg, #409EFF, #53a8ff);
}

.borrow-card .card-icon {
    background: linear-gradient(135deg, #67C23A, #85ce61);
}

.user-card .card-icon {
    background: linear-gradient(135deg, #E6A23C, #f5a657);
}

.type-card .card-icon {
    background: linear-gradient(135deg, #F56C6C, #f78989);
}

.content-section {
    margin-bottom: 20px;
}

.card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    
    span {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
    }
}

.book-recommend {
    margin-bottom: 20px;
    
    .recommend-list {
        min-height: 200px;
    }
    
    .book-item {
        display: flex;
        align-items: center;
        margin-bottom: 16px;
        padding: 12px;
        border-radius: 8px;
        transition: all 0.3s;
        
        &:hover {
            background-color: #f5f7fa;
        }
        
        .book-cover {
            width: 60px;
            height: 80px;
            background-color: #ecf5ff;
            border-radius: 4px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 12px;
            
            i {
                font-size: 30px;
                color: #409EFF;
            }
        }
        
        .book-info {
            flex: 1;
        }
        
        .book-name {
            font-size: 16px;
            font-weight: 500;
            color: #303133;
            margin-bottom: 6px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        
        .book-author {
            font-size: 14px;
            color: #909399;
        }
    }
}

.activity-card {
    margin-bottom: 20px;
    
    .activity-content {
        min-height: 300px;
    }
    
    .chart-container {
        height: 100%;
    }
    
    .chart-placeholder {
        padding: 16px;
    }
    
    .data-item {
        margin-bottom: 24px;
    }
    
    .data-label {
        font-size: 16px;
        color: #606266;
        margin-bottom: 12px;
    }
}

.empty-placeholder {
    height: 200px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #909399;
    
    i {
        font-size: 48px;
        margin-bottom: 16px;
    }
    
    .placeholder-text {
        font-size: 14px;
    }
}

.chart-section {
    margin-bottom: 20px;
}

.chart-card {
    margin-bottom: 20px;
    
    .chart-container {
        height: 300px;
        width: 100%;
    }
}
</style>
