/**
 * 日期相关的工具类
 * 
 * 前面加了 export 表示把这个接口暴露出去
 * 
 * @author 小道仙
 * @date 2019年11月27日
 */ 

let now = new Date(); //当前日期   
let nowDayOfWeek = now.getDay(); //今天本周的第几天    
let nowDay = now.getDate(); //当前日    
let nowMonth = now.getMonth(); //当前月    
let nowYear = now.getYear(); //当前年    
nowYear += (nowYear < 2000) ? 1900 : 0; //

//格局化日期：yyyy-MM-dd    
function formatDate(date) {       
    let myyear = date.getFullYear();        
    let mymonth = date.getMonth()+1;        
    let myweekday = date.getDate();         
    if(mymonth < 10){            
        mymonth = "0" + mymonth;        
    }        
    if(myweekday < 10){            
        myweekday = "0" + myweekday;        
    }        
    return (myyear+"-"+mymonth + "-" + myweekday);   
 }
//获得某月的天数    
function getMonthDays(myMonth){        
    let monthStartDate = new Date(nowYear, myMonth, 1);        
    let monthEndDate = new Date(nowYear, myMonth + 1, 1);        
    let days = (monthEndDate - monthStartDate)/(1000 * 60 * 60 * 24);        
    return days;    
}

/**
 * 获取当前周起止时间
 * 
 * eg: {weekStartDate: "2019-11-25", weekEndtDate: "2019-12-01"}
 * @return result 
 */
export function getCurWeekStartAndEnd() {
    // nowDayOfWeek 等于 0 的时候，表示周日
    let curnowDayOfWeek = nowDayOfWeek == 0 ? 7 : nowDayOfWeek
    let weekStartDate = new Date(nowYear, nowMonth, nowDay - curnowDayOfWeek + 1)
    let weekEndtDate = new Date(nowYear, nowMonth, nowDay - curnowDayOfWeek + 7)
    let result = {
        weekStartDate : formatDate(weekStartDate),
        weekEndtDate : formatDate(weekEndtDate)
    }
    return result
}

/**
 * 获取当前月起止时间
 * 
 * eg: {monthStartDate: "2019-11-01", monthEndtDate: "2019-12-30"}
 * @return result 
 */
export function getCurMonthStartAndEnd() {
    let monthStartDate = new Date(nowYear, nowMonth, 1)
    let monthEndtDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth))
    let result = {
        monthStartDate : formatDate(monthStartDate),
        monthEndtDate : formatDate(monthEndtDate)
    }
    return result
}

/**
 * 获取当前年的起止日期
 * 
 * eg
 */
export function getCurYearStartAndEnd() {
    let yearStartDate = new Date(nowYear, 0, 1)
    let yearEndtDate = new Date(nowYear, 11, 31)
    let result = {
        yearStartDate : formatDate(yearStartDate),
        yearEndtDate : formatDate(yearEndtDate)
    }
    return result
}

export const nowdDA = new Date()