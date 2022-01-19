<template>
  <a-layout-header class="header-option">

    <a-tooltip title="generate" placement="bottom">
      <a-button
        @click="generatePetriNet"
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
import {generateNet} from '../../util/FetchData'
export default {
  name: "OptionHeader.vue",
  data() {
    return {
      params: {
        placeCount: 0,
        tranCount: 0,
        netType:'',
      },
      netVisible: false,
    }
  },
  methods: {
    closeNet() {
      this.netVisible = false;
    },
    loadPetri(file) {
      this.$bus.$emit('loadPetri', file);
    },
    generatePetriNet() {
      let that = this;
      that.$bus.$emit('generateNet');
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
