<template>
  <a-layout-sider width="300" theme="light" class="select-area">
    <a-row>
      <a-checkable-tag
        v-model="tag.checked0"
        @change="toggleNodeShow0"
        class="tag"
      >工具
      </a-checkable-tag
      >
      <div align="center">
        <a-list :grid="{ gutter: 8, column: 1 }" v-if="tag.toolShow">
          <a-list-item>
            <a-button-group>
              <a-button
                v-for="(tool, index) in field.tools"
                :key="index"
                :icon="tool.icon"
                :type="currentTool.type == tool.type ? 'primary' : 'default'"
                @click="$bus.$emit('selectTool',tool.type)"
              >
              </a-button>
            </a-button-group>
          </a-list-item>
        </a-list>
      </div>
    </a-row>
    <a-row>
      <a-checkable-tag
        v-model="tag.checked1"
        @change="toggleNodeShow1"
        class="tag"
      >Petri节点
      </a-checkable-tag>
      <div align="center">
        <a-list :grid="{ gutter: 8, column: 2 }" v-if="tag.commonNodeShow">
          <a-list-item
            v-for="(commonNode, index) in field.commonNodes"
            :key="index"
          >
            <div
              :class="commonNode.className"
              class="node-item"
              :type="commonNode.type"
              belongto="commonNodes"
            >
              {{ commonNode.nodeName }}
            </div>
          </a-list-item>
        </a-list>
      </div>
    </a-row>
    <a-row>
      <div align="attr">
        <flow-attr :flowData="flowData"  :currentSelect="currentSelect"></flow-attr>
      </div>
    </a-row>
  </a-layout-sider>
</template>

<script>
import {tools, commonNodes} from "../../config/basic-node-config.js";
import FlowAttr from "./FlowAttr";
export default {
  name: "LeftSide",
  components:{
    FlowAttr
  },
  props:['flowData','currentSelect',"test"],
  data() {
    return {
      tag: {
        checked0: true,
        checked1: true,
        checked2: true,
        checked3: true,
        toolShow: true,
        commonNodeShow: true
      },
      field: {
        tools,
        commonNodes
      },
      toggleNodeShow0(flag) {
        if (!flag) {
          this.tag.toolShow = false;
        } else {
          this.tag.toolShow = true;
        }
      },
      toggleNodeShow1(flag) {
        if (!flag) {
          this.tag.commonNodeShow = false;
        } else {
          this.tag.commonNodeShow = true;
        }
      },
      currentTool: {
        type: "drag",
        icon: "drag",
        name: "拖拽"
      },
    }
  },

}
</script>

<style lang="scss" scoped>
$primary-color: #409EFF;
.select-area {
  border-right: 1px solid #e4e7ed;
}

.tag {
  margin: 6px;
}

.node-item {

  background: #f4f6fc;
  text-align: center;
  margin: 5px;
  cursor: move;

  &:hover {
    color: $primary-color;
    outline: 1px dashed $primary-color;
  }
}
.place-node {
  height: 50px;
  width: 50px;
  line-height: 50px;
  border-radius: 50%;
  white-space: nowrap;

  &:hover {
    color: $primary-color;
    outline: 1px dashed $primary-color;
  }
}

.tran-node {
  margin-top: 17px;
  border-radius: 50%;
  height: 20px;
  width: 40px;
  line-height: 20px;
  color: white;
  border: 1px solid #777;
  border-radius: 5px;
  background-color: black;
  white-space: nowrap;

  &:hover {
    color: $primary-color;
    outline: 1px dashed $primary-color;
  }
}

</style>
