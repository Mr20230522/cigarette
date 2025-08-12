import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/login_phone',
    component: () => import('@/views/login_phone'),
    hidden: true
  },
  {
    path: '/bigScreen',
    component: () => import('@/views/bigScreen.vue'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  // 根路径重定向到 bigScreen
  {
    path: '/',
    component: Layout,
    redirect: '/bigScreen',
    hidden: true
  },
  // 首页路由
  {
    path: '/index',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/dataVisualization',
    name: 'DataVisualization',
    component: () => import('@/views/cigaretteFront/dataVisualization/dataVisualization'),

  },
  {
    path: '/showMapLocation',
    name: 'ShowMapLocation',
    component: () => import('@/views/component/showMapLocation')
  },
  {
    path: '/cigaretteFront',
    component: () => import('@/views/cigaretteFront/menu/menu'),
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: '/cigaretteIndex',
        name: 'cigaretteIndex',
        component: () => import('@/views/cigaretteFront/menu/cigaretteIndex')
      },
      {
        path: '/uploadCase',
        name: 'uploadCase',
        component: () => import('@/views/cigaretteFront/caution/uploadCase')
      },{
        path: '/uploadCaution',
        name: 'uploadCaution',
        component: () => import('@/views/cigaretteFront/caution/uploadCaution')
      },{
        path: '/uploadPersonnel',
        name: 'uploadPersonnel',
        component: () => import('@/views/cigaretteFront/caution/uploadPersonnel')
      },{
        //这里是我要删除的路由
        path: '/uploadVehicle',
        name: 'uploadVehicle',
        component: () => import('@/views/cigaretteFront/caution/uploadVehicle')
      },{
        path: '/carema',
        name: 'carema',
        component: () => import('@/views/cigaretteFront/detection/carema')
      },{
        path: '/personelInformation',
        name: 'personelInformation',
        component: () => import('@/views/cigaretteFront/detection/personelInformation')
      },{
        path: '/vehicleInformation',
        name: 'vehicleInformation',
        component: () => import('@/views/cigaretteFront/detection/vehicleInformation')
      },{
        path: '/clockLog',
        name: 'clockLog',
        component: () => import('@/views/cigaretteFront/document/clockLog')
      },{
        path: '/document',
        name: 'document',
        component: () => import('@/views/cigaretteFront/document/document')
      },{
        path: '/dutyLog',
        name: 'dutyLog',
        component: () => import('@/views/cigaretteFront/document/dutyLog')
      },{
        path: '/notice',
        name: 'notice',
        component: () => import('@/views/cigaretteFront/document/notice')
      },
      {
        path: '/profile',
        name: 'profile',
        component: () => import('@/views/cigaretteFront/menu/profile/profile'),
      },
    ]
  }
]

// 动态路由
export const dynamicRoutes = [
  // ...保持你原有的动态路由配置不变
]

// 防止连续点击多次路由报错
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
