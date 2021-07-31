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
		//拖动排序所需数据
		pageInfo: {
			rowHeight: 30,
			scrollHeight: 85,
			startIndex: null,
			scrollY: true,
			readyPlaceIndex: null,
			startY: 0,
			selectedIndex: null,
		},
		movableViewInfo: {
			y: 0,
			showClass: 'none',
			data: {}
		},
	},

	//选择框选中
	checkboxChange: function (e) {
		var checkboxItems = this.data.list
		var values = e.detail.value;
		for (var i = 0, lenI = checkboxItems.length; i < lenI; ++i) {
			checkboxItems[i].ischeck = false;

			for (var j = 0, lenJ = values.length; j < lenJ; ++j) {
				if (checkboxItems[i].id == values[j]) {
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
	//拖动实现
	dragStart: function (event) {
		var startIndex = event.target.dataset.index
		// console.log('获取到的元素为', this.data.list[startIndex])
		// 初始化页面数据
		var pageInfo = this.data.pageInfo
		pageInfo.startY = event.touches[0].clientY
		pageInfo.readyPlaceIndex = startIndex
		pageInfo.selectedIndex = startIndex
		pageInfo.scrollY = false
		pageInfo.startIndex = startIndex
		// console.log( pageInfo.startY )
		this.setData({
			'movableViewInfo.y': pageInfo.startY - (pageInfo.rowHeight / 2),
		})
		// 初始化拖动控件数据
		var movableViewInfo = this.data.movableViewInfo
		movableViewInfo.data = this.data.list[startIndex]
		movableViewInfo.showClass = "inline"

		this.setData({
			movableViewInfo: movableViewInfo,
			pageInfo: pageInfo
		})
	},

	dragMove: function (event) {
		var list = this.data.list
		var pageInfo = this.data.pageInfo
		// 计算拖拽距离
		var movableViewInfo = this.data.movableViewInfo
		var movedDistance = event.touches[0].clientY - pageInfo.startY
		movableViewInfo.y = pageInfo.startY - (pageInfo.rowHeight / 2) + movedDistance
		// console.log('移动的距离为', movedDistance)

		// 修改预计放置位置
		var movedIndex = parseInt(movedDistance / pageInfo.rowHeight)
		var readyPlaceIndex = pageInfo.startIndex + movedIndex
		if (readyPlaceIndex < 0) {
			readyPlaceIndex = 0
		} else if (readyPlaceIndex >= list.length) {
			readyPlaceIndex = list.length - 1
		}
		if (readyPlaceIndex != pageInfo.selectedIndex) {
			var selectedData = list[pageInfo.selectedIndex]
			list.splice(pageInfo.selectedIndex, 1)
			list.splice(readyPlaceIndex, 0, selectedData)
			pageInfo.selectedIndex = readyPlaceIndex
		}
		// 移动movableView
		pageInfo.readyPlaceIndex = readyPlaceIndex
		// console.log('移动到了索引', readyPlaceIndex, '选项为', list[readyPlaceIndex])
		this.setData({
			movableViewInfo: movableViewInfo,
			list: list,
			pageInfo: pageInfo
		})

	},

	dragEnd: function (event) {
		// 重置页面数据
		var pageInfo = this.data.pageInfo
		pageInfo.readyPlaceIndex = null
		pageInfo.startY = null
		pageInfo.selectedIndex = null
		pageInfo.startIndex = null
		pageInfo.scrollY = true
		// 隐藏movableView
		var movableViewInfo = this.data.movableViewInfo
		movableViewInfo.showClass = 'none'
		var optionList = this.data.list
		this.setData({
			pageInfo: pageInfo,
			movableViewInfo: movableViewInfo,
		})
		api.dailySort({
			"sortList": optionList
		}).then(res => {
			wx.showToast({
				title: res.msg
			})
		})

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
		//获取头部默认标签
		thus.getDefaultLabel();
		// 获取列表数据
		// thus.getTaskList()
		// 获取标签数据 
		thus.getListLabel()
		//获取头部标签数据
		thus.getListLabelTotal()
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