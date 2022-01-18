import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import {flowConfig} from "../components/flow/config/args-config";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    loginFlag: false,
    registerFlag: false,
    forgetFlag: false,
    userInfo:{
      userId: null,
      avatar: null,
      userName: null,
      email: null,
    }
  },
  mutations: {

    changeToken(state, token) {
      state.currentSelect.token = token;
    },

    changeUserName(state, userName) {
      state.userInfo.userName=userName;
    },

    login(state, user) {
      state.userInfo.userId = user.id;
      state.userInfo.userName = user.userName;
      state.userInfo.email = user.email;
      state.userInfo.avatar = user.avatar;
    },

    logout(state) {
      state.userInfo.userId = null;
      state.userInfo.avatar = null;
      state.userInfo.userName = null;
      state.userInfo.email = null;
    },

    saveLoginUrl(state, url) {
      state.loginUrl = url;
    },
    saveEmail(state, email) {
      state.userInfo.email = email;
    },

    updateUserInfo(state, user) {
      state.userInfo.userName = user.nickname;
    },

    updateAvatar(state, avatar) {
      state.userInfo.avatar = avatar;
    },

    closeModel(state) {
      state.registerFlag = false;
      state.loginFlag = false;
    },
  },
  actions: {},
  modules: {},
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
});
