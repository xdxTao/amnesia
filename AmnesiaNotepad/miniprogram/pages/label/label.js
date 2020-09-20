// 同组件，页面要用的话也需要引入这两个
import api from '../../utils/api.js'
// pages/label/label.js
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		list: [],
		label: '',
		labelId: '',
		// 是否是编辑
		isEditor: false,

		movableViewInfo: {
			y: 0,
			showClass: 'none',
			data: {}
		},

		pageInfo: {
			rowHeight: 30,
			scrollHeight: 85,
			startIndex: null,
			scrollY: true,
			readyPlaceIndex: null,
			startY: 0,
			selectedIndex: null,
		}
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
		api.labelSort({
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
		this.getList()
	},

	/**
	 * 打开任务添加窗口
	 */
	openTask: function (e) {
		let labelname = e.currentTarget.dataset.labelname
		let labelId = e.currentTarget.dataset.labelid
		var animation = wx.createAnimation({
			duration: 1000,
			timingFunction: 'ease',
			delay: 100
		});
		animation.opacity(1).translateY(-500).step()
		this.setData({
			ani: animation.export()
		})
		if (labelname != '') {
			this.setData({
				label: labelname,
				labelId: labelId,
				isEditor: true
			})
		}

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
			label: '',
			isEditor: false
		})
	},
	/**
	 * 新增任务
	 */
	addLabel() {
		if (this.data.label == '') {
			return wx.showToast({
				title: '请输入标签',
				icon: 'none',
				duration: 1500
			})
		}
		let thus = this
		api.addLabel({
			labelName: this.data.label
		}).then(res => {
			if (res.success == true) {
				// 获取列表数据
				thus.getList()
				wx.showToast({
					title: res.msg
				})
				thus.closeTask()
			}
		})
	},
	getList() {
		api.labelList({}).then(res => {
			if (res.success == true) {
				this.setData({
					list: res.data
				})
			}
		})
	},
	/**
	 * 获取input输入的数据
	 */
	getLabelInput(e) {
		this.setData({
			label: e.detail.value
		})
	},
	/**
	 * 改变默认标签
	 */
	changeDefault(e) {
		let labelid = e.currentTarget.dataset.labelid
		let userid = e.currentTarget.dataset.userid
		let status = e.detail.value
		let thus = this
		api.updateDefault({
			"labelId": labelid,
			"userId": userid,
			"status": status
		}).then(res => {
			thus.getList()
		})
	},
	/**
	 * 是否显示
	 */
	changeShow(e) {
		let labelid = e.currentTarget.dataset.labelid
		let labelStatus = e.detail.value == true ? 1 : 0
		let thus = this
		api.updateLabel({
			"labelId": labelid,
			"labelStatus": labelStatus
		}).then(res => {
			thus.getList()
		})
	},
	/**
	 * 修改排序
	 */
	changeSort(e) {
		let labelid = e.currentTarget.dataset.labelid
		let labelSort = e.detail.value
		let thus = this
		api.updateLabel({
			"labelId": labelid,
			"labelSort": labelSort
		}).then(res => {
			thus.getList()
		})
	},
	/**
	 * 更新标签名
	 */
	updateName() {
		if (this.data.label == '') {
			return wx.showToast({
				title: '请输入标签',
				icon: 'none',
				duration: 1500
			})
		}
		if (this.data.labelId == '') {
			return wx.showToast({
				title: '系统异常',
				icon: 'none',
				duration: 1500
			})
		}
		console.log(this.data.labelId);

		let thus = this
		api.updateLabel({
			labelName: this.data.label,
			labelId: this.data.labelId
		}).then(res => {
			if (res.success == true) {
				// 获取列表数据
				thus.getList()
				wx.showToast({
					title: res.msg
				})
				thus.closeTask()
			}
		})

	}

})