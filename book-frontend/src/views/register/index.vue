<template>
    <div class="register-container">
        <div class="register-background">
            <div class="register-content">
                <div class="register-header">
                    <h2 class="register-title">注册新账号</h2>
                    <p class="register-subtitle">加入我们的图书管理系统</p>
                </div>

                <el-form
                    ref="loginForm"
                    :model="loginForm"
                    :rules="loginRules"
                    class="register-form"
                    auto-complete="on"
                    label-position="left"
                >
                    <!-- 用户名 -->
                    <el-form-item prop="username">
                        <div class="input-with-icon">
                            <i class="el-icon-user input-icon"></i>
                            <el-input
                                ref="username"
                                v-model="loginForm.username"
                                placeholder="请输入用户名"
                                name="username"
                                type="text"
                                tabindex="1"
                                auto-complete="on"
                            />
                        </div>
                    </el-form-item>

                    <!-- 密码 -->
                    <el-form-item prop="password">
                        <div class="input-with-icon">
                            <i class="el-icon-lock input-icon"></i>
                            <el-input
                                ref="password"
                                v-model="loginForm.password"
                                type="password"
                                placeholder="请输入密码"
                                name="password"
                                tabindex="2"
                                auto-complete="on"
                            />
                        </div>
                    </el-form-item>

                    <!-- 确认密码 -->
                    <el-form-item prop="repeat">
                        <div class="input-with-icon">
                            <i class="el-icon-key input-icon"></i>
                            <el-input
                                ref="repeat"
                                v-model="loginForm.repeat"
                                type="password"
                                placeholder="请确认密码"
                                name="repeat"
                                tabindex="3"
                                auto-complete="on"
                                @keyup.enter.native="handleRight"
                            />
                        </div>
                    </el-form-item>

                    <!-- 按钮区域 -->
                    <div class="form-actions">
                        <a
                            href="javascript:;"
                            class="custom-confirm-button"
                            :class="{'disabled': loading}"
                            @click="!loading && handleRight()"
                        >
                            确认
                        </a>
                        <a
                            href="javascript:;"
                            class="custom-back-button"
                            @click="handleBack"
                        >
                            返回登录
                        </a>
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
import { register } from "@/api/user";

export default {
    name: "Register",
    data() {
        const validateRepeat = (rule, value, callback) => {
            if (value !== this.loginForm.password) {
                callback(new Error("两次输入的密码不一致!"));
            } else {
                callback();
            }
        };
        return {
            loginForm: {
                username: "",
                password: "",
                repeat: "",
            },
            loginRules: {
                username: [
                    {
                        required: true,
                        message: "请输入用户名",
                        trigger: "blur",
                    },
                ],
                password: [
                    { required: true, message: "请输入密码", trigger: "blur" },
                ],
                repeat: [
                    {
                        required: true,
                        message: "请再次输入密码",
                        trigger: "blur",
                    },
                    { trigger: "blur", validator: validateRepeat },
                ],
            },
            loading: false,
        };
    },
    methods: {
        handleRight() {
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    this.loading = true;
                    register({
                        username: this.loginForm.username,
                        password: this.loginForm.password,
                    }).then((res) => {
                        this.loading = false;
                        if (res === 0) {
                            this.$message.error("注册失败，可能账号重复了");
                        } else {
                            this.$message.success("注册成功");
                            setTimeout(() => {
                                this.handleBack();
                            }, 1500);
                        }
                    }).catch(() => {
                        this.loading = false;
                    });
                } else {
                    console.log("不允许提交!");
                    return false;
                }
            });
        },
        handleBack() {
            this.$router.push("/login");
        },
    }
};
</script>

<style lang="scss" scoped>
.register-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    width: 100%;

    .register-background {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: url("../../assets/bg8.jpg") center center / cover no-repeat;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .register-content {
        width: 440px;
        background: rgba(255, 255, 255, 0.95);
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        padding: 40px;
        position: relative;
        backdrop-filter: blur(10px);

        .register-header {
            text-align: center;
            margin-bottom: 40px;

            .register-title {
                color: #4f46e5;
                font-size: 28px;
                font-weight: 700;
                margin: 0 0 8px;
            }

            .register-subtitle {
                color: #6b7280;
                font-size: 16px;
                margin: 0;
            }
        }

        .register-form {
            .input-with-icon {
                position: relative;
                display: flex;
                align-items: center;

                .input-icon {
                    position: absolute;
                    left: 12px;
                    font-size: 18px;
                    color: #6b7280;
                    z-index: 1;
                }

                .el-input {
                    width: 100%;

                    ::v-deep .el-input__inner {
                        padding-left: 40px;
                        height: 48px;
                        border-radius: 8px;
                        border: 1px solid #e5e7eb;
                        transition: all 0.3s;

                        &:focus {
                            border-color: #4f46e5;
                            box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
                        }
                    }
                }
            }
        }

        .form-actions {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            gap: 15px;
            margin-top: 30px;

            .custom-confirm-button {
                flex: 1;
                height: 48px;
                font-size: 16px;
                background: linear-gradient(to right, #4338ca, #4f46e5);
                border: none;
                border-radius: 8px;
                padding: 0 12px;
                color: white;
                cursor: pointer;
                outline: none;
                transition: all 0.3s;
                display: flex;
                align-items: center;
                justify-content: center;
                text-decoration: none;
                box-shadow: 0 2px 10px rgba(79, 70, 229, 0.3);

                &:hover, &:focus {
                    background: linear-gradient(to right, #3730a3, #4338ca);
                    box-shadow: 0 4px 12px rgba(79, 70, 229, 0.4);
                }

                &.disabled {
                    opacity: 0.7;
                    cursor: not-allowed;
                    box-shadow: none;
                }
            }

            .custom-back-button {
                flex: 1;
                height: 48px;
                font-size: 16px;
                background: linear-gradient(to right, #e5e7eb, #f3f4f6);
                color: #4b5563;
                border: 1px solid #e5e7eb;
                border-radius: 8px;
                transition: all 0.3s;
                padding: 0 12px;
                cursor: pointer;
                outline: none;
                display: flex;
                align-items: center;
                justify-content: center;
                text-decoration: none;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);

                &:hover {
                    background: linear-gradient(to right, #f3f4f6, #f9fafb);
                    color: #4f46e5;
                    border-color: #4f46e5;
                    box-shadow: 0 4px 8px rgba(79, 70, 229, 0.15);
                }
            }
        }
    }
}

// 适配深色主题的样式
@media (prefers-color-scheme: dark) {
    .register-content {
        background: rgba(31, 41, 55, 0.95);

        .register-header {
            .register-title {
                color: #818cf8;
            }

            .register-subtitle {
                color: #9ca3af;
            }
        }
    }
}
</style>
