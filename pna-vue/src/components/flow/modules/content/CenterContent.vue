<template>
  <a-layout>
    <OptionHeader></OptionHeader>
    <a-layout-content class="content">
      <a-spin tip="Loading..." :spinning="spinning" size="large"></a-spin>
      <flow-area
        :browserType="browserType"
        :flowData="flowData"
        :select.sync="currentSelect"
        :selectGroup.sync="currentSelectGroup"
        :plumb="plumb"
        :currentTool="currentTool"
        :placeId="placeId"
        :tranId="tranId"
      >
      </flow-area>

      <vue-context-menu
        :contextMenuData="linkContextMenuData"
        @deleteLink="$bus.$emit('deleteLink')"
      >
      </vue-context-menu>
    </a-layout-content>
  </a-layout>

</template>

<script>
import OptionHeader from "./OptionHeader";
import FlowArea from "./FlowArea";
import {flowConfig} from "../../config/args-config";

export default {
  name: "CenterContent",
  props:['browserType','flowData','select','selectGroup','plumb','currentTool','placeId','tranId'],
  components: {
    OptionHeader,
    FlowArea,
  },
  data(){
    return{
      spinning: false,
      linkContextMenuData: flowConfig.contextMenu.link,
      currentSelect:this.select,
      currentSelectGroup:this.selectGroup,
    }
  },
  watch:{
    select(val){
        this.currentSelect=val;
    },
    selectGroup(val){
      this.currentSelectGroup=val
    },
    currentSelect:{
      handler(val){
        this.$emit("update:select",val);
      },
      deep:true,
    },
    currentSelectGroup:{
      handler(val){
        this.$emit("update:selectGroup",val)
      },
      deep:true,
    }
  }
}
</script>

<style scoped>
.content {
  background: #fafafa;
  height: 100%;
  border: 2px dashed rgba(170, 170, 170, 0.7);
}
</style>
