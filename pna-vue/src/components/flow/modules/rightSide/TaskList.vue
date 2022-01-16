<template>
  <div
    v-infinite-scroll="handleInfiniteOnLoad"
    class="demo-infinite-container"
    :infinite-scroll-disabled="busy"
    :infinite-scroll-distance="10"
    style="padding: 0 10px;align-self: stretch;flex-grow: 1"
  >
    <a-list :data-source="data" :locale="{emptyText:'当前没有执行中的任务'}">
        <a-list-item slot="renderItem"  slot-scope="{ id,subTime,algName,status,owner }" >
          <a-list-item-meta :description="subTime|datetime">
            <v-card-text slot="title">{{ algName }}
              <template v-if="status=='success'">
                <a-icon type="check-circle" theme="twoTone" two-tone-color="#52c41a" />
              </template>
              <template v-else-if="status=='running'">
                <a-icon type="clock-circle" theme="twoTone" two-tone-color="#52c41a"/>
              </template>
              <template v-else-if="status=='error'">
                <a-icon type="info-circle" theme="twoTone" two-tone-color="red"/>
              </template>
              <template v-else>
                <a-icon type="clock-circle" theme="twoTone" two-tone-color="#FFCC00"/>
              </template>
              {{ status }}
            </v-card-text>
            <v-card-text slot="title">{{ owner }}</v-card-text>
          </a-list-item-meta>
          <a-button v-if="isOwner" type="link" @click="cancelTask(id)">Cancel</a-button>
        </a-list-item>
      <a-spin v-if="loading" class="demo-loading"/>
    </a-list>
  </div>

</template>

<script>
import infiniteScroll from 'vue-infinite-scroll';
import {RecycleScroller} from 'vue-virtual-scroller';
import 'vue-virtual-scroller/dist/vue-virtual-scroller.css';

export default {
  name: "TaskList",
  directives: {infiniteScroll},
  components: {
    RecycleScroller,
  },
  data() {
    return {
      data: [{
        id: 0,
        owner:"黄蓉",
        algName: "可达图",
        subTime: new Date(),
        status: "error"
      }, {
        id: 1,
        owner:"sadads",
        algName: "ew",
        subTime: new Date(),
        status: "success"

      }, {
        id: 2,
        owner:"曦寒",
        algName: "ew",
        subTime: new Date(),
        status: "running"
      }, {
        id: 3,
        owner:"",
        algName: "ew",
        subTime: new Date(),
        status: "waiting"
      },
        {
          id: 4,
          owner:"",
          algName: "asdf",
          subTime: new Date(),
          status: "waiting"
        },
        {
          id: 5,
          owner:"",
          algName: "asdf",
          subTime: new Date(),
          status: "waiting"
        },
        {
          id: 6,
          algName: "asdf",
          subTime: new Date(),
          status: "waiting"
        },
        {
          id: 7,
          algName: "asdf",
          subTime: new Date(),
          status: "waiting"
        },
        {
          id: 8,
          algName: "asdf",
          subTime: new Date(),
          status: "正在运行"
        },
        {
          id: 9,
          algName: "asdf",
          subTime: new Date(),
          status: "正在运行"
        },{
          id: 10,
          algName: "asdf",
          subTime: new Date(),
          status: "正在运行"
        },{
          id: 11,
          algName: "asdf",
          subTime: new Date(),
          status: "正在运行"
        },{
          id: 21,
          algName: "asdf",
          subTime: new Date(),
          status: "正在运行"
        }],
      isOwner: true,
      loading: false,
      busy: false,
    }
  },
  methods: {
    cancelTask(id){
      //todo 取消任务执行
    },
    fetchData(callback) {
      reqwest({
        url: "fakeDataUrl",
        type: 'json',
        method: 'get',
        contentType: 'application/json',
        success: res => {
          callback(res);
        },
      });
    },
    handleInfiniteOnLoad() {
      const data = this.data;
      this.loading = true;
      if (data.length > 0) {
        this.$message.success('获取任务列表成功');
        this.busy = true;
        this.loading = false;
        this.loading = false
        return;
      }
      // this.fetchData(res => {
      //   this.data = data.concat(res.results).map((item, index) => ({ ...item, index }));
      //   this.loading = false;
      // });
    },
  }
}
</script>

<style scoped>
.demo-infinite-container {
  border-radius: 4px;
  overflow: auto;
  padding: 8px 24px;
  height: 450px;
}
</style>
