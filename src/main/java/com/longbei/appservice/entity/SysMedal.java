package com.longbei.appservice.entity;

import java.util.Date;

public class SysMedal {
    private Integer id;

    private String medalname;//勋章的名字

    private String medalphotos;//勋章图片

    private String medalbrief;//勋章简介

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

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
     * 勋章的名字
     * @return medalname 勋章的名字
     */
    public String getMedalname() {
        return medalname;
    }

    /**
     * 勋章的名字
     * @param medalname 勋章的名字
     */
    public void setMedalname(String medalname) {
        this.medalname = medalname == null ? null : medalname.trim();
    }

    /**
     * 勋章图片
     * @return medalphotos 勋章图片
     */
    public String getMedalphotos() {
        return medalphotos;
    }

    /**
     * 勋章图片
     * @param medalphotos 勋章图片
     */
    public void setMedalphotos(String medalphotos) {
        this.medalphotos = medalphotos == null ? null : medalphotos.trim();
    }

    /**
     * 勋章简介
     * @return medalbrief 勋章简介
     */
    public String getMedalbrief() {
        return medalbrief;
    }

    /**
     * 勋章简介
     * @param medalbrief 勋章简介
     */
    public void setMedalbrief(String medalbrief) {
        this.medalbrief = medalbrief == null ? null : medalbrief.trim();
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