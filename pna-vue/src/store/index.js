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
    userId: null,
    avatar: null,
    userName: null,
    email: null,
  },
  mutations: {

    changeToken(state, token) {
      state.currentSelect.token = token;
    },

    login(state, user) {
      state.userId = user.id;
      state.userName = user.userName;
      state.email = user.email;
    },

    logout(state) {
      state.userId = null;
      state.avatar = null;
      state.userName = null;
      state.email = null;
    },

    saveLoginUrl(state, url) {
      state.loginUrl = url;
    },
    saveEmail(state, email) {
      state.email = email;
    },

    updateUserInfo(state, user) {
      state.userName = user.nickname;
    },

    updateAvatar(state, avatar) {
      state.avatar = avatar;
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
