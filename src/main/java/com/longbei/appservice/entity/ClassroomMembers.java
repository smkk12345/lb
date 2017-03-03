package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ClassroomMembers {
    private Integer id;

    private Integer classroomid;//教室id

    private Long userid;//用户id

    private Integer itype;//0—加入教室 1—退出教室

    private String userstatus;//用户在教室中的身份。0 — 普通学员 1—助教

    private Date createtime;//加入教室时间

    private Date updatetime;//退出教室时间
    
    private String hascharge;//是否已经付费。0 未付费 1 付费
    
    private Integer likes; //赞总数量
    
    private Integer flowers; //花总数量
    
    private Integer diamonds = 0; //钻石总数量
    
    private AppUserMongoEntity appUserMongoEntityUserid; //消息用户信息----Userid
    
    private Integer allimp; //用户在教室所发的微进步总数

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
     * 教室id
     * @return classroomid 教室id
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getClassroomid() {
        return classroomid;
    }

    /**
     * 教室id
     * @param classroomid 教室id
     */
    public void setClassroomid(Integer classroomid) {
        this.classroomid = classroomid;
    }

    /**
     * 用户id
     * @return userid 用户id
     */
    @JsonInclude(Include.ALWAYS)
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
     * 0—加入教室 1—退出教室
     * @return itype 0—加入教室 1—退出教室
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getItype() {
        return itype;
    }

    /**
     * 0—加入教室 1—退出教室
     * @param itype 0—加入教室 1—退出教室
     */
    public void setItype(Integer itype) {
        this.itype = itype;
    }

    /**
     * 用户在教室中的身份。0 — 普通学员 1—助教
     * @return usetstatus 用户在教室中的身份。0 — 普通学员 1—助教
     */
    @JsonInclude(Include.ALWAYS)
    public String getUserstatus() {
        return userstatus;
    }

    /**
     * 用户在教室中的身份。0 — 普通学员 1—助教
     * @param usetstatus 用户在教室中的身份。0 — 普通学员 1—助教
     */
    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus == null ? null : userstatus.trim();
    }

    @JsonInclude(Include.ALWAYS)
    public String getHascharge() {
		return hascharge;
	}

	public void setHascharge(String hascharge) {
		this.hascharge = hascharge;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getFlowers() {
		return flowers;
	}

	public void setFlowers(Integer flowers) {
		this.flowers = flowers;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getDiamonds() {
		return diamonds;
	}

	public void setDiamonds(Integer diamonds) {
		this.diamonds = diamonds;
	}

	/**
     * 加入教室时间
     * @return createtime 加入教室时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 加入教室时间
     * @param createtime 加入教室时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 退出教室时间
     * @return updatetime 退出教室时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 退出教室时间
     * @param updatetime 退出教室时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	public AppUserMongoEntity getAppUserMongoEntityUserid() {
		return appUserMongoEntityUserid;
	}

	public void setAppUserMongoEntityUserid(AppUserMongoEntity appUserMongoEntityUserid) {
		this.appUserMongoEntityUserid = appUserMongoEntityUserid;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getAllimp() {
		return allimp;
	}

	public void setAllimp(Integer allimp) {
		this.allimp = allimp;
	}
}