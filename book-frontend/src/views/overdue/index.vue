<template>
    <div class="app-container">

        <!-- 逾期统计卡片 -->
        <el-row :gutter="20" class="stats-row">
            <el-col :span="8">
                <el-card shadow="hover" class="stats-card">
                    <div class="stats-item">
                        <div class="stats-icon icon-warning">
                            <i class="el-icon-warning"></i>
                        </div>
                        <div class="stats-info">
                            <div class="stats-title">当前逾期中</div>
                            <div class="stats-value warning">{{ statistics.currentOverdueCount || 0 }} <span class="stats-unit">人</span></div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" class="stats-card">
                    <div class="stats-item">
                        <div class="stats-icon icon-danger">
                            <i class="el-icon-s-data"></i>
                        </div>
                        <div class="stats-info">
                            <div class="stats-title">历史总逾期次数</div>
                            <div class="stats-value danger">{{ statistics.totalOverdueCount || 0 }} <span class="stats-unit">次</span></div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" class="stats-card">
                    <div class="stats-item">
                        <div class="stats-icon icon-money">
                            <i class="el-icon-money"></i>
                        </div>
                        <div class="stats-info">
                            <div class="stats-title">累计罚金总额</div>
                            <div class="stats-value money">¥{{ statistics.totalFine || 0 }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 搜索 -->
        <div class="filter-container">
            <el-input v-model="listQuery.bookname" placeholder="图书名" style="width: 200px;" class="filter-item"
                      @keyup.enter.native="handleFilter"/>
            <el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item"
                      @keyup.enter.native="handleFilter"/>
            <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
                搜索
            </el-button>
        </div>

        <!-- 表格 -->
        <el-table
                :data="list"
                border
                fit
                highlight-current-row
                style="width: 100%;"
                max-height="530"
                v-loading="listLoading"
                :row-class-name="tableRowClassName">
            <el-table-column label="读者名" width="110px" align="center">
                <template slot-scope="{row}">
                    <span>{{ row.username }}</span>
                </template>
            </el-table-column>

            <el-table-column label="图书名" min-width="150" align="center">
                <template slot-scope="{row}">
                    <span>{{ row.bookname }}</span>
                </template>
            </el-table-column>

            <el-table-column label="借阅时间" align="center" min-width="150">
                <template slot-scope="{row}">
                    <span>{{ row.borrowtimestr }}</span>
                </template>
            </el-table-column>

            <el-table-column label="应还日期" align="center" min-width="150">
                <template slot-scope="{row}">
                    <span class="overdue-text">{{ row.duedatestr || '-' }}</span>
                </template>
            </el-table-column>

            <el-table-column label="实际归还日期" align="center" min-width="150">
                <template slot-scope="{row}">
                    <span v-if="row.returntimestr">{{ row.returntimestr }}</span>
                    <span v-else style="color: #F56C6C;">未归还</span>
                </template>
            </el-table-column>

            <el-table-column label="逾期天数" align="center" width="100">
                <template slot-scope="{row}">
                    <span class="overdue-text">{{ row.overduedays }} 天</span>
                </template>
            </el-table-column>

            <el-table-column label="罚金" align="center" width="100">
                <template slot-scope="{row}">
                    <span class="overdue-text">¥{{ row.fine }}</span>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <pagination
                v-show="total>0"
                :total="total"
                :page.sync="listQuery.page"
                :limit.sync="listQuery.limit"
                @pagination="getList"/>
    </div>
</template>

<script>
import {queryOverdueByPage, getOverdueStatistics} from '@/api/borrow'
import Pagination from '@/components/Pagination'

export default {
    name: 'Overdue',
    components: {Pagination},
    data() {
        return {
            list: [],
            total: 0,
            listLoading: true,
            statistics: {
                currentOverdueCount: 0,
                totalOverdueCount: 0,
                totalFine: 0
            },
            listQuery: {
                page: 1,
                limit: 10,
                bookname: '',
                username: ''
            }
        }
    },
    created() {
        this.getStatistics()
        this.getList()
    },
    methods: {
        getStatistics() {
            getOverdueStatistics().then(response => {
                this.statistics = response
            })
        },
        getList() {
            this.listLoading = true
            queryOverdueByPage(this.listQuery).then(response => {
                this.list = response.data
                this.total = response.count
                this.listLoading = false
            }).catch(() => {
                this.listLoading = false
            })
        },
        handleFilter() {
            this.listQuery.page = 1
            this.getList()
        },
        tableRowClassName() {
            return 'overdue-row'
        }
    }
}
</script>

<style scoped>
/* 统计卡片 */
.stats-row {
    margin-bottom: 20px;
}
.stats-card {
    border-radius: 8px;
}
.stats-item {
    display: flex;
    align-items: center;
}
.stats-icon {
    width: 60px;
    height: 60px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 20px;
    font-size: 26px;
    color: #fff;
}
.icon-warning {
    background: linear-gradient(135deg, #F6AD55, #ED8936);
}
.icon-danger {
    background: linear-gradient(135deg, #FC8181, #E53E3E);
}
.icon-money {
    background: linear-gradient(135deg, #68D391, #38A169);
}
.stats-info {
    flex: 1;
}
.stats-title {
    font-size: 13px;
    color: #909399;
    margin-bottom: 5px;
}
.stats-value {
    font-size: 24px;
    font-weight: bold;
}
.stats-value.warning {
    color: #E6A23C;
}
.stats-value.danger {
    color: #F56C6C;
}
.stats-value.money {
    color: #67C23A;
}
.stats-unit {
    font-size: 13px;
    font-weight: normal;
    color: #909399;
}

/* 搜索 */
.filter-container {
    margin-bottom: 15px;
}
.filter-item {
    margin-right: 10px;
}

/* 表格逾期红字 */
.overdue-text {
    color: #F56C6C;
    font-weight: bold;
}
</style>