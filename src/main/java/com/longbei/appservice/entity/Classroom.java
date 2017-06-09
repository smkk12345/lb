package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Classroom {
    private Integer id;
    
    private Long classroomid;

    private String classtitle;//教室标题

    private String classphotos;//教室图片

    private Integer classcateid;//教室类别id

    private Integer classinvoloed;//教室参与人数

    private Integer classlimited;//教室限制人数

    private String classbrief;//教室简介

    private String classnotice;//教室公告

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private String ptype;//十全十美类型
    
    private String ispublic; //是否所有人可见。0 所有人可见。1，部分可见
    
    private long userid;
    
    private long cardid; //名片id
    
    private Integer charge;  //课程价格
    
    private String isfree; //是否免费。0 免费 1 收费
    
    private Integer freecoursenum; //免费课程数量

    private String isdel; //0 未删除。1 删除
    
    
    //----------------扩展字段--------------------------
    
    private UserCard userCard; //名片类
    
    private String pickey; //最新课程视频截图key
    
    private String fileurl; //课程默认封面---视频文件url（转码后）
    
    private String isadd; //访问用户是否已加入教室  0：未加入  1：加入

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
     * 教室标题
     * @return classtitle 教室标题
     */
    public String getClasstitle() {
        return classtitle;
    }

    /**
     * 教室标题
     * @param classtitle 教室标题
     */
    public void setClasstitle(String classtitle) {
        this.classtitle = classtitle == null ? null : classtitle.trim();
    }

    /**
     * 教室图片
     * @return classphotos 教室图片
     */
    public String getClassphotos() {
        return classphotos;
    }

    /**
     * 教室图片
     * @param classphotos 教室图片
     */
    public void setClassphotos(String classphotos) {
        this.classphotos = classphotos == null ? null : classphotos.trim();
    }

    /**
     * 教室类别id
     * @return classcateid 教室类别id
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getClasscateid() {
		return classcateid;
	}

    /**
     * 教室类别id
     * @param classcateid 教室类别id
     */
    public void setClasscateid(Integer classcateid) {
		this.classcateid = classcateid;
	}

	/**
     * 教室参与人数
     * @return classinvoloed 教室参与人数
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getClassinvoloed() {
        return classinvoloed;
    }
    
    /**
     * 教室参与人数
     * @param classinvoloed 教室参与人数
     */
    public void setClassinvoloed(Integer classinvoloed) {
        this.classinvoloed = classinvoloed;
    }

    /**
     * 教室限制人数
     * @return classlimited 教室限制人数
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getClasslimited() {
        return classlimited;
    }

    /**
     * 教室限制人数
     * @param classlimited 教室限制人数
     */
    public void setClasslimited(Integer classlimited) {
        this.classlimited = classlimited;
    }

    /**
     * 教室简介
     * @return classbrief 教室简介
     */
    public String getClassbrief() {
        return classbrief;
    }

    /**
     * 教室简介
     * @param classbrief 教室简介
     */
    public void setClassbrief(String classbrief) {
        this.classbrief = classbrief == null ? null : classbrief.trim();
    }

    @JsonInclude(Include.ALWAYS)
    public String getIsfree() {
		return isfree;
	}

	public void setIsfree(String isfree) {
		this.isfree = isfree;
	}

	/**
     * 教室公告
     * @return classnotice 教室公告
     */
    public String getClassnotice() {
        return classnotice;
    }

    /**
     * 教室公告
     * @param classnotice 教室公告
     */
    public void setClassnotice(String classnotice) {
        this.classnotice = classnotice == null ? null : classnotice.trim();
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

    /**
     * 更新时间
     * @return updatetime 更新时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 十全十美类型
     * @return ptype 十全十美类型
     */
    @JsonInclude(Include.ALWAYS)
    public String getPtype() {
        return ptype;
    }

    /**
     * 十全十美类型
     * @param ptype 十全十美类型
     */
    public void setPtype(String ptype) {
        this.ptype = ptype == null ? null : ptype.trim();
    }

    @JsonInclude(Include.ALWAYS)
	public Long getClassroomid() {
		return classroomid;
	}

	public void setClassroomid(Long classroomid) {
		this.classroomid = classroomid;
	}

	@JsonInclude(Include.ALWAYS)
	public String getIspublic() {
		return ispublic;
	}

	public void setIspublic(String ispublic) {
		this.ispublic = ispublic;
	}

	@JsonInclude(Include.ALWAYS)
	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getCharge() {
		return charge;
	}

	public void setCharge(Integer charge) {
		this.charge = charge;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getFreecoursenum() {
		return freecoursenum;
	}

	public void setFreecoursenum(Integer freecoursenum) {
		this.freecoursenum = freecoursenum;
	}

	@JsonInclude(Include.ALWAYS)
	public long getCardid() {
		return cardid;
	}

	public void setCardid(long cardid) {
		this.cardid = cardid;
	}

	public UserCard getUserCard() {
		return userCard;
	}

    public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getIsadd() {
		return isadd;
	}

	public void setIsadd(String isadd) {
		this.isadd = isadd;
	}

    @JsonInclude(Include.ALWAYS)
    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    @JsonInclude(Include.ALWAYS)
	public String getPickey() {
		return pickey;
	}

	public void setPickey(String pickey) {
		this.pickey = pickey;
	}

}