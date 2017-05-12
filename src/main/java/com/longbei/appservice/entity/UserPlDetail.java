package com.longbei.appservice.entity;

import java.util.Date;

public class UserPlDetail {
    private Integer id;

    private Long userid;

    private Integer leve = 1;

    private String ptype;//十全十美类型

    private String perfectname;//十全十美名字 缓存下

    private String toplevel;//0非最高级 1最高级

    private Date createtime;

    private Date updatetime;

    private Integer scorce = 0;

    private Integer totalscorce = 0;

    //前端扩展字段
    private String photo;

    public Integer getTotalscorce() {
        return totalscorce;
    }

    public void setTotalscorce(Integer totalscorce) {
        this.totalscorce = totalscorce;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
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
     * @return userid 
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 
     * @param userid 
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 
     * @return leve 
     */
    public Integer getLeve() {
        return leve;
    }

    /**
     * 
     * @param leve 
     */
    public void setLeve(Integer leve) {
        this.leve = leve;
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

    /**
     * 十全十美名字 缓存下
     * @return perfectname 十全十美名字 缓存下
     */
    public String getPerfectname() {
        return perfectname;
    }

    /**
     * 十全十美名字 缓存下
     * @param perfectname 十全十美名字 缓存下
     */
    public void setPerfectname(String perfectname) {
        this.perfectname = perfectname == null ? null : perfectname.trim();
    }

    /**
     * 
     * @return createtime 
     */
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

    /**
     * 
     * @return updatetime 
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 
     * @param updatetime 
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 
     * @return scorce 
     */
    public Integer getScorce() {
        return scorce;
    }

    /**
     * 
     * @param scorce 
     */
    public void setScorce(Integer scorce) {
        this.scorce = scorce;
    }

    public void setToplevel(String toplevel) {
        this.toplevel = toplevel;
    }

    public String getToplevel() {
        return toplevel;
    }
}