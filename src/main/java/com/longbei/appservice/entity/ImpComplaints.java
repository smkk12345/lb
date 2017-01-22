package com.longbei.appservice.entity;

import java.util.Date;

public class ImpComplaints extends ImpComplaintsKey {
    private Long userid;//用户id

    private Long impid;//进步组id

    private String content;//反馈内容

    private Date createtime;//反馈时间

    private String status;//0--未处理 1---无用 2---有用

    private Date dealtime;//处理时间

    private Long dealuser;//处理人

    private String checkoption;

    private String contenttype;

    private Long rankid;//榜单id

    private String gtype;//0 零散 1 目标中 2 榜中 3 圈中 4教室中

    /**
     * 用户id
     * @return userid 用户id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 进步组id
     * @return impid 进步组id
     */
    public Long getImpid() {
        return impid;
    }

    /**
     * 进步组id
     * @param impid 进步组id
     */
    public void setImpid(Long impid) {
        this.impid = impid;
    }

    /**
     * 反馈内容
     * @return content 反馈内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 反馈内容
     * @param content 反馈内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 反馈时间
     * @return createtime 反馈时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 反馈时间
     * @param createtime 反馈时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 0--未处理 1---无用 2---有用
     * @return status 0--未处理 1---无用 2---有用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 0--未处理 1---无用 2---有用
     * @param status 0--未处理 1---无用 2---有用
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 处理时间
     * @return dealtime 处理时间
     */
    public Date getDealtime() {
        return dealtime;
    }

    /**
     * 处理时间
     * @param dealtime 处理时间
     */
    public void setDealtime(Date dealtime) {
        this.dealtime = dealtime;
    }

    /**
     * 处理人
     * @return dealuser 处理人
     */
    public Long getDealuser() {
        return dealuser;
    }

    /**
     * 处理人
     * @param dealuser 处理人
     */
    public void setDealuser(Long dealuser) {
        this.dealuser = dealuser;
    }

    /**
     * 
     * @return checkoption 
     */
    public String getCheckoption() {
        return checkoption;
    }

    /**
     * 
     * @param checkoption 
     */
    public void setCheckoption(String checkoption) {
        this.checkoption = checkoption == null ? null : checkoption.trim();
    }

    /**
     * 
     * @return contenttype 
     */
    public String getContenttype() {
        return contenttype;
    }

    /**
     * 
     * @param contenttype 
     */
    public void setContenttype(String contenttype) {
        this.contenttype = contenttype == null ? null : contenttype.trim();
    }

    /**
     * 榜单id
     * @return rankid 榜单id
     */
    public Long getRankid() {
        return rankid;
    }

    /**
     * 榜单id
     * @param rankid 榜单id
     */
    public void setRankid(Long rankid) {
        this.rankid = rankid;
    }

    /**
     * 0 零散 1 目标中 2 榜中 3 圈中 4教室中
     * @return gtype 0 零散 1 目标中 2 榜中 3 圈中 4教室中
     */
    public String getGtype() {
        return gtype;
    }

    /**
     * 0 零散 1 目标中 2 榜中 3 圈中 4教室中
     * @param gtype 0 零散 1 目标中 2 榜中 3 圈中 4教室中
     */
    public void setGtype(String gtype) {
        this.gtype = gtype == null ? null : gtype.trim();
    }
}