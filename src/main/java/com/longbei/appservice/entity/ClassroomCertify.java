package com.longbei.appservice.entity;

public class ClassroomCertify {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 申请开教室说明
     */
    private String content;

    /**
     * 相关资料
     */
    private String data;

    /**
     * 申请人用户id
     */
    private Long userid;

    /**
     * 审核操作人用户id
     */
    private String operateuid;

    /**
     * 处理说明
     */
    private String remark;

    /**
     * 审核结果。0 未审核 1 审核通过。2 审核不通过 3 撤销认证
     */
    private String status;

    /**
     * 提交审核时间
     */
    private String createtime;

    /**
     * 审核时间
     */
    private String checktime;

    /**
     * 更新时间
     */
    private String updatetime;


    //----------------拓展字段-------------

    private AppUserMongoEntity user; //用户信息

    private UserIdcard userIdcard; //身份认证

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getOperateuid() {
        return operateuid;
    }

    public void setOperateuid(String operateuid) {
        this.operateuid = operateuid == null ? null : operateuid.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getChecktime() {
        return checktime;
    }

    public void setChecktime(String checktime) {
        this.checktime = checktime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public AppUserMongoEntity getUser() {
        return user;
    }

    public void setUser(AppUserMongoEntity user) {
        this.user = user;
    }

    public UserIdcard getUserIdcard() {
        return userIdcard;
    }

    public void setUserIdcard(UserIdcard userIdcard) {
        this.userIdcard = userIdcard;
    }

}