// 同组件，页面要用的话也需要引入这两个
import { createStoreBindings } from 'mobx-miniprogram-bindings'
import { store } from '../../store'
import  api  from '../../utils/api.js'
Page({
 	/**
	 * 页面的初始数据
	 */
	data: {
        bgColor:['#fff','#fff','#fff','#fff'],
        userSort: -1
	},
    inBox: function(e){
        let index = e.currentTarget.dataset['index'];  
        const thus = this
        var cur = 'bgColor[' + index + ']'
        this.setData({
            [cur] : '#f8f8f8'
        })　

        var timer = setTimeout(function(){
            thus.setData({
                [cur] : '#fff'
            })　
            clearTimeout(timer)  
        },1*100)
    },
    onLoad() {
        const thus = this
        // 做绑定操作
       	this.storeBindings = createStoreBindings(this, {
			store,
            fields: ['token'],// 这里取在store.js里的变量名，还是一样，用到什么取什么
			actions: [], // 同上，这里是方法名字
        })
        api.userNumber({
        }).then(res => {
            this.setData({
                userSort: res.data,
            })
        })
    },
    onUnload() {
      // 记住，一定要在页面卸载前，销毁实例，否则会造成内存泄漏！！
       this.storeBindings.destroyStoreBindings()
	},
})