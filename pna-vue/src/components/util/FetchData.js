import * as api from '../config/axiosConfig'

export function login(params) {
  return api.post('/api/login', params)
}

export function userInfo(){
  return api.get('/api/userInfo');
}

export function logout() {
  return api.get("/api/logout")
}


export function ip() {
  return api.get('/api/ip')
}

export function register(params) {
  return api.post('/api/register',params);
}

export function taskList(taskReq){
  return api.post('/api/tasks',taskReq)
}

export function  deleteTask(taskId){
  return api.get('/api/delete/task/'+taskId);
}

export function cancelTask(taskId){
  return api.get('/api/cancel/'+taskId);
}

export function algRequest(algReq) {
  return api.post("/api/algReq", algReq)
}
export function generateNet(params){
  return api.post('/api/generateNet',params)
}


