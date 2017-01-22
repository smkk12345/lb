package com.longbei.appservice.entity;

public class SysCommon {
    private Integer id;

    private String key;//当前的属性的key值

    private String info;//具体的描述信息－－会很长 比如公司简介

    private String remark;//备注

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
     * 当前的属性的key值
     * @return key 当前的属性的key值
     */
    public String getKey() {
        return key;
    }

    /**
     * 当前的属性的key值
     * @param key 当前的属性的key值
     */
    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    /**
     * 具体的描述信息－－会很长 比如公司简介
     * @return info 具体的描述信息－－会很长 比如公司简介
     */
    public String getInfo() {
        return info;
    }

    /**
     * 具体的描述信息－－会很长 比如公司简介
     * @param info 具体的描述信息－－会很长 比如公司简介
     */
    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}