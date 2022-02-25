<template>
  <div style="width: 100%; height: 100%; overflow: hidden; position: relative">
    <!--  流程图界面 -->
    <div
      id="flowContainer"
      class="flow-container"
      :class="{
        grid: true,
        zoomIn: currentTool.type == 'zoom-in',
        zoomOut: currentTool.type == 'zoom-out',
        canScale: container.scaleFlag,
        canDrag: container.dragFlag,
        canMultiple: rectangleMultiple.flag
      }"
      :style="{
        top: container.pos.top + 'px',
        left: container.pos.left + 'px',
        transform: 'scale(' + container.scale + ')',
        transformOrigin:
          container.scaleOrigin.x + 'px ' + container.scaleOrigin.y + 'px'
      }"
      @click.stop="containerHandler"
      @mousedown="mousedownHandler"
      @mousemove="mousemoveHandler"
      @mouseup="mouseupHandler"
      @mousewheel="scaleContainer"
      @DOMMouseScroll="scaleContainer"
      @contextmenu="showContainerContextMenu"
    >
      <flow-node
        v-for="(node, index) in flowData.nodeList"
        :key="index"
        :node="node"
        :plumb="plumb"
        :select.sync="currentSelect"
        :selectGroup.sync="currentSelectGroup"
        :currentTool="currentTool"
        @showNodeContextMenu="showNodeContextMenu"
        @isMultiple="isMultiple"
        @updateNodePos="updateNodePos"
        @alignForLine="alignForLine"
        @hideAlignLine="hideAlignLine"
      >
      </flow-node>
      <div
        class="rectangle-multiple"
        v-if="rectangleMultiple.flag && rectangleMultiple.multipling"
        :style="{
          top: rectangleMultiple.position.top + 'px',
          left: rectangleMultiple.position.left + 'px',
          width: rectangleMultiple.width + 'px',
          height: rectangleMultiple.height + 'px'
        }"
      ></div>
    </div>
    <div class="container-scale">缩放倍数：{{ container.scaleShow }}%</div>
    <div class="mouse-position">
      x: {{ mouse.position.x }}, y: {{ mouse.position.y }}
    </div>
    <vue-context-menu
      :contextMenuData="containerContextMenuData"
      @flowInfo="flowInfo"
      @paste="paste"
      @selectAll="selectAll"
      @saveFlow="saveFlow"
      @verticaLeft="verticaLeft"
      @verticalCenter="verticalCenter"
      @verticalRight="verticalRight"
      @levelUp="levelUp"
      @levelCenter="levelCenter"
      @levelDown="levelDown"
      @addRemark="addRemark"
    >
    </vue-context-menu>
    <vue-context-menu
      :contextMenuData="nodeContextMenuData"
      @copyNode="copyNode"
      @deleteNode="deleteNode"
    >
    </vue-context-menu>
  </div>
</template>

<script>
import jsplumb from "jsplumb";
import {commonNodes} from "../../config/basic-node-config.js";
import { flowConfig } from "../../config/args-config.js";
import $ from "jquery";
import "jquery-ui/ui/widgets/draggable";
import "jquery-ui/ui/widgets/droppable";
import "jquery-ui/ui/widgets/resizable";
import { Utils } from "../../util/utils.js";
import FlowNode from "../FlowNode";

export default {
  name:"flowArea",
  props: [
    "browserType",
    "flowData",
    "plumb",
    "select",
    "selectGroup",
    "currentTool"
  ],
  components: {
    jsplumb,
    FlowNode
  },
  mounted() {
    this.initFlowArea();
    this.listenShortcut();
    this.initEventListener();
  },
  data() {
    return {
      ctx: null,
      currentSelect: this.select,
      currentSelectGroup: this.selectGroup,
      container: {
        pos: {
          top: -3000,
          left: -3000
        },
        dragFlag: false,
        draging: false,
        scale: flowConfig.defaultStyle.containerScale.init,
        scaleFlag: false,
        scaleOrigin: {
          x: 0,
          y: 0
        },
        scaleShow: Utils.mul(flowConfig.defaultStyle.containerScale.init, 100),
        auxiliaryLine: {
          isOpen: flowConfig.defaultStyle.isOpenAuxiliaryLine,
          isShowXLine: false,
          isShowYLine: false,
          controlFnTimesFlag: true
        }
      },
      auxiliaryLinePos: {
        x: 0,
        y: 0
      },
      mouse: {
        position: {
          x: 0,
          y: 0
        },
        tempPos: {
          x: 0,
          y: 0
        }
      },
      rectangleMultiple: {
        flag: false,
        multipling: false,
        position: {
          top: 0,
          left: 0
        },
        height: 0,
        width: 0
      },
      containerContextMenuData: flowConfig.contextMenu.container,
      nodeContextMenuData: flowConfig.contextMenu.node,
      tempLinkId: "",
      clipboard: []
    };
  },
  beforeDestroy() {
    this.clearEventListener();
  },
  methods: {
    initEventListener(){
      this.$bus.$on('hideDragFlag',()=>{
        this.container.dragFlag = false;
      });
      this.$bus.$on('hideScaleFlag',()=>{
        this.container.scaleFlag = false;
      });
    },
    clearEventListener(){
      this.$bus.$off('hideDragFlag');
    },

    listenShortcut() {
      const that = this;
      document.onkeydown = function (e) {
        let event = window.event || e;
        let key = event.keyCode;
        switch (key) {
          case flowConfig.shortcut.multiple.code:
            that.rectangleMultiple.flag = true;
            break;
          case flowConfig.shortcut.dragContainer.code:
            that.container.dragFlag = true;
            break;
          case flowConfig.shortcut.scaleContainer.code:
            that.container.scaleFlag = true;
            break;
          case flowConfig.shortcut.dragTool.code:
            that.$bus.$emit('selectTool',"drag");
            break;
          case flowConfig.shortcut.connTool.code:
            that.$bus.$emit('selectTool',"connection");
            break;
          case flowConfig.shortcut.zoomInTool.code:
            that.$bus.$emit('selectTool',"zoom-in");
            break;
          case flowConfig.shortcut.zoomOutTool.code:
            that.$bus.$emit('selectTool',"zoom-out");
            break;
          case 37:
            that.$bus.$emit('moveNode','left')
            break;
          case 38:
            that.$bus.$emit('moveNode','up')
            break;
          case 39:
            that.$bus.$emit('moveNode','right')
            break;
          case 40:
            that.$bus.$emit('moveNode','down')
            break;
        }
      };
      document.onkeyup = function (e) {
        let event = window.event || e;
        let key = event.keyCode;
        if (key == flowConfig.shortcut.dragContainer.code) {
          that.container.dragFlag = false;
        } else if (key == flowConfig.shortcut.scaleContainer.code) {
          event.preventDefault();
          that.container.scaleFlag=false;
        } else if (key == flowConfig.shortcut.multiple.code) {
          that.rectangleMultiple.flag = false;
        }
      };
      Utils.consoleLog(["初始化快捷键成功..."]);
    },
    initFlowArea() {
      const that = this;
      that.ctx = document.getElementById("flowContainer").parentNode;
      $(".flow-container").droppable({
        // 决定哪些元素可以拖拽到流程图面板上
        accept: function(t) {
          if (t[0].className.indexOf("node-item") != -1) {
            let event = window.event || "firefox";
            // 如果流程图页面包含事件的源
            if (that.ctx.contains(event.srcElement) || event == "firefox") {
              return true;
            }
          }
          return false;
        },
        // 当被拖拽元素落在dropable元素上时，dropable所添加的class
        hoverClass: "flow-container-active",
        drop:function(event, ui) {
          let belongto = ui.draggable.attr("belongto");
          let type = ui.draggable.attr("type");
          that.$bus.$emit("selectTool", "drag");
          let node = commonNodes.filter(n => n.type == type);
          if (node && node.length >= 0) node = node[0];
          if (!node) {
            that.$message.error("未知的节点类型！");
            return;
          }
          that.addNewNode(node);
        }
      });
    },
    mousedownHandler(e) {
      const that = this;

      let event = window.event || e;

      if (event.button == 0) {
        if (that.container.dragFlag) {
          that.mouse.tempPos = that.mouse.position;
          that.container.draging = true;
        }

        that.currentSelectGroup = [];
        if (that.rectangleMultiple.flag) {
          that.mouse.tempPos = that.mouse.position;
          that.rectangleMultiple.multipling = true;
        }
      }
    },
    mousemoveHandler(e) {
      const that = this;

      let event = window.event || e;

      if (event.target.id == "flowContainer") {
        that.mouse.position = {
          x: event.offsetX,
          y: event.offsetY
        };
      } else {
        let cn = event.target.className;
        let tn = event.target.tagName;
        if (
          cn != "lane-text" &&
          cn != "lane-text-div" &&
          tn != "svg" &&
          tn != "path" &&
          tn != "I"
        ) {
          that.mouse.position.x = event.target.offsetLeft + event.offsetX;
          that.mouse.position.y = event.target.offsetTop + event.offsetY;
        }
      }
      if (that.container.draging) {
        let nTop =
          that.container.pos.top +
          (that.mouse.position.y - that.mouse.tempPos.y);
        let nLeft =
          that.container.pos.left +
          (that.mouse.position.x - that.mouse.tempPos.x);
        if (nTop >= 0) nTop = 0;
        if (nLeft >= 0) nLeft = 0;
        that.container.pos = {
          top: nTop,
          left: nLeft
        };
      }
      if (that.rectangleMultiple.multipling) {
        let h = that.mouse.position.y - that.mouse.tempPos.y;
        let w = that.mouse.position.x - that.mouse.tempPos.x;
        let t = that.mouse.tempPos.y;
        let l = that.mouse.tempPos.x;
        if (h >= 0 && w < 0) {
          w = -w;
          l -= w;
        } else if (h < 0 && w >= 0) {
          h = -h;
          t -= h;
        } else if (h < 0 && w < 0) {
          h = -h;
          w = -w;
          t -= h;
          l -= w;
        }
        that.rectangleMultiple.height = h;
        that.rectangleMultiple.width = w;
        that.rectangleMultiple.position.top = t;
        that.rectangleMultiple.position.left = l;
      }
    },
    mouseupHandler() {
      const that = this;

      if (that.container.draging) that.container.draging = false;
      if (that.rectangleMultiple.multipling) {
        that.judgeSelectedNode();
        that.rectangleMultiple.multipling = false;
        that.rectangleMultiple.width = 0;
        that.rectangleMultiple.height = 0;
      }
    },
    judgeSelectedNode() {
      const that = this;

      let ay = that.rectangleMultiple.position.top;
      let ax = that.rectangleMultiple.position.left;
      let by = ay + that.rectangleMultiple.height;
      let bx = ax + that.rectangleMultiple.width;

      let nodeList = that.flowData.nodeList;
      nodeList.forEach(function(node, index) {
        if (node.y >= ay && node.x >= ax && node.y <= by && node.x <= bx) {
          that.plumb.addToDragSelection(node.id);
          that.currentSelectGroup.push(node);
        }
      });
    },
    scaleContainer(e) {
      const that = this;

      let event = window.event || e;

      if (that.container.scaleFlag) {
        if (that.browserType == 2) {
          if (event.detail < 0) {
            that.enlargeContainer();
          } else {
            that.narrowContainer();
          }
        } else {
          if (event.deltaY < 0) {
            that.enlargeContainer();
          } else if (that.container.scale) {
            that.narrowContainer();
          }
        }
      }
    },
    enlargeContainer() {
      const that = this;
      that.container.scaleOrigin.x = that.mouse.position.x;
      that.container.scaleOrigin.y = that.mouse.position.y;
      let newScale = Utils.add(
        that.container.scale,
        flowConfig.defaultStyle.containerScale.onceEnlarge
      );
      if (newScale <= flowConfig.defaultStyle.containerScale.max) {
        that.container.scale = newScale;
        that.container.scaleShow = Utils.mul(that.container.scale, 100);
        that.plumb.setZoom(that.container.scale);
      }
    },
    narrowContainer() {
      const that = this;
      that.container.scaleOrigin.x = that.mouse.position.x;
      that.container.scaleOrigin.y = that.mouse.position.y;
      let newScale = Utils.sub(
        that.container.scale,
        flowConfig.defaultStyle.containerScale.onceNarrow
      );
      if (newScale >= flowConfig.defaultStyle.containerScale.min) {
        that.container.scale = newScale;
        that.container.scaleShow = Utils.mul(that.container.scale, 100);
        that.plumb.setZoom(that.container.scale);
      }
    },
    showContainerContextMenu(e) {
      let event = window.event || e;

      event.preventDefault();
      $(".vue-contextmenuName-node-menu").css("display", "none");
      $(".vue-contextmenuName-link-menu").css("display", "none");
      this.selectContainer();
      let x = event.clientX;
      let y = event.clientY;
      this.containerContextMenuData.axis = { x, y };
    },
    showNodeContextMenu(e) {
      let event = window.event || e;

      event.preventDefault();
      $(".vue-contextmenuName-flow-menu").css("display", "none");
      $(".vue-contextmenuName-link-menu").css("display", "none");
      let x = event.clientX;
      let y = event.clientY;
      this.nodeContextMenuData.axis = { x, y };
    },
    flowInfo() {
      const that = this;

      let nodeList = that.flowData.nodeList;
      let linkList = that.flowData.linkList;
      alert(
        "当前流程图中有 " +
          nodeList.length +
          " 个节点，有 " +
          linkList.length +
          " 条连线。"
      );
    },
    paste() {
      const that = this;
      let dis = 0;
      that.clipboard.forEach(function(node, index) {
        let newNode = Object.assign({}, node);
        if (node.type==="place") {
          that.$bus.$emit("placeIdInc");
          newNode.id = "p-" + that.flowData.attr.maxPlaceId;
        } else if (node.type == "transaction") {
          that.$bus.$emit("tranInc");
          newNode.id = "t-" + that.flowData.attr.maxTranId;
        }
        let nodePos = that.computeNodePos(
          that.mouse.position.x + dis,
          that.mouse.position.y + dis
        );
        newNode.x = nodePos.x;
        newNode.y = nodePos.y;
        dis += 20;
        that.flowData.nodeList.push(newNode);
      });
    },
    selectAll() {
      const that = this;
      that.flowData.nodeList.forEach(function(node, index) {
        that.plumb.addToDragSelection(node.id);
        that.currentSelectGroup.push(node);
      });
    },
    saveFlow() {
      this.$bus.$emit("saveFlow");
    },
    checkAlign() {
      if (this.currentSelectGroup.length < 2) {
        this.$message.error("请选择至少两个节点！");
        return false;
      }
      return true;
    },
    verticaLeft() {
      const that = this;

      if (!that.checkAlign()) return;
      let nodeList = that.flowData.nodeList;
      let selectGroup = that.currentSelectGroup;
      let baseX = selectGroup[0].x;
      let baseY = selectGroup[0].y;
      for (let i = 1; i < selectGroup.length; i++) {
        baseY =
          baseY +
          selectGroup[i - 1].height +
          flowConfig.defaultStyle.alignSpacing.vertical;
        let f = nodeList.filter(n => n.id == selectGroup[i].id)[0];
        f.tx = baseX;
        f.ty = baseY;
        that.plumb.animate(
          selectGroup[i].id,
          { top: baseY, left: baseX },
          {
            duration: flowConfig.defaultStyle.alignDuration,
            complete: function() {
              f.x = f.tx;
              f.y = f.ty;
            }
          }
        );
      }
    },
    verticalCenter() {
      const that = this;

      if (!that.checkAlign()) return;
      let nodeList = that.flowData.nodeList;
      let selectGroup = that.currentSelectGroup;
      let baseX = selectGroup[0].x;
      let baseY = selectGroup[0].y;
      let firstX = baseX;
      for (let i = 1; i < selectGroup.length; i++) {
        baseY =
          baseY +
          selectGroup[i - 1].height +
          flowConfig.defaultStyle.alignSpacing.vertical;
        baseX =
          firstX +
          Utils.div(selectGroup[0].width, 2) -
          Utils.div(selectGroup[i].width, 2);
        let f = nodeList.filter(n => n.id == selectGroup[i].id)[0];
        f.tx = baseX;
        f.ty = baseY;
        that.plumb.animate(
          selectGroup[i].id,
          { top: baseY, left: baseX },
          {
            duration: flowConfig.defaultStyle.alignDuration,
            complete: function() {
              f.x = f.tx;
              f.y = f.ty;
            }
          }
        );
      }
    },
    verticalRight() {
      const that = this;

      if (!that.checkAlign()) return;
      let nodeList = that.flowData.nodeList;
      let selectGroup = that.currentSelectGroup;
      let baseX = selectGroup[0].x;
      let baseY = selectGroup[0].y;
      let firstX = baseX;
      for (let i = 1; i < selectGroup.length; i++) {
        baseY =
          baseY +
          selectGroup[i - 1].height +
          flowConfig.defaultStyle.alignSpacing.vertical;
        baseX = firstX + selectGroup[0].width - selectGroup[i].width;
        let f = nodeList.filter(n => n.id == selectGroup[i].id)[0];
        f.tx = baseX;
        f.ty = baseY;
        that.plumb.animate(
          selectGroup[i].id,
          { top: baseY, left: baseX },
          {
            duration: flowConfig.defaultStyle.alignDuration,
            complete: function() {
              f.x = f.tx;
              f.y = f.ty;
            }
          }
        );
      }
    },
    levelUp() {
      const that = this;

      if (!that.checkAlign()) return;
      let nodeList = that.flowData.nodeList;
      let selectGroup = that.currentSelectGroup;
      let baseX = selectGroup[0].x;
      let baseY = selectGroup[0].y;
      for (let i = 1; i < selectGroup.length; i++) {
        baseX =
          baseX +
          selectGroup[i - 1].width +
          flowConfig.defaultStyle.alignSpacing.level;
        let f = nodeList.filter(n => n.id == selectGroup[i].id)[0];
        f.tx = baseX;
        f.ty = baseY;
        that.plumb.animate(
          selectGroup[i].id,
          { top: baseY, left: baseX },
          {
            duration: flowConfig.defaultStyle.alignDuration,
            complete: function() {
              f.x = f.tx;
              f.y = f.ty;
            }
          }
        );
      }
    },
    levelCenter() {
      const that = this;

      if (!that.checkAlign()) return;
      let nodeList = that.flowData.nodeList;
      let selectGroup = that.currentSelectGroup;
      let baseX = selectGroup[0].x;
      let baseY = selectGroup[0].y;
      let firstY = baseY;
      for (let i = 1; i < selectGroup.length; i++) {
        baseY =
          firstY +
          Utils.div(selectGroup[0].height, 2) -
          Utils.div(selectGroup[i].height, 2);
        baseX =
          baseX +
          selectGroup[i - 1].width +
          flowConfig.defaultStyle.alignSpacing.level;
        let f = nodeList.filter(n => n.id == selectGroup[i].id)[0];
        f.tx = baseX;
        f.ty = baseY;
        that.plumb.animate(
          selectGroup[i].id,
          { top: baseY, left: baseX },
          {
            duration: flowConfig.defaultStyle.alignDuration,
            complete: function() {
              f.x = f.tx;
              f.y = f.ty;
            }
          }
        );
      }
    },
    levelDown() {
      const that = this;

      if (!that.checkAlign()) return;
      let nodeList = that.flowData.nodeList;
      let selectGroup = that.currentSelectGroup;
      let baseX = selectGroup[0].x;
      let baseY = selectGroup[0].y;
      let firstY = baseY;
      for (let i = 1; i < selectGroup.length; i++) {
        baseY = firstY + selectGroup[0].height - selectGroup[i].height;
        baseX =
          baseX +
          selectGroup[i - 1].width +
          flowConfig.defaultStyle.alignSpacing.level;
        let f = nodeList.filter(n => n.id == selectGroup[i].id)[0];
        f.tx = baseX;
        f.ty = baseY;
        that.plumb.animate(
          selectGroup[i].id,
          { top: baseY, left: baseX },
          {
            duration: flowConfig.defaultStyle.alignDuration,
            complete: function() {
              f.x = f.tx;
              f.y = f.ty;
            }
          }
        );
      }
    },
    addRemark() {
      const that = this;
      alert("添加备注(待完善)...");
    },
    copyNode() {
      const that = this;

      that.clipboard = [];
      if (that.currentSelectGroup.length > 0) {
        that.clipboard = Object.assign([], that.currentSelectGroup);
      } else if (that.currentSelect.id) {
        that.clipboard.push(that.currentSelect);
      }
    },
    getConnectionsByNodeId(nodeId) {
      const that = this;
      let conns1 = that.plumb.getConnections({
        source: nodeId
      });
      let conns2 = that.plumb.getConnections({
        target: nodeId
      });
      return conns1.concat(conns2);
    },
    deleteNode() {
      const that = this;
      let nodeList = that.flowData.nodeList;
      let linkList = that.flowData.linkList;
      let arr = [];

      arr.push(Object.assign({}, that.currentSelect));

      arr.forEach(function(c, index) {
        let conns = that.getConnectionsByNodeId(c.id);
        conns.forEach(function(conn, index) {
          linkList.splice(
            linkList.findIndex(
              link =>
                link.sourceId == conn.sourceId || link.targetId == conn.targetId
            ),
            1
          );
        });
        that.plumb.deleteEveryEndpoint();
        let inx = nodeList.findIndex(node => node.id == c.id);
        nodeList.splice(inx, 1);
        that.$nextTick(() => {
          linkList.forEach(function(link, index) {
            let conn = that.plumb.connect({
              source: link.sourceId,
              target: link.targetId,
              anchor: flowConfig.jsPlumbConfig.anchor.default,
              connector: [
                'Straight',
                {
                  gap: 5,
                  cornerRadius: 8,
                  alwaysRespectStubs: true
                }
              ],
              paintStyle: {
                stroke: '#2a2929',
                strokeWidth: 2
              }
            });
            if (link.weight != "") {
              conn.setLabel({
                label: link.weight,
                cssClass: "linkWeight"
              });
            }
          });
        });
      });
      that.selectContainer();
    },
    addNewNode(node) {
      const that = this;

      let x = that.mouse.position.x;
      let y = that.mouse.position.y;
      let nodePos = that.computeNodePos(x, y);
      x = nodePos.x;
      y = nodePos.y;

      let newNode = Object.assign({}, node);
      if (newNode.type==="place") {
        that.$bus.$emit("placeIdInc");
        newNode.id = "p-" + that.flowData.attr.maxPlaceId;
      } else if (newNode.type === "transaction") {
        that.$bus.$emit("tranIdInc");
        newNode.id = "t-" + that.flowData.attr.maxTranId;
      }
      newNode.height = 50;
      newNode.y = y - 25;
      newNode.x=x-25;
      that.flowData.nodeList.push(newNode);
    },
    computeNodePos(x, y) {
      const pxx = flowConfig.defaultStyle.alignGridPX[0];
      const pxy = flowConfig.defaultStyle.alignGridPX[1];
      if (x % pxx) x = pxx - (x % pxx) + x;
      if (y % pxy) y = pxy - (y % pxy) + y;
      return {
        x: x,
        y: y
      };
    },
    containerHandler() {
      const that = this;

      that.selectContainer();
      let toolType = that.currentTool.type;
      if (toolType == "zoom-in") {
        that.enlargeContainer();
      } else if (toolType == "zoom-out") {
        that.narrowContainer();
      }
    },
    selectContainer() {
      this.currentSelect = {};
      this.$bus.$emit("getShortcut");
    },
    isMultiple(callback) {
      callback(this.rectangleMultiple.flag);
    },
    updateNodePos() {
      const that = this;

      let nodeList = that.flowData.nodeList;
      that.currentSelectGroup.forEach(function(node, index) {
        let l = parseInt($("#" + node.id).css("left"));
        let t = parseInt($("#" + node.id).css("top"));
        let f = nodeList.filter(n => n.id == node.id)[0];
        f.x = l;
        f.y = t;
      });
    },
    alignForLine(e) {
      const that = this;

      if (that.selectGroup.length > 1) return;
      if (that.container.auxiliaryLine.controlFnTimesFlag) {
        let elId = e.el.id;
        let nodeList = that.flowData.nodeList;
        nodeList.forEach(function(node, index) {
          if (elId != node.id) {
            let dis = flowConfig.defaultStyle.showAuxiliaryLineDistance,
              elPos = e.pos,
              elH = e.el.offsetHeight,
              elW = e.el.offsetWidth,
              disX = elPos[0] - node.x,
              disY = elPos[1] - node.y;
            if (
              (disX >= -dis && disX <= dis) ||
              (disX + elW >= -dis && disX + elW <= dis)
            ) {
              that.container.auxiliaryLine.isShowYLine = true;
              that.auxiliaryLinePos.x = node.x + that.container.pos.left;
              let nodeMidPointX = node.x + node.width / 2;
              if (nodeMidPointX == elPos[0] + elW / 2) {
                that.auxiliaryLinePos.x =
                  nodeMidPointX + that.container.pos.left;
              }
            }
            if (
              (disY >= -dis && disY <= dis) ||
              (disY + elH >= -dis && disY + elH <= dis)
            ) {
              that.container.auxiliaryLine.isShowXLine = true;
              that.auxiliaryLinePos.y = node.y + that.container.pos.top;
              let nodeMidPointY = node.y + node.height / 2;
              if (nodeMidPointY == elPos[1] + elH / 2) {
                that.auxiliaryLinePos.y =
                  nodeMidPointY + that.container.pos.left;
              }
            }
          }
        });
        that.container.auxiliaryLine.controlFnTimesFlag = false;
        setTimeout(function() {
          that.container.auxiliaryLine.controlFnTimesFlag = true;
        }, 200);
      }
    },
    hideAlignLine() {
      if (this.container.auxiliaryLine.isOpen) {
        this.container.auxiliaryLine.isShowXLine = false;
        this.container.auxiliaryLine.isShowYLine = false;
      }
    }
  },
  watch: {
    select(val) {
      this.currentSelect = val;
      if (this.tempLinkId != "") {
        $("#" + this.tempLinkId).removeClass("link-active");
        this.tempLinkId = "";
      }
      if (this.currentSelect.type == "link") {
        this.tempLinkId = this.currentSelect.id;
        $("#" + this.currentSelect.id).addClass("link-active");
      }
    },
    selectGroup(val) {
      this.currentSelectGroup = val;
      if (this.currentSelectGroup.length <= 0) this.plumb.clearDragSelection();
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
$active-color: #409EFF;

.btn-wrapper-simple {
  height: 24px !important;
  line-height: 24px !important;
}
.vue-contextmenu-listWrapper {
  padding-left: 1px !important;
}
.child-ul-wrapper {
  padding-left: 1px !important;
}

.flow-container {
  width: 1000%;
  height: 1000%;
  position: relative;
  transition: transform 0.5s ease 0s,transform-origin 0.5s ease 0s;

  &.grid {
    background-image: -webkit-linear-gradient(90deg, rgba(235, 235, 235, 1) 5%, rgba(0, 0, 0, 0) 5%);
    background-image: -moz-linear-gradient(90deg, rgba(235, 235, 235, 1) 5%, rgba(0, 0, 0, 0) 5%);
    background-image: -o-linear-gradient(90deg, rgba(235, 235, 235, 1) 5%, rgba(0, 0, 0, 0) 5%);
    background-image: -webkit-gradient(linear, 0 100%, 0 0, color-stop(0.05, rgba(235, 235, 235, 1)), color-stop(0.05, rgba(0, 0, 0, 0)));
    background-image: linear-gradient(90deg, rgba(235, 235, 235, 1) 5%, rgba(0, 0, 0, 0) 5%),linear-gradient(rgba(235, 235, 235, 1) 5%, rgba(0, 0, 0, 0) 5%);
    background-size: 1rem 1rem;
  }

  &.zoomIn {
    cursor: zoom-in;
  }
  &.zoomOut {
    cursor: zoom-out;
  }
  &.canScale {
    cursor: url(/src/assets/search.png), default;
  }
  &.canDrag {
    cursor: grab;
  }
  &.canMultiple {
    cursor: url(/src/assets/multip-pointer.png), default;
  }
}

.rectangle-multiple {
  position: absolute;
  border: 1px dashed #31676f;
  background-color: #0cceea29;
}

.flow-container-active {
  background-color: #e4e4e438;
  cursor: crosshair;
}

.auxiliary-line-x {
  position: absolute;
  border: 0.5px solid $active-color;
  width: 100%;
  z-index: 9999;
}
.auxiliary-line-y {
  position: absolute;
  border: 0.5px solid $active-color;
  height: 100%;
  z-index: 9999;
}

.link-active {
  outline: 2px dashed $active-color;
}

.container-scale {
  position: absolute;
  top: 0;
  left: 5px;
}

.mouse-position {
  position: absolute;
  bottom: 0;
  right: 5px;
}

.common-remarks {
  width: 100px;
  height: 150px;
  position: absolute;
  background-color: #ffffaa;
}

</style>
