// 同组件，页面要用的话也需要引入这两个
import {
	createStoreBindings
} from 'mobx-miniprogram-bindings'
import {
	store
} from '../../store'
import api from '../../utils/api.js'

Page({
	data: {
		list: []
	},

	/**
	 * 页面每次进入加载
	 */
	onShow: function () {
		const thus = this
		if (store.token == null || store.token == '') {
			wx.login({
				success(res) {
					if (res.code) {
						api.login({
							code: res.code
						}).then(res => {
							store.token = res.data.openId
							// 获取列表数据
							thus.getEditionList()
						})
					} else {
						console.log('登录失败！' + res.errMsg)
					}
				}
			})
		} else {
			thus.getEditionList()
		}
	},
	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		const thus = this
		// 做绑定操作
		this.storeBindings = createStoreBindings(this, {
			store,
			fields: [
				'token'
			],
			actions: ['update'], // 同上，这里是方法名字
		})
	},
	onUnload() {
		// 记住，一定要在页面卸载前，销毁实例，否则会造成内存泄漏！！
		this.storeBindings.destroyStoreBindings()
	},
	/**
	 * 获取列表数据 使用的频率太大了直接提出来
	 */
	getEditionList() {
		api.edition().then(res => {
			if (res.data != null) {
				if (res.data.length > 0) {
					this.setData({
						isempty: false,
					})
				} else {
					this.setData({
						isempty: true,
					})
				}
				this.setData({
					list: res.data
				})
			} else if (res.success == false) {
				// 跳转到授权页面
				wx.switchTab({
					url: '/pages/personal/personal'
				})
			}
		})
	},
})