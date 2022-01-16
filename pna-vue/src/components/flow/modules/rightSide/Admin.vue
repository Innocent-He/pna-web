<template>
<div id="admin">
  <template
    v-if="!this.$store.state.userId"
  >
    <a-button
      @click="openLogin"
      size="large"
      shape="circle"
      class="login-avatar"
      id="login-btn"
    >
      <i class="iconfont"> 登录</i>
    </a-button>
  </template>

  <template v-else>
    <a-avatar
      class="login-avatar"
      shape="circle"
      size="large"
      icon="user"
      style="background: lightskyblue"
    >
<!--    //todo  记得放一个默认头像-->
    </a-avatar>
    <a-button
    @click="logout" id="logout" size="small">退出登录</a-button>
    <TaskList></TaskList>
  </template>
</div>
</template>

<script>
import axios from "axios";
import TaskList from "./TaskList";
export default {
  name: "Admin",
  components:{
    TaskList,
  },
  methods:{
    openLogin() {
      this.$store.state.loginFlag = true;
    },
    logout() {
      //todo 记得做退出登录逻辑
      axios.get("http://127.0.0.1:9001/logout/"+this.$store.state.userId).then(({ data }) => {
        if (data.success) {
          this.$message.success("注销成功");
          this.$store.commit("logout");
        } else {
          this.$toast({ type: "error", message: data.message });
        }
      });
    }
  }
}
</script>

<style scoped>

#admin{
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
#logout{
  align-self: stretch;
}
#login-btn{
  background-image: linear-gradient(to bottom right, pink, skyblue);
}
.login-avatar{
  width: 150px;
  height: 150px;
}
.iconfont {
  font-family: Arial, Helvetica, sans-serif;
  font-style: normal;
  font-size: 20px;
  font-weight: bold;
}

</style>
