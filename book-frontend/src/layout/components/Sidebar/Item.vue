<script>
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  },
  render(h, context) {
    const { icon, title } = context.props
    const vnodes = []

    if (icon) {
      if (icon.includes('el-icon')) {
        vnodes.push(<div class="menu-icon-wrapper"><i class={[icon, 'menu-icon']} /></div>)
      } else if(icon.includes('icon-r')) {
        vnodes.push(<div class="menu-icon-wrapper"><i class={['iconfont', icon, 'menu-icon']} /></div>)
      } else {
        vnodes.push(<div class="menu-icon-wrapper"><svg-icon icon-class={icon} class-name="menu-icon" /></div>)
      }
    } else {
      vnodes.push(<div class="menu-icon-wrapper"><i class={['el-icon-s-data', 'menu-icon']} /></div>)
    }

    if (title) {
      vnodes.push(<span slot='title' class="menu-title">{(title)}</span>)
    }
    return vnodes
  }
}
</script>

<style lang="scss" scoped>
.menu-icon-wrapper {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  margin-right: 12px;
  background: rgba(24, 144, 255, 0.1);
  border-radius: 6px;
  padding: 6px;
  transition: all 0.3s;
}

.menu-icon {
  color: inherit;
  font-size: 18px;
  text-align: center;
  vertical-align: middle;
  transition: all 0.3s;
}

.menu-title {
  vertical-align: middle;
  display: inline-block;
  line-height: 24px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s;
}

.el-menu-item.is-active .menu-icon-wrapper {
  background: rgba(24, 144, 255, 0.15);
}

.el-menu-item.is-active .menu-icon {
  color: #1890FF;
}

.el-menu-item.is-active .menu-title {
  color: #1890FF;
  font-weight: 600;
}

.el-menu-item:hover .menu-icon-wrapper,
.el-submenu__title:hover .menu-icon-wrapper {
  transform: scale(1.05);
}
</style>
