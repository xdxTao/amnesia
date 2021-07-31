// 同组件，页面要用的话也需要引入这两个
import {
	createStoreBindings
} from 'mobx-miniprogram-bindings'
import {
	store
} from '../../store'
import api from '../../utils/api.js'
import config from '../../utils/config.js'

var dateTimePicker = require('../../utils/dateTimePicker.js');

Page({
	data: {
		isload: true,
		isempty: true,
		list: [],
		labelList: [],
		selectLabelId: '',
		// 任务数据
		taskId: '',
		taskTitle: '',
		taskDesc: '',
		curDayList: ['星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
		curDay: new Date().getDay(),
		// 是否是编辑
		isEditor: false,
		// 当前任务状态，用来判断是否可以更新
		curSts: 0,
		dateTimeArray: null,
		dateTime: null,
		taskNoticeTime: null,
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
		api.taskSort({
			"sortList": optionList
		}).then(res => {
			wx.showToast({
				title: res.msg
			})
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
							thus.setData({
								isload: false,
							})
							if (res.data.helpRead == null || res.data.helpRead != 1) {
								wx.showModal({
									title: '提示',
									content: '点击确定查看入门帮助!',
									success: function (res) {
										if (res.confirm) {
											api.completeHelpRead()
											wx.navigateTo({
												url: '../help/help',
											})
										}
									}
								})
							}
							// 获取列表数据
							thus.getTaskList()
							// 获取标签数据 
							thus.getListLabel()
						})
					} else {
						console.log('登录失败！' + res.errMsg)
					}
				}
			})
		} else {
			thus.getTaskList()
			// 获取标签数据
			thus.getListLabel()
			thus.setData({
				isload: false,
			})
		}
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
		});

	},
	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

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
		var obj = dateTimePicker.dateTimePicker(this.data.startYear, this.data.endYear);
		// 精确到分的处理，将数组的秒去掉
		var lastArray = obj.dateTimeArray.pop();
		var lastTime = obj.dateTime.pop();


		this.setData({
			ani: animation.export(),
			dateTimeArray: obj.dateTimeArray,
			dateTime: obj.dateTime
		});
		// this.setData({
		// })
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
			taskDesc: '',
			taskTitle: '',
			isEditor: false,
			selectLabelId: '',
			taskNoticeTime: ''
		})
		this.onEditorReady()
	},
	/**
	 * 获取input输入的数据
	 */
	getTaskInput(e) {
		this.setData({
			taskTitle: e.detail.value
		})
	},
	/**
	 * 获取editor输入的数据
	 */
	getTaskEditor(e) {
		this.setData({
			taskDesc: e.detail.html
		})
	},
	/**
	 * 新增任务
	 */
	addTask() {
		if (this.data.taskTitle == '') {
			return wx.showToast({
				title: '请输入任务标题',
				icon: 'none',
				duration: 1500
			})
		}
		if (this.data.taskNoticeTime) {
			console.log(this.data.taskNoticeTime)
			console.log(2)
		}
		console.log(config.tmplIds)
		console.log()

		console.log(this.data.taskNoticeTime)
		let thus = this
		let taskNoticeTime = this.data.taskNoticeTime
		let tmplId = [];
		tmplId[0] = config.tmplIds

		if (taskNoticeTime) {
			console.log('ff')
			wx.requestSubscribeMessage({
				tmplIds: tmplId,
				success: (res) => {
					console.log(111)
					console.log(res[tmplId])
					if (res[tmplId] == 'accept') {
						console.log(33)
						//调用改变通知状态接口
						api.addTask({
							taskTitle: this.data.taskTitle,
							taskDesc: this.data.taskDesc,
							labelId: this.data.selectLabelId,
							taskNoticeTime: this.data.taskNoticeTime
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
						// 获取列表数据
						// this.getList()
					} else if (res[tmplId] == 'reject') {
						// 获取列表数据
						// this.getList()
					}
				},
				fail: (errCode, errMessage) => {
					console.log(errCode)
					console.log(errMessage)

					wx.showToast({
						title: '系统异常',
						icon: 'none',
						duration: 1500
					})
					// 获取列表数据
					// this.getList()

				}
			})
		} else {
			api.addTask({
				taskTitle: this.data.taskTitle,
				taskDesc: this.data.taskDesc,
				labelId: this.data.selectLabelId,
				taskNoticeTime: this.data.taskNoticeTime
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
		}

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
		if (this.data.taskId == '') {
			return wx.showToast({
				title: '系统异常',
				icon: 'none',
				duration: 1500
			})
		}
		let thus = this
		let taskNoticeTime = this.data.taskNoticeTime
		let tmplId = [];
		tmplId[0] = config.tmplIds
		if(taskNoticeTime){
			wx.requestSubscribeMessage({
				tmplIds: tmplId,
				success: (res) => {
					console.log(111)
					console.log(res[tmplId])
					if (res[tmplId] == 'accept') {
						console.log(33)
						//调用更新任务
						api.updateTask({
							taskTitle: this.data.taskTitle,
							taskDesc: this.data.taskDesc,
							taskId: this.data.taskId,
							labelId: this.data.selectLabelId,
							taskNoticeTime: this.data.taskNoticeTime
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
					} else if (res[tmplId] == 'reject') {
						// 获取列表数据
						// this.getList()
					}
				},
				fail: (errCode, errMessage) => {
					console.log(errCode)
					console.log(errMessage)

					wx.showToast({ 
						title: '系统异常',
						icon: 'none',
						duration: 1500
					})
					// 获取列表数据
					// this.getList()

				}
			})
		}else{
			api.updateTask({
				taskTitle: this.data.taskTitle,
				taskDesc: this.data.taskDesc,
				taskId: this.data.taskId,
				labelId: this.data.selectLabelId,
				taskNoticeTime: this.data.taskNoticeTime
	
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
		}
		
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
					html: thus.data.taskDesc
				})
			}).exec()
	},
	/**
	 * 查看任务描述/编辑
	 */
	seeTaskDesc(e) {
		this.setData({
			taskDesc: e.target.dataset.desc,
			taskTitle: e.target.dataset.title,
			taskId: e.target.dataset.id,
			curSts: e.target.dataset.type,
			selectLabelId: e.target.dataset.labelid,
			taskNoticeTime: e.target.dataset.tasknoticetime,

			isEditor: true
		})
		console.log(e.target.dataset)
		this.openTask()
		this.onEditorReady()
	},
	/**
	 * 任务完成
	 */
	completeTask(e) {
		const thus = this
		api.completeTask({
			taskId: e.target.dataset.id
		}).then(res => {
			if (res.success == true) {
				wx.showToast({
					title: res.msg,
					icon: 'success',
					duration: 1500
				})
				// 获取列表数据
				thus.getTaskList()
			}
		})
	},
	/**
	 * 获取列表数据 使用的频率太大了直接提出来
	 */
	getTaskList() {
		api.taskList({
			taskType: 0
		}).then(res => {
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
	/**
	 * 任务转移
	 */
	transfer(e) {
		if (e.target.dataset.type == 1) {
			return wx.showToast({
				title: '已完成任务不支持转移',
				icon: 'none',
				duration: 1500
			})
		}
		let taskId = e.target.dataset.id
		if (taskId == '') {
			return wx.showToast({
				title: '系统异常',
				icon: 'none',
				duration: 1500
			})
		}
		let thus = this
		api.transfer({
			taskId: taskId
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
	 * 改变选中的标签
	 */
	changeLabel(e) {
		this.setData({
			selectLabelId: e.currentTarget.dataset.labelid
		})
	},
	//修改时间数据
	changeDateTime(e) {
		var arr = this.data.dateTime,
			dateArr = this.data.dateTimeArray;
		arr[e.detail.column] = e.detail.value;
		dateArr[2] = dateTimePicker.getMonthDay(dateArr[0][arr[0]], dateArr[1][arr[1]]);
		var year = arr[0] + 1978;
		var month = arr[1] + 1;
		if (month < 10) {
			month = "0" + month
		}
		var day = arr[2] + 1;
		if (day < 10) {
			day = "0" + day
		}
		var hour = arr[3];
		if (hour < 10) {
			hour = "0" + hour
		}
		var min = arr[4];
		if (min < 10) {
			min = "0" + min
		}
		this.setData({
			dateTime: e.detail.value,
			taskNoticeTime: year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + "00"
		});
		// console.log(this.data.taskNoticeTime)
	},
	//纵列修改时间 
	changeDateTimeColumn(e) {
		var arr = this.data.dateTime,
			dateArr = this.data.dateTimeArray;

		arr[e.detail.column] = e.detail.value;
		dateArr[2] = dateTimePicker.getMonthDay(dateArr[0][arr[0]], dateArr[1][arr[1]]);
		this.setData({
			dateTimeArray: dateArr,
			dateTime: arr,
		});
	},
	//取消时间选择
	dateChangCancel() {
		var obj = dateTimePicker.dateTimePicker(this.data.startYear, this.data.endYear);
		// 精确到分的处理，将数组的秒去掉
		var lastArray = obj.dateTimeArray.pop();
		var lastTime = obj.dateTime.pop();
		this.openTask()
		this.setData({
			taskNoticeTime: ''
		})
	}
})