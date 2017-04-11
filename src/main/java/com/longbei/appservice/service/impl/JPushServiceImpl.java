package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.service.JPushService;
import com.longbei.appservice.service.api.HttpClient;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Created by wangyongzhi 17/4/9.
 */
@Service("jPushService")
public class JPushServiceImpl extends BaseServiceImpl implements JPushService {

    /**
     * 发送push消息
     * @param status 消息表示
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
        pushMessage.put("tag", Constant.JPUSH_TAG_COUNT_1001);
        BaseResp<Object> baseResp = HttpClient.jPushService.messagePush(userId,title,content,pushMessage.toString());
        if(baseResp.getCode() == 0){
            return true;
        }
        return false;
    }
}
