// 同组件，页面要用的话也需要引入这两个
import { createStoreBindings } from 'mobx-miniprogram-bindings'
import { store } from '../../store'
import  api  from '../../utils/api.js'
Page({
	data: {
		isempty:true,
		list:[],
		labelList:[],
		selectLabelId:'',
		// 任务数据
		taskId: '',
		taskTitle: '',
		taskDesc: '',
		curDayList:['星期天','星期一','星期二','星期三','星期四','星期五','星期六'],
		curDay: new Date().getDay(),
		// 是否是编辑
		isEditor: false,
		// 当前任务状态，用来判断是否可以更新
		curSts: 0
	},
	/**
	 * 页面每次进入加载
	 */
	onShow:function(){		
		const thus = this
		// 判断用户是否已经注册
		wx.login({
			success (res) {
				if (res.code) {
					api.login({ code: res.code }).then(res => {						
						store.token = res.data					
						// 判断用户是否授权
						wx.getSetting({
							success(res) {
								let auto = res.authSetting["scope.userInfo"] == undefined ? false : true
								console.log(auto);
								// 同步授权
								api.synAuthorize({authorize: auto}).then(res=>{
									// 获取列表数据
									thus.getTaskList()
								})
							}
						})
						
					})
				} else {
					console.log('登录失败！' + res.errMsg)
				}
			}
		})
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
				'isAuthorize',
				'token'
			],
			actions: ['update'], // 同上，这里是方法名字
		})
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
	openTask: function(){
		this.getListLabel()
		var animation = wx.createAnimation({
			duration: 1000,
			timingFunction: 'ease',
			delay: 100
		});
		animation.opacity(1).translateY(-470).step()
		this.setData({
			ani:  animation.export()
		})
	},
	/**
	 * 关闭任务添加窗口
	 */
	closeTask(){
		var animation = wx.createAnimation({
			duration: 1000,
			timingFunction: 'ease',
			delay: 100
		});
		animation.opacity(1).translateY(415).step()
		this.setData({
			ani:  animation.export()
		})
		// 清空数据
		this.setData({
			taskDesc: '',
			taskTitle: '',
			isEditor: false,
			selectLabelId: 'taskDesc'
		})
		this.onEditorReady()
	},
	/**
	 * 获取input输入的数据
	 */
	getTaskInput(e){
		this.setData({
			taskTitle : e.detail.value
		})
	},
	/**
	 * 获取editor输入的数据
	*/
	getTaskEditor(e){
		this.setData({
			taskDesc : e.detail.html
		})
	},
	/**
	 * 新增任务
	 */
	addTask(){
		if(this.data.taskTitle == ''){
			return wx.showToast({title: '请输入任务标题',icon: 'none',duration: 1500})
		}
		let thus = this
		api.addTask({
			taskTitle : this.data.taskTitle,
			taskDesc : this.data.taskDesc,
			labelId : this.data.selectLabelId}).then(res => {
			if(res.success == true){
				// 获取列表数据
				thus.getTaskList()
				wx.showToast({title: res.msg})
				thus.closeTask()
			}
		})
	},
	/**
	 * 更新任务
	 */
	updateTask(){	
		if(this.data.curSts == 1){
			return wx.showToast({title: '已完成任务不支持更新',icon: 'none',duration: 1500})
		}	
		if(this.data.taskId == ''){
			return wx.showToast({title: '系统异常',icon: 'none',duration: 1500})
		}
		let thus = this
		api.updateTask({
			taskTitle : this.data.taskTitle,
			taskDesc : this.data.taskDesc,
			taskId : this.data.taskId,
			labelId : this.data.selectLabelId
		}).then(res => {			
			if(res.success == true){
				// 获取列表数据
				thus.getTaskList()
				wx.showToast({title: res.msg})
				thus.closeTask()
			}
		})
	},

	/**
	 * 编辑器数据回显
	 */
	onEditorReady(){
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
	seeTaskDesc(e){
		this.setData({
			taskDesc: e.target.dataset.desc,
			taskTitle: e.target.dataset.title,
			taskId: e.target.dataset.id,
			curSts: e.target.dataset.type,
			selectLabelId: e.target.dataset.labelid,
			isEditor: true
		})
		this.openTask()
		this.onEditorReady()
	},
	/**
	 * 任务完成
	 */
	completeTask(e){
		const thus = this
		api.completeTask({taskId: e.target.dataset.id}).then(res => {
			if(res.success == true){
				wx.showToast({title: res.msg,icon: 'success',duration: 1500})
				// 获取列表数据
				thus.getTaskList()
			}
		})
	},
	/**
	 * 获取列表数据 使用的频率太大了直接提出来
	 */
	getTaskList(){
		api.taskList({taskType: 0}).then(res => {
			if(res.data != null){
				if(res.data.length > 0){
					this.setData({
						isempty: false,
					})
				}
				this.setData({
					list: res.data
				})
			}else if(res.success == false){
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
	transfer(e){
		if(e.target.dataset.type == 1){
			return wx.showToast({title: '已完成任务不支持转移',icon: 'none',duration: 1500})
		}	
		let taskId = e.target.dataset.id
		if(taskId == ''){
			return wx.showToast({title: '系统异常',icon: 'none',duration: 1500})
		}
		let thus = this
		api.transfer({
			taskId : taskId
		}).then(res => {			
			if(res.success == true){
				// 获取列表数据
				thus.getTaskList()
				wx.showToast({title: res.msg})
			}
		})
	},
	/**
	 * 标签列表
	 */
	getListLabel(){
		api.labelListAll({}).then(res => {
            if(res.success == true){
                this.setData({
					labelList: res.data
				})
            }
		})
	},
	/**
	 * 改变选中的标签
	 */
	changeLabel(e){
		this.setData({
			selectLabelId: e.currentTarget.dataset.labelid
		})
	}
})