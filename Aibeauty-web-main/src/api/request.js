import axios from 'axios'
import config from '../config/index.js';
import { ElMessage } from 'element-plus'
import { useTokenStore } from '../assets/stores/index.js';
const NETWORK_ERROR = '网络请求异常,请稍后重试.....'
const WRONG_PASSWORD = '密码错误,请重新输入！'
// 创建一个axios实例对象
const service = axios.create({
  baseURL: config.baseApi
})

// 在请求之前做一些事情
service.interceptors.request.use((req) => {
  // 可以自定义header
  // jwt-token认证的时候 
  const tokenStore = useTokenStore();
  const token = tokenStore.$state.token;
  req.headers['Authorization'] = `Bearer ${token}`;
  // req.headers['Authorization'] = `Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUsImV4cCI6MTcxODk3MjA5NH0.DOEc8g98-66l7L7MSZuJVBUCvNT1D3XjQQrpyljtF3Q`;
  return req
})

// 在请求之后做一些事情
service.interceptors.response.use((res) => {
  const { code, data, message } = res.data
  // console.log(res)
  if (code == 200) {
    return data
  } else {
    // 网络请求错误或者其他情况
    // ElMessage.error(message || NETWORK_ERROR)
    // return Promise.reject(message || NETWORK_ERROR)
    return Promise.reject(NETWORK_ERROR)
  }
})

// 封装的核心函数
function request(options) {
  options.method = options.method || 'get'
  if (options.method.toLowerCase() == 'get') {
    options.params = options.data
  }
  // 对mock的处理
  let isMock = config.mock
  if (typeof options.mock !== 'undefined') {
    isMock = options.mock
  }
  // 对线上环境做处理
  if (config.env == 'prod') {
    service.defaults.baseURL = config.baseApi
  } else {
    service.defaults.baseURL = isMock ? config.mockApi : config.baseApi
  }

  return service(options)
}
export default request