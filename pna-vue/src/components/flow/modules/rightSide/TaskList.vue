<template>
  <div
    v-infinite-scroll="handleInfiniteOnLoad"
    class="demo-infinite-container"
    :infinite-scroll-disabled="busy"
    :infinite-scroll-distance="10"
    style="padding: 0 10px; height:450px;align-self: stretch;flex-grow: 1"
  >
    <a-button @click="refresh" type="primary">刷新列表</a-button>
    <a-list :data-source="data" :locale="{emptyText:'当前没有执行中的任务'}">
        <a-list-item slot="renderItem"  slot-scope="{ id,algName,status,ownerName,createTime }" >
          <a-list-item-meta :description="createTime|datetime">
            <v-card-text slot="title">{{ algName }}
              <template v-if="status==2">
                <a-icon type="check-circle" theme="twoTone" two-tone-color="#52c41a" />
                运行成功
              </template>
              <template v-else-if="status==1">
                <a-icon type="clock-circle" theme="twoTone" two-tone-color="#52c41a"/>
                运行中
              </template>
              <template v-else-if="status==0">
                <a-icon type="clock-circle" theme="twoTone" two-tone-color="#FFCC00"/>
                等待执行中
              </template>
              <template v-else>
                <a-icon type="info-circle" theme="twoTone" two-tone-color="red"/>
                运行失败
              </template>

            </v-card-text>
            <v-card-text slot="title">{{ ownerName }}</v-card-text>
          </a-list-item-meta>
          <a-button v-if="$store.state.userInfo.userName==ownerName&&status==0" type="link" @click="cancelTask(id)">Cancel</a-button>
        </a-list-item>
      <a-spin v-if="loading" class="demo-loading"/>
    </a-list>
  </div>

</template>

<script>
import infiniteScroll from 'vue-infinite-scroll';
import {RecycleScroller} from 'vue-virtual-scroller';
import 'vue-virtual-scroller/dist/vue-virtual-scroller.css';
import {taskList,cancelTask} from "../../util/FetchData";

export default {
  name: "TaskList",
  directives: {infiniteScroll},
  components: {
    RecycleScroller,
  },
  props:{
    userName:{
      type:String,
      default:'all',
    }
  },
  data() {
    return {
      data: [],
      loading: false,
      busy: false,
    }
  },
  methods: {
    refresh() {
      this.handleInfiniteOnLoad()
    },
    cancelTask(id){
      cancelTask(id).then(({data})=>{
        if (data.success) {
          this.$message.success('取消成功')
        }else{
          this.$message.error('取消失败，请检查任务状态');
        }
      })
    },
    fetchData(callback) {
      taskList(this.userName).then(({data})=>{
        if (data.success) {
          this.$message.success("刷新成功")
          callback(data);
        }else{
          this.$message.error('刷新失败');
        }
      })
    },
    handleInfiniteOnLoad() {
      const data = this.data;
      this.loading = true;
      this.fetchData(res => {
        this.data = data.concat(res.data).map((item, index) => ({ ...item, index }));
        this.loading = false;
      });
    },
  },
  computed:{

  }
}
</script>

<style lang="scss" scoped>
$primary-color: #409EFF;



.demo-infinite-container {
  border-radius: 4px;
  overflow: auto;
  padding: 8px 24px;
  height: 450px;
}
</style>
