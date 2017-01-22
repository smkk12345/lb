package com.longbei.appservice.entity;

import java.util.Date;

public class CircleMembers {
    private Integer id;

    private Long circleid;//兴趣圈id

    private Long userid;//用户id

    private Integer itype;//0—入圈 1—退圈

    private Date createtime;//入圈时间

    private Date updatetime;//退圈时间

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
     * 兴趣圈id
     * @return circleid 兴趣圈id
     */
    public Long getCircleid() {
        return circleid;
    }

    /**
     * 兴趣圈id
     * @param circleid 兴趣圈id
     */
    public void setCircleid(Long circleid) {
        this.circleid = circleid;
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
     * 0—入圈 1—退圈
     * @return itype 0—入圈 1—退圈
     */
    public Integer getItype() {
        return itype;
    }

    /**
     * 0—入圈 1—退圈
     * @param itype 0—入圈 1—退圈
     */
    public void setItype(Integer itype) {
        this.itype = itype;
    }

    /**
     * 入圈时间
     * @return createtime 入圈时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 入圈时间
     * @param createtime 入圈时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 退圈时间
     * @return updatetime 退圈时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 退圈时间
     * @param updatetime 退圈时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}