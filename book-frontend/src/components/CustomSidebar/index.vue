<template>
  <div class="custom-sidebar">
    <!-- 首页菜单项 -->
    <div class="menu-item" :class="{ active: $route.path === '/dashboard' }" @click="navigate('/dashboard')">
      <div class="menu-icon-wrapper">
        <i class="el-icon-s-grid menu-icon"></i>
      </div>
      <span class="menu-title">首页</span>
    </div>
    
    <!-- 图书管理菜单组 -->
    <div class="menu-group">
      <div class="menu-item parent-menu" 
           :class="{ active: $route.path.includes('/bookmanage'), expanded: bookManageExpanded }" 
           @click="toggleExpand('bookManage')">
        <div class="menu-icon-wrapper">
          <i class="el-icon-reading menu-icon"></i>
        </div>
        <span class="menu-title">图书管理</span>
        <i class="el-icon-arrow-down menu-arrow"></i>
      </div>
      
      <div class="submenu-container" :class="{ expanded: bookManageExpanded }">
        <div class="menu-item sub-menu" 
             :class="{ active: $route.path.includes('/bookmanage/bookinfo') }" 
             @click.stop="navigate('/bookmanage/bookinfo')">
          <div class="menu-icon-wrapper">
            <i class="el-icon-notebook-1 menu-icon"></i>
          </div>
          <span class="menu-title">图书信息管理</span>
        </div>
        
        <div v-if="isAdmin" class="menu-item sub-menu" 
             :class="{ active: $route.path.includes('/bookmanage/booktype') }" 
             @click.stop="navigate('/bookmanage/booktype')">
          <div class="menu-icon-wrapper">
            <i class="el-icon-collection-tag menu-icon"></i>
          </div>
          <span class="menu-title">图书类型管理</span>
        </div>
        
        <div class="menu-item sub-menu" 
             :class="{ active: $route.path.includes('/bookmanage/borrow') }" 
             @click.stop="navigate('/bookmanage/borrow')">
          <div class="menu-icon-wrapper">
            <i class="el-icon-document menu-icon"></i>
          </div>
          <span class="menu-title">借阅信息管理</span>
        </div>
      </div>
    </div>
    
    <!-- 其他管理菜单组 -->
    <div v-if="isAdmin" class="menu-group">
      <div class="menu-item parent-menu" 
           :class="{ active: $route.path.includes('/other'), expanded: otherManageExpanded }" 
           @click="toggleExpand('otherManage')">
        <div class="menu-icon-wrapper">
          <i class="el-icon-setting menu-icon"></i>
        </div>
        <span class="menu-title">其他管理</span>
        <i class="el-icon-arrow-down menu-arrow"></i>
      </div>
      
      <div class="submenu-container" :class="{ expanded: otherManageExpanded }">
        <div class="menu-item sub-menu" 
             :class="{ active: $route.path.includes('/other/user') }" 
             @click.stop="navigate('/other/user')">
          <div class="menu-icon-wrapper">
            <i class="el-icon-user-solid menu-icon"></i>
          </div>
          <span class="menu-title">用户管理</span>
        </div>
        
        <div class="menu-item sub-menu" 
             :class="{ active: $route.path.includes('/other/password') }" 
             @click.stop="navigate('/other/password')">
          <div class="menu-icon-wrapper">
            <i class="el-icon-key menu-icon"></i>
          </div>
          <span class="menu-title">密码更改</span>
        </div>
      </div>
    </div>
    
    <!-- 普通用户的密码更改菜单项 -->
    <div v-if="!isAdmin" class="menu-item" 
         :class="{ active: $route.path.includes('/other/password') }" 
         @click="navigate('/other/password')">
      <div class="menu-icon-wrapper">
        <i class="el-icon-key menu-icon"></i>
      </div>
      <span class="menu-title">密码更改</span>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'CustomSidebar',
  data() {
    return {
      bookManageExpanded: false,
      otherManageExpanded: false
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ]),
    isAdmin() {
      return this.roles.includes('admin')
    }
  },
  methods: {
    navigate(path) {
      this.$router.push(path);
    },
    isSubActive(subs) {
      return subs.some(sub => this.$route.path.includes(sub));
    },
    toggleExpand(menu) {
      if (menu === 'bookManage') {
        this.bookManageExpanded = !this.bookManageExpanded;
      } else if (menu === 'otherManage') {
        this.otherManageExpanded = !this.otherManageExpanded;
      }
    }
  },
  created() {
    // 自动展开当前路径所在的菜单
    if (this.$route.path.includes('/bookmanage')) {
      this.bookManageExpanded = true;
    }
    if (this.$route.path.includes('/other') && this.isAdmin) {
      this.otherManageExpanded = true;
    }
  }
}
</script>

<style lang="scss" scoped>
.custom-sidebar {
  height: 100%;
  
  .menu-group {
    margin-bottom: 4px;
    position: relative;
  }
  
  .menu-item {
    display: flex;
    align-items: center;
    height: 56px;
    padding: 0 20px;
    cursor: pointer;
    transition: all 0.3s;
    position: relative;
    border-left: 4px solid transparent;
    margin: 4px 0;
    border-radius: 0 6px 6px 0;
    margin-right: 12px;
    
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
    
    &.parent-menu {
      &.expanded {
        background-color: #f0f7ff;
        
        .menu-arrow {
          transform: rotate(180deg);
          color: #1890FF;
        }
      }
    }
    
    &.sub-menu {
      height: 46px;
      padding-left: 48px;
      margin: 2px 12px 2px 0;
      border-radius: 0 6px 6px 0;
      
      &:hover {
        background-color: #e6f2ff;
      }
      
      .menu-icon-wrapper {
        width: 28px;
        height: 28px;
      }
      
      .menu-icon {
        font-size: 16px;
      }
    }
  }
  
  .submenu-container {
    max-height: 0;
    overflow: hidden;
    transition: max-height 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    background-color: rgba(240, 245, 255, 0.5);
    border-radius: 0 0 8px 0;
    margin-top: -4px;
    
    &.expanded {
      max-height: 200px; // 足够容纳子菜单
      padding-top: 4px;
      padding-bottom: 4px;
    }
  }
  
  .menu-icon-wrapper {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    background: rgba(24, 144, 255, 0.1);
    border-radius: 8px;
    transition: all 0.3s;
    margin-right: 12px;
  }
  
  .menu-icon {
    font-size: 18px;
    color: #414750;
  }
  
  .menu-title {
    font-size: 14px;
    line-height: 1.5;
    flex: 1;
  }
  
  .menu-arrow {
    font-size: 12px;
    transition: transform 0.3s;
    margin-left: 8px;
    color: #9da5b1;
  }
}
</style> 