<template>
  <div class="custom-menu">
    <router-link to="/dashboard" class="menu-item" :class="{ active: isActive('/dashboard') }">
      <div class="menu-icon-wrapper">
        <i class="el-icon-s-home menu-icon"></i>
      </div>
      <span class="menu-title">首页</span>
    </router-link>
    
    <router-link to="/bookmanage" class="menu-item" :class="{ active: isActive('/bookmanage') }">
      <div class="menu-icon-wrapper">
        <i class="el-icon-notebook-2 menu-icon"></i>
      </div>
      <span class="menu-title">图书管理</span>
    </router-link>
    
    <router-link to="/bookmanage/bookinfo" class="menu-item" :class="{ active: isActive('/bookmanage/bookinfo') }">
      <div class="menu-icon-wrapper sub-icon">
        <i class="el-icon-search menu-icon"></i>
      </div>
      <span class="menu-title">图书信息管理</span>
    </router-link>
    
    <router-link to="/bookmanage/booktype" class="menu-item" :class="{ active: isActive('/bookmanage/booktype') }">
      <div class="menu-icon-wrapper sub-icon">
        <i class="el-icon-collection menu-icon"></i>
      </div>
      <span class="menu-title">图书类型管理</span>
    </router-link>
    
    <router-link to="/bookmanage/borrow" class="menu-item" :class="{ active: isActive('/bookmanage/borrow') }">
      <div class="menu-icon-wrapper sub-icon">
        <i class="el-icon-tickets menu-icon"></i>
      </div>
      <span class="menu-title">借阅信息管理</span>
    </router-link>
    
    <router-link to="/other" class="menu-item" :class="{ active: isActive('/other') }">
      <div class="menu-icon-wrapper">
        <i class="el-icon-more-outline menu-icon"></i>
      </div>
      <span class="menu-title">其他管理</span>
    </router-link>
    
    <router-link to="/other/user" class="menu-item" :class="{ active: isActive('/other/user') }">
      <div class="menu-icon-wrapper sub-icon">
        <i class="el-icon-user menu-icon"></i>
      </div>
      <span class="menu-title">用户管理</span>
    </router-link>
    
    <router-link to="/other/password" class="menu-item" :class="{ active: isActive('/other/password') }">
      <div class="menu-icon-wrapper sub-icon">
        <i class="el-icon-lock menu-icon"></i>
      </div>
      <span class="menu-title">密码更改</span>
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'CustomMenu',
  methods: {
    isActive(path) {
      return this.$route.path.startsWith(path);
    }
  }
}
</script>

<style lang="scss" scoped>
.custom-menu {
  padding: 10px 0;
  
  .menu-item {
    display: flex;
    align-items: center;
    padding: 12px 20px;
    text-decoration: none;
    color: #414750;
    transition: all 0.3s;
    margin: 4px 0;
    border-radius: 0 6px 6px 0;
    margin-right: 12px;
    border-left: 4px solid transparent;
    
    &:hover {
      background-color: #e6f2ff;
      .menu-icon-wrapper {
        transform: scale(1.05);
      }
    }
    
    &.active {
      background-color: #e6f2ff;
      color: #1890FF;
      font-weight: 600;
      border-left: 4px solid #1890FF;
      
      .menu-icon-wrapper {
        background: rgba(24, 144, 255, 0.15);
      }
      
      .menu-icon {
        color: #1890FF;
      }
    }
  }
  
  .menu-icon-wrapper {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 24px;
    height: 24px;
    background: rgba(24, 144, 255, 0.1);
    border-radius: 6px;
    padding: 6px;
    transition: all 0.3s;
    margin-right: 12px;
    
    &.sub-icon {
      margin-left: 20px;
    }
  }
  
  .menu-icon {
    font-size: 18px;
    color: inherit;
  }
  
  .menu-title {
    font-size: 14px;
  }
}
</style> 