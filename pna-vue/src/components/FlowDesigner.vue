<template>
  <div style="height: 100%">
    <a-layout class="container">
      <LeftSide :plumb="plumb" :flowData="flowData" :currentSelect="currentSelect">
      </LeftSide>
      <CenterContent
        :browserType="browserType"
        :flowData="flowData"
        :select.sync="currentSelect"
        :selectGroup.sync="currentSelectGroup"
        :plumb="plumb"
        :currentTool="currentTool"
      ></CenterContent>
      <RightSide
        :flowData="flowData">
      </RightSide>
    </a-layout>
  </div>
</template>

<script>

import jsplumb from "jsplumb";
import {tools, commonNodes} from "./config/basic-node-config.js";
import {flowConfig} from "./config/args-config.js";
import $ from "jquery";
import "jquery-ui/ui/widgets/draggable";
import "jquery-ui/ui/widgets/droppable";
import "jquery-ui/ui/widgets/resizable";
import html2canvas from "html2canvas";
import canvg from "canvg";
import {Utils} from "./util/utils.js";
import FlowArea from "./modules/content/FlowArea";
import LeftSide from "./modules/leftSide/LeftSide";
import CenterContent from "./modules/content/CenterContent";
import RightSide from "./modules/rightSide/RightSide";
import {ip, login,userInfo} from "./util/FetchData"

export default {
  name: "pna-web",
  components: {
    RightSide,
    CenterContent,
    jsplumb,
    flowConfig,
    html2canvas,
    canvg,
    FlowArea,
    LeftSide
  },
  mounted() {
    const that = this;
    that.dealCompatibility();
    that.initNodeSelectArea();
    that.initJsPlumb();
    that.initFlow();
    that.listenPage();
    that.initEventListener();
  },
  beforeDestroy() {
    const that = this;
    that.clearEventListener();
  },

  data() {
    return {
      browserType: 3,
      plumb: {},
      field: {
        tools,
        commonNodes
      },
      currentTool: {
        type: "drag",
        icon: "drag",
        name: "拖拽"
      },
      activeShortcut: true,
      flowPicture: {
        url: "",
        modalVisible: false,
        closable: false,
        maskClosable: false
      },
      currentSelect: {},
      currentSelectGroup: [],
      flowData: {
        nodeList: [],
        linkList: [],
        attr: {
          id: null,
          name: "",
          des: "",
          createTime: "",
          maxPlaceId: 0,
          maxTranId: 0,
        },
        status: flowConfig.flowStatus.CREATE
      },
    };
  },
  methods: {
    initEventListener() {
      let that = this;
      that.$bus.$on('changeLinkWeight', (weight) => {
        that.changeLinkWeight(weight);
      })
      that.$bus.$on('changeToken', (token) => {
        that.currentSelect.token = token;
      });
      that.$bus.$on('changeAttrDes', (des) => {
        that.flowData.attr.des = des;
      })
      that.$bus.$on('changeAttrName', (name) => {
        that.flowData.attr.name = name;
      })
      that.$bus.$on('selectTool', (type) => {
        that.selectTool(type);
      })
      that.$bus.$on('loadPetri', (file) => {
        that.loadPetri(file);
      })
      that.$bus.$on('saveFlow', () => {
        that.saveFlow();
      })
      that.$bus.$on('clear', () => {
        that.clear();
      })
      that.$bus.$on('moveNode', (type) => {
        that.moveNode(type);
      })
      that.$bus.$on('placeIdInc', () => {
        this.flowData.attr.maxPlaceId++;
      })
      that.$bus.$on('tranIdInc', () => {
        this.flowData.attr.maxTranId++;
      })
      that.$bus.$on('generatePetriNet', (type) => {
        this.generatePetriNet(type);
      })
      that.$bus.$on('getShortcut', () => {
        this.activeShortcut = true;
      })
      that.$bus.$on('deleteLink', () => {
        that.deleteLink();
      })
      that.$bus.$on('loadFlow', (json) => {
        that.loadFlow(json);
      })
      that.$bus.$on('saveFlow', () => {
        that.saveFlow();
      })
      that.$bus.$on('loginTemp', () => {
        that.loginTempAdmin();
      })
    },
    clearEventListener() {
      let that = this;
      that.$bus.$off('changeLinkWeight');
      that.$bus.$off('changeToken');
      that.$bus.$off('changeAttrDes');
      that.$bus.$off('changeAttrName');
      that.$bus.$off('selectTool');
      that.$bus.$off('loadPetri');
      that.$bus.$off('saveFlow');
      that.$bus.$off('clear');
      that.$bus.$off('moveNode');
      that.$bus.$off('placeIdInc');
      that.$bus.$off('tranIdInc');
      that.$bus.$off('generatePetriNet');
      that.$bus.$off('getShortcut');
      that.$bus.$off('loadFlow')
      that.$bus.$off('saveFlow')
      that.$bus.$off('loginTemp')
    },
    changeLinkWeight(weight) {
      let that = this;
      that.currentSelect.weight = weight;
      let conn = that.plumb.getConnections({
        source: that.currentSelect.sourceId,
        target: that.currentSelect.targetId
      })[0];
      if (weight) {
        conn.setLabel({
          label: weight,
          cssClass: "linkLabel"
        });
      } else {
        let labelOverlay = conn.getLabelOverlay();
        if (labelOverlay) conn.removeOverlay(labelOverlay.id);
      }
    },
    getBrowserType() {
      let userAgent = navigator.userAgent;
      let isOpera = userAgent.indexOf("Opera") > -1;
      if (isOpera) {
        return 1;
      }
      if (userAgent.indexOf("Firefox") > -1) {
        return 2;
      }
      if (userAgent.indexOf("Chrome") > -1) {
        return 3;
      }
      if (userAgent.indexOf("Safari") > -1) {
        return 4;
      }
      if (
        userAgent.indexOf("compatible") > -1 &&
        userAgent.indexOf("MSIE") > -1 &&
        !isOpera
      ) {
        alert("IE浏览器支持性较差，推荐使用Firefox或Chrome");
        return 5;
      }
      if (userAgent.indexOf("Trident") > -1) {
        alert("Edge浏览器支持性较差，推荐使用Firefox或Chrome");
        return 6;
      }
    },
    dealCompatibility() {
      const that = this;

      that.browserType = that.getBrowserType();
      if (that.browserType == 2) {
        flowConfig.shortcut.scaleContainer = {
          code: 16,
          codeName: "SHIFT(chrome下为ALT)",
          shortcutName: "画布缩放"
        };
      }
    },
    initJsPlumb() {
      const that = this;

      that.plumb = jsPlumb.getInstance(flowConfig.jsPlumbInsConfig);

      that.plumb.bind("beforeDrop", function (info) {
        let sourceId = info.sourceId;
        let targetId = info.targetId;

        if (sourceId.charAt(0) == targetId.charAt(0)) {
          that.$message.info("Nodes of the same type cannot be connected.");
          return false;
        }
        return true;
      });

      that.plumb.bind("connection", function (conn, e) {
        let connObj = conn.connection.canvas;
        let o = {},
          id,
          label;
        if (
          that.flowData.status == flowConfig.flowStatus.CREATE ||
          that.flowData.status == flowConfig.flowStatus.MODIFY
        ) {
          id = "link-" + Utils.getId();
          label = "";
        } else if (that.flowData.status == flowConfig.flowStatus.LOADING) {
          let l = that.flowData.linkList[that.flowData.linkList.length - 1];
          id = l.id;
          label = l.label;
        }
        connObj.id = id;
        o.type = "link";
        o.id = id;
        o.sourceId = conn.sourceId;
        o.targetId = conn.targetId;
        o.label = label;
        o.cls = {
          linkType: "Straight",
          linkColor: "#2a2929",
          linkThickness: 2
        };
        $("#" + id).bind("contextmenu", function (e) {
          that.showLinkContextMenu(e);
          that.currentSelect = that.flowData.linkList.filter(
            l => l.id == id
          )[0];
        });
        $("#" + id).bind("click", function (e) {
          let event = window.event || e;
          event.stopPropagation();
          that.currentSelect = that.flowData.linkList.filter(
            l => l.id == id
          )[0];
        });
        if (that.flowData.status != flowConfig.flowStatus.LOADING)
          that.flowData.linkList.push(o);
      });

      that.plumb.importDefaults({
        ConnectionsDetachable: flowConfig.jsPlumbConfig.conn.isDetachable
      });

      Utils.consoleLog(["实例化JsPlumb成功..."]);
    },
    initNodeSelectArea() {
      $(document).ready(function () {
        $(".node-item").draggable({
          opacity: flowConfig.defaultStyle.dragOpacity,
          helper: "clone",
          cursorAt: {
            top: 16,
            left: 60
          },
          containment: "window",
          revert: "invalid"
        });
        Utils.consoleLog(["初始化节点选择列表成功..."]);
      });
    },


    listenPage() {
      window.onbeforeunload = function (e) {
        e = e || window.event;
        if (e) {
          e.returnValue = "关闭提示";
        }
        return "关闭提示";
      };
    },
    loginTempAdmin() {
      let that = this;
      let cookie = that.$cookies.get('pna-token')
      if (!cookie) {
        ip().then(({data}) => {
          if (data.success) {
            that.$store.commit('changeUserName', data.data)
            login(that.$store.state.userInfo);
          }
        });
      }else{
        userInfo().then(({data}) => {
          if (data.success) {
            that.$store.commit('login', data.data)
          }
        });
      }
    },
    initFlow() {
      const that = this;
      that.loginTempAdmin();
      if (that.flowData.status == flowConfig.flowStatus.CREATE) {
        that.flowData.attr.name = "flow-" + Utils.getId();
        that.flowData.attr.createTime = new Date().format("yyyy-MM-dd hh:mm:ss");
      } else {
        that.loadFlow();
      }
      Utils.consoleLog(["初始化流程图成功..."]);
    },
    loadFlow(json) {
      const that = this;
      if (that.flowData.nodeList.length) {
        this.$message.error("请先清空当前数据");
        return;
      }
      let loadData = JSON.parse(json);
      that.flowData.attr.maxPlaceId = loadData.attr.maxPlaceId;
      that.flowData.attr.maxTranId = loadData.attr.maxTranId;
      that.flowData.attr.name = loadData.attr.name;
      that.flowData.config = loadData.config;
      that.flowData.status = flowConfig.flowStatus.LOADING;
      let nodeList = loadData.nodeList;
      nodeList.forEach(function (node, index) {
        that.flowData.nodeList.push(node);
      });
      that.plumb.batch(function () {
        that.$nextTick(() => {
          let linkList = loadData.linkList;
          linkList.forEach(function (link, index) {
            that.flowData.linkList.push(link);
            let conn = that.plumb.connect({
              source: link.sourceId,
              target: link.targetId,
              anchor: flowConfig.jsPlumbConfig.anchor.default,
              connector: [
                "Straight",
                {
                  gap: 5,
                  cornerRadius: 8,
                  alwaysRespectStubs: true
                }
              ],
              paintStyle: {
                stroke: "#2a2929",
                strokeWidth: 2
              }
            });
            if (link.weight) {
              conn.setLabel({
                label: link.weight + "",
                cssClass: "linkWeight"
              });
            }
          });
          that.currentSelect = {};
          that.currentSelectGroup = [];
          that.flowData.status = flowConfig.flowStatus.MODIFY;
        });
      }, true);
      let canvasSize = that.computeCanvasSize();
      that.$refs.content.container.pos = {
        top: -canvasSize.minY + 100,
        left: -canvasSize.minX + 100
      };
    },

    selectTool(type) {
      let tool = tools.filter(t => t.type == type);
      if (tool && tool.length >= 0) this.currentTool = tool[0];
      switch (type) {
        case "drag":
          this.changeToDrag();
          break;
        case "connection":
          this.changeToConnection();
          break;
        case "zoom-in":
          this.changeToZoomIn();
          break;
        case "zoom-out":
          this.changeToZoomOut();
          break;
      }
    },
    changeToDrag() {
      const that = this;
      that.flowData.nodeList.forEach(function (node, index) {
        let f = that.plumb.toggleDraggable(node.id);
        if (!f) {
          that.plumb.toggleDraggable(node.id);
        }
        that.plumb.unmakeSource(node.id);
        that.plumb.unmakeTarget(node.id);
      });
    },
    changeToConnection() {
      const that = this;

      that.flowData.nodeList.forEach(function (node, index) {
        let f = that.plumb.toggleDraggable(node.id);
        if (f) {
          that.plumb.toggleDraggable(node.id);
        }
        that.plumb.makeSource(
          node.id,
          flowConfig.jsPlumbConfig.makeSourceConfig
        );
        that.plumb.makeTarget(
          node.id,
          flowConfig.jsPlumbConfig.makeTargetConfig
        );
      });

      that.currentSelect = {};
      that.currentSelectGroup = [];
    },
    changeToZoomIn() {
      console.log("切换到放大工具");
    },
    changeToZoomOut() {
      console.log("切换到缩小工具");
    },
    checkFlow() {
      const that = this;
      let nodeList = that.flowData.nodeList;

      if (nodeList.length <= 0) {
        this.$message.error("流程图中无任何节点！");
        return false;
      }
      return true;
    },

    loadPetri(file) {
      let that = this;
      let reader = new FileReader();
      reader.readAsText(file, "UTF-8");
      reader.onload = function () {
        that.loadFlow(this.result);
      };
      return false;
    },
    saveFlow() {
      const that = this;
      let petriObj = Object.assign({}, that.flowData);
      petriObj.linkList.forEach(link => delete link.cls);
      petriObj.attr.maxPlaceId = that.flowData.attr.maxPlaceId;
      petriObj.attr.maxTranId = that.flowData.attr.maxTranId;
      if (!that.checkFlow()) return;
      petriObj.status = flowConfig.flowStatus.SAVE;

      let petriJson = JSON.stringify(petriObj);

      const data = `\uFEFF${petriJson}`;
      const blob = new Blob([data], {type: "text,charset=UTF-8"});
      const downloadElement = document.createElement("a");
      // 创建下载链接
      const href = window.URL.createObjectURL(blob);
      downloadElement.href = href;
      // 下载文件名
      downloadElement.download = `${petriObj.attr.name}.txt`;
      document.body.appendChild(downloadElement);
      downloadElement.click();
      // 移除元素
      document.body.removeChild(downloadElement);
      // 释放blob对象
      window.URL.revokeObjectURL(href);

      this.$message.success("保存数据成功！");
    },

    computeCanvasSize() {
      const that = this;
      let nodeList = Object.assign([], that.flowData.nodeList),
        minX = nodeList[0].x,
        minY = nodeList[0].y,
        maxX = nodeList[0].x + nodeList[0].width,
        maxY = nodeList[0].y + nodeList[0].height;
      nodeList.forEach(function (node, index) {
        minX = Math.min(minX, node.x);
        minY = Math.min(minY, node.y);
        maxX = Math.max(maxX, node.x + node.width);
        maxY = Math.max(maxY, node.y + node.height);
      });
      let canvasWidth = maxX - minX;
      let canvasHeight = maxY - minY;
      return {
        width: canvasWidth,
        height: canvasHeight,
        minX: minX,
        minY: minY,
        maxX: maxX,
        maxY: maxY
      };
    },
    clear() {
      const that = this;
      that.flowData.nodeList.forEach(function (node, index) {
        that.plumb.remove(node.id);
      });
      that.flowData.attr.name = "flow-" + Utils.getId();
      that.flowData.attr.createTime = new Date().format("yyyy-MM-dd hh:mm:ss");
      that.flowData.attr.maxPlaceId = 0;
      that.flowData.attr.maxTranId = 0;
      that.currentSelect = {};
      that.currentSelectGroup = [];
      that.flowData.nodeList = [];
      that.flowData.linkList = [];
    },


    showLinkContextMenu(e) {
      let event = window.event || e;

      event.preventDefault();
      event.stopPropagation();
      $(".vue-contextmenuName-flow-menu").css("display", "none");
      $(".vue-contextmenuName-node-menu").css("display", "none");
      let x = event.clientX;
      let y = event.clientY;
      this.linkContextMenuData.axis = {x, y};
    },
    deleteLink() {
      const that = this;
      let sourceId = that.currentSelect.sourceId;
      let targetId = that.currentSelect.targetId;
      that.plumb.deleteConnection(
        that.plumb.getConnections({
          source: sourceId,
          target: targetId
        })[0]
      );
      let linkList = this.flowData.linkList;
      linkList.splice(
        linkList.findIndex(
          link => link.sourceId == sourceId || link.targetId == targetId
        ),
        1
      );
      that.currentSelect = {};
    },
    moveNode(type) {
      const that = this;
      let m = flowConfig.defaultStyle.movePx,
        isX = true;
      switch (type) {
        case "left":
          m = -m;
          break;
        case "up":
          m = -m;
          isX = false;
          break;
        case "right":
          break;
        case "down":
          isX = false;
      }

      if (that.currentSelectGroup.length > 0) {
        that.currentSelectGroup.forEach(function (node, index) {
          if (isX) {
            node.x += m;
          } else {
            node.y += m;
          }
        });
        that.plumb.repaintEverything();
      } else if (that.currentSelect.id) {
        if (isX) {
          that.currentSelect.x += m;
        } else {
          that.currentSelect.y += m;
        }
        that.plumb.repaintEverything();
      }
    },

  },
};
</script>

<style lang="scss">
$primary-color: #409EFF;
* {
  margin: 0;
  padding: 0;
  list-style: none;
}

.container {
  border: 2px solid #e4e7ed;
  height: 100%;
  display: flex;
  moz-user-select: -moz-none;
  -moz-user-select: none;
  -o-user-select: none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

.ant-layout-footer {
  padding: 4px 8px;
}

.foot {
  height: auto;
  text-align: center;
}

.tool-item {
  background: #f4f6fc;
  height: 32px;
  line-height: 32px;
  margin: 5px;
  padding-left: 8px;
  text-align: center;
  cursor: pointer;

  &:hover {
    background: #d2d3d6;
  }

  &.active {
    background: black;
  }
}


.ant-list-grid .ant-list-item {
  margin-bottom: 8px;
}

.linkWeight {
  background-color: white;
  padding: 1px;
  border: 1px solid #346789;
  border-radius: 5px;
  opacity: 0.8;
  z-index: 3;
}

.ant-upload-list-text {
  display: none;
}

</style>

