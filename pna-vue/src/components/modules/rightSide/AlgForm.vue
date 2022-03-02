<template>
  <div>
    <a-modal
      :title="'任务提交'"
      :visible="algVisible"
      @cancel="algVisible=false"
      :footer="null"
    >
      <a-form-model
        id="components-form-demo-validate-other"
        :form="algReq"
        @submit="submit"
      >
        <slot name="alg" :data="algReq"></slot>
        <a-form-model-item label="最长等待时间">
          <a-slider
            v-model.number="algReq.timeLevel"
            :max="5"
            :step="1"
            :included="false"
            :range="false"
            :marks="{ 0: '5min', 1: '10min', 2: '30min', 3: '1hour', 4: '12hour', 5: '1day' }"
          />
        </a-form-model-item>
        <a-form-model-item>
          <a-checkbox v-if="$store.state.userInfo.email" v-model="enableEmail">
            邮箱通知结果
          </a-checkbox>
          <a-checkbox v-else :disabled="true">
            邮箱通知结果,此功能需要登录账号
          </a-checkbox>
        </a-form-model-item>
        <a-button type="primary" @click="submit" class="alg-form-button">
          提交
        </a-button>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import {algRequest} from "../../util/FetchData";

export default {
  name: "AlgForm",
  props: ['flowData', 'activeAlg'],
  data() {
    return {
      algReq: {
        params: {
          netType:'',
          tranCount:0,
          placeCount:0,
          step: null,
        },
        algName: '',
        timeLevel:0,
        petri: this.flowData,
        email: this.email,
      },
      algVisible: false,
      enableEmail: false,
    }
  },
  methods: {
    submit(e) {
      e.preventDefault();
      let that = this;
      algRequest(that.algReq).then(({data}) => {
        if (data.success) {
          that.$message.success("提交成功");
          that.algVisible = false;
        } else {
          this.$message.error(data.message);
        }
      })
    },
  },
  computed: {
    email() {
      if (this.enableEmail) {
        return this.$store.state.userInfo.email;
      }
      return '';
    }
  },
  watch: {
    activeAlg:{
      handler(val){
        this.algReq.algName = val;
      }
    },
    email: {
      handler(val) {
        this.algReq.email = val;
      },
      deep: true,
    }
  }
}
</script>

<style scoped>
.alg-form-button {
  display: block;
  margin: 10px auto;
}
</style>
