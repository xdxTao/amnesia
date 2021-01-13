import apiRequest from './request.js';
// 真机测试需要用这个ip
const HOST = 'http://127.0.0.1:8082/api/'
// const HOST = 'http://192.168.137.143:8082/api/'

import {
    store
} from '../store'
const API_LIST = {
    // 登录
    login: {
        method: 'GET',
        url: 'user/login'
    },
    // 获取用户编号
    userNumber: {
        method: 'GET',
        url: 'user/userNumber'
    },
    // 使用帮助已读
    completeHelpRead: {
        method: 'GET',
        url: 'user/completeHelpRead'
    },
    // 获取任务列表数据
    taskList: {
        method: 'GET',
        url: 'task/list'
    },
    // 新增任务
    addTask: {
        method: 'POST',
        url: 'task/add'
    },
    // 更新任务
    updateTask: {
        method: 'POST',
        url: 'task/update'
    },
    // 任务完成
    completeTask: {
        method: 'GET',
        url: 'task/complete'
    },
    // 任务转移
    transfer: {
        method: 'GET',
        url: 'task/transfer'
    },
    // 标签列表
    labelList: {
        method: 'POST',
        url: 'label/list'
    },
    // 标签列表带统计
    labelListAll: {
        method: 'GET',
        url: 'label/listAll'
    },
    // 添加标签
    addLabel: {
        method: 'POST',
        url: 'label/add'
    },
    // 修改默认标签
    updateDefault: {
        method: 'POST',
        url: 'label/updateDefault'
    },
    // 修改标签
    updateLabel: {
        method: 'POST',
        url: 'label/update'
    },
    // 获取默认标签
    getDefaultLabel: {
        method: 'POST',
        url: 'label/getDefaultLabel'
    },
    // 标签拖动排序
    labelSort: {
        method: 'POST',
        url: 'label/sort'
    },
    // 任务拖动排序
    taskSort: {
        method: 'POST',
        url: 'task/sort'
    },
    // 版本更新数据
    edition: {
        method: 'GET',
        url: 'other/editionInfo'
    },
    // 日常任务新增
    copyAdd: {
        method: 'POST',
        url: 'tmp/add'
    },
    // 日常任务列表显示
    copyTaskList: {
        method: 'GET',
        url: 'tmp/list'
    },
     // 日常任务列表数据删除
     copyDel: {
        method: 'GET',
        url: 'tmp/del'
    },
     // 日常任务列表复制
     copyTask: {
        method: 'POST',
        url: 'tmp/copy'
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
                'token': store.token
            });
        }
    }
    return resource;
}
const Api = new MyHttp({}, API_LIST);
export default Api;