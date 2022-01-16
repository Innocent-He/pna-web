<template>
  <!-- common-circle-node -->

  <div
    v-if="node.type == 'place'"
    :id="node.id"
    class="place normal"
    :class="{ active: isActive() }"
    :style="{
      top: node.y + 'px',
      left: node.x + 'px',
      cursor:
        currentTool.type == 'drag'
          ? 'move'
          : currentTool.type == 'connection'
          ? 'crosshair'
          : currentTool.type == 'zoom-in'
          ? 'zoom-in'
          : currentTool.type == 'zoom-out'
          ? 'zoom-out'
          : 'default'
    }"
    @click.stop="selectNode"
    @contextmenu.stop="showNodeContextMenu"
  >
    {{ node.id }}
  </div>

  <!-- common-rectangle-node -->
  <div
    v-else-if="node.type == 'transaction'"
    :id="node.id"
    class="transaction normal"
    :class="{ active: isActive() }"
    :style="{
      top: node.y + 'px',
      left: node.x + 'px',
      cursor:
        currentTool.type == 'drag'
          ? 'move'
          : currentTool.type == 'connection'
          ? 'crosshair'
          : currentTool.type == 'zoom-in'
          ? 'zoom-in'
          : currentTool.type == 'zoom-out'
          ? 'zoom-out'
          : 'default'
    }"
    @click.stop="selectNode"
    @contextmenu.stop="showNodeContextMenu"
  >
    {{ node.id }}
  </div>
  <div v-else></div>
</template>

<script>
import jsplumb from "jsplumb";
import { flowConfig } from "../config/args-config.js";
import "jquery-ui/ui/widgets/draggable";
import "jquery-ui/ui/widgets/droppable";
import "jquery-ui/ui/widgets/resizable";


export default {
  props: ["select", "selectGroup", "node", "plumb", "currentTool"],
  components: {
    jsplumb
  },
  mounted() {
    this.registerNode();
  },
  data() {
    return {
      currentSelect: this.select,
      currentSelectGroup: this.selectGroup
    };
  },
  methods: {
    registerNode() {
      const that = this;

      that.plumb.draggable(that.node.id, {
        containment: "parent",
        handle: function(e, el) {
          var possibles = el.parentNode.querySelectorAll(
            ".place,.transaction"
          );
          for (var i = 0; i < possibles.length; i++) {
            if (possibles[i] === el)
              return true;
          }
          return false;
        },
        grid: flowConfig.defaultStyle.alignGridPX,
        drag: function(e) {
          if (flowConfig.defaultStyle.isOpenAuxiliaryLine) {
            that.$emit("alignForLine", e);
          }
        },
        stop: function(e) {
          that.node.x = e.pos[0];
          that.node.y = e.pos[1];
          if (that.currentSelectGroup.length > 1) {
            that.$emit("updateNodePos");
          }
          that.$emit("hideAlignLine");
        }
      });
      that.currentSelect = that.node;
      that.currentSelectGroup = [];
    },
    selectNode() {
      const that = this;
      that.currentSelect = this.node;
      that.$emit("isMultiple", flag => {
        if (!flag) {
          that.currentSelectGroup = [];
        } else {
          let f = that.currentSelectGroup.filter(s => s.id == that.node.id);
          if (f.length <= 0) {
            that.plumb.addToDragSelection(that.node.id);
            that.currentSelectGroup.push(that.node);
          }
        }
      });
    },
    showNodeContextMenu(e) {
      this.$emit("showNodeContextMenu", e);
      this.selectNode();
    },
    isActive() {
      const that = this;
      if (that.currentSelect.id == that.node.id) return true;
      let f = that.currentSelectGroup.filter(n => n.id == that.node.id);
      if (f&&f.length > 0) return true;
      return false;
    }
  },
  watch: {
    select(val) {
      this.currentSelect = val;
    },

    selectGroup(val) {
      this.currentSelectGroup = val;
    },


    currentSelect: {
      handler(val) {
        this.$emit("update:select", val);
      },
      deep: true
    },

    currentSelectGroup: {
      handler(val) {
        this.$emit("update:selectGroup", val);
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
$common-node-bg: #f4f6fc;
$common-node-bg-hover: #f4f6fcb0;
$common-node-active: #409eff;




.place {
  position: absolute;
  height: 30px;
  width: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 50%;
  border: 1px solid #777;
  background-color: $common-node-bg;
  margin: 5px;
  white-space: nowrap;

  &:hover {
    background-color: $common-node-bg-hover;
    z-index: 2;
  }

  &.active {
    outline: 2px dashed $common-node-active;
    outline-offset: 0px;
  }
}


.transaction {
  position: absolute;
  border-radius: 50%;
  height: 20px;
  width: 40px;
  line-height: auto;
  text-align: center;
  color:white;
  border: 1px solid #777;
  border-radius: 5px;
  background-color: black;
  white-space: nowrap;


  &:hover {
    background-color: black;
    z-index: 2;
  }

  &.active {
    outline: 2px dashed $common-node-active;
    outline-offset: 0px;
  }
}




</style>
