// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Antd from 'ant-design-vue'
import App from './App'
import 'ant-design-vue/dist/antd.css'
import router from './router'
import VueContextMenu from 'vue-contextmenu'
import vcolorpicker from 'vcolorpicker'

import store from "./store"
import dayjs from "dayjs";
import vuetify from "./plugins/vuetify";

Vue.config.productionTip = false

Vue.use(Antd)
Vue.use(VueContextMenu)
Vue.use(vcolorpicker)


Vue.filter("date", function(value) {
  return dayjs(value).format("YYYY-MM-DD");
});

Vue.filter("year", function(value) {
  return dayjs(value).format("YYYY");
});

Vue.filter("hour", function(value) {
  return dayjs(value).format("HH:mm:ss");
});
Vue.filter("datetime", function(value) {
  return dayjs(value).format("YYYY-MM-DD HH:mm:ss");
});

Vue.filter("num", function(value) {
  if (value >= 1000) {
    return (value / 1000).toFixed(1) + "k";
  }
  return value;
});
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  vuetify,
  components: { App },
  template: '<App/>',
  beforeCreate() {
    Vue.prototype.$bus=this;
  }
})
