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
		list: [],
		labelList: [],
		labelTotalList: [],
		selectLabelTotalId: '',
		// 任务数据
		tmpTitle: '',
		tmpDesc: '',
		// 是否是编辑
		isEditor: false,
		// 当前任务状态，用来判断是否可以更新
		curSts: 0,
	},

	//选择框选中
	checkboxChange: function (e) {
		var checkboxItems = this.data.list, values = e.detail.value;
        for (var i = 0, lenI = checkboxItems.length; i < lenI; ++i) {
            checkboxItems[i].checked = false;

            for (var j = 0, lenJ = values.length; j < lenJ; ++j) {
                if(checkboxItems[i].id == values[j]){
                    checkboxItems[i].ischeck = true;
                    break;
                }
            }
		}
		
        this.setData({
            list: checkboxItems,
            ids: e.detail.value
        });
    },

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		// 做绑定操作
		this.storeBindings = createStoreBindings(this, {
			store,
			fields: [
				'token'
			],
			actions: ['update'], // 同上，这里是方法名字
		})
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
							thus.setData()
							//获取头部默认标签
							thus.getDefaultLabel();
							// 获取列表数据
							// thus.getTaskList()
							// 获取标签数据 
							thus.getListLabel()
							//获取头部标签数据
							thus.getListLabelTotal()

						})
					} else {
						console.log('登录失败！' + res.errMsg)
					}
				}
			})
		} else {
			//获取头部默认标签
			thus.getDefaultLabel();
			// thus.getTaskList()
			// 获取标签数据
			thus.getListLabel()
			//获取头部标签数据
			thus.getListLabelTotal()

			thus.setData()
		}
	},
	onUnload() {
		// 记住，一定要在页面卸载前，销毁实例，否则会造成内存泄漏！！
		this.storeBindings.destroyStoreBindings()
	},
	/**
	 * 打开任务添加窗口
	 */
	openTask: function () {
		var animation = wx.createAnimation({
			duration: 1000,
			timingFunction: 'ease',
			delay: 100
		});
		animation.opacity(1).translateY(-475).step()
		this.setData({
			ani: animation.export()
		})
	},
	/**
	 * 关闭任务添加窗口
	 */
	closeTask() {
		var animation = wx.createAnimation({
			duration: 1000,
			timingFunction: 'ease',
			delay: 100
		});
		animation.opacity(1).translateY(415).step()
		this.setData({
			ani: animation.export()
		})
		// 清空数据
		this.setData({
			tmpDesc: '',
			tmpTitle: '',
			isEditor: false,
		})
		this.onEditorReady()
	},
	/**
	 * 获取input输入的数据
	 */
	getTaskInput(e) {
		this.setData({
			tmpTitle: e.detail.value
		})
	},
	/**
	 * 获取editor输入的数据
	 */
	getTaskEditor(e) {
		this.setData({
			tmpDesc: e.detail.html
		})
	},
	/**
	 * 新增任务
	 */
	addTask() {
		if (this.data.tmpTitle == '') {
			return wx.showToast({
				title: '请输入任务标题',
				icon: 'none',
				duration: 1500
			})
		}
		let thus = this
		api.copyAdd({
			tmpTitle: this.data.tmpTitle,
			tmpDesc: this.data.tmpDesc,
			labelId: this.data.selectLabelTotalId
		}).then(res => {
			if (res.success == true) {
				// 获取列表数据
				thus.getTaskList()
				wx.showToast({
					title: res.msg
				})
				thus.closeTask()
			}
		})
	},
	/**
	 * 更新任务
	 */
	updateTask() {
		if (this.data.curSts == 1) {
			return wx.showToast({
				title: '已完成任务不支持更新',
				icon: 'none',
				duration: 1500
			})
		}
		let thus = this
		api.updateTask({
			tmpTitle: this.data.tmpTitle,
			tmpDesc: this.data.tmpDesc,
			labelId: this.data.selectLabelTotalId
		}).then(res => {
			if (res.success == true) {
				// 获取列表数据
				thus.getTaskList()
				wx.showToast({
					title: res.msg
				})
				thus.closeTask()
			}
		})
	},
	/**
	 * 编辑器数据回显
	 */
	onEditorReady() {
		const thus = this
		const query = wx.createSelectorQuery()
		query.select('#editor')
			.context(function (res) {
				res.context.setContents({
					html: thus.data.tmpDesc
				})
			}).exec()
	},
	/**
	 * 查看任务描述/编辑
	 */
	seeTaskDesc(e) {
		this.setData({
			tmpDesc: e.target.dataset.desc,
			tmpTitle: e.target.dataset.title,
			curSts: e.target.dataset.type,
			selectLabelTotalId: e.target.dataset.labelid,
			isEditor: true
		})
		this.openTask()
		this.onEditorReady()
	},
	/**
	 * 标签列表
	 */
	getListLabel() {
		api.labelListAll({}).then(res => {
			if (res.success == true) {
				this.setData({
					labelList: res.data
				})
			}
		})
	},

	/**
	 * 头部标签总列表
	 */
	getListLabelTotal() {
		api.labelListAll({}).then(res => {
			if (res.success == true) {
				this.setData({
					labelTotalList: res.data
				})
			}
		})
	},
	
	/**
	 * 改变选中的头部标签
	 */
	changeLabelTotal(e) {
		let labelId = e.currentTarget.dataset.labelid
		this.setData({
			selectLabelTotalId: labelId
		})
		this.getTaskList()
	},

	/**
	 * 获取列表数据 使用的频率太大了直接提出来
	 */
	getTaskList() {
		let labelId = this.data.selectLabelTotalId
		api.copyTaskList({
			labelId: labelId
		}).then(res => {
			if (res.data != null) {
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
	/**
	 * 获取默认标签
	 */
	getDefaultLabel() {
		api.getDefaultLabel({}).then(res => {
			if (res.success == true) {
				this.setData({
					selectLabelTotalId: res.data
				})
				// 获取列表数据
				this.getTaskList()
			}
		})
	},
		/**
	 * 删除列表数据
	 */
	delTask(e) {
		let id = e.target.dataset.id
		let thus = this
		api.copyDel({
			id: id
		}).then(res => {
			if (res.success == true) {
				// 获取列表数据
				thus.getTaskList()
				wx.showToast({
					title: res.msg
				})
			}
		})
	},
			/**
	 * 复制添加列表数据
	 */
	copyTask(e) {
		var ids = this.data.ids
		this.data.ids = null
		console.log(ids)
		// let labelId = this.data.selectLabelTotalId
		api.copyTask({
			ids: ids
		}).then(res => {
			if (res.success == true) {
				// 获取列表数据
				// thus.getTaskList()
				wx.showToast({
					title: res.msg
				})
			}
			this.getTaskList()
		})
	},

})