<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <div class="logo-icon collapsed">
          <i class="el-icon-reading"></i>
        </div>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <div class="logo-container">
          <div class="logo-icon">
            <i class="el-icon-reading"></i>
          </div>
          <h1 class="sidebar-title">图书管理系统</h1>
        </div>
      </router-link>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 0.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 70px;
  background: linear-gradient(135deg, #1e3a8a, #1e40af);
  text-align: center;
  overflow: hidden;
  box-shadow: 0 1px 0 0 rgba(0, 0, 0, 0.05);

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .logo-container {
      display: flex;
      align-items: center;
      width: 100%;
      padding-left: 20px;
      height: 100%;
    }

    .logo-icon {
      width: 40px;
      height: 40px;
      min-width: 40px;
      background: rgba(255, 255, 255, 0.15);
      border-radius: 8px;
      margin-right: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1), 0 0 15px rgba(255, 255, 255, 0.1) inset;
      backdrop-filter: blur(5px);
      
      &.collapsed {
        margin: 0;
        width: 40px;
        height: 40px;
      }
      
      i {
        font-size: 24px;
        color: #fff;
      }
    }

    & .sidebar-title {
      margin: 0;
      color: #fff;
      font-weight: 600;
      font-size: 18px;
      font-family: "Microsoft YaHei", Arial, sans-serif;
      letter-spacing: 1px;
      white-space: nowrap;
      line-height: 40px;
      display: flex;
      align-items: center;
      height: 40px;
      text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
    }
  }
}
</style>
