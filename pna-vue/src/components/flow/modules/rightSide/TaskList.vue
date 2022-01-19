<template>
  <div
    v-infinite-scroll="handleInfiniteOnLoad"
    class="demo-infinite-container"
    :infinite-scroll-disabled="busy"
    :infinite-scroll-distance="10"
    style="padding: 0 10px; height:450px;align-self: stretch;flex-grow: 1"
  >
    <a-list :data-source="tasks" :locale="{emptyText:'当前没有执行中的任务'}">
        <a-list-item slot="renderItem"  slot-scope="{ id,result,algName,status,ownerName,createTime }" >
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
          <a-button v-if="isOwner(ownerName)&&status===0" type="link" @click="cancelTask(id)">Cancel</a-button>
          <a-button v-else-if="isOwner(ownerName)&&status===2&&algName==='generate'" type="link" @click="generateNet(result)">Generate</a-button>
          <a-button v-else-if="isOwner(ownerName)&&status===2" type="link" @click="download(result)">Download</a-button>
          <a-button v-if="isOwner(ownerName)&&(status===2||status===3)" type="link" @click="deleteTask(id)">Delete</a-button>
        </a-list-item>
      <a-spin v-if="loading" class="demo-loading"/>
    </a-list>
  </div>

</template>

<script>
import infiniteScroll from 'vue-infinite-scroll';
import {RecycleScroller} from 'vue-virtual-scroller';
import 'vue-virtual-scroller/dist/vue-virtual-scroller.css';
import {taskList,cancelTask,deleteTask} from "../../util/FetchData";

export default {
  name: "TaskList",
  directives: {infiniteScroll},
  components: {
    RecycleScroller,
  },
  props:['userName'],
  mounted() {
    let that=this;
    that.$bus.$on('clearTask',()=>{
      that.tasks.splice(0);
      that.taskReq.pageNo=1;
      that.handleInfiniteOnLoad();
    });
  },
  beforeDestroy() {
    this.$bus.$off('clearTask');
  },
  data() {
    return {
      tasks: [],
      loading: false,
      busy: false,
      taskReq:{
        pageNo:1,
        pageSize:10,
        ownerName:this.userName,
      }
    }
  },
  methods: {
    cancelTask(id){
      cancelTask(id).then(({data})=>{
        if (data.success) {
          this.$message.success('取消成功')
        }else{
          this.$message.error('取消失败，请检查任务状态');
        }
      })
    },

    handleInfiniteOnLoad() {
      let that=this;
      const originData = this.tasks;
      this.loading = true;
      taskList(that.taskReq).then(({data})=>{
        if (data.success) {
          if (data.data.totalCount <= originData.length) {
            that.$message.warning('已加载全部数据');
            that.busy=true;
            that.loading=false;
            return;
          }
          this.$message.success("刷新成功");
          this.tasks = originData.concat(data.data.content).map((item, index) => ({ ...item, index }));
          that.loading=false;
          that.taskReq.pageNo++;
        }else{
          this.$message.error('刷新失败');
        }
      })
    },
    generateNet(result) {
      this.$bus.$emit("loadFlow",result);
    },
    isOwner(algName){
      return algName===this.$store.state.userInfo.userName;
    },
    deleteTask(taskId) {
      deleteTask(taskId).then(({data})=>{
          if (data.success) {
            this.$message.success("清除成功");
            this.$bus.$emit('clearTask');
            this.handleInfiniteOnLoad()
            return;
          }
        this.$message.error("清除失败");
      })
    },
    download(result) {
      const data = `\uFEFF${result}`;
      const blob = new Blob([data], {type: "text,charset=UTF-8"});
      const downloadElement = document.createElement("a");
      // 创建下载链接
      const href = window.URL.createObjectURL(blob);
      downloadElement.href = href;
      // 下载文件名
      downloadElement.download = `result.txt`;
      document.body.appendChild(downloadElement);
      downloadElement.click();
      // 移除元素
      document.body.removeChild(downloadElement);
      // 释放blob对象
      window.URL.revokeObjectURL(href);
      this.$message.success("下载结果成功");
    }
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
