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
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/cigaretteFront/dataVisualization/dataVisualization'),
        name: 'DataVisualization',
        meta: { title: '数据可视化' }
      }
    ]
  },
  {
    path: '/showMapLocation',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/component/showMapLocation'),
        name: 'ShowMapLocation',
        meta: { title: '地图位置' }
      }
    ]
  },
  {
    path: '/cigaretteFront',
    component: () => import('@/views/cigaretteFront/menu/menu'),
    redirect: '/cigaretteIndex',
    children: [
      {
        path: '/cigaretteIndex',
        component: () => import('@/views/cigaretteFront/menu/cigaretteIndex'),
        name: 'CigaretteIndex',
        meta: { title: '烟草前端首页' }
      },
      {
        path: '/uploadCase',
        component: () => import('@/views/cigaretteFront/caution/uploadCase'),
        name: 'UploadCase',
        meta: { title: '上传案例' }
      },
      {
        path: '/uploadCaution',
        component: () => import('@/views/cigaretteFront/caution/uploadCaution'),
        name: 'UploadCaution',
        meta: { title: '上传警示' }
      },
      {
        path: '/uploadPersonnel',
        component: () => import('@/views/cigaretteFront/caution/uploadPersonnel'),
        name: 'UploadPersonnel',
        meta: { title: '上传人员' }
      },
      // 其他子路由...
      {
        path: '/profile',
        component: () => import('@/views/cigaretteFront/menu/profile/profile'),
        name: 'Profile',
        meta: { title: '个人资料' }
      }
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
