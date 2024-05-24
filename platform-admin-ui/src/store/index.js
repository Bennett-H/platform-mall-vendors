import Vue from 'vue'
import Vuex from 'vuex'
import cloneDeep from 'lodash/cloneDeep'
import common from './modules/common'
import user from './modules/user'
import message from './modules/message'
import wxAccount from './modules/wxAccount'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    common,
    user,
    message,
    wxAccount
  },
  mutations: {
    // 重置vuex本地储存状态
    resetStore (state) {
      Object.keys(state).forEach((key) => {
        state[key] = cloneDeep(window.SITE_CONFIG.storeState[key])
      })
    }
  },
  strict: false
})
