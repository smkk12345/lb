package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class SysScoringRule implements Serializable {
    private Integer id;

    private String ruleversion;//版本

    private Float likeproportion;//赞加分比例

    private Float flowerproportion;//花加分比例

    private Date createtime;//创建日期

    private String rulestatus;//0 新规则，1老规则

    private Long createuser;//创建人

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
     * 版本
     * @return ruleversion 版本
     */
    public String getRuleversion() {
        return ruleversion;
    }

    /**
     * 版本
     * @param ruleversion 版本
     */
    public void setRuleversion(String ruleversion) {
        this.ruleversion = ruleversion == null ? null : ruleversion.trim();
    }

    /**
     * 赞加分比例
     * @return likeproportion 赞加分比例
     */
    public Float getLikeproportion() {
        return likeproportion;
    }

    /**
     * 赞加分比例
     * @param likeproportion 赞加分比例
     */
    public void setLikeproportion(Float likeproportion) {
        this.likeproportion = likeproportion;
    }

    /**
     * 花加分比例
     * @return flowerproportion 花加分比例
     */
    public Float getFlowerproportion() {
        return flowerproportion;
    }

    /**
     * 花加分比例
     * @param flowerproportion 花加分比例
     */
    public void setFlowerproportion(Float flowerproportion) {
        this.flowerproportion = flowerproportion;
    }

    /**
     * 创建日期
     * @return createtime 创建日期
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 创建日期
     * @param createtime 创建日期
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 0 新规则，1老规则
     * @return rulestatus 0 新规则，1老规则
     */
    public String getRulestatus() {
        return rulestatus;
    }

    /**
     * 0 新规则，1老规则
     * @param rulestatus 0 新规则，1老规则
     */
    public void setRulestatus(String rulestatus) {
        this.rulestatus = rulestatus == null ? null : rulestatus.trim();
    }

    /**
     * 创建人
     * @return createuser 创建人
     */
    public Long getCreateuser() {
        return createuser;
    }

    /**
     * 创建人
     * @param createuser 创建人
     */
    public void setCreateuser(Long createuser) {
        this.createuser = createuser;
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