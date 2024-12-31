/**
 * 整个项目api的管理
 * 
 */
import request from "./request.js"

export default {
  login(params) {
    return request({
      url: '/users/login',
      method: 'post',
      data: params
    })
  },
  getUserInfo(params) {
    return request({
      url: '/users/getUserInfo',
      method: 'get',
      data: params
    })
  },
  test(params) {
    return request({
      url: '/users/test',
      method: 'get',
      data: params
    })
  },
  getEmployeeList(params) {
    return request({
      url: '/employee/list',
      method: 'get',
      data: params
    })
  },
  getStoreList(params) {
    return request({
      url: '/stores/getAllStoresInfo',
      method: 'post',
      data: params
    })
  },
  addStore(params) {
    return request({
      url: '/stores/register',
      method: 'post',
      data: params
    })
  },
  addStoreByCreateUser(params) {
    return request({
      url: '/stores/addByCreateUser',
      method: 'post',
      data: params
    })
  },
  addStoreByChooseUser(params) {
    return request({
      url: '/stores/addByChooseUser',
      method: 'post',
      data: params
    })
  },
  addEmployee(params) {
    return request({
      url: '/users/register',
      method: 'post',
      data: params
    })
  },
  deleteEmployee(params) {
    return request({
      url: '/users/delete',
      method: 'delete',
      data: params
    })
  },
  searchUserInfo(params) {
    return request({
      url: '/users/searchUserInfo',
      method: 'get',
      data: params
    })
  },
  getAllInfo(params) {
    return request({
      url: '/users/getAllInfo',
      method: 'get',
      data: params
    })
  },
  getGoodsList(params) {
    return request({
      url: '/goods/getGoodsList',
      method: 'get',
      data: params
    })
  },
  getCustomersList(params) {
    return request({
      url: '/customers/webGetList',
      method: 'get',
      data: params
    })
  },
  //获得左侧动态菜单栏的接口
  getMenu(params) {
    return request({
      url: '/common/getMenu',
      method: 'get',
      data: params
    })
  },
  getAvatar(params) {
    return request({
      url: '/users/getImage',
      method: 'get',
      data: params,
    })
  },
  updateUser(params) {
    return request({
      url: '/users/updateUser',
      method: 'put',
      data: params,
    })
  },
  updateStore(params) {
    return request({
      url: '/stores/updateInfo',
      method: 'put',
      data: params,
    })
  },
  getInfo(params) {
    return request({
      url: '/users/getInfo',
      method: 'get',
      data: params,
    })
  },
  getCategories() {
    return request({
      url: '/goods/categories',
      method: 'get',
    })
  },
  getAllBrand() {
    return request({
      url: '/products/getAllBrand',
      method: 'get',
    })
  },
  createGood(params) {
    return request({
      url: '/goods/create',
      method: 'post',
      data: params,
    })
  },
  editGood(params) {
    return request({
      url: '/goods/edit',
      method: 'post',
      data: params,
    })
  },
  deleteGood(params) {
    return request({
      url: '/goods/delete',
      method: 'delete',
      data: params,
    })
  }
}