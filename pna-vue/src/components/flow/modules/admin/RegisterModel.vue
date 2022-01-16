<template>

  <a-modal :visible="registerFlag" @cancel="registerFlag=false" :footer="null">
    <a-form
      id="components-form-demo-normal-login"
      :form="form"
      class="register-form"
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
          v-decorator="emailDecorator"
          placeholder="Email"
        >
          <a-icon slot="prefix" type="mail" style="color: rgba(0,0,0,.25)"/>
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
        <a-button type="primary" html-type="submit" class="login-form-button">
          注册
        </a-button>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import axios from "axios";

export default {
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: 'normal_register' });
  },
  data() {
    return {
      adminInfo: {
        username: "",
        email: "",
        password: "",
      },
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
      emailDecorator:[
        'email',
        {
          rules: [{
            required: false,
            message: '请检查邮箱格式!',
            pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
          }]
        },
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
    registerFlag: {
      set(value) {
        this.$store.state.registerFlag = value;
      },
      get() {
        return this.$store.state.registerFlag;
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
      let that=this;
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values);
          axios.post("/api/register",values).then(({ data }) => {
            if (data.success) {
              this.$message.success("注册成功！");
            } else {
              this.$message.success("注册失败！失败原因:"+data.message);
            }
          });
          that.$store.commit("closeModel");
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
    }
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
