package com.longbei.appservice.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SnsFans {
    private Integer id;

    private Long userid;//关注人id

    private Long likeuserid;//被关注人id

    private Date createtime;//创建时间
    
//    private String likeNickname;  //被关注者昵称
//    
//    private String liekAvatar;  //被关注者头像
    
    private String isfriend = "0"; //是否是好友    0：不是   1：是

    private String isfans = "0"; //是否是粉丝    0：不是   1：是

    private String isfocus = "0"; //是否是关注    0：不是   1：是


    
    private String isread = "1";// 0 未读 1 已读
    
    private AppUserMongoEntity appUserMongoEntityLikeuserid; //粉丝用户信息----likeuserid

    //----------------扩展字段----------
//    private String remark; //好友备注
    
    public SnsFans(){};
    
    public SnsFans(long userid,long likeuserid){
    		super();
    		this.userid =userid;
    		this.likeuserid = likeuserid;
            this.createtime = new Date();
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

//	public String getLikeNickname() {
//		return likeNickname;
//	}
//
//	public void setLikeNickname(String likeNickname) {
//		this.likeNickname = likeNickname;
//	}
//
//	public String getLiekAvatar() {
//		return liekAvatar;
//	}
//
//	public void setLiekAvatar(String liekAvatar) {
//		this.liekAvatar = liekAvatar;
//	}

	@JsonInclude(Include.ALWAYS)
	public String getIsfriend() {
		return isfriend;
	}

	public void setIsfriend(String isfriend) {
		this.isfriend = isfriend;
	}

	@JsonInclude(Include.ALWAYS)
	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public AppUserMongoEntity getAppUserMongoEntityLikeuserid() {
		return appUserMongoEntityLikeuserid;
	}

	public void setAppUserMongoEntityLikeuserid(AppUserMongoEntity appUserMongoEntityLikeuserid) {
		this.appUserMongoEntityLikeuserid = appUserMongoEntityLikeuserid;
	}
//
//	public String getRemark() {
//		return remark;
//	}
//
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}

	@JsonInclude(Include.ALWAYS)
	public String getIsfans() {
		return isfans;
	}

	public void setIsfans(String isfans) {
		this.isfans = isfans;
	}

	@JsonInclude(Include.ALWAYS)
	public String getIsfocus() {
		return isfocus;
	}

	public void setIsfocus(String isfocus) {
		this.isfocus = isfocus;
	}
    
}