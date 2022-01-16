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
    <a-button id="algBtn" type="primary" @click="activeAlgModel('test')">
      <a-icon type="share-alt"/>
      Test
    </a-button>
    <AlgForm ref="algform">
      <template slot="alg" slot-scope="params" v-if="activeAlg=='reach'">
        <a-form-item label="限制发射步数:">
          <a-input
            v-model.number="step"
            placeholder="Please enter number of transmit steps"
          />
        </a-form-item>
      </template>
      <template v-else-if="activeAlg=='simphon'">

      </template>
      <template v-else-if="activeAlg=='ew'">

      </template>
      <template v-else-if="activeAlg=='test'">

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
  props: ["flowData", "placeId", "tranId", "editFlag"],
  data() {
    return {
      activeAlg: '',
      step: null,
    };
  },
  methods: {
    activeAlgModel(alg) {
      this.activeAlg = alg;
      this.$refs.algform.algVisible = true;
    },
  }
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
