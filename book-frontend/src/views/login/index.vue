<template>
    <div class="login-container">
        <div class="login-background">
            <div class="login-content">
                <div class="login-header">
                    <h2 class="login-title">图书管理系统</h2>
                    
                </div>

                <el-form
                    ref="loginForm"
                    :model="loginForm"
                    :rules="loginRules"
                    class="login-form"
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
                                :key="passwordType"
                                ref="password"
                                v-model="loginForm.password"
                                :type="passwordType"
                                placeholder="请输入密码"
                                name="password"
                                tabindex="2"
                                auto-complete="on"
                                @keyup.enter.native="handleLogin"
                            />
                            <span class="show-pwd" @click="showPwd">
                                <i :class="passwordType === 'password' ? 'el-icon-view' : 'el-icon-hide'"></i>
                            </span>
                        </div>
                    </el-form-item>

                    <!-- 权限 -->
                    <el-form-item prop="authority">
                        <div class="input-with-icon">
                            <i class="el-icon-medal input-icon"></i>
                            <el-select
                                v-model="loginForm.isadmin"
                                placeholder="请选择登录身份"
                                style="width: 100%"
                            >
                                <el-option :key="0" label="读者" :value="0"></el-option>
                                <el-option :key="1" label="管理员" :value="1"></el-option>
                            </el-select>
                        </div>
                    </el-form-item>

                    <!-- 登录按钮 -->
                    <div class="form-actions">
                        <a
                            href="javascript:;"
                            class="custom-login-button"
                            :class="{'disabled': loading}"
                            @click="!loading && handleLogin()"
                        >
                            登录
                        </a>
                        <a
                            href="javascript:;"
                            class="custom-register-button"
                            @click="handleRegister"
                        >
                            注册账号
                        </a>
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Login",
    data() {
        const validateUsername = (rule, value, callback) => {
            if (!value) {
                callback(new Error('请输入用户名'));
            } else {
                callback();
            }
        };
        const validatePassword = (rule, value, callback) => {
            if (!value) {
                callback(new Error('请输入密码'));
            } else {
                callback();
            }
        };
        return {
            loginForm: {
                username: "",
                password: "",
                isadmin: 0,
            },
            loginRules: {
                username: [
                    {
                        required: true,
                        trigger: "blur",
                        validator: validateUsername,
                    },
                ],
                password: [
                    {
                        required: true,
                        trigger: "blur",
                        validator: validatePassword,
                    },
                ],
            },
            loading: false,
            passwordType: "password",
            redirect: undefined
        };
    },
    methods: {
        showPwd() {
            if (this.passwordType === "password") {
                this.passwordType = "";
            } else {
                this.passwordType = "password";
            }
            this.$nextTick(() => {
                this.$refs.password.focus();
            });
        },
        handleLogin() {
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    this.loading = true;
                    this.$store
                        .dispatch("user/login", this.loginForm)
                        .then(() => {
                            this.$router.push({ path: "/" });
                            this.loading = false;
                        })
                        .catch((e) => {
                            console.log(e);
                            this.loading = false;
                            if (
                                e.response == undefined ||
                                e.response.data == undefined
                            ) {
                                this.$message({
                                    showClose: true,
                                    message: e,
                                    type: "error",
                                    duration: 5000,
                                });
                            } else {
                                this.$message({
                                    showClose: true,
                                    message: e.response.data,
                                    type: "error",
                                    duration: 5000,
                                });
                            }
                        });
                } else {
                    console.log("不允许提交!");
                    return false;
                }
            });
        },
        handleRegister() {
            console.log("注册按钮");
            this.$router.push({ path: "/register" }); // 进注册页面
        },
    }
};
</script>

<style lang="scss" scoped>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    width: 100%;

    .login-background {
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

    .login-content {
        width: 440px;
        background: rgba(255, 255, 255, 0.95);
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        padding: 40px;
        position: relative;
        backdrop-filter: blur(10px);

        .login-header {
            text-align: center;
            margin-bottom: 40px;

            .login-title {
                color: #1e3a8a;
                font-size: 28px;
                font-weight: 700;
                margin: 0 0 8px;
            }

            .login-subtitle {
                color: #6b7280;
                font-size: 16px;
                margin: 0;
            }
        }

        .login-form {
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
                            border-color: #3b82f6;
                            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
                        }
                    }
                }

                .el-select {
                    width: 100%;

                    ::v-deep .el-input__inner {
                        padding-left: 40px;
                        height: 48px;
                        border-radius: 8px;
                    }
                }

                .show-pwd {
                    position: absolute;
                    right: 12px;
                    color: #6b7280;
                    cursor: pointer;
                    font-size: 18px;

                    &:hover {
                        color: #3b82f6;
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

            .custom-login-button {
                flex: 1;
                height: 48px;
                font-size: 16px;
                background: linear-gradient(to right, #1e3a8a, #3b82f6);
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
                box-shadow: 0 2px 10px rgba(59, 130, 246, 0.3);

                &:hover, &:focus {
                    background: linear-gradient(to right, #1e40af, #2563eb);
                    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
                }

                &.disabled {
                    opacity: 0.7;
                    cursor: not-allowed;
                    box-shadow: none;
                }
            }

            .custom-register-button {
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
                    color: #3b82f6;
                    border-color: #3b82f6;
                    box-shadow: 0 4px 8px rgba(59, 130, 246, 0.15);
                }
            }
        }
    }
}

// 适配深色主题的样式
@media (prefers-color-scheme: dark) {
    .login-content {
        background: rgba(31, 41, 55, 0.95);

        .login-header {
            .login-title {
                color: #60a5fa;
            }

            .login-subtitle {
                color: #9ca3af;
            }
        }
    }
}
</style>
