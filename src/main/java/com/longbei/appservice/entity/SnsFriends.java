package com.longbei.appservice.entity;

import java.util.Date;

public class SnsFriends {
    private Integer id;

    private Long userid;

    private Long friendid;

    private String remark;//备注

    private String ispublic;//是否公开

    private Date createtime;

    public SnsFriends(){};
    
    public SnsFriends(long userid,long friendid){
    		super();
    		this.userid = userid;
    		this.friendid = friendid;
    };
    
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
     * @return friendid 
     */
    public Long getFriendid() {
        return friendid;
    }

    /**
     * 
     * @param friendid 
     */
    public void setFriendid(Long friendid) {
        this.friendid = friendid;
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
     * 是否公开
     * @return ispublic 是否公开
     */
    public String getIspublic() {
        return ispublic;
    }

    /**
     * 是否公开
     * @param ispublic 是否公开
     */
    public void setIspublic(String ispublic) {
        this.ispublic = ispublic == null ? null : ispublic.trim();
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
}