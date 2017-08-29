package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class SysRulePerfectTen implements Serializable {
    private Integer id;//自增主键id

    private Integer minscore;//最小分值

    private Integer maxscore;//最大分值

    private Integer plevel;//等级

    private String ptype;//类型

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private Integer score;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    /**
     * 自增主键id
     * @return id 自增主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 自增主键id
     * @param id 自增主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 最小分值
     * @return minscore 最小分值
     */
    public Integer getMinscore() {
        return minscore;
    }

    /**
     * 最小分值
     * @param minscore 最小分值
     */
    public void setMinscore(Integer minscore) {
        this.minscore = minscore;
    }

    /**
     * 最大分值
     * @return maxscore 最大分值
     */
    public Integer getMaxscore() {
        return maxscore;
    }

    /**
     * 最大分值
     * @param maxscore 最大分值
     */
    public void setMaxscore(Integer maxscore) {
        this.maxscore = maxscore;
    }

    /**
     * 等级
     * @return plevel 等级
     */
    public Integer getPlevel() {
        return plevel;
    }

    /**
     * 等级
     * @param plevel 等级
     */
    public void setPlevel(Integer plevel) {
        this.plevel = plevel;
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
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
}