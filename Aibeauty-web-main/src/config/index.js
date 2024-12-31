/**
 * 环境配置文件
 * 一般在企业级项目里面有三个环境
 * 开发环境
 * 测试环境
 * 线上环境
 */
// 当前的环境
const env = import.meta.env.MODE || 'prod'

const EnvConfig = {
  development: {
    baseApi: 'http://127.0.0.1:8080',
    mockApi: 'http://127.0.0.1:4523/m1/4177433-0-default',
  },
//   test: {
//     baseApi: '//test.future.com/api',
//     mockApi: '',
//   },
//   pro: {
//     baseApi: '//future.com/api',
//     mockApi: '',
//   },
}

export default {
  env,
  // mock的总开关,
  mock: true,
  ...EnvConfig[env]
}