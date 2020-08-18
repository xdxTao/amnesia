import {
    configure,
    observable,
    action
} from 'mobx-miniprogram'

// 不允许在动作外部修改状态
// configure({
//     enforceActions: 'observed'
// });

export const store = observable({
    isAuthorize: false,
    token: '',
    requestPrefix:'http://127.0.0.1:8080/api/',
    // actions
    update: action(function () {
        this.isAuthorize = true
    })
})