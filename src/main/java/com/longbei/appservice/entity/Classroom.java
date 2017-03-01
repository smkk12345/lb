package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Classroom {
    private Integer id;
    
    private Long classroomid;

    private String classtitle;//教室标题

    private String classphotos;//教室图片

    private Integer claacateid;//教室类别id

    private Integer classinvoloed;//教室参与人数

    private Integer classlimited;//教室限制人数

    private String classbrief;//教室简介

    private Integer classverify;//0 — 不需要。1—需要

    private String classnotice;//教室公告

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private String ptype;//十全十美类型
    
    private String ispublic; //是否所有人可见。0 所有人可见。1，部分可见
    
    private long userid;

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
     * @return claacateid 教室类别id
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getClaacateid() {
        return claacateid;
    }

    /**
     * 教室类别id
     * @param claacateid 教室类别id
     */
    public void setClaacateid(Integer claacateid) {
        this.claacateid = claacateid;
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

    /**
     * 0 — 不需要。1—需要
     * @return classverify 0 — 不需要。1—需要
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getClassverify() {
        return classverify;
    }

    /**
     * 0 — 不需要。1—需要
     * @param classverify 0 — 不需要。1—需要
     */
    public void setClassverify(Integer classverify) {
        this.classverify = classverify;
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
}