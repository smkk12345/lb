package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.longbei.appservice.common.web.CustomDoubleSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RankAwardRelease {
    private Integer id;

    private String rankid;//榜单id

    private String awardid;//奖品的id

    private Integer awardlevel;//奖品等级 1 2 3

    private Double awardrate;//获奖的人数

    private Integer awardcount;

    private String awardnickname;//奖品的名称 特等奖 一等奖 优秀奖

    private Date createtime;

    private Award award;

    private Long userid;

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public String getAwardnickname() {
        return awardnickname;
    }

    public void setAwardnickname(String awardnickname) {
        this.awardnickname = awardnickname;
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
     * 
     * @return rankid 
     */
    public String getRankid() {
        return rankid;
    }

    /**
     * 
     * @param rankid 
     */
    public void setRankid(String rankid) {
        this.rankid = rankid == null ? null : rankid.trim();
    }

    /**
     * 
     * @return awardid 
     */
    public String getAwardid() {
        return awardid;
    }

    /**
     * 
     * @param awardid 
     */
    public void setAwardid(String awardid) {
        this.awardid = awardid == null ? null : awardid.trim();
    }

    /**
     * 
     * @return awardlevel 
     */
    public Integer getAwardlevel() {
        return awardlevel;
    }

    /**
     * 
     * @param awardlevel 
     */
    public void setAwardlevel(Integer awardlevel) {
        this.awardlevel = awardlevel;
    }

    /**
     * 
     * @return awardrate 
     */
    @JsonSerialize(using = CustomDoubleSerialize.class)
    public Double getAwardrate() {
        return awardrate;
    }

    /**
     * 
     * @param awardrate 
     */
    public void setAwardrate(Double awardrate) {
        this.awardrate = awardrate;
    }

    /**
     * 
     * @return awardcount 
     */
    public Integer getAwardcount() {
        return awardcount;
    }

    /**
     * 
     * @param awardcount 
     */
    public void setAwardcount(Integer awardcount) {
        this.awardcount = awardcount;
    }

    /**
     * 
     * @return createtime 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 
     * @param createtime 
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}