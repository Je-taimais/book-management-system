<template>
    <div class="app-container">

        <!-- 搜索 -->
        <div class="filter-container">
            <el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item"
                      @keyup.enter.native="handleFilter"/>
            <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
                搜索
            </el-button>
        </div>

        <!-- 统计卡片 -->
        <el-row :gutter="20" style="margin-bottom: 20px;">
            <el-col :span="8">
                <el-card shadow="hover" class="stat-card">
                    <div class="stat-title">总缴费金额</div>
                    <div class="stat-value">¥ {{ totalAmount.toFixed(2) }}</div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" class="stat-card">
                    <div class="stat-title">总缴费笔数</div>
                    <div class="stat-value">{{ totalCount }}</div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 表格 -->
        <el-table
                :data="list"
                border
                fit
                highlight-current-row
                style="width: 100%;"
                max-height="530"
                v-loading="listLoading">
            <el-table-column
                    label="ID"
                    prop="paymentid"
                    v-if="show"
                    sortable="custom"
                    align="center"
                    width="80">
                <template slot-scope="{row}">
                    <span>{{ row.paymentid }}</span>
                </template>
            </el-table-column>

            <el-table-column label="缴费ID" width="90" align="center" v-if="false">
                <template slot-scope="{row}">
                    <span>{{ row.paymentid }}</span>
                </template>
            </el-table-column>

            <el-table-column label="借阅ID" width="90" align="center">
                <template slot-scope="{row}">
                    <span>{{ row.borrowid }}</span>
                </template>
            </el-table-column>

            <el-table-column label="用户ID" width="90" align="center">
                <template slot-scope="{row}">
                    <span>{{ row.userid }}</span>
                </template>
            </el-table-column>

            <el-table-column label="缴费金额" width="120" align="center">
                <template slot-scope="{row}">
                    <span style="color: #F56C6C; font-weight: bold;">¥ {{ row.amount }}</span>
                </template>
            </el-table-column>

            <el-table-column label="缴费时间" align="center" min-width="160">
                <template slot-scope="{row}">
                    <span>{{ row.paymenttime }}</span>
                </template>
            </el-table-column>

            <el-table-column label="类型" width="100" align="center">
                <template slot-scope="{row}">
                    <el-tag v-if="row.paymenttype === 1" type="warning" size="small">罚金缴费</el-tag>
                </template>
            </el-table-column>

            <el-table-column label="备注" align="center" min-width="150">
                <template slot-scope="{row}">
                    <span>{{ row.remark || '-' }}</span>
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
import {queryPaymentsByPage} from '@/api/payment'

export default {
    name: 'Payment',
    data() {
        return {
            list: [],
            total: 0,
            totalAmount: 0,
            totalCount: 0,
            listLoading: true,
            show: false,
            listQuery: {
                page: 1,
                limit: 10,
                username: ''
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        getList() {
            this.listLoading = true
            queryPaymentsByPage(this.listQuery).then(response => {
                this.list = response.data || []
                this.total = response.count || 0
                // 计算统计
                this.calcStats()
                this.listLoading = false
            }).catch(() => {
                this.listLoading = false
            })
        },
        calcStats() {
            // 从当前页数据计算（如需全量统计需后端提供接口）
            let amount = 0
            this.list.forEach(item => {
                amount += parseFloat(item.amount || 0)
            })
            this.totalAmount = amount
            this.totalCount = this.list.length
        },
        handleFilter() {
            this.listQuery.page = 1
            this.getList()
        }
    }
}
</script>

<style scoped>
.stat-card {
    text-align: center;
}
.stat-title {
    font-size: 14px;
    color: #909399;
    margin-bottom: 10px;
}
.stat-value {
    font-size: 28px;
    font-weight: bold;
    color: #303133;
}
</style>
