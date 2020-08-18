// 同组件，页面要用的话也需要引入这两个
import { createStoreBindings } from 'mobx-miniprogram-bindings'
import { store } from '../../store'
import  api  from '../../utils/api.js'
Page({
	data: {
		isempty:true,
		list:[],
		// 任务数据
		labelList:[],
		selectLabelId:'',
		taskId: '',
		taskTitle: '',
		taskDesc: '',
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
								// 同步授权
								api.synAuthorize({authorize: res.authSetting["scope.userInfo"]}).then(res=>{
									thus.getDefaultLabel();
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
		var animation = wx.createAnimation({
			duration: 1000,
			timingFunction: 'ease',
			delay: 100
		});
		animation.opacity(1).translateY(-415).step()
		this.setData({
			ani:  animation.export(),
			isEditor: true
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
			isEditor: false
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
		api.addTask({taskTitle : this.data.taskTitle,taskDesc : this.data.taskDesc}).then(res => {			
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
	 * 查看任务描述
	 */
	seeTaskDesc(e){
		this.openTask()
		this.onEditorReady()	
		this.setData({
			taskDesc: e.target.dataset.desc,
			taskTitle: e.target.dataset.title,
			taskId: e.target.dataset.id,
			curSts: e.target.dataset.type,
			isEditor: false
		})
	},
	/**
	 * 完成任务
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
	 * 更新任务
	 */
	updateTask(){
		if(this.data.curSts == 1){
			return wx.showToast({title: '已完成任务不支持更新',icon: 'none',duration: 1500})
		}	
		console.log(this.data.curSts);
		
		if(this.data.taskId == ''){
			return wx.showToast({title: '系统异常',icon: 'none',duration: 1500})
		}
		let thus = this
		api.updateTask({
			taskTitle : this.data.taskTitle,
			taskDesc : this.data.taskDesc,
			taskId : this.data.taskId
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
	 * 获取列表数据 使用的频率太大了直接提出来
	 */
	getTaskList(){
		let labelId = this.data.selectLabelId
		api.taskList({taskType: 1,labelId: labelId}).then(res => {
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
		api.labelListAll({flag: 1}).then(res => {
            if(res.success == true){
                this.setData({
					labelList: res.data
				})
            }
		})
	},
	/**
	 * 获取默认标签
	 */
	getDefaultLabel(){
		api.getDefaultLabel({}).then(res => {
            if(res.success == true){
                this.setData({
					selectLabelId: res.data
				})
				// 获取列表数据
				this.getTaskList()
				// 获取标签数据
				this.getListLabel()
            }
		})
	},
	/**
	 * 切换标签
	 */
	changeLabel(e){
		let labelId = e.currentTarget.dataset.labelid
		this.setData({
			selectLabelId: labelId
		})
		this.getTaskList()
	}
})