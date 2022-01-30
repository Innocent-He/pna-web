<template>
  <div>
    <a-tabs size="small" defaultActiveKey="flow-attr" :activeKey="activeKey">
      <a-tab-pane key="flow-attr">
        <span slot="tab">
          <a-icon type="cluster" />
          Petri属性
        </span>
        <a-form layout="horizontal">
          <a-form-item
            label="名称"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input :value="flowData.attr.name" @change="petriNameChange" />
          </a-form-item>
        </a-form>
        <a-form layout="horizontal">
          <a-form-item
            label="创建时间"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input :value="flowData.attr.createTime" disabled />
          </a-form-item>
        </a-form>
        <a-form layout="horizontal">
          <a-form-item
            label="描述"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input :value="flowData.attr.des" @change="petriDesChange" />
          </a-form-item>
        </a-form>
      </a-tab-pane>
      <a-tab-pane key="node-attr">
        <span slot="tab">
          <a-icon type="profile" />
          库所属性
        </span>
        <template
          v-if="currentSelect.type && currentSelect.type.indexOf('place') != -1"
        >
          <a-form layout="horizontal">
            <a-form-item
              label="类型"
              :label-col="formItemLayout.labelCol"
              :wrapper-col="formItemLayout.wrapperCol"
            >
              <a-tag color="purple">{{ currentSelect.type }}</a-tag>
            </a-form-item>
            <a-form-item
              label="id"
              :label-col="formItemLayout.labelCol"
              :wrapper-col="formItemLayout.wrapperCol"
            >
              <a-input :value="currentSelect.id" disabled />
            </a-form-item>
            <a-form-item
              label="token"
              :label-col="formItemLayout.labelCol"
              :wrapper-col="formItemLayout.wrapperCol"
            >
              <a-input
                placeholder="请输入token数量"
                :value="currentSelect.token"
                @change="tokenChange"
              />

            </a-form-item>
            <a-form-item
              label="类型"
              :label-col="formItemLayout.labelCol"
              :wrapper-col="formItemLayout.wrapperCol"
            >
              <a-input
                :value="currentSelect.nodeName"
                placeholder="节点类型"
                disabled
              />
            </a-form-item>
          </a-form>
        </template>
      </a-tab-pane>
      <a-tab-pane key="tran-attr">
        <span slot="tab">
          <a-icon type="profile" />
          变迁属性
        </span>
        <template v-if="currentSelect.type == 'transaction'">
          <a-form layout="horizontal">
            <a-form-item
              label="类型"
              :label-col="formItemLayout.labelCol"
              :wrapper-col="formItemLayout.wrapperCol"
            >
              <a-tag color="purple">{{ currentSelect.type }}</a-tag>
            </a-form-item>
            <a-form-item
              label="id"
              :label-col="formItemLayout.labelCol"
              :wrapper-col="formItemLayout.wrapperCol"
            >
              <a-input :value="currentSelect.id" disabled />
            </a-form-item>
          </a-form>
        </template>
      </a-tab-pane>
      <a-tab-pane key="link-attr">
        <span slot="tab">
          <a-icon type="branches" />
          连线属性
        </span>
        <a-form layout="horizontal">
          <a-form-item
            label="id"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input :value="currentSelect.id" disabled />
          </a-form-item>
          <a-form-item
            label="源节点"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input :value="currentSelect.sourceId" disabled />
          </a-form-item>
          <a-form-item
            label="目标节点"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input :value="currentSelect.targetId" disabled />
          </a-form-item>
          <a-form-item
            label="弧权值"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input :value="currentSelect.weight" @change="linkWeightChange" />
          </a-form-item>
        </a-form>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>


export default {
  props:['flowData',"currentSelect"],
  data() {
    return {
      formItemLayout: {
        labelCol: { span: 6 },
        wrapperCol: { span: 16 }
      },
      activeKey: "flow-attr",
    };
  },
  methods: {
    tokenChange(e) {
      this.$bus.$emit('changeToken',e.target.value)
    },
    linkWeightChange(e) {
      this.$bus.$emit('changeLinkWeight',e.target.value)
    },
    petriDesChange(e) {
      this.$bus.$emit('changeAttrDes',e.target.value)
    },
    petriNameChange(e) {
      this.$bus.$emit('changeAttrName',e.target.value)
    }
  },
  watch: {
    currentSelect: {
      handler(val){
        if (!val.type) {
          this.activeKey = "flow-attr";
        } else if (val.type == "link") {
          this.activeKey = "link-attr";
        } else if (val.type == "transaction") {
          this.activeKey = "tran-attr";
        } else {
          this.activeKey = "node-attr";
        }
      },
      deep:true,
    },

  }
};
</script>

<style lang="scss">
.ant-form-item {
  margin-bottom: 6px;
}
.attr-area {
  border-left: 1px solid #e4e7ed;
}
</style>
