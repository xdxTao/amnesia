//封装request
let apiRequest = (url, method, data, header) => { //接收所需要的参数，如果不够还可以自己自定义参数
    let promise = new Promise(function (resolve, reject) {
        wx.showNavigationBarLoading() //在标题栏中显示加载
        wx.request({
            url: url,
            data: data ? data : null,
            method: method,
            header: header ? header : { 'content-type': 'application/x-www-form-urlencoded' },
            complete: function () {
                wx.hideNavigationBarLoading(); //完成停止加载
                wx.stopPullDownRefresh(); //停止下拉刷新
            },
            success: function (res) {
                if(res.data.success == false){
                    wx.showToast({title: res.data.errDesc,icon: 'none',duration: 2000})
                }
                //接口调用成功
                resolve(res.data); //根据业务需要resolve接口返回的json的数据
            },
            fail: function (res) {
                wx.showModal({
                    showCancel: false,
                    confirmColor: '#1d8f59',
                    content: '数据加载失败，请检查您的网络，点击确定重新加载数据!',
                    success: function (res) {
                        if (res.confirm) {
                            apiRequest(url, method, data, header);
                        }
                    }
                });
                wx.hideLoading();
            }
        })
    });
    return promise; //注意，这里返回的是promise对象
}
export default apiRequest;