import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import {flowConfig} from "../components/flow/config/args-config";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    searchFlag: false,
    loginFlag: false,
    registerFlag: false,
    forgetFlag: false,
    emailFlag: false,
    drawer: false,
    loginUrl: '',
    userId: null,
    avatar: null,
    nickname: null,
    intro: null,
    loginType: null,
    email: null,

  },
  mutations: {
    changeToken(state,token){
      state.currentSelect.token=token;
    },

    login(state, user) {
      state.userId = user.id;
      state.nickname = user.userName;
      state.email = user.email;
    },
    logout(state) {
      state.userId = null;
      state.avatar = null;
      state.nickname = null;
      state.intro = null;
      state.webSite = null;
      state.articleLikeSet = [];
      state.commentLikeSet = [];
      state.email = null;
      state.loginType = null;
    },
    saveLoginUrl(state, url) {
      state.loginUrl = url;
    },
    saveEmail(state, email) {
      state.email = email;
    },
    updateUserInfo(state, user) {
      state.nickname = user.nickname;
      state.intro = user.intro;
      state.webSite = user.webSite;
    },
    savePageInfo(state, pageList) {
      state.pageList = pageList;
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
    closeModel(state) {
      state.registerFlag = false;
      state.loginFlag = false;
      state.searchFlag = false;
      state.emailFlag = false;
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
