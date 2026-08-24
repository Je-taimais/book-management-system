<template>
  <div class="app-container">
    <el-card class="password-card" shadow="hover">
      <div slot="header" class="card-header">
        <span><i class="el-icon-lock"></i> 密码修改</span>
      </div>
      <el-form
        ref="form"
        :model="form"
        :rules="passwordRules"
        label-width="120px"
        class="password-form"
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
            v-model="form.oldPassword"
            type="password"
            placeholder="请输入旧密码"
            prefix-icon="el-icon-key"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="form.newPassword"
            type="password"
            placeholder="请输入新密码"
            prefix-icon="el-icon-lock"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认新密码" prop="repeat">
          <el-input
            v-model="form.repeat"
            type="password"
            placeholder="请再输入一遍新密码"
            prefix-icon="el-icon-lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            @click="onSubmit" 
            icon="el-icon-check"
            :loading="loading"
            class="submit-button"
          >
            确认
          </el-button>
          <el-button 
            @click="resetForm" 
            icon="el-icon-refresh-right"
          >
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <div class="password-tips">
        <h4><i class="el-icon-info-circle"></i> 密码修改提示</h4>
        <ul>
          <li>为保证账号安全，建议定期修改密码</li>
          <li>密码长度建议大于6位，包含数字和字母</li>
          <li>请勿使用与其他网站相同的密码</li>
        </ul>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { alterPassword } from "@/api/user";


export default {
  data() {
    const validateRepeat = (rule, value, callback) => {
      if (value !== this.form.newPassword) {
        callback(new Error("两次输入的密码不一致!"));
      } else {
        callback();
      }
    };
    const validateNewPassword = (rule, value, callback) => {
      if (value === this.form.oldPassword) {
        callback(new Error("新密码不能与旧密码相同!"));
      } else {
        callback();
      }
    };
    return {
      form: {
        oldPassword: "",
        newPassword: "",
        repeat: "",
      },
      loading: false,
      passwordRules: {
        oldPassword: [
          { required: true, message: "请输入旧密码", trigger: "blur" },
        ],
        newPassword: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          { min: 6, message: "密码长度至少6个字符", trigger: "blur" },
          { validator: validateNewPassword, trigger: "blur" }
        ],
        repeat: [
          { required: true, message: "请再输入新密码", trigger: "blur" },
          { trigger: "blur", validator: validateRepeat },
        ],
      },
    };
  },
  mounted() {

  },
  methods: {
    onSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true;
          const isadmin = this.roles[0] === "admin" ? 1 : 0;
          alterPassword({
            userid: this.id,
            username: this.name,
            isadmin: isadmin,
            oldPassword: this.form.oldPassword,
            newPassword: this.form.newPassword,
          }).then((res) => {
            this.loading = false;
            if (res === 0) {
              this.$message.error("旧密码不正确");
            } else {
              this.$message({
                type: 'success',
                message: '密码修改成功',
                duration: 2000
              });
              this.resetForm();
            }
          }).catch(() => {
            this.loading = false;
          });
        } else {
          return false;
        }
      });
    },
    resetForm() {
      this.$refs.form.resetFields();
    }
  },
  computed: {
    // 获得user信息
    ...mapGetters(["id", "name", "roles"]),
  },
};
</script>

<style lang="scss" scoped>
.app-container {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.password-card {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  border-radius: 8px;
  
  .card-header {
    display: flex;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
    color: #303133;
    
    i {
      margin-right: 8px;
      font-size: 20px;
      color: #409EFF;
    }
  }
  
  .password-form {
    margin-top: 20px;
    
    .el-form-item {
      margin-bottom: 22px;
    }
    
    .el-input {
      .el-input__inner {
        border-radius: 4px;
      }
    }
    
    .submit-button {
      min-width: 80px;
      border-radius: 4px;
      margin-right: 15px;
    }
    
    .el-button {
      padding: 9px 20px;
      
      i {
        margin-right: 5px;
        font-weight: bold;
      }
    }
  }
  
  .password-tips {
    margin-top: 30px;
    padding: 15px;
    background-color: #f8f8f8;
    border-radius: 4px;
    
    h4 {
      font-size: 15px;
      color: #606266;
      margin-top: 0;
      margin-bottom: 10px;
      display: flex;
      align-items: center;
      
      i {
        margin-right: 6px;
        color: #E6A23C;
      }
    }
    
    ul {
      margin: 0;
      padding-left: 20px;
      
      li {
        color: #909399;
        font-size: 13px;
        line-height: 1.8;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .password-card {
    width: 100%;
  }
  
  .password-form {
    .el-form-item__label {
      float: none;
      text-align: left;
      display: block;
      margin-bottom: 8px;
    }
    
    .el-form-item__content {
      margin-left: 0 !important;
    }
  }
}
</style>
