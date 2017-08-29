package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class UserAccount implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户id
     */
    private Long userid;

    /**
     * 1 冻结 0 解冻
     */
    private String status;

    /**
     * 冻结时长
     */
    private Long freezetime;

    /**
     * 
     */
    private String createtime;

    /**
     * 
     */
    private String updatetime;

    private String endtime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人id
     */
    private Long operateuid;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getFreezetime() {
        return freezetime;
    }

    public void setFreezetime(Long freezetime) {
        this.freezetime = freezetime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getOperateuid() {
        return operateuid;
    }

    public void setOperateuid(Long operateuid) {
        this.operateuid = operateuid;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}