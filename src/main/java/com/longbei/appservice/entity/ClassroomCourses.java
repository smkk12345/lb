package com.longbei.appservice.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ClassroomCourses {
    private Integer id;

    private String coursetitle;//课程标题

    private Integer coursesort;//课程序号

    private String coursetype;//coursetype 课程类型.  0 不收费 1 收费

    private String coursebrief;//课程简介

    private String pickey;//截图key

    private String coursecontent;//课程内容

    private long classroomid;//所属教室id

    private Long uploaduserid;//上传人id

    private Long createuserid;//创建人id

    private String isdel;//0 — 未删除。1 —删除

    private Date createtime;//创建时间

    private Date udpatetime;//更新时间
    
    private String isdefault;//isdefault是否 默认   1 默认封面  0 非默认
    
    private String fileurl; //视频文件url（转码后）
    
    private String sourcekey; //源文件
    
    private String duration; //音频时长
    
    private String isup; //isup是否上架   0：未上架    1：已上架

    private Date starttime;//直播开始时间

    private Date endtime;//直播结束时间

    private String teachingtypes;//教学类型 0 录播 1直播

    private String playback ;//0 未开启回放 1 开启回放


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
     * 课程标题
     * @return coursetitle 课程标题
     */
    public String getCoursetitle() {
        return coursetitle;
    }

    /**
     * 课程标题
     * @param coursetitle 课程标题
     */
    public void setCoursetitle(String coursetitle) {
        this.coursetitle = coursetitle == null ? null : coursetitle.trim();
    }

    /**
     * 课程序号
     * @return coursesort 课程序号
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getCoursesort() {
        return coursesort;
    }

    /**
     * 课程序号
     * @param coursesort 课程序号
     */
    public void setCoursesort(Integer coursesort) {
        this.coursesort = coursesort;
    }

    /**
     * 课程类型
     * @return coursetype 课程类型
     */
    @JsonInclude(Include.ALWAYS)
    public String getCoursetype() {
        return coursetype;
    }

    /**
     * 课程类型
     * @param coursetype 课程类型
     */
    public void setCoursetype(String coursetype) {
        this.coursetype = coursetype == null ? null : coursetype.trim();
    }

    /**
     * 课程简介
     * @return coursebrief 课程简介
     */
    public String getCoursebrief() {
        return coursebrief;
    }

    /**
     * 课程简介
     * @param coursebrief 课程简介
     */
    public void setCoursebrief(String coursebrief) {
        this.coursebrief = coursebrief == null ? null : coursebrief.trim();
    }

    public String getPickey() {
		return pickey;
	}

	public void setPickey(String pickey) {
		this.pickey = pickey;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getSourcekey() {
		return sourcekey;
	}

	public void setSourcekey(String sourcekey) {
		this.sourcekey = sourcekey;
	}

	/**
     * 课程内容
     * @return coursecontent 课程内容
     */
    public String getCoursecontent() {
        return coursecontent;
    }

    /**
     * 课程内容
     * @param coursecontent 课程内容
     */
    public void setCoursecontent(String coursecontent) {
        this.coursecontent = coursecontent == null ? null : coursecontent.trim();
    }

    /**
     * 所属教室id
     * @return classroomid 所属教室id
     */
    @JsonInclude(Include.ALWAYS)
    public long getClassroomid() {
        return classroomid;
    }

    /**
     * 所属教室id
     * @param classroomid 所属教室id
     */
    public void setClassroomid(long classroomid) {
        this.classroomid = classroomid;
    }

    /**
     * 上传人id
     * @return uploaduserid 上传人id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getUploaduserid() {
        return uploaduserid;
    }

    /**
     * 上传人id
     * @param uploaduserid 上传人id
     */
    public void setUploaduserid(Long uploaduserid) {
        this.uploaduserid = uploaduserid;
    }

    /**
     * 创建人id
     * @return createuserid 创建人id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getCreateuserid() {
        return createuserid;
    }

    /**
     * 创建人id
     * @param createuserid 创建人id
     */
    public void setCreateuserid(Long createuserid) {
        this.createuserid = createuserid;
    }

    /**
     * 0 — 未删除。1 —删除
     * @return isdel 0 — 未删除。1 —删除
     */
    @JsonInclude(Include.ALWAYS)
    public String getIsdel() {
        return isdel;
    }

    /**
     * 0 — 未删除。1 —删除
     * @param isdel 0 — 未删除。1 —删除
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
     * @return udpatetime 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUdpatetime() {
        return udpatetime;
    }

    /**
     * 更新时间
     * @param udpatetime 更新时间
     */
    public void setUdpatetime(Date udpatetime) {
        this.udpatetime = udpatetime;
    }

    @JsonInclude(Include.ALWAYS)
	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@JsonInclude(Include.ALWAYS)
	public String getIsup() {
		return isup;
	}

	public void setIsup(String isup) {
		this.isup = isup;
	}

    public Date getEndtime() {
        return endtime;
    }

    public Date getStarttime() {
        return starttime;
    }

    public String getTeachingtypes() {
        return teachingtypes;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public void setTeachingtypes(String teachingtypes) {
        this.teachingtypes = teachingtypes;
    }

    public void setPlayback(String playback) {
        this.playback = playback;
    }

    public String getPlayback() {
        return playback;
    }

}
