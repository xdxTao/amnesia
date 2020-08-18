// 同组件，页面要用的话也需要引入这两个
import  api  from '../../utils/api.js'
// pages/label/label.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        list:[],
		label: '',
		labelId:'',
        // 是否是编辑
		isEditor: false,
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
	openTask: function(e){
		let labelname = e.currentTarget.dataset.labelname
		let labelId = e.currentTarget.dataset.labelid
		var animation = wx.createAnimation({
			duration: 1000,
			timingFunction: 'ease',
			delay: 100
		});
		animation.opacity(1).translateY(-500).step()
		this.setData({
			ani:  animation.export()
		})
		if(labelname != ''){
			this.setData({
				label: labelname,
				labelId:labelId,
				isEditor : true
			})
		}

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
			label: '',
			isEditor:false
		})
    },
    	/**
	 * 新增任务
	 */
	addLabel(){       
		if(this.data.label == ''){
			return wx.showToast({title: '请输入标签',icon: 'none',duration: 1500})
		}
		let thus = this
		api.addLabel({labelName : this.data.label}).then(res => {			
			if(res.success == true){
				// 获取列表数据
				thus.getList()
				wx.showToast({title: res.msg})
				thus.closeTask()
			}
		})
    },
    getList(){
        api.labelList({}).then(res => {
            if(res.success == true){
                this.setData({
					list: res.data
				})
            }
		})
    },
    /**
	 * 获取input输入的数据
	*/
	getLabelInput(e){
		this.setData({
			label : e.detail.value
		})
	},
	/**
	 * 改变默认标签
	 */
	changeDefault(e){
		let labelid = e.currentTarget.dataset.labelid
		let userid = e.currentTarget.dataset.userid
		let status = e.detail.value
		let thus = this
		api.updateDefault({
			"labelId":labelid,
			"userId":userid,
			"status":status
		}).then(res => {
            thus.getList()
		})
	},
	/**
	 * 是否显示
	 */
	changeShow(e){
		let labelid = e.currentTarget.dataset.labelid
		let labelStatus = e.detail.value == true ? 1 : 0
		let thus = this
		api.updateLabel({
			"labelId":labelid,
			"labelStatus":labelStatus
		}).then(res => {
            thus.getList()
		})
	},
	/**
	 * 修改排序
	 */
	changeSort(e){
		let labelid = e.currentTarget.dataset.labelid
		let labelSort = e.detail.value
		let thus = this
		api.updateLabel({
			"labelId":labelid,
			"labelSort":labelSort
		}).then(res => {
            thus.getList()
		})
	},
	/**
	 * 更新标签名
	 */
	updateName(){
		if(this.data.label == ''){
			return wx.showToast({title: '请输入标签',icon: 'none',duration: 1500})
		}
		if(this.data.labelId == ''){
			return wx.showToast({title: '系统异常',icon: 'none',duration: 1500})
		}
		console.log(this.data.labelId);
		
		let thus = this
		api.updateLabel({labelName : this.data.label,labelId : this.data.labelId}).then(res => {			
			if(res.success == true){
				// 获取列表数据
				thus.getList()
				wx.showToast({title: res.msg})
				thus.closeTask()
			}
		})

	}

})
