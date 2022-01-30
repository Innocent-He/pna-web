import Vue from 'vue'
import Router from 'vue-router'
import FlowDesigner from '../components/FlowDesigner'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Pna',
      component: resolve => require(["../components/FlowDesigner"],resolve),
      meta:{
        title: "PNA-WEB"
      },
    },
  ]
})
