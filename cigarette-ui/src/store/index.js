import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import dict from './modules/dict'
import user from './modules/user'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
import settings from './modules/settings'
import getters from './getters'
import scnSettings from './modules/scnSettings'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    dict,
    user,
    tagsView,
    permission,
    settings,
    // 项目A模块（强制命名空间隔离）
    scnSettings: {
      ...scnSettings,
      namespaced: true  // 关键！即使文件本身没写namespaced也强制启用
    }
  },
  getters
})

export default store
