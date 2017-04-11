package com.longbei.appservice.service;

/**
 * Created by wangyongzhi 17/4/9.
 */
public interface JPushService extends BaseService {

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
    boolean pushMessage(String status,String userId,String title,String content,String msgid,String tag);
}
