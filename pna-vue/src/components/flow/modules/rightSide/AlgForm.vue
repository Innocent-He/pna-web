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
            v-decorator="['slider']"
            :marks="{ 0: '30min', 20: '1h', 40: '2h', 60: '5h', 80: '1d', 100: '无限制' }"
          />
        </a-form-model-item>
        <a-form-model-item>
          <a-checkbox-group v-model="algReq.enableEmail">
            <a-checkbox v-if="$store.state.userId" name="enableEmail">
              邮箱通知结果
            </a-checkbox>
            <a-checkbox v-else disabled name="enableEmail" style="display: block;width: 100%">
              邮箱通知结果,此功能需要登录账号
            </a-checkbox>
          </a-checkbox-group>
        </a-form-model-item>
        <a-button type="primary" @click="submit" class="alg-form-button">
          提交
        </a-button>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AlgForm",
  props: ['flowData','activeAlg'],
  data() {
    return {
      algReq: {
        params: {
          step: null,
        },
        algName:'',
        petri: this.flowData
      },
      algVisible: false,
      enableEmail: false,
    }
  },
  methods: {
    submit(e) {
      e.preventDefault();
      let that = this;
      that.algReq.petri.attr.ownerName=that.$store.state.userName;
      axios.post("/api/algReq",that.algReq).then(({data}) => {
        if (data.success) {
          that.$message.success(data.message);
          that.algVisible = false;
        } else {
          this.$message.error(data.message);
        }
      })
    },
  },
  watch:{
    activeAlg(val){
      this.algReq.algName=val;
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
