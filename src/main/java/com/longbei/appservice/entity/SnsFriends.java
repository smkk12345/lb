package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;

public class SnsFriends {
    private Integer id;

    private Long userid;

    private Long friendid;

    private String nickname;  //好友昵称/好友备注昵称

    private AppUserMongoEntity appUserMongoEntity;//用户基本信息实体

    private String remark; //备注

    private String ispublic;//是否公开 0.不公开 1.公开

    private String isfans = "0";//是否关注   0：未关注   1：已关注

    private Date createtime;

    private Integer isdel;//是否删除 0:未删除 1:已删除

    private Date updatetime;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

	@JsonInclude(Include.ALWAYS)
	public String getIsfans() {
		return isfans;
	}

	public void setIsfans(String isfans) {
		this.isfans = isfans;
	}

    /**
     * 获取用户基本信息实体
     * @return
     */
    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    /**
     * 设置用户基本信息实体
     * @param appUserMongoEntity
     */
    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}