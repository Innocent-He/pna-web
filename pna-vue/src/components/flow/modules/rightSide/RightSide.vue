<template>
  <a-layout-sider width="350" theme="light" class="attr-area">
    <a-tabs size="small" defaultActiveKey="alg" :activeKey="activeKey" style="height: 100%">
      <a-tab-pane key="alg">
        <span slot="tab" @click="activeKey='alg'">
          <a-icon type="calculator" />
          算法
        </span>
        <Algorithm :flowData="flowData"></Algorithm>
      </a-tab-pane>
      <a-tab-pane key="admin" id="adminTab">
        <span slot="tab" @click="activeKey='admin'">
          <a-icon type="user" />
          用户
        </span>
        <Admin id="admin"></Admin>
      </a-tab-pane>
      <a-tab-pane key="list">
        <span slot="tab" @click="activeKey='list'">
          <a-icon type="unordered-list" />
          执行队列
        </span>
        <TaskList></TaskList>
      </a-tab-pane>
    </a-tabs>
  </a-layout-sider>
</template>

<script>
import Admin from "./Admin";
import TaskList from "./TaskList";
import AlgForm from "./AlgForm"
import Algorithm from "./Algorithm";
export default {
  name: "RightSide",
  props:['flowData'],
  components:{
    Algorithm,
    TaskList,
    Admin,
    AlgForm
  },
  data(){
    return{
      activeKey:"alg",
    }
  }
}
</script>

<style scoped>

#admin {
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>
