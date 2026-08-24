import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import "@/api/initialize"
import 'element-ui/lib/theme-chalk/index.css'

// 引入echarts
import * as echarts from 'echarts'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import './assets/font/iconfont.css'
import router from './router'


import '@/permission' // permission control


if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

Vue.use(ElementUI)
// 将echarts挂载到Vue原型上，方便全局使用
Vue.prototype.$echarts = echarts

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  mounted() {
  },
  store,
  render: h => h(App)
})
