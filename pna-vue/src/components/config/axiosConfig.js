import axios from "axios";  //导入axios
import notification from "ant-design-vue/es/notification";

// if (process.env.NODE_ENV === 'development') {
//   axios.defaults.baseURL = 'http://localhost:9001';}
// else if (process.env.NODE_ENV === 'dev') {
//   axios.defaults.baseURL = 'http://localhost:9001';
// }
// else if (process.env.NODE_ENV === 'production') {
if (process.env.NODE_ENV === 'production') {
  axios.defaults.baseURL = 'https://pna.codey.top';
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
    console.log(error)
    if (error.response.status) {
      switch (error.response.status) {
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
            description: "请求失败，请检查网络"
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
