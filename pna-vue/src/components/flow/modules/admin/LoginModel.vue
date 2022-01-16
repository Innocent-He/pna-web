<template>

  <a-modal :visible="loginFlag" @cancel="loginFlag=false" :footer="null">
    <a-form
      id="components-form-demo-normal-login"
      :form="form"
      class="login-form"
      @submit="handleSubmit"
    >
      <a-form-item>
        <a-input
          v-decorator="userDecorator"
          placeholder="Username"
        >
          <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)"/>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-input
          v-decorator="pswDecorator"
          type="password"
          placeholder="Password"
        >
          <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)"/>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-checkbox
          v-decorator="[
          'remember',
          {
            valuePropName: 'checked',
            initialValue: true,
          },
        ]"
        >
        记住我
        </a-checkbox>
        <a-button class="login-form-forgot" @click="openForget" type="link">
          忘记密码？
        </a-button>
        <a-button type="primary" html-type="submit" class="login-form-button">
          登录
        </a-button>
        <a-button @click="openRegister" type="link">
          注册账号
        </a-button>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import axios from "axios";

export default {
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: 'normal_login' });
  },
  data() {
    return {
      userDecorator:[
        'username',
        {
          rules: [{
            required: true,
            message: '请输入用户名!',
            whitespace:true
          }]
        }
      ],
      pswDecorator: [
        'password',
        {
          rules: [{
            required: true,
            message: '请输入密码!',
            whitespace:true
          }]
        },
      ],
      show: false,
      rules: {
        email: [{validator: this.validPassWord, trigger: 'change'}],
        pass: [{validator: this.validEmail, trigger: 'change'}],
      },
    };
  },
  computed: {
    loginFlag: {
      set(value) {
        this.$store.state.loginFlag = value;
      },
      get() {
        return this.$store.state.loginFlag;
      }
    },
    isMobile() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return false;
      }
      return true;
    },
  },
  methods: {

    handleSubmit(e) {
      e.preventDefault();
      let that=this
      that.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values);
          axios.post("/api/login",values).then(({ data }) => {
            console.log(data)
            if (data.success) {
              this.$message.success("登录成功！");
              that.$store.commit("closeModel");
              that.$store.commit("login",data.data)
            } else {
              this.$message.success("登录失败！失败原因:"+data.message);
            }
          });

        }
      });
    },
    openRegister() {
      this.$store.state.loginFlag = false;
      this.$store.state.registerFlag = true;
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
