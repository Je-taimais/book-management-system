<template>
    <div class="app-container">

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
                v-loading="listLoading">
            <!-- 隐藏列 -->
            <el-table-column
                    label="id"
                    prop="borrowid"
                    v-if="show"
                    sortable="custom"
                    align="center"
                    width="80">
                <template slot-scope="{row}">
                    <span>{{ row.borrowid }}</span>
                </template>
            </el-table-column>

            <!-- 用户名称 -->
            <el-table-column label="用户名" width="110px" align="center">
                <template slot-scope="{row}">
                    <span>{{ row.username }}</span>
                </template>
            </el-table-column>

            <!-- 图书名 -->
            <el-table-column label="图书名" width="150px" align="center">
                <template slot-scope="{row}">
                    <span>{{ row.bookname }}</span>
                </template>
            </el-table-column>

            <!-- 借书时间 -->
            <el-table-column label="借书时间" align="center" min-width="150">
                <template slot-scope="{row}">
                    <span>{{ row.borrowtimestr }}</span>
                </template>
            </el-table-column>

            <!-- 应还日期 (新增) -->
            <el-table-column label="应还日期" align="center" min-width="150">
                <template slot-scope="{row}">
                    <span :class="{ 'overdue-text': row.isoverdue === 1 }">{{ row.duedatestr || '-' }}</span>
                </template>
            </el-table-column>

            <!-- 还书时间 -->
            <el-table-column label="还书时间" align="center" min-width="150">
                <template slot-scope="{row}">
                    <span v-if="row.returntime != null">{{ row.returntimestr }}</span>
                    <span v-else style="color: #F56C6C;">等待还书</span>
                </template>
            </el-table-column>

            <!-- 逾期状态 (新增) -->
            <el-table-column label="逾期状态" align="center" width="100">
                <template slot-scope="{row}">
                    <el-tag v-if="row.isoverdue === 1" type="danger" size="small">已逾期</el-tag>
                    <el-tag v-else-if="row.returntime != null" type="success" size="small">正常</el-tag>
                    <span v-else>-</span>
                </template>
            </el-table-column>

            <!-- 逾期天数 (新增) -->
            <el-table-column label="逾期天数" align="center" width="90">
                <template slot-scope="{row}">
                    <span :class="{ 'overdue-text': row.isoverdue === 1 }">
                        {{ row.isoverdue === 1 ? row.overduedays + ' 天' : '-' }}
                    </span>
                </template>
            </el-table-column>

            <!-- 罚金 (新增) -->
            <el-table-column label="罚金" align="center" width="90">
                <template slot-scope="{row}">
                    <span :class="{ 'overdue-text': row.isoverdue === 1 }">
                        {{ row.isoverdue === 1 ? '¥' + row.fine : '-' }}
                    </span>
                </template>
            </el-table-column>

            <!-- 续借次数 -->
            <el-table-column label="续借次数" align="center" width="90">
                <template slot-scope="{row}">
                    <span>{{ row.renewcount != null ? row.renewcount + ' / 2' : '0 / 2' }}</span>
                </template>
            </el-table-column>

            <!-- 操作 -->
            <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
                <template slot-scope="{row}">
                    <el-button v-if="row.returntime == null" type="primary" size="mini"
                               @click="handleReturn(row)">
                        归还
                    </el-button>
                    <el-button
                            v-if="row.returntime == null && (row.renewcount == null || row.renewcount < 2) && !isOverdue(row)"
                            type="success" size="mini"
                            @click="handleRenew(row)">
                        续借
                    </el-button>
                    <el-button v-permission="['admin']" size="mini" type="danger"
                               @click="handleDelete(row)">
                        删除
                    </el-button>
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
import {queryBorrowsByPage, deleteBorrow, returnBook, renewBook} from '@/api/borrow'
import Pagination from '@/components/Pagination'

export default {
    name: 'Borrow',
    components: {Pagination},
    data() {
        return {
            list: [],
            total: 0,
            listLoading: true,
            show: false,
            listQuery: {
                page: 1,
                limit: 10,
                bookname: '',
                username: '',
                userid: ''
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        // 判断某条借阅记录是否已逾期
        isOverdue(row) {
            if (!row.duedatestr) return false
            return new Date().getTime() > new Date(row.duedatestr).getTime()
        },
        // 获得分页列表
        getList() {
            this.listLoading = true
            // 读者只能看到自己的记录
            if (this.$store.getters.roles[0] !== 'admin') {
                this.listQuery.userid = this.$store.getters.id
            }
            queryBorrowsByPage(this.listQuery).then(response => {
                this.list = response.data
                this.total = response.count
                this.listLoading = false
            }).catch(() => {
                this.listLoading = false
            })
        },
        // 过滤搜索
        handleFilter() {
            this.listQuery.page = 1
            this.getList()
        },
        // 删除记录
        handleDelete(row) {
            this.$confirm('确认删除该借阅记录？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                deleteBorrow(row).then(res => {
                    if (res === 0) {
                        this.$message.error('删除失败！该书籍未归还不能删除')
                        return
                    }
                    this.$message.success('删除成功')
                    this.getList()
                })
            })
        },
        // 归还图书
        handleReturn(row) {
            // 检查是否逾期，给出不同提示
            const duedateStr = row.duedatestr
            let confirmMessage = '确认归还该图书？'
            let confirmType = 'info'

            if (duedateStr) {
                const dueTime = new Date(duedateStr).getTime()
                const nowTime = new Date().getTime()
                if (nowTime > dueTime) {
                    const diffDays = Math.ceil((nowTime - dueTime) / (1000 * 60 * 60 * 24))
                    const fine = (diffDays * 0.5).toFixed(2)
                    confirmMessage = '该图书应还日期为 ' + row.duedatestr +
                        '，已逾期 ' + diffDays + ' 天，预计罚金 ¥' + fine +
                        '（0.5元/天）。\n\n归还时系统将自动计算逾期并生成缴费记录，确认归还？'
                    confirmType = 'warning'
                }
            }

            this.$confirm(confirmMessage, '归还确认', {
                confirmButtonText: '确定归还',
                cancelButtonText: '取消',
                type: confirmType
            }).then(() => {
                returnBook(row.borrowid, row.bookid).then(res => {
                    this.afterReturn(res)
                }).catch(() => {
                    this.$message.error('归还失败，请稍后重试')
                })
            })
        },
        // 归还后的统一处理
        afterReturn(res) {
            if (res.status === 0) {
                const msg = res.message || '还书成功'
                // 根据是否逾期显示不同级别消息
                if (res.data && res.data.isOverdue === 1) {
                    this.$message.warning(msg)
                } else {
                    this.$message.success(msg)
                }
            } else {
                this.$message.error(res.message || '还书失败')
            }
            this.getList()
        },
        // 续借图书
        handleRenew(row) {
            this.$confirm('确认续借《' + row.bookname + '》？\n续借后应还日期将延长30天。', '续借确认', {
                confirmButtonText: '确定续借',
                cancelButtonText: '取消',
                type: 'info'
            }).then(() => {
                renewBook(row.borrowid).then(res => {
                    if (res.status === 0) {
                        this.$message.success('续借成功！应还日期已延长30天')
                        this.getList()
                    } else {
                        this.$message.error('续借失败：' + (res.message || '未知错误'))
                    }
                }).catch(() => {
                    this.$message.error('续借失败，请稍后重试')
                })
            })
        }
    }
}
</script>

<style scoped>
.overdue-text {
    color: #F56C6C;
    font-weight: bold;
}
</style>