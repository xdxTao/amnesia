package com.xdx.timingtask;

import com.xdx.common.utils.DateUtils;
import com.xdx.common.utils.wx.WxMsgUtils;
import com.xdx.entitys.pojo.SyTask;
import com.xdx.entitys.pojo.SyUser;
import com.xdx.mapper.task.SyTaskMapper;
import com.xdx.mapper.user.SyUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代办消息提醒
 *
 * @author 小道仙
 * @date 2021年4月19日
 */
@Configuration
@Async
@Slf4j
public class TodoNoticeTask {

    @Value("${msg-tmpl.todo}")
    private String msgTmplTodo;

    @Autowired
    private SyTaskMapper taskMapper;

    @Autowired
    private SyUserMapper userMapper;

    /**
     * 每30秒查询一次看看是否有需要发送代办消息的
     */
//    @Scheduled(fixedRate = 1000 * 30)
    public void remove(){
        List<Map<String,String>> syTasks = taskMapper.selectTodo();
        if (syTasks != null && !syTasks.isEmpty()){
            SyTask tmpUpdate = new SyTask();
            String tmpTaskId = "";
            for (Map item : syTasks){
                try {
                    tmpTaskId = ""+ item.get("taskId");
                    Map<String,Map<String,String>> data = new HashMap<>();
                    Map<String,String> thing1 = new HashMap<>();
                    thing1.put("value", item.get("taskTitle")+"");
                    data.put("thing1",thing1);
                    Map<String,String> time2 = new HashMap<>();
                    time2.put("value", DateUtils.parseDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
                    data.put("time2",time2);
                    Map<String,String> thing4 = new HashMap<>();
                    thing4.put("value", "点击前往办理");
                    data.put("thing4",thing4);
                    // 发送消息
                    String errCode = WxMsgUtils.sendTmplMsg(item.get("wxOpenId")+"", msgTmplTodo, "", "", data);

                    // 更新消息发送状态为已发送
                    tmpUpdate.setTaskId(Integer.parseInt(tmpTaskId));
                    tmpUpdate.setTaskNoticeStatus(1);
                    // 用户取消提醒
                    if ("43101".equals(errCode)){
                        SyUser user = userMapper.selectById(item.get("userId")+"");
                        user.setMsgNotice(user.getMsgNotice().replace("1,",""));
                        userMapper.updateById(user);
                    }
                }catch (Exception e){
                    // 2 为发送失败
                    tmpUpdate.setTaskNoticeStatus(2);
                    log.error("发送失败taskId:"+tmpTaskId, e);
                }finally {
                    // 更新消息发送状态
                    taskMapper.updateById(tmpUpdate);
                }
            }
        }
    }
}
