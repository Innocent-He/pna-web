<template>
  <a-layout-header class="header-option">
    <a-modal title="Generate petri net" v-model="netVisible" @cancel="netVisible=false">
      <a-input
        v-model.number="placeCount"
        placeholder="Please enter number of place count"
      />
      <br/>
      <br/>
      <a-input
        v-model.number="tranCount"
        placeholder="Please enter number of tran count"
      />

      <template slot="footer">
        <a-button key="acNet" type="primary" @click="$bus.$emit('generatePetriNet','ac')">
          AcNet
        </a-button>
        <a-button key="fcNet" type="primary" @click="$bus.$emit('generatePetriNet','fc')">
          FcNet
        </a-button>
      </template>
    </a-modal>

    <a-tooltip title="generate" placement="bottom">
      <a-button
        @click="netVisible = true"
        class="header-option-button"
        size="small"
        icon="gateway"
      ></a-button>
    </a-tooltip>
    <a-tooltip title="upload" placement="bottom">
      <a-upload :beforeUpload="loadPetri">
        <a-button
          class="header-option-button"
          size="small"
          icon="upload"
        ></a-button>
      </a-upload>
    </a-tooltip>
    <a-tooltip title="save" placement="bottom">
      <a-button
        @click="$bus.$emit('saveFlow')"
        class="header-option-button"
        size="small"
        icon="save"
      ></a-button>
    </a-tooltip>

    <a-popconfirm
      title="确认要重新绘制吗？"
      placement="bottom"
      okText="确认"
      cancelText="取消"
      @confirm="$bus.$emit('clear')"
    >
      <a-tooltip title="重新绘制" placement="bottom">
        <a-button
          class="header-option-button"
          size="small"
          icon="delete"
        ></a-button>
      </a-tooltip>
    </a-popconfirm>
  </a-layout-header>
</template>

<script>
export default {
  name: "OptionHeader.vue",
  data(){
    return{
      netVisible:false,
      placeCount: null,
      tranCount: null,
    }
  },
  methods:{
    closeNet() {
      this.netVisible = false;
    },
    loadPetri(file){
      this.$bus.$emit('loadPetri',file);
    },
  },
}
</script>

<style scoped>
.header-option {
  background: white;
  height: 36px;
  line-height: 36px;
  border-bottom: 2px solid #e4e7ed;
  text-align: right;
}

.header-option-button {
  border: 0;
  margin-left: 8px;
}

</style>
