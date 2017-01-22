package com.longbei.appservice.entity;

import java.util.Date;

public class SnsFans {
    private Integer id;

    private Long userid;//关注人id

    private Long likeuserid;//被关注人id

    private Date createtime;//创建时间

    public SnsFans(){};
    
    public SnsFans(long userid,long likeuserid){
    		super();
    		this.userid =userid;
    		this.likeuserid = likeuserid;
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
     * 关注人id
     * @return userid 关注人id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 关注人id
     * @param userid 关注人id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 被关注人id
     * @return likeuserid 被关注人id
     */
    public Long getLikeuserid() {
        return likeuserid;
    }

    /**
     * 被关注人id
     * @param likeuserid 被关注人id
     */
    public void setLikeuserid(Long likeuserid) {
        this.likeuserid = likeuserid;
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
}