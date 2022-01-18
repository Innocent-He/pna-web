import axios from "axios";  //导入axios
import notification from "ant-design-vue/es/notification";

if (process.env.NODE_ENV == 'development') {
  axios.defaults.baseURL = 'http://127.0.0.1:8080';}
else if (process.env.NODE_ENV == 'dev') {
  axios.defaults.baseURL = 'http://127.0.0.1:9001';
}
else if (process.env.NODE_ENV == 'production') {
  axios.defaults.baseURL = 'http://pna.codey.com';
}

const service = axios.create({
  timeout: 60 * 1000, // 请求超时时间,
  headers: {"Content-Type": "application/json"},
  withCredentials: true,
  crossDomain: true,
});

service.interceptors.response.use(
  response => {
    if (response.status === 200) {
      return Promise.resolve(response);
    } else {
      return Promise.reject(response);
    }
  },
  // 服务器状态码不是200的情况
  error => {
    if (error.response.status) {
      switch (error.response.status) {
        // 401: 未登录
        // 未登录则跳转登录页面，并携带当前页面的路径
        // 在登录成功后返回当前页面，这一步需要在登录页操作。
        case 401:
          notification.error({
            message: "请求失败",
            description: "登录过期，请重新登录"
          });
          break;
        // 403 token过期
        // 登录过期对用户进行提示
        // 清除本地token和清空vuex中token对象
        // 跳转登录页面
        case 403:
          notification.error({
            message: "请求失败",
            description: "登录过期，请重新登录"
          });
          break;
        // 404请求不存在
        case 404:
          notification.error({
            message: "请求失败",
            description: "网络请求不存在"
          });
          break;
        // 其他错误，直接抛出错误提示
        default:
          notification.error({
            message: "请求失败",
            description: error.response.data.message
          });
      }
      return Promise.reject(error.response);
    }
  }
);

export function get(url, paramObj) {
  let newObj = {};
  for (let key in paramObj) {
    if (paramObj[key]) {
      newObj[key]=paramObj[key];
    }
  }
  return new Promise((resolve, reject) => {
    service
      .get(url, {
        params:newObj
      })
      .then(res => {
        resolve(res);
      }).catch(err => {
      reject(err);
    })
  })
}

export function post(url, params) {
  return new Promise((resolve, reject) => {
    service
      .post(url, JSON.stringify(params))
      .then(res => {
        resolve(res);
      })
      .catch(err => {
        reject(err);
      })
  })
}
