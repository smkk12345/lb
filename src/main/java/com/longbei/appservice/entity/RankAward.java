package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.longbei.appservice.common.web.CustomDoubleSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RankAward {
    private Integer id;

    private String rankid;

    private String awardid;

    private Integer awardlevel;

    private Double awardrate;

    private Integer awardcount;

    private String awardnickname;

    private Date createtime;

    private Award award;

    private Rank rank;

    private Integer pccoins;

    public Integer getPccoins() {
        return pccoins;
    }

    public void setPccoins(Integer pccoins) {
        this.pccoins = pccoins;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

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
}