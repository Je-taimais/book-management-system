<template>
    <div class="app-container">
        <!-- 顶部功能区 -->
        <el-card class="filter-container" shadow="hover">
            <el-form :inline="true" :model="queryParam" class="search-form" size="small">
                <div class="form-row">
                    <div class="form-item-container">
                        <label class="form-label">书名</label>
            <el-input
                v-model="queryParam.bookname"
                            placeholder="输入书名关键字"
                            prefix-icon="el-icon-search"
                            clearable
                @keyup.enter.native="handleFilter"
                            class="form-input"
            />
                    </div>
                    <div class="form-item-container">
                        <label class="form-label">作者</label>
            <el-input
                v-model="queryParam.bookauthor"
                            placeholder="输入作者名"
                            prefix-icon="el-icon-edit"
                            clearable
                @keyup.enter.native="handleFilter"
                            class="form-input"
            />
                    </div>
                    <div class="form-item-container">
                        <label class="form-label">图书类型</label>
            <el-select
                v-model="queryParam.booktypeid"
                filterable
                            placeholder="选择图书类型"
                clearable
                            class="form-input"
            >
                <el-option
                    v-for="item in typeData"
                    :key="item.booktypeid"
                    :label="item.booktypename"
                    :value="item.booktypeid"
                />
            </el-select>
                    </div>
                </div>
                
                <el-row :gutter="20" type="flex" align="middle" justify="start" class="action-row">
                    <el-col>
                        <el-form-item class="action-buttons">
            <el-button
                v-waves
                type="primary"
                                icon="el-icon-search"
                @click="handleFilter"
            >
                搜索
            </el-button>
            <el-button
                v-permission="['admin']"
                type="success"
                                icon="el-icon-plus"
                @click="handleCreate"
            >
                添加图书
            </el-button>
            <el-button
                v-permission="['admin']"
                type="danger"
                                icon="el-icon-delete"
                @click="handleDeleteSome"
                                :disabled="selectedRows.length === 0"
            >
                批量删除
            </el-button>
                            <el-button
                                icon="el-icon-refresh"
                                @click="handleShowAll"
                            >
                                显示全部
                            </el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </el-card>

        <!-- 数据展示切换 -->
        <div class="control-panel">
            <div class="view-switch">
                <el-radio-group v-model="viewMode" size="small">
                    <el-radio-button label="table">表格视图</el-radio-button>
                    <el-radio-button label="card">卡片视图</el-radio-button>
                </el-radio-group>
            </div>
        </div>

        <!-- 表格视图 -->
        <el-card shadow="hover" v-show="viewMode === 'table'" class="table-container">
            <el-table
                ref="multipleTable"
                :data="tableData"
                border
                stripe
                highlight-current-row
                @selection-change="handleSelectionChange"
                style="width: 100%"
                v-loading="tableLoading"
            >
                <el-table-column fixed type="selection" width="50" align="center"></el-table-column>
                <el-table-column fixed prop="bookid" label="序号" width="80" align="center"></el-table-column>
                <el-table-column label="图书封面" width="100" align="center">
                    <template slot-scope="scope">
                        <el-image
                            :src="$store.state.settings.baseApi + scope.row.bookimg"
                            style="width: 60px; height: 80px; border-radius: 4px;"
                            :preview-src-list="[$store.state.settings.baseApi + scope.row.bookimg]"
                            fit="cover"
                        >
                            <div slot="error" class="image-error">
                                <i class="el-icon-picture-outline"></i>
                            </div>
                        </el-image>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="bookname"
                    label="图书名称"
                    min-width="150"
                    show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                    prop="bookauthor"
                    label="作者"
                    min-width="120"
                    show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                    prop="bookprice"
                    label="价格"
                    min-width="80"
                    align="center"
                >
                    <template slot-scope="scope">
                        <span class="book-price">{{ scope.row.bookprice }} 元</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="booktypename"
                    label="图书类型"
                    min-width="100"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-tag size="small">{{ scope.row.booktypename }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="bookdesc"
                    label="图书描述"
                    min-width="200"
                    show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                    prop="stock"
                    label="库存"
                    min-width="100"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.stock <= 0 ? 'danger' : (scope.row.stock <= 2 ? 'warning' : 'success')" size="small">
                            剩余 {{ scope.row.stock }} 本
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="isborrowed"
                    label="图书状态"
                    min-width="100"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.isborrowed === 1 ? 'danger' : 'success'" size="small">
                            {{ scope.row.isborrowed === 1 ? "已借出" : "在馆" }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" min-width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                            v-permission="['admin']"
                            type="primary"
                            size="mini"
                            icon="el-icon-edit"
                            @click="handleUpdate(scope.row)"
                            circle
                        ></el-button>
                        <el-button
                            v-permission="['admin']"
                            type="danger"
                            size="mini"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
                            circle
                        ></el-button>
                        <el-button
                            :disabled="scope.row.isborrowed === 1 || scope.row.stock <= 0"
                            type="success"
                            size="mini"
                            icon="el-icon-document"
                            @click="handleBorrow(scope.row)"
                            circle
                        ></el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页器 -->
            <div class="pagination-container">
                <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="queryParam.page"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="queryParam.limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="recordTotal"
                >
                </el-pagination>
            </div>
        </el-card>

        <!-- 卡片视图 -->
        <div v-show="viewMode === 'card'" class="card-view-container">
            <el-row :gutter="24">
                <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in tableData" :key="item.bookid">
                    <el-card shadow="hover" class="book-card">
                        <div class="book-cover">
                            <el-image
                                :src="$store.state.settings.baseApi + item.bookimg"
                                fit="contain"
                                :preview-src-list="[]"
                            >
                                <div slot="error" class="image-error">
                                    <i class="el-icon-picture-outline"></i>
                                </div>
                            </el-image>
                            <div class="book-status">
                                <el-tag :type="item.isborrowed === 1 ? 'danger' : 'success'" size="mini">
                                    {{ item.isborrowed === 1 ? "已借出" : "在馆" }}
                                </el-tag>
                            </div>
                        </div>
                        <div class="book-info">
                            <h3 class="book-title" :title="item.bookname">{{ item.bookname }}</h3>
                            <div class="book-meta">
                                <span class="book-author" :title="item.bookauthor">作者: {{ item.bookauthor }}</span>
                                <span class="book-price">¥ {{ item.bookprice }}</span>
                            </div>
                            <div class="book-category">
                                <el-tag size="mini" effect="plain">{{ item.booktypename }}</el-tag>
                            </div>
                            <p class="book-desc" :title="item.bookdesc">{{ item.bookdesc }}</p>
                            <div class="book-actions">
                                <el-button
                                    v-permission="['admin']"
                                    type="primary"
                                    size="mini"
                                    icon="el-icon-edit"
                                    @click="handleUpdate(item)"
                                    circle
                                ></el-button>
                                <el-button
                                    v-permission="['admin']"
                                    type="danger"
                                    size="mini"
                                    icon="el-icon-delete"
                                    @click="handleDelete(item)"
                                    circle
                                ></el-button>
                                <el-button
                                    :disabled="item.isborrowed === 1 || item.stock <= 0"
                                    type="success"
                                    size="mini"
                                    icon="el-icon-document"
                                    @click="handleBorrow(item)"
                                    circle
                                ></el-button>
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
            <div class="pagination-container">
                <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="queryParam.page"
                    :page-sizes="[12, 24, 36, 48]"
                    :page-size="queryParam.limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="recordTotal"
                >
                </el-pagination>
            </div>
        </div>

        <!--图书表单弹出框-->
        <el-dialog
            :title="formTitle"
            :visible.sync="dialogFormVisible"
            width="60%"
            top="5vh"
            :close-on-click-modal="false"
        >
            <el-row :gutter="20">
                <el-col :span="16">
                    <!--图书信息表单-->
                    <el-form
                        :model="form"
                        :rules="rules"
                        ref="ruleForm"
                        label-width="100px"
                        class="book-form"
                    >
                        <el-form-item label="图书名称" prop="bookname">
                            <el-input v-model="form.bookname" placeholder="请输入图书名称"></el-input>
                        </el-form-item>

                        <el-form-item label="作者" prop="bookauthor">
                            <el-input v-model="form.bookauthor" placeholder="请输入作者"></el-input>
                        </el-form-item>

                        <el-form-item label="价格" prop="bookprice">
                            <el-input-number v-model="form.bookprice" :min="0" :precision="2" style="width: 100%"></el-input-number>
                        </el-form-item>

                        <el-form-item label="图书类型" prop="booktypeid">
                            <el-select
                                v-model="form.booktypeid"
                                placeholder="请选择图书类型"
                                style="width: 100%"
                            >
                                <el-option
                                    v-for="item in typeData"
                                    :key="item.booktypeid"
                                    :label="item.booktypename"
                                    :value="item.booktypeid"
                                >
                                </el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="图书描述" prop="bookdesc">
                            <el-input
                                type="textarea"
                                v-model="form.bookdesc"
                                placeholder="请输入图书描述"
                                :rows="4"
                            ></el-input>
                        </el-form-item>
                        
                        <el-form-item label="借阅状态" prop="isborrowed">
                            <el-radio-group v-model="form.isborrowed">
                                <el-radio :label="0">在馆</el-radio>
                                <el-radio :label="1">已借出</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="库存数量" prop="stock">
                            <el-input-number v-model="form.stock" :min="0" :max="999" style="width: 100%"></el-input-number>
                            <div class="form-tip">可借数量，0 表示暂无库存</div>
                        </el-form-item>
                    </el-form>
                </el-col>
                <el-col :span="8">
                    <div class="upload-container">
                        <h3>图书封面</h3>
                        <!--上传图片-->
                        <el-upload
                            class="avatar-uploader"
                            action="http://localhost:9111/BookManager/upload/uploadImg"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                            :before-upload="beforeAvatarUpload"
                        >
                            <img
                                v-if="form.bookimg"
                                :src="$store.state.settings.baseApi + form.bookimg"
                                class="avatar"
                                alt="封面无法显示"
                            />
                            <div v-else class="upload-placeholder">
                                <i class="el-icon-plus"></i>
                                <div class="upload-text">点击上传</div>
                            </div>
                        </el-upload>
                        <div class="upload-tip">
                            支持 JPG、PNG 格式，建议尺寸 400x600 像素
                        </div>
                    </div>
                </el-col>
            </el-row>

            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">
                    取 消
                </el-button>
                <el-button type="primary" @click="submitForm">
                    确 定
                </el-button>
            </div>
        </el-dialog>

        <!--借阅用户选择弹出框-->
        <el-dialog
            title="选择借阅用户"
            :visible.sync="dialogFormVisible2"
            width="500px"
            :close-on-click-modal="false"
        >
            <el-form :model="form2">
                <el-form-item label="用户" prop="userid" label-width="80px">
                    <el-select
                        v-model="form2.userid"
                        placeholder="请选择用户"
                        style="width: 100%"
                        filterable
                    >
                        <el-option
                            v-for="item in userData"
                            :key="item.userid"
                            :label="item.username"
                            :value="item.userid"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible2 = false">
                    取 消
                </el-button>
                <el-button type="primary" @click="submitForm2">
                    确 定
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { mapGetters } from "vuex";
import permission from "@/directive/permission/index.js"; // 权限判断指令
import waves from "@/directive/waves"; // waves directive

import {
    getCount,
    queryBookInfosByPage,
    addBookInfo,
    deleteBookInfo,
    deleteBookInfos,
    updateBookInfo,
} from "@/api/bookinfo";
import { queryBookTypes } from "@/api/booktype";
import { borrowBook, checkCanBorrow } from "@/api/borrow";
import { queryUsers } from "@/api/user";

export default {
    name: "Bookinfo",
    directives: { waves, permission },
    // 创建后
    created() {
        // 从服务器获取数据表格第一页的信息
        queryBookInfosByPage(this.queryParam).then((res) => {
            console.log("首页数据获取成功", res);
            this.tableData = res.data;
            this.recordTotal = res.count;
        });
        // 从服务器获取所有的图书类型
        queryBookTypes().then((res) => {
            console.log("图书类型获取成功", res);
            this.typeData = res;
        });
    },
    mounted() {
        if (this.roleIsAdmin === false) {
            this.queryParam.limit = 5;
            this.handleSizeChange(this.queryParam.limit);
        }
    },
    methods: {
        // 分页大小改变监听
        handleSizeChange(curSize) {
            const params = this.queryParam;
            params.limit = curSize;
            queryBookInfosByPage(params).then((res) => {
                console.log("分页数据获取成功", res);
                this.tableData = res.data;
                this.recordTotal = res.count;
            });
        },

        // 点击分页监听方法
        handleCurrentChange(curPage) {
            const params = this.queryParam;
            params.page = curPage;

            queryBookInfosByPage(params).then((res) => {
                console.log("分页数据获取成功", res);
                this.tableData = res.data;
                this.recordTotal = res.count;
            });
        },

        // 搜索图书
        handleFilter() {
            this.queryParam.page = 1;
            queryBookInfosByPage(this.queryParam).then((res) => {
                if (res.code === 0) {
                    this.tableData = res.data;
                    this.recordTotal = res.count;
                }
            });
        },

        // 显示全部
        handleShowAll() {
            this.queryParam.page = 1;
            this.queryParam.bookname = null;
            this.queryParam.bookauthor = null;
            this.queryParam.booktypeid = null;
            queryBookInfosByPage(this.queryParam).then((res) => {
                if (res.code === 0) {
                    this.tableData = res.data;
                    this.recordTotal = res.count;
                }
            });
        },

        // 图片上传成功监听
        handleAvatarSuccess(res, file) {
            console.log(res);
            console.log(file);
            if (res.code === 0) {
                this.$message.success("上传成功");
                this.form.bookimg = res.data;
            } else {
                this.$message.error("上传失败，请联系管理员");
            }
        },

        // 图片上传之前监听
        beforeAvatarUpload(file) {
            const isJPG = file.type === "image/jpeg";
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error("上传封面图片只能是 JPG 格式!");
            }
            if (!isLt2M) {
                this.$message.error("上传封面图片大小不能超过 2MB!");
            }
            return isJPG && isLt2M;
        },

        // 点击添加记录
        handleCreate() {
            // 从服务器获取所有的图书类型
            queryBookTypes().then((res) => {
                console.log("图书类型获取成功", res);
                this.typeData = res;
            });
            // 表单是添加状态
            this.formType = 0;
            // 将空数据置入form
            this.form = {
                bookid: null,
                bookname: "",
                bookauthor: "",
                bookprice: "",
                booktypeid: 1,
                bookdesc: "",
                isborrowed: 0,
                stock: 3,
                bookimg: "",
            };
            // 显示表单框
            this.dialogFormVisible = true;
        },

        // 点击编辑记录
        handleUpdate(row) {
            // 从服务器获取所有的图书类型
            queryBookTypes().then((res) => {
                console.log("图书类型获取成功", res);
                this.typeData = res;
            });
            // 表单是编辑状态
            this.formType = 1;
            // 将行数据置入form
            this.form = {
                bookid: row.bookid,
                bookname: row.bookname,
                bookauthor: row.bookauthor,
                bookprice: row.bookprice,
                booktypeid: row.booktypeid,
                bookdesc: row.bookdesc,
                isborrowed: row.isborrowed,
                stock: row.stock,
                bookimg: row.bookimg,
            };
            // 显示表单框
            this.dialogFormVisible = true;
        },

        // 点击借阅图书
        handleBorrow(row) {
            if (this.roleIsAdmin) {
                // 管理员代借，先检查读者资格
                this.dialogFormVisible2 = true;
                this.form2.bookid = row.bookid;
                queryUsers().then((res) => {
                    this.userData = res;
                });
            } else {
                // 读者自己借书，先检查资格
                checkCanBorrow(this.id).then(res => {
                    if (res.status === 0) {
                        // 可以借书
                        this.$confirm("您确定要借书吗?", "提示").then(() => {
                            borrowBook(this.id, row.bookid).then((res2) => {
                                this.afterBorrow(res2, row);
                            });
                        }).catch(() => {});
                    } else {
                        // 不可借书，显示原因
                        const data = res.data || {};
                        let msg = res.message || '不可借书';
                        const overdueList = data.overdueUnreturned || [];
                        const unpaidList = data.unpaidOverdue || [];
                        if (overdueList.length > 0) {
                            msg += '\n\n逾期未还图书：'
                            overdueList.forEach(item => {
                                const dueStr = item.duedatestr || (item.duedate ? new Date(item.duedate).toLocaleDateString() : '未知')
                                msg += '\n- ' + item.bookname + '（应还：' + dueStr + '）'
                            })
                        }
                        if (unpaidList.length > 0) {
                            msg += '\n\n未缴罚金记录：'
                            unpaidList.forEach(item => {
                                msg += '\n- ' + item.bookname + ' 罚金 ¥' + item.fine
                            })
                        }
                        this.$alert(msg, '借书受限', {
                            confirmButtonText: '我知道了',
                            type: 'warning'
                        });
                    }
                }).catch(() => {
                    this.$message.error('检查借书资格失败，请稍后重试');
                });
            }
        },

        // 借书后的统一处理
        afterBorrow(res, row) {
            if (res.status === 0 || res === 1) {
                this.$message.success("借书成功");
                this.handleCurrentChange(this.queryParam.page);
            } else {
                this.$message.error("借书失败：" + (res.message || ''));
            }
            this.dialogFormVisible2 = false;
        },

        // 添加和更新的提交表单
        submitForm() {
            if (this.formType === 0) {
                // 添加记录
                addBookInfo(this.form).then((res) => {
                    if (res === 1) {
                        this.$message.success("添加记录成功");
                        // 跳转到末尾
                        getCount().then((res) => {
                            this.recordTotal = res;
                            this.queryParam.page = 1;
                            this.handleCurrentChange(this.queryParam.page);
                        });
                    } else {
                        this.$message.error("添加记录失败");
                    }
                    this.dialogFormVisible = false; // 关闭对话框
                });
            } else if (this.formType === 1) {
                //更新记录
                updateBookInfo(this.form).then((res) => {
                    if (res === 1 || res === 0) {
                        this.$message.success("更新记录成功");
                        this.handleCurrentChange(this.queryParam.page);
                    } else {
                        this.$message.error("更新记录失败");
                    }
                    this.dialogFormVisible = false; // 关闭对话框
                });
            }
        },

        // 借书的提交表单（管理员代借）
        submitForm2() {
            borrowBook(this.form2.userid, this.form2.bookid).then((res) => {
                this.afterBorrow(res, this.form2);
            }).catch(() => {
                this.$message.error('借书失败');
                this.dialogFormVisible2 = false;
            });
        },

        // 删除记录
        handleDelete(row, index) {
            this.$confirm("确定要删除该条记录吗?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                deleteBookInfo(row).then((res) => {
                    if (res === 1) {
                        this.$message.success("删除记录成功");
                        this.tableData.splice(index, 1);
                        // 如果删完了，获取上一页
                        if (this.tableData.length === 0) {
                            this.queryParam.page = this.queryParam.page - 1;
                            this.handleCurrentChange(this.queryParam.page);
                        }
                    } else if (res === -1) {
                        this.$message.error(
                            "该图书存在与读者的借阅信息，请尝试先删除所有的本图书借阅信息再重试"
                        );
                    } else {
                        this.$message.error("删除记录失败");
                    }
                });
            });
        },

        // 删除一些
        handleDeleteSome() {
            this.$confirm("确定要删除这些记录吗?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                // 获取选中的对象数组
                const items = this.$refs.multipleTable.selection;
                deleteBookInfos(items).then((res) => {
                    if (res > 0) {
                        this.$message.success("删除" + res + "条记录成功");
                        if (this.tableData.length === res) {
                            //如果本页内容全部删光了
                            //当前页为上一页
                            if (this.queryParam.page !== 0) {
                                this.queryParam.page = this.queryParam.page - 1;
                            }
                        }
                        // 重载当前页
                        this.handleCurrentChange(this.queryParam.page);
                    } else {
                        this.$message.error("删除记录失败");
                    }
                });
            });
        },

        // 表格多选框选择事件
        handleSelectionChange(selection) {
            this.selectedRows = selection;
        },
    },
    data() {
        return {
            // 表格数据
            tableData: [],
            // 记录总数
            recordTotal: 0,
            // 图书类型数据
            typeData: [],
            // 用户数据
            userData: [],
            // 查询参数
            queryParam: {
                page: 1,
                limit: 10,
                bookname: null,
                bookauthor: null,
                booktypeid: null,
            },
            // 对话框表单显示
            dialogFormVisible: false,
            dialogFormVisible2: false,
            // 表单类型（添加数据:0,修改数据:1）
            formType: 0,
            // 表单数据
            form: {
                bookid: null,
                bookname: "",
                bookauthor: "",
                bookprice: 0,
                booktypeid: 1,
                bookdesc: "",
                isborrowed: 0,
                bookimg: "",
            },
            form2: {
                userid: 1,
                bookid: 1,
            },
            rules: {
                bookname: [
                    {
                        required: true,
                        message: "请输入图书名称",
                        trigger: "blur",
                    },
                ],
                bookauthor: [
                    { required: true, message: "请输入作者", trigger: "blur" },
                ],
                bookprice: [
                    { required: true, message: "请输入价格", trigger: "blur" },
                ],
                booktypeid: [
                    { required: true, message: "请选择类型", trigger: "blur" },
                ],
                bookdesc: [
                    { required: true, message: "请输入描述", trigger: "blur" },
                ],
                isborrowed: [
                    { required: true, message: "请选择状态", trigger: "blur" },
                ],
            },
            viewMode: "table",
            tableLoading: false,
            selectedRows: [],
        };
    },
    computed: {
        // 获得user信息
        ...mapGetters(["id", "name", "roles"]),
        // 通过表单类型计算表单标题
        formTitle() {
            return this.formType === 0 ? "添加记录" : "修改记录";
        },
        roleIsAdmin() {
            if (this.roles[0] === "admin") return true;
            else return false;
        },
    },
};
</script>

<style lang="scss" scoped>
.app-container {
    padding: 20px;
    background-color: #f5f7fa;
    min-height: calc(100vh - 84px);
}

.filter-container {
    margin-bottom: 20px;
    
    .search-form {
        .form-row {
            display: flex;
            flex-wrap: wrap;
            margin-bottom: 15px;
        }
        
        .form-item-container {
            display: flex;
            align-items: center;
            margin-right: 20px;
            margin-bottom: 10px;
            
            .form-label {
                white-space: nowrap;
                margin-right: 10px;
                color: #606266;
                font-size: 14px;
            }
            
            .form-input {
                width: 180px;
            }
        }
    }
    
    .action-row {
        margin-top: 5px;
    }
    
    .action-buttons {
        margin-bottom: 0;
        
        .el-button {
            margin-right: 10px;
            margin-bottom: 5px;
        }
    }
}

.control-panel {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 20px;
    
    .view-switch {
        .el-radio-button {
            margin-left: 5px;
        }
    }
}

.table-container {
    margin-bottom: 20px;
    border-radius: 8px;
    overflow: hidden;
}

.pagination-container {
    margin-top: 20px;
    text-align: right;
    padding: 10px 0;
}

.card-view-container {
    margin-bottom: 20px;
}

.book-card {
    margin-bottom: 24px;
    height: 520px;
    overflow: hidden;
    transition: all 0.3s;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    
    &:hover {
        transform: translateY(-5px);
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }
    
    .book-cover {
        position: relative;
        height: 300px;
        background-color: #f9f9f9;
        display: flex;
        align-items: center;
        justify-content: center;
        overflow: hidden;
        padding: 15px;
        
        .el-image {
            max-width: 100%;
            max-height: 270px;
            margin: 0 auto;
            display: block;
            
            ::v-deep img {
                object-fit: contain;
                vertical-align: middle;
                width: auto;
                height: auto;
                max-width: 100%;
                max-height: 270px;
            }
        }
        
        .book-status {
            position: absolute;
            top: 10px;
            right: 10px;
            z-index: 1;
        }
    }
    
    .book-info {
        padding: 15px;
        flex: 1;
        display: flex;
        flex-direction: column;
        
        .book-title {
            font-size: 16px;
            font-weight: bold;
            margin: 0 0 10px 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            cursor: default;
        }
        
        .book-meta {
            display: flex;
            justify-content: space-between;
            font-size: 14px;
            color: #606266;
            margin-bottom: 10px;
            
            .book-author {
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                max-width: 70%;
                cursor: default;
            }
        }
        
        .book-category {
            margin-bottom: 10px;
        }
        
        .book-stock {
            margin-bottom: 10px;
        }
        
        .book-desc {
            font-size: 13px;
            color: #909399;
            margin: 0 0 15px 0;
            height: 60px;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            cursor: default;
            flex: 1;
        }
        
        .book-actions {
            display: flex;
            justify-content: flex-end;
            margin-top: auto;
            
            .el-button {
                margin-left: 8px;
                
                &:hover {
                    transform: scale(1.1);
                }
            }
        }
    }
}

.book-form {
    .el-select {
        width: 100%;
    }
    
    .form-tip {
        font-size: 12px;
        color: #909399;
        margin-top: 4px;
    }
}

.upload-container {
    text-align: center;
    
    h3 {
        margin-top: 0;
        margin-bottom: 20px;
        font-size: 16px;
        color: #303133;
    }
    
    .avatar-uploader {
    border: 1px dashed #d9d9d9;
        border-radius: 8px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
        width: 180px;
        height: 240px;
        margin: 0 auto 10px;
        
        &:hover {
            border-color: #409EFF;
        }
        
        .avatar {
            width: 100%;
            height: 100%;
            display: block;
            object-fit: cover;
        }
        
        .upload-placeholder {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            
            i {
    font-size: 28px;
    color: #8c939d;
                margin-bottom: 10px;
            }
            
            .upload-text {
                font-size: 14px;
                color: #8c939d;
            }
        }
    }
    
    .upload-tip {
        font-size: 12px;
        color: #909399;
        line-height: 1.5;
    }
}

.image-error {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: #f5f7fa;
    color: #909399;
    
    i {
        font-size: 24px;
    }
}

.book-price {
    color: #F56C6C;
    font-weight: bold;
}

.el-dialog {
    border-radius: 8px;
    overflow: hidden;
    
    .dialog-footer {
        padding-top: 10px;
    }
}
</style>
