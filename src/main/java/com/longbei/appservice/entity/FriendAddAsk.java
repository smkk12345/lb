package com.longbei.appservice.entity;

import net.sf.json.JSONArray;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户请求加好友 实体类
 * Created by wangyongzhi 17/3/3.
 */
public class FriendAddAsk implements Serializable {
    //过多长时间后,用户可以再次申请加好友
    public static final Integer EXPIRETIME = 7 * 24 * 3600;
    public static final Integer MessageContentMaxLength = 100;
    public static final Integer STATUS_PENDING = 0;//请求加为好友 待处理
    public static final Integer STATUS_PASS = 1;//请求加为好友 已同意
    public static final Integer STATUS_FAIL = 2;//请求加为好友 未通过

    public enum Source{
        circle,//兴趣圈
        search,//搜索
        unknown//未知
    }

    private Long id;
    private Long senderUserId;//请求人id
    private Long receiveUserId;//接收人id
    private Date createDate;//请求时间
    private Integer status;//状态 0.待处理 1.同意 2.拒绝
    private Source source;//加好友的来源 0.兴趣圈 2.搜索
    private String lastMessage;//最后一条消息
    private JSONArray message;//回复的消息内容
    private boolean senderIsRead;//请求发送者 是否已读
    private boolean receiveIsRead;//被加为好友的用户 消息是否已读
    private Date updateDate;//更新时间

    private AppUserMongoEntity appUserMongoEntity;//请求人/被请求人的用户详细信息

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取请求人id
     * @return
     */
    public Long getSenderUserId() {
        return senderUserId;
    }

    /**
     * 设置请求人id
     * @param senderUserId
     */
    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    /**
     * 获取被请求人id
     * @return
     */
    public Long getReceiveUserId() {
        return receiveUserId;
    }

    /**
     * 设置被请求人id
     * @param receiveUserId
     */
    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    /**
     * 获取请求的时间
     * @return
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置请求的时间
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取请求的状态
     * @return
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置请求的状态
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取加好友的对话信息
     * @return
     */
    public JSONArray getMessage() {
        return message;
    }

    /**
     * 设置加好友的对话信息
     * @param message
     */
    public void setMessage(JSONArray message) {
        this.message = message;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
    }

    public boolean isSenderIsRead() {
        return senderIsRead;
    }

    public void setSenderIsRead(boolean senderIsRead) {
        this.senderIsRead = senderIsRead;
    }

    public boolean isReceiveIsRead() {
        return receiveIsRead;
    }

    public void setReceiveIsRead(boolean receiveIsRead) {
        this.receiveIsRead = receiveIsRead;
    }

    /**
     * 获取回复中最后一条消息
     * @return
     */
    public String getLastMessage() {
        return lastMessage;
    }

    /**
     * 设置回复中最后一条消息
     * @param lastMessage
     */
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     * 获取更新时间
     * @return
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新时间
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
