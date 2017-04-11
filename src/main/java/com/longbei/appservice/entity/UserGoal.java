package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UserGoal {
    private Integer id;

    private Long goalid;
    private Long userid;

    private String goaltag;//目标关键字

    private Date createtime;

    private String isdel;//是否删除  0 未删除 1 删除

    private Date updatetime;

    private String ispublic;//可见程度 0 私密 1 好友可见 2 全部可见

    private String ptype;

    private String needwarn;//提醒 0不提醒 1提醒

    private String warntime;

    private String week;

    private Integer icount;//进步更新条数

    private Integer likes; //点赞数

    private Integer flowers; //送花数
    
    
    //------------------------------------扩展字段----------------
    
    private Integer goalCount = 0;//目标中已发进步条数
    
    private String pickey; //目标主进步key
    
    private Date starttime; //目标主进步更新日期
    
    private String itype;//目标主进步  类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
    
    private Improve improve;
    

    public void setIcount(Integer icount) {
        this.icount = icount;
    }

    @JsonInclude(Include.ALWAYS)
    public Integer getIcount() {
        return icount;
    }

    @JsonInclude(Include.ALWAYS)
    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getNeedwarn() {
        return needwarn;
    }

    public void setNeedwarn(String needwarn) {
        this.needwarn = needwarn;
    }

    public String getWarntime() {
        return warntime;
    }

    public void setWarntime(String warntime) {
        this.warntime = warntime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    @JsonInclude(Include.ALWAYS)
    public String getIspublic() {
        return ispublic;
    }

    public void setIspublic(String ispublic) {
        this.ispublic = ispublic;
    }

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

    @JsonInclude(Include.ALWAYS)
    public Long getGoalid() {
        return goalid;
    }

    public void setGoalid(Long goalid) {
        this.goalid = goalid;
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
     * 目标关键字
     * @return goaltag 目标关键字
     */
    @JsonInclude(Include.ALWAYS)
    public String getGoaltag() {
        return goaltag;
    }

    /**
     * 目标关键字
     * @param goaltag 目标关键字
     */
    public void setGoaltag(String goaltag) {
        this.goaltag = goaltag == null ? null : goaltag.trim();
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
     * 是否删除  0 未删除 1 删除
     * @return isdel 是否删除  0 未删除 1 删除
     */
    @JsonInclude(Include.ALWAYS)
    public String getIsdel() {
        return isdel;
    }

    /**
     * 是否删除  0 未删除 1 删除
     * @param isdel 是否删除  0 未删除 1 删除
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
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
	public Integer getGoalCount() {
		return goalCount;
	}

	public void setGoalCount(Integer goalCount) {
		this.goalCount = goalCount;
	}

	public Improve getImprove() {
		return improve;
	}

	public void setImprove(Improve improve) {
		this.improve = improve;
	}

	public String getPickey() {
		return pickey;
	}

	public void setPickey(String pickey) {
		this.pickey = pickey;
	}

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	@JsonInclude(Include.ALWAYS)
	public String getItype() {
		return itype;
	}

	public void setItype(String itype) {
		this.itype = itype;
	}
	
}