<template>
  <div id="admin">
    <template
      v-if="!this.$store.state.userInfo.userId"
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
      <p>临时用户:{{$store.state.userInfo.userName}}</p>
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
      <i>用户:{{$store.state.userInfo.userName}}</i>
      <i>邮箱:{{$store.state.userInfo.email}}</i>
      <a-button
        @click="logout"
        id="logout"
        size="small">退出登录
      </a-button>
    </template>
    <TaskList :queryAll="false"></TaskList>
    <LoginModel ref="login"/>
    <RegisterModel ref="register"/>
  </div>
</template>

<script>
import TaskList from "./TaskList";
import LoginModel from "../admin/LoginModel";
import RegisterModel from "../admin/RegisterModel";
import {logout} from "../../util/FetchData"

export default {
  name: "Admin",
  components: {
    TaskList,
    LoginModel,
    RegisterModel
  },
  methods: {
    openLogin() {
      this.$refs.login.loginVisible = true;
    },
    logout() {
      logout().then(({data}) => {
        if (data.success) {
          this.$message.success("注销成功");
          this.$store.commit("logout");
          this.$cookies.remove("pna-token", '/')
          this.$bus.$emit('loginTemp');
          this.$bus.$emit('clearTask');
        } else {
          this.$message.error("注销失败，原因:" + data.message);
        }
      });
    }
  }
}
</script>

<style scoped>

#admin {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

#logout {
  align-self: stretch;
}

#login-btn {
  background-image: linear-gradient(to bottom right, pink, skyblue);
}

.login-avatar {
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
