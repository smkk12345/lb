package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class RankAcceptAward {
    /**
     * 
     */
    private Integer id;

    /**
     * 榜单id
     */
    private Long rankid;

    /**
     * 榜单标题
     */
    private String ranktitle; //榜单标题

    /**
     * 用户id
     */
    private Long userid;

    /**
     * 用户手机号
     */
    private String username;

    /**
     * 显示用
     */
    private AppUserMongoEntity appUserMongoEntity;

    /**
     * 获奖码
     */
    private String receivecode;

    /**
     * 领奖日期
     */
    private Date acceptdate;

    /**
     * 确认领奖时间
     */
    private Date confirmacceptdate;

    /**
     * 奖品等级
     */
    private Integer awardlevel;

    /**
     * 排名信息
     */
    private Integer sortnum;

    /**
     * 搜索用
     */
    private String awardtitle;

    /**
     * 奖品id
     */
    private Integer awardid;

    /**
     * 奖品类别
     */
    private String awardcateid;

    /**
     * 奖项名称
     */
    private String awardnickname;

    /**
     * 状态 0 - 未领取 1 - 已领取 2 - 已经发货 待确认（实物 物流） 3 - 已完成 4 - 已经失效
     */
    private Integer status;

    /**
     * 收货人
     */
    private String reciverusername;

    /**
     * 收货人手机号
     */
    private String reciverusertel;

    /**
     * 收货人地址
     */
    private String reciveruseraddr;

    /**
     * 发奖方式 0 - 非物流 1 - 物流
     */
    private String publishawardtype;

    /**
     * 物流公司
     */
    private String logisticscompany;

    /**
     * 物流单号
     */
    private String logisticsnum;

    /**
     * 发货日期
     */
    private Date dispatchdate;

    /**
     * 备注
     */
    private String remarker;

    /**
     * 处理人id
     */
    private Integer handleuserid;


    private Date screatedate;

    /**
     * 创建日期
     */
    private Date createdate;

    private Date ecreatedate;

    private List<Award> awards; //查询用

    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
    }

    public String getPublishawardtype() {
        return publishawardtype;
    }

    public void setPublishawardtype(String publishawardtype) {
        this.publishawardtype = publishawardtype;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public String getAwardtitle() {
        return awardtitle;
    }

    public void setAwardtitle(String awardtitle) {
        this.awardtitle = awardtitle;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getScreatedate() {
        return screatedate;
    }

    public void setScreatedate(Date screatedate) {
        this.screatedate = screatedate;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getEcreatedate() {
        return ecreatedate;
    }

    public void setEcreatedate(Date ecreatedate) {
        this.ecreatedate = ecreatedate;
    }

    public String getAwardnickname() {
        return awardnickname;
    }

    public void setAwardnickname(String awardnickname) {
        this.awardnickname = awardnickname;
    }

    public String getRanktitle() {
        return ranktitle;
    }

    public void setRanktitle(String ranktitle) {
        this.ranktitle = ranktitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAwardcateid() {
        return awardcateid;
    }

    public void setAwardcateid(String awardcateid) {
        this.awardcateid = awardcateid;
    }

    public String getReceivecode() {
        return receivecode;
    }

    public void setReceivecode(String receivecode) {
        this.receivecode = receivecode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRankid() {
        return rankid;
    }

    public void setRankid(Long rankid) {
        this.rankid = rankid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getAcceptdate() {
        return acceptdate;
    }

    public void setAcceptdate(Date acceptdate) {
        this.acceptdate = acceptdate;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getConfirmacceptdate() {
        return confirmacceptdate;
    }

    public void setConfirmacceptdate(Date confirmacceptdate) {
        this.confirmacceptdate = confirmacceptdate;
    }

    public Integer getAwardlevel() {
        return awardlevel;
    }

    public void setAwardlevel(Integer awardlevel) {
        this.awardlevel = awardlevel;
    }

    public Integer getAwardid() {
        return awardid;
    }

    public void setAwardid(Integer awardid) {
        this.awardid = awardid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReciverusername() {
        return reciverusername;
    }

    public void setReciverusername(String reciverusername) {
        this.reciverusername = reciverusername == null ? null : reciverusername.trim();
    }

    public String getReciverusertel() {
        return reciverusertel;
    }

    public void setReciverusertel(String reciverusertel) {
        this.reciverusertel = reciverusertel == null ? null : reciverusertel.trim();
    }

    public String getReciveruseraddr() {
        return reciveruseraddr;
    }

    public void setReciveruseraddr(String reciveruseraddr) {
        this.reciveruseraddr = reciveruseraddr == null ? null : reciveruseraddr.trim();
    }

    public String getLogisticscompany() {
        return logisticscompany;
    }

    public void setLogisticscompany(String logisticscompany) {
        this.logisticscompany = logisticscompany == null ? null : logisticscompany.trim();
    }

    public String getLogisticsnum() {
        return logisticsnum;
    }

    public void setLogisticsnum(String logisticsnum) {
        this.logisticsnum = logisticsnum == null ? null : logisticsnum.trim();
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getDispatchdate() {
        return dispatchdate;
    }

    public void setDispatchdate(Date dispatchdate) {
        this.dispatchdate = dispatchdate;
    }

    public String getRemarker() {
        return remarker;
    }

    public void setRemarker(String remarker) {
        this.remarker = remarker == null ? null : remarker.trim();
    }

    public Integer getHandleuserid() {
        return handleuserid;
    }

    public void setHandleuserid(Integer handleuserid) {
        this.handleuserid = handleuserid;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}