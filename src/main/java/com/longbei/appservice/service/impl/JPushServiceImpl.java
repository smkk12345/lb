package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.service.JPushService;
import com.longbei.appservice.service.api.outernetservice.IJPushService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by wangyongzhi 17/4/9.
 */
@Service("jPushService")
public class JPushServiceImpl extends BaseServiceImpl implements JPushService {

    @Autowired
    private IJPushService iJPushService;
    /**
     * 发送push消息
     * @param status 消息标识
     * @param userId 接受消息的用户id
     * @param title 消息的标题
     * @param content 消息内容
     * @param msgid 消息的id
     * @param tag 消息的唯一标识码 在Constant中定义
     * @return
     */
    @Override
    public boolean pushMessage(String status, String userId,String title, String content, String msgid, String tag) {
        JSONObject pushMessage = new JSONObject();
        pushMessage.put("status",status);
        pushMessage.put("userid",userId);
        pushMessage.put("content",content);
        pushMessage.put("msgid",msgid);
        pushMessage.put("tag", tag);
        BaseResp<Object> baseResp = iJPushService.messagePush(userId,title,content,pushMessage.toString());
        if(baseResp.getCode() == 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean pushMessageToAll(String status,
                                    String title,
                                    String content,
                                    String msgid,
                                    String tag,
                                    Map<String,Object> exeMap) {
        JSONObject pushMessage = new JSONObject();
        pushMessage.put("status",status);
        pushMessage.put("content",content);
        pushMessage.put("msgid",msgid);
        pushMessage.put("tag", tag);
        pushMessage.putAll(exeMap);
        BaseResp<Object> baseResp = iJPushService.messagePushAll(title,content,pushMessage.toString());
        if(baseResp.getCode() == 0){
            return true;
        }
        return false;
    }
}
