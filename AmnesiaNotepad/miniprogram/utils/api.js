import apiRequest from './request.js';
// const HOST = 'http://127.0.0.1:8081/api/';
// 真机测试需要用这个ip
// const HOST = 'http://192.168.16.106:8081/api/'
const HOST = 'https://amnesia.xdx97.com:8081/api/'
import { store } from '../store'
const API_LIST = {
    // 登录
    login:{
        method: 'GET',
        url: 'user/login'
    },
    // 判断是否授权
    isAuthorize:{
        method: 'GET',
        url: 'user/isAuthorize'
    },
    // 用户授权
    authorize:{
        method: 'GET',
        url: 'user/authorize'
    },
    // 同步授权
    synAuthorize:{
        method: 'GET',
        url: 'user/synAuthorize'
    },
    // 获取用户编号
    userNumber:{
        method: 'GET',
        url: 'user/userNumber'
    },
    // 获取任务列表数据
    taskList:{
        method: 'GET',
        url: 'task/list'
    },
    // 新增任务
    addTask:{
        method: 'POST',
        url: 'task/add'
    },
    // 更新任务
    updateTask:{
        method: 'POST',
        url: 'task/update'
    },
    // 任务完成
    completeTask:{
        method: 'GET',
        url: 'task/complete'
    },
    // 任务转移
    transfer:{
        method: 'GET',
        url: 'task/transfer'
    },
    // 标签列表
    labelList:{
        method: 'POST',
        url: 'label/list'
    },
    // 标签列表带统计
    labelListAll:{
        method: 'GET',  
        url: 'label/listAll'
    },
    // 添加标签
    addLabel:{
        method: 'POST',
        url: 'label/add'
    },
    // 修改默认标签
    updateDefault:{
        method: 'POST',
        url: 'label/updateDefault'
    },
    // 修改标签
    updateLabel:{
        method: 'POST',
        url: 'label/update'
    },
    // 获取默认标签
    getDefaultLabel:{
        method: 'POST',
        url: 'label/getDefaultLabel'
    },
    
}

/*
多参数合并
*/
function MyHttp(defaultParams, API_LIST) {

    let _build_url = HOST;
    let resource = {};
    for (let actionName in API_LIST) {

        let _config = API_LIST[actionName];
        resource[actionName] = (pdata) => {
            let _params_data = pdata;
            return apiRequest(_build_url + _config.url, _config.method, _params_data, {
            'token' : store.token
            });
        }
    }
    return resource;
}
const Api = new MyHttp({}, API_LIST);
export default Api;