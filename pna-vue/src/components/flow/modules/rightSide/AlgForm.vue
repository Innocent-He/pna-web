<template>
  <div>
    <a-modal
      :title="'任务提交'"
      :visible="algVisible"
      @cancel="algVisible=false"
      :footer="null"
    >
      <a-form
        id="components-form-demo-validate-other"
        :form="form"
        @submit="handleSubmit"
      >
        <slot name="alg" :data="params"></slot>
        <a-form-item label="最长等待时间">
          <a-slider
            v-decorator="['slider']"
            :marks="{ 0: '30min', 20: '1h', 40: '2h', 60: '5h', 80: '1d', 100: '无限制' }"
          />
        </a-form-item>
        <a-form-item >
          <a-checkbox-group
            v-decorator="['checkbox-group', { initialValue: ['A', 'B'] }]"
            style="width: 100%;"
          >
            <a-checkbox v-if="$store.state.userId" name="enableEmail" v-model="enableEmail">
              邮箱通知结果
            </a-checkbox>
            <a-checkbox v-else disabled name="enableEmail" v-model="enableEmail" style="display: block;width: 100%">
              邮箱通知结果,此功能需要登录账号
            </a-checkbox>

          </a-checkbox-group>
        </a-form-item>
        <a-button type="primary" html-type="submit" class="alg-form-button">
          提交
        </a-button>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
export default {
  beforeCreate() {
    this.form = this.$form.createForm(this, {name: 'submit_alg'});
  },
  name: "AlgForm",
  data() {
    return {
      params:{},
      algVisible: false,
      enableEmail:false,
    }
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      let that = this
      that.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values);
          //todo 请求后端做登录
          that.$store.commit("login", {
            "avatar": "asdas"
          })
          that.$store.commit("closeModel");
        }
      });
    },
  }
}
</script>

<style scoped>
.alg-form-button{
  display: block;
  margin: 10px auto;
}
</style>
