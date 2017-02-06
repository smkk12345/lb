package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SnsFriends {
    private Integer id;

    private Long userid;

    private Long friendid;

    private String friendNickname;  //好友昵称
    
    private String friendAvatar;   //好友头像
    
    private String username;
    
    private String remark; //备注

    private String ispublic;//是否公开
    
    private String islike = "0";//是否关注   0：未关注   1：已关注

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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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

	public String getFriendNickname() {
		return friendNickname;
	}

	public void setFriendNickname(String friendNickname) {
		this.friendNickname = friendNickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFriendAvatar() {
		return friendAvatar;
	}

	public void setFriendAvatar(String friendAvatar) {
		this.friendAvatar = friendAvatar;
	}

	@JsonInclude(Include.ALWAYS)
	public String getIslike() {
		return islike;
	}

	public void setIslike(String islike) {
		this.islike = islike;
	}
    
}