import Vue from 'vue'
import Router from 'vue-router'
import FlowDesigner from '../components/flow/FlowDesigner'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Pna',
      component: resolve => require(["../components/flow/FlowDesigner"],resolve),
      meta:{
        title: "PNA-WEB"
      },
    },
    // {
    //   path: '/user',
    //   component: resolve => require(["../components/admin/views/User.vue"], resolve),
    //   meta: {
    //     title: "个人中心"
    //   }
    // }

  ]
})
