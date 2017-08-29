package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UserImpCoinDetail implements Serializable {
    private Integer id;

    private Long userid;
    
    private Long friendid;

    private String origin;//来源   0:签到   1:发进步  2:分享  3：邀请好友  4：榜获奖  5：收到钻石礼物  
    						//6：收到鲜花礼物  7:兑换商品  8：公益抽奖获得进步币  
    						//9：公益抽奖消耗进步币   10.消耗进步币(例如超级用户扣除进步币)
    						//11:取消订单返还进步币      12:兑换鲜花

    private Integer number;//数量

    private Long impid;//业务id

    private Date createtime;

    private Date updatetime;
    
    private AppUserMongoEntity appUserMongoEntity; //用户进步币信息----Friendid

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

    @JsonInclude(Include.ALWAYS)
    public Long getFriendid() {
		return friendid;
	}

	public void setFriendid(Long friendid) {
		this.friendid = friendid;
	}

	/**
     * 来源     0:签到   1:发进步
     * @return origin 来源      0:签到   1:发进步
     */
    @JsonInclude(Include.ALWAYS)
    public String getOrigin() {
        return origin;
    }

    /**
     * 来源      0:签到   1:发进步
     * @param origin 来源     0:签到   1:发进步
     */
    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }

    /**
     * 数量
     * @return number 数量
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getNumber() {
        return number;
    }

    /**
     * 数量
     * @param number 数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 业务id
     * @return impid 业务id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getImpid() {
        return impid;
    }

    /**
     * 业务id
     * @param impid 业务id
     */
    public void setImpid(Long impid) {
        this.impid = impid;
    }

    /**
     * 
     * @return createtime 
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

    /**
     * 
     * @return updatetime 
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }
    
    /**
     * 
     * @param updatetime 
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	public AppUserMongoEntity getAppUserMongoEntity() {
		return appUserMongoEntity;
	}

	public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
		this.appUserMongoEntity = appUserMongoEntity;
	}
}