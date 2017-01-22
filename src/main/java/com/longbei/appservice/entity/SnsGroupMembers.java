package com.longbei.appservice.entity;

public class SnsGroupMembers {
    private Integer id;

    private Long groupid;//群id

    private Long userid;//用户id

    private String remark;//备注

    private String createtime;//创建时间

    private String updatetime;//更新时间

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
     * 群id
     * @return groupid 群id
     */
    public Long getGroupid() {
        return groupid;
    }

    /**
     * 群id
     * @param groupid 群id
     */
    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    /**
     * 用户id
     * @return userid 用户id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
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

    /**
     * 创建时间
     * @return createtime 创建时间
     */
    public String getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    /**
     * 更新时间
     * @return updatetime 更新时间
     */
    public String getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }
}