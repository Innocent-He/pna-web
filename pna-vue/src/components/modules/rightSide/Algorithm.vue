<template>
  <div id="alg">
    <a-button id="algBtn" type="primary" @click="activeAlgModel('reach')">
      <a-icon type="share-alt"/>
      Reachable graph
    </a-button>
    <a-button id="algBtn" type="primary" @click="activeAlgModel('simphon')">
      <a-icon type="share-alt"/>
      Simphon
    </a-button>
    <a-button id="algBtn" type="primary" @click="activeAlgModel('ew')">
      <a-icon type="share-alt"/>
      EventWait
    </a-button>
    <a-button id="algBtn" type="primary" @click="activeAlgModel('deadPred')">
      <a-icon type="share-alt"/>
      Dead Pred
    </a-button>
    <a-button id="algBtn" type="primary" @click="activeAlgModel('fwDeadPred')">
      <a-icon type="share-alt"/>
      Dead Pred-FW
    </a-button>
    <a-button id="algBtn" type="primary" @click="activeAlgModel('bwDeadPred')">
      <a-icon type="share-alt"/>
      Dead Pred-BW
    </a-button>
    <a-button id="algBtn" type="primary" @click="activeAlgModel('test')">
      <a-icon type="share-alt"/>
      Test
    </a-button>
    <AlgForm ref="algform" :flowData="flowData" :activeAlg="activeAlg">
      <template slot="alg" slot-scope="props">
        <template v-if="activeAlg=='reach'">
          <a-form-model-item label="限制发射步数:">
            <a-input
              v-model.number="props.data.params.step"
              placeholder="Please enter number of transmit steps"
            />
          </a-form-model-item>
        </template>

        <template v-else-if="activeAlg=='simphon'">
        </template>

        <template v-else-if="activeAlg=='ew'">
        </template>

        <template v-else-if="activeAlg=='generate'">
          <a-form-model-item label="库所数目:">
            <a-input
              v-model.number="props.data.params.placeCount"
            />
          </a-form-model-item>
          <a-form-model-item label="变迁数目:">
            <a-input
              v-model.number="props.data.params.tranCount"
            />
          </a-form-model-item>
          <a-form-model-item label="网类型:">
            <a-radio-group v-model="props.data.params.netType">
              <a-radio value="ac">Ac</a-radio>
              <a-radio value="fc">Fc</a-radio>
            </a-radio-group>
          </a-form-model-item>
        </template>
        <template v-else>

        </template>
      </template>


    </AlgForm>
  </div>
</template>
<script>
import AlgForm from "./AlgForm";

export default {
  components: {
    AlgForm,
  },
  props: ["flowData"],
  mounted() {
    let that = this;
    that.$bus.$on('generateNet', () => {
      that.activeAlgModel('generate');
    })
  },
  data() {
    return {
      activeAlg: '',
    };
  },
  methods: {
    activeAlgModel(alg) {
      this.activeAlg = alg;
      this.$refs.algform.algVisible = true;
    },
  },
};
</script>

<style lang="scss">
#algBtn {
  margin-left: 10px;
  margin-top: 10px;
}

.submit {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}
</style>
