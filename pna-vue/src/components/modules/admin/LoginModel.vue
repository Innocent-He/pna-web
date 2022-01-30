<template>

  <a-modal :visible="loginVisible" @cancel="loginVisible=false" :footer="null">
    <a-form-model
      ref="loginForm"
      id="components-form-demo-normal-login"
      :model="form"
      class="login-form"
    >
      <a-form-model-item
        has-feedback
        label="UserName"
        prop="user"
      >
        <a-input
          v-model="form.userName"
          placeholder="Username"
        >
          <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)"/>
        </a-input>
      </a-form-model-item>

      <a-form-model-item
        has-feedback
        label="PassWord"
        prop="pass"
      >
        <a-input
          v-model="form.passWord"
          placeholder="PassWord"
          type="password"
        >
          <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)"/>
        </a-input>
      </a-form-model-item>


      <a-form-model-item>
        <a-checkbox v-model="form.remember"
        >
          记住我
        </a-checkbox>
        <a-button class="login-form-forgot" @click="openForget" type="link">
          忘记密码？
        </a-button>
        <a-button type="primary" html-type="submit" @click="handleSubmit" class="login-form-button">
          登录
        </a-button>
        <a-button @click="openRegister" type="link">
          注册账号
        </a-button>
      </a-form-model-item>
    </a-form-model>
    <RegisterModel ref="register"/>
  </a-modal>
</template>

<script>
import {login} from "../../util/FetchData"
import RegisterModel from "./RegisterModel";

export default {
  components:{
    RegisterModel
  },
  data() {
    return {
      loginVisible:false,
      form: {
        userName: '',
        passWord: '',
        remember:true,
      },
      show: false,
      rules: {
        user: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        pass: [{required: true, message: '请输入密码', trigger: 'blur'}],
      },
    };
  },
  computed: {
    isMobile() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return false;
      }
      return true;
    },
  },
  methods: {

    handleSubmit() {
      let that=this;
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          login(this.form).then(({data}) => {
            if (data.success) {
              this.$message.success("登录成功！");
              that.loginVisible=false;
              that.$store.commit("login", data.data);
              that.$bus.$emit('clearTask')
            } else {
              this.$message.success("登录失败！失败原因:" + data.message);
            }
          });
        } else {
          return false;
        }
      });
    },
    openRegister() {
      this.$refs.register.registerVisible=true;
    },
    openForget() {
      this.$store.state.loginFlag = false;
      this.$store.state.forgetFlag = true;
    },
  }
};
</script>

<style scoped>
#components-form-demo-normal-login .login-form {
  max-width: 300px;
}

#components-form-demo-normal-login .login-form-forgot {
  float: right;
}

#components-form-demo-normal-login .login-form-button {
  width: 100%;
}

.social-login-wrapper a {
  text-decoration: none;
}

</style>
