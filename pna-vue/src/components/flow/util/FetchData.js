import * as api from '../config/axiosConfig'
import axios from "axios";

export function login(params) {
  return api.post('/api/login', params)
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

export function taskList(userName){
  return api.get('/api/tasks/'+userName)
}

export function cancelTask(taskId){
  return api.get('/api/cancel/'+taskId);
}

export function algRequest(algReq) {
  return api.post("/api/algReq", algReq)
}
