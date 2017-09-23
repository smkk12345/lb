package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class UserInComeDetail implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Long userid;

    /**
     * 明细类型  0 教室学费，1 直播送礼物，2 提现 3 提现失败 4提现成功，5 退学费，6转入钱包，7 教室送礼
     */
    private String itype;

    /**
     * 龙币数量
     */
    private Integer num;

    /**
     * 业务类型。0 - 教室收入 1 - 提现
     */
    private String businesstype;

    /**
     * 业务相关联id。businesstype = 0 时 教室id businesstype=1 时 结算单id
     */
    private Long businesstid;

    /**
     * 业务title 如果为教室 则为教室title
     */
    private String btitle;

    /**
     * 来源用户id
     */
    private Long originuserid;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Date updatetime;

    /**
     * 备注
     */
    private String remarker;

    /**
     * 明细id
     */
    private Long detailid;

    /**
     * 明细类型 0 - 收入 1 - 支出
     */
    private String detailtype;

    /**
     * 教室类型  0:运营  1:app  2:商户
     */
    private String csourcetype;

    /**
     * 明细状态 0 - 结算中 1 - 成功 2 - 失败
     */
    private String detailstatus;

    /**
     * 显示使用
     */
    private Classroom classroom;

    /**
     * 显示用
     */
    private UserInComeOrder userInComeOrder;

    /**
     * 显示使用
     */
    private AppUserMongoEntity appUserMongoEntity;

    public UserInComeOrder getUserInComeOrder() {
        return userInComeOrder;
    }

    public void setUserInComeOrder(UserInComeOrder userInComeOrder) {
        this.userInComeOrder = userInComeOrder;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
    }

    public String getDetailstatus() {
        return detailstatus;
    }

    public void setDetailstatus(String detailstatus) {
        this.detailstatus = detailstatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getItype() {
        return itype;
    }

    public void setItype(String itype) {
        this.itype = itype == null ? null : itype.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype == null ? null : businesstype.trim();
    }

    public Long getBusinesstid() {
        return businesstid;
    }

    public void setBusinesstid(Long businesstid) {
        this.businesstid = businesstid;
    }

    public String getBtitle() {
        return btitle;
    }

    public void setBtitle(String btitle) {
        this.btitle = btitle == null ? null : btitle.trim();
    }

    public Long getOriginuserid() {
        return originuserid;
    }

    public void setOriginuserid(Long originuserid) {
        this.originuserid = originuserid;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getRemarker() {
        return remarker;
    }

    public void setRemarker(String remarker) {
        this.remarker = remarker == null ? null : remarker.trim();
    }

    public Long getDetailid() {
        return detailid;
    }

    public void setDetailid(Long detailid) {
        this.detailid = detailid;
    }

    public String getDetailtype() {
        return detailtype;
    }

    public void setDetailtype(String detailtype) {
        this.detailtype = detailtype == null ? null : detailtype.trim();
    }

    public String getCsourcetype() {
        return csourcetype;
    }

    public void setCsourcetype(String csourcetype) {
        this.csourcetype = csourcetype == null ? null : csourcetype.trim();
    }
}