package com.longbei.appservice.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RankImage {

    private Integer id;

    private Long rankid;  //榜单id

    private String ranktitle;//榜单标题

    private String rankbrief;//榜单简介

    private String rankrule; //榜单规则

    private String rankdetail;//榜单详情

    private Integer ranklimite;//限制人数

    private Integer rankinvolved;//参与人数

    private String rankscope;//榜单范围

    private String rankphotos;//榜单图片

    private Double rankrate;//榜单中奖率

    private Date sstarttime;

    private Date starttime;//开始时间

    private Date estarttime;

    private Date sendtime;

    private Date endtime;//结束时间

    private Date eendtime;

    private String areaname;//区域名字

    private String coordinate;//坐标

    private String isfinish;//是否结束。0—为结束 1—一结束

    private String createuserid;//榜单创建人id

    private String ranktype;//榜单类型。0—公共榜 1--定制榜

    private String ispublic;//是否公开  0—公开 1—不公开

    private Integer rankcateid;//榜单类别id

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private Integer likescore;//赞对应排名分数

    private Integer flowerscore;//花排名积分

    private Integer diamondscore;//钻石对应排名分数

    private String codeword;//口令


    private String ptype;//十全十美类型

    private String companyname;  //公司名称

    private String companyphotos;  //公司图片

    private String companybrief;  //公司简介

    private String sourcetype;  //来源类型。0 运营端创建   1  b端创建 2 app用户创建。

    private String checkstatus; //审核状态 0 - 为审核  1 - 审核不通过  2 - 审核通过

    private String isauto;  //是否自动发布 0 - 不自动发布 1 - 自动发布 2 - 定时发布

    private String autotime; //定时发布时间

    //-------
    private String joinlastday; //入榜截止天数

    private String maximprovenum; //每人每日最多可更新微进步

    private String minimprovenum; //每人每日最多可更新微进步

    private String ischeck;  //本榜获奖名单开启人工审核 0 - 不开 1 - 开

    private String isrealname; //本榜须实名认证 0 - 不需要 1 - 需要

    private String islonglevel; //本榜须达到龙级  0 - 不需要 1 - 需要

    private String longlevel; //龙级

    private String isperfectlevel; //本榜须达到单项能级 0 - 不需要 1 - 需要

    private String perfectlevel; //能级

    private String perfectid; //能级id

    private String iswork; //本榜须工作认证 0 - 不需要 1 - 需要

    private String isschool; //本榜须学历认证 0 - 不需要 1 - 需要

    private String isgroup; //本榜同时创建龙群 0 - 不需要 1 - 需要

    private String rankcardid; //榜主名片id

    private String isup; // 0 - 未发布  1 - 已发布

    private String isdel; // 0 - 未删除  1 - 已删除

    private String notice; //榜单公告

    private String joincode; //入榜口令

    //--------

    private List<RankAward> rankAwards; //榜单奖品

    private List<RankCheckDetail> rankCheckDetails;//榜单审核列表

    public List<RankCheckDetail> getRankCheckDetails() {
        return rankCheckDetails;
    }

    public void setRankCheckDetails(List<RankCheckDetail> rankCheckDetails) {
        this.rankCheckDetails = rankCheckDetails;
    }

    public String getRankrule() {
        return rankrule;
    }

    public void setRankrule(String rankrule) {
        this.rankrule = rankrule;
    }

    public String getJoincode() {
        return joincode;
    }

    public void setJoincode(String joincode) {
        this.joincode = joincode;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getIsup() {
        return isup;
    }

    public void setIsup(String isup) {
        this.isup = isup;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getSstarttime() {
        return sstarttime;
    }

    public void setSstarttime(Date sstarttime) {
        this.sstarttime = sstarttime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getEstarttime() {
        return estarttime;
    }

    public void setEstarttime(Date estarttime) {
        this.estarttime = estarttime;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getEendtime() {
        return eendtime;
    }

    public void setEendtime(Date eendtime) {
        this.eendtime = eendtime;
    }

    public String getJoinlastday() {
        return joinlastday;
    }

    public void setJoinlastday(String joinlastday) {
        this.joinlastday = joinlastday;
    }

    public String getMaximprovenum() {
        return maximprovenum;
    }

    public void setMaximprovenum(String maximprovenum) {
        this.maximprovenum = maximprovenum;
    }

    public String getMinimprovenum() {
        return minimprovenum;
    }

    public void setMinimprovenum(String minimprovenum) {
        this.minimprovenum = minimprovenum;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getIsrealname() {
        return isrealname;
    }

    public void setIsrealname(String isrealname) {
        this.isrealname = isrealname;
    }

    public String getIslonglevel() {
        return islonglevel;
    }

    public void setIslonglevel(String islonglevel) {
        this.islonglevel = islonglevel;
    }

    public String getLonglevel() {
        return longlevel;
    }

    public void setLonglevel(String longlevel) {
        this.longlevel = longlevel;
    }

    public String getIsperfectlevel() {
        return isperfectlevel;
    }

    public void setIsperfectlevel(String isperfectlevel) {
        this.isperfectlevel = isperfectlevel;
    }

    public String getPerfectlevel() {
        return perfectlevel;
    }

    public void setPerfectlevel(String perfectlevel) {
        this.perfectlevel = perfectlevel;
    }

    public String getPerfectid() {
        return perfectid;
    }

    public void setPerfectid(String perfectid) {
        this.perfectid = perfectid;
    }

    public String getIswork() {
        return iswork;
    }

    public void setIswork(String iswork) {
        this.iswork = iswork;
    }

    public String getIsschool() {
        return isschool;
    }

    public void setIsschool(String isschool) {
        this.isschool = isschool;
    }

    public String getIsgroup() {
        return isgroup;
    }

    public void setIsgroup(String isgroup) {
        this.isgroup = isgroup;
    }

    public String getRankcardid() {
        return rankcardid;
    }

    public void setRankcardid(String rankcardid) {
        this.rankcardid = rankcardid;
    }

    public List<RankAward> getRankAwards() {
        return rankAwards;
    }

    public void setRankAwards(List<RankAward> rankAwards) {
        this.rankAwards = rankAwards;
    }

    public String getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(String checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getIsauto() {
        return isauto;
    }

    public void setIsauto(String isauto) {
        this.isauto = isauto;
    }

    public String getAutotime() {
        return autotime;
    }

    public void setAutotime(String autotime) {
        this.autotime = autotime;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanyphotos() {
        return companyphotos;
    }

    public void setCompanyphotos(String companyphotos) {
        this.companyphotos = companyphotos;
    }

    public String getCompanybrief() {
        return companybrief;
    }

    public void setCompanybrief(String companybrief) {
        this.companybrief = companybrief;
    }


    public Long getRankid() {
        return rankid;
    }

    public void setRankid(Long rankid) {
        this.rankid = rankid;
    }

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 榜单标题
     * @return ranktitle 榜单标题
     */
    public String getRanktitle() {
        return ranktitle;
    }

    /**
     * 榜单标题
     * @param ranktitle 榜单标题
     */
    public void setRanktitle(String ranktitle) {
        this.ranktitle = ranktitle == null ? null : ranktitle.trim();
    }

    /**
     * 榜单简介
     * @return rankbrief 榜单简介
     */
    public String getRankbrief() {
        return rankbrief;
    }

    /**
     * 榜单简介
     * @param rankbrief 榜单简介
     */
    public void setRankbrief(String rankbrief) {
        this.rankbrief = rankbrief == null ? null : rankbrief.trim();
    }

    /**
     * 榜单详情
     * @return rankdetail 榜单详情
     */
    public String getRankdetail() {
        return rankdetail;
    }

    /**
     * 榜单详情
     * @param rankdetail 榜单详情
     */
    public void setRankdetail(String rankdetail) {
        this.rankdetail = rankdetail == null ? null : rankdetail.trim();
    }

    /**
     * 限制人数
     * @return ranklimite 限制人数
     */
    public Integer getRanklimite() {
        return ranklimite;
    }

    /**
     * 限制人数
     * @param ranklimite 限制人数
     */
    public void setRanklimite(Integer ranklimite) {
        this.ranklimite = ranklimite;
    }

    /**
     * 参与人数
     * @return rankinvolved 参与人数
     */
    public Integer getRankinvolved() {
        return rankinvolved;
    }

    /**
     * 参与人数
     * @param rankinvolved 参与人数
     */
    public void setRankinvolved(Integer rankinvolved) {
        this.rankinvolved = rankinvolved;
    }

    /**
     * 榜单范围
     * @return rankscope 榜单范围
     */
    public String getRankscope() {
        return rankscope;
    }

    /**
     * 榜单范围
     * @param rankscope 榜单范围
     */
    public void setRankscope(String rankscope) {
        this.rankscope = rankscope == null ? null : rankscope.trim();
    }

    /**
     * 榜单图片
     * @return rankphotos 榜单图片
     */
    public String getRankphotos() {
        return rankphotos;
    }

    /**
     * 榜单图片
     * @param rankphotos 榜单图片
     */
    public void setRankphotos(String rankphotos) {
        this.rankphotos = rankphotos == null ? null : rankphotos.trim();
    }

    public Double getRankrate() {
        return rankrate;
    }

    public void setRankrate(Double rankrate) {
        this.rankrate = rankrate;
    }

    /**
     * 开始时间
     * @return starttime 开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 开始时间ååååå
     * @param starttime 开始时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 结束时间
     * @return endtime 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 结束时间
     * @param endtime 结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 区域名字
     * @return areaname 区域名字
     */
    public String getAreaname() {
        return areaname;
    }

    /**
     * 区域名字
     * @param areaname 区域名字
     */
    public void setAreaname(String areaname) {
        this.areaname = areaname == null ? null : areaname.trim();
    }

    /**
     * 坐标
     * @return coordinate 坐标
     */
    public String getCoordinate() {
        return coordinate;
    }

    /**
     * 坐标
     * @param coordinate 坐标
     */
    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate == null ? null : coordinate.trim();
    }

    /**
     * 是否结束。0—为结束 1—一结束
     * @return isfinish 是否结束。0—为结束 1—一结束
     */
    public String getIsfinish() {
        return isfinish;
    }

    /**
     * 是否结束。0—为结束 1—一结束
     * @param isfinish 是否结束。0—为结束 1—一结束
     */
    public void setIsfinish(String isfinish) {
        this.isfinish = isfinish == null ? null : isfinish.trim();
    }

    /**
     * 榜单创建人id
     * @return createuserid 榜单创建人id
     */
    public String getCreateuserid() {
        return createuserid;
    }

    /**
     * 榜单创建人id
     * @param createuserid 榜单创建人id
     */
    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
    }

    /**
     * 榜单类型。0—公共榜 1--定制榜
     * @return ranktype 榜单类型。0—公共榜 1--定制榜
     */
    public String getRanktype() {
        return ranktype;
    }

    /**
     * 榜单类型。0—公共榜 1--定制榜
     * @param ranktype 榜单类型。0—公共榜 1--定制榜
     */
    public void setRanktype(String ranktype) {
        this.ranktype = ranktype == null ? null : ranktype.trim();
    }

    /**
     * 是否公开  0—公开 1—不公开
     * @return ispublic 是否公开  0—公开 1—不公开
     */
    public String getIspublic() {
        return ispublic;
    }

    /**
     * 是否公开  0—公开 1—不公开
     * @param ispublic 是否公开  0—公开 1—不公开
     */
    public void setIspublic(String ispublic) {
        this.ispublic = ispublic == null ? null : ispublic.trim();
    }

    /**
     * 榜单类别id
     * @return rankcateid 榜单类别id
     */
    public Integer getRankcateid() {
        return rankcateid;
    }

    /**
     * 榜单类别id
     * @param rankcateid 榜单类别id
     */
    public void setRankcateid(Integer rankcateid) {
        this.rankcateid = rankcateid;
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 更新时间
     * @return updatetime 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 赞对应排名分数
     * @return likescore 赞对应排名分数
     */
    public Integer getLikescore() {
        return likescore;
    }

    /**
     * 赞对应排名分数
     * @param likescore 赞对应排名分数
     */
    public void setLikescore(Integer likescore) {
        this.likescore = likescore;
    }

    /**
     * 花排名积分
     * @return flowerscore 花排名积分
     */
    public Integer getFlowerscore() {
        return flowerscore;
    }

    /**
     * 花排名积分
     * @param flowerscore 花排名积分
     */
    public void setFlowerscore(Integer flowerscore) {
        this.flowerscore = flowerscore;
    }

    /**
     * 钻石对应排名分数
     * @return diamondscore 钻石对应排名分数
     */
    public Integer getDiamondscore() {
        return diamondscore;
    }

    /**
     * 钻石对应排名分数
     * @param diamondscore 钻石对应排名分数
     */
    public void setDiamondscore(Integer diamondscore) {
        this.diamondscore = diamondscore;
    }

    /**
     * 口令 
     * @return codeword 口令 
     */
    public String getCodeword() {
        return codeword;
    }

    /**
     * 口令 
     * @param codeword 口令 
     */
    public void setCodeword(String codeword) {
        this.codeword = codeword == null ? null : codeword.trim();
    }

    /**
     * 十全十美类型
     * @return ptype 十全十美类型
     */
    public String getPtype() {
        return ptype;
    }

    /**
     * 十全十美类型
     * @param ptype 十全十美类型
     */
    public void setPtype(String ptype) {
        this.ptype = ptype == null ? null : ptype.trim();
    }
}