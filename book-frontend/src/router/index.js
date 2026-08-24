import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/register',
    component: () => import('@/views/register/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'el-icon-s-grid' }
    }]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [

  {
    path: '/bookmanage',
    name: 'Bookmanage',
    component: Layout,
    redirect: '/bookmanage/bookinfo',
    alwaysShow: true,
    meta: {
      title: '图书管理',
      icon: 'el-icon-book'
    },
    children: [
      {
        path: 'bookinfo',
        name: 'Bookinfo',
        component: () => import('@/views/bookinfo/index'),
        meta: {
          title: '图书信息管理',
          icon: 'el-icon-notebook-1',
          roles: ['admin', 'reader'],
          noCache: true
        }
      },
      {
        path: 'booktype',
        name: 'Booktype',
        component: () => import('@/views/booktype/index'),
        meta: {
          title: '图书类型管理',
          icon: 'el-icon-collection-tag',
          roles: ['admin'],
          noCache: true
        }
      },
      {
        path: 'borrow',
        name: 'Borrow',
        component: () => import('@/views/borrow/index'),
        meta: {
          title: '借阅信息管理',
          icon: 'el-icon-document',
          roles: ['admin', 'reader'],
          noCache: true
        }
      },
    ]
  },

  {
    path: '/other',
    name: 'Other',
    component: Layout,
    redirect: '/other/user',
    alwaysShow: true,
    meta: {
      title: '其他管理',
      icon: 'el-icon-setting'
    },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index'),
        meta: {
          title: '用户管理',
          icon: 'el-icon-avatar',
          roles: ['admin'],
          noCache: true
        }
      },
      {
        path: 'password',
        name: 'Password',
        component: () => import('@/views/password/index'),
        meta: {
          title: '密码更改',
          icon: 'el-icon-unlock',
          roles: ['admin', 'reader'],
          noCache: true
        }
      }
    ]
  },

  // 逾期管理（仅管理员）
  {
    path: '/overdue',
    name: 'Overdue',
    component: Layout,
    redirect: '/overdue/index',
    alwaysShow: false,
    children: [
      {
        path: 'index',
        name: 'OverdueIndex',
        component: () => import('@/views/overdue/index'),
        meta: {
          title: '逾期管理',
          icon: 'el-icon-warning',
          roles: ['admin'],
          noCache: true
        }
      }
    ]
  },

  // 缴费管理（仅管理员）
  {
    path: '/payment',
    name: 'Payment',
    component: Layout,
    redirect: '/payment/index',
    alwaysShow: false,
    children: [
      {
        path: 'index',
        name: 'PaymentIndex',
        component: () => import('@/views/payment/index'),
        meta: {
          title: '缴费管理',
          icon: 'el-icon-money',
          roles: ['admin'],
          noCache: true
        }
      }
    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router