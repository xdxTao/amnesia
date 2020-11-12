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
    token: '',
    // actions
    update: action(function () {
   
    })
})