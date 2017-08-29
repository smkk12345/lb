package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ImpComplaints implements Serializable {
    private Long id;

    private Long userid;//用户id
    
    private Long comuserid; //被投诉人id

    private Long impid;//进步组id

    private String content;//反馈内容

    private Date createtime;//反馈时间

    private String status;//status 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理  4: 已忽略

    private Date dealtime;//处理时间

    private String dealuser;//处理人

    private String checkoption;

    private String contenttype;

    private Long businessid;//类型业务id

    private String businesstype; //businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
    
    private String username; //投诉人手机号
    
    private String nickname; //投诉人昵称
    
    private String cusername; //被投诉人手机号
    
    private String cnickname; //被投诉人昵称
    
    
    
    private String businessname; //各类型名称  即龙榜，圈子，教室，其他
    
    private String businesstitle; //各类型title  即榜名称，圈子名称，教室名称
    
    private Integer compcount = 0; //各类型进步被投诉次数
    
    private Improve improve;
    

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
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

    @JsonInclude(Include.ALWAYS)
    public Long getComuserid() {
		return comuserid;
	}

	public void setComuserid(Long comuserid) {
		this.comuserid = comuserid;
	}

	/**
     * 进步组id
     * @return impid 进步组id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getImpid() {
        return impid;
    }

    /**
     * 进步组id
     * @param impid 进步组id
     */
    public void setImpid(Long impid) {
        this.impid = impid;
    }

    /**
     * 反馈内容
     * @return content 反馈内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 反馈内容
     * @param content 反馈内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 反馈时间
     * @return createtime 反馈时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 反馈时间
     * @param createtime 反馈时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理
     * @return status 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理
     */
    @JsonInclude(Include.ALWAYS)
    public String getStatus() {
        return status;
    }

    /**
     * 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理
     * @param status 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 处理时间
     * @return dealtime 处理时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getDealtime() {
        return dealtime;
    }

    /**
     * 处理时间
     * @param dealtime 处理时间
     */
    public void setDealtime(Date dealtime) {
        this.dealtime = dealtime;
    }

    /**
     * 处理人
     * @return dealuser 处理人
     */
    @JsonInclude(Include.ALWAYS)
    public String getDealuser() {
        return dealuser;
    }

    /**
     * 处理人
     * @param dealuser 处理人
     */
    public void setDealuser(String dealuser) {
        this.dealuser = dealuser;
    }

    /**
     * 
     * @return checkoption 
     */
    public String getCheckoption() {
        return checkoption;
    }

    /**
     * 
     * @param checkoption 
     */
    public void setCheckoption(String checkoption) {
        this.checkoption = checkoption == null ? null : checkoption.trim();
    }

    /**
     * 
     * @return contenttype 
     */
    @JsonInclude(Include.ALWAYS)
    public String getContenttype() {
        return contenttype;
    }

    /**
     * 
     * @param contenttype 
     */
    public void setContenttype(String contenttype) {
        this.contenttype = contenttype == null ? null : contenttype.trim();
    }

    /**
     * 榜单id
     * @return businessid 榜单id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getBusinessid() {
        return businessid;
    }

    /**
     * 榜单id
     * @param businessid 榜单id
     */
    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    /**
     * 
     * @return businesstype 
     */
    @JsonInclude(Include.ALWAYS)
    public String getBusinesstype() {
        return businesstype;
    }

    /**
     * 
     * @param businesstype 
     */
    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype == null ? null : businesstype.trim();
    }

    @JsonInclude(Include.ALWAYS)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@JsonInclude(Include.ALWAYS)
	public String getCusername() {
		return cusername;
	}

	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	public String getCnickname() {
		return cnickname;
	}

	public void setCnickname(String cnickname) {
		this.cnickname = cnickname;
	}

	public String getBusinessname() {
		return businessname;
	}

	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}

	public String getBusinesstitle() {
		return businesstitle;
	}

	public void setBusinesstitle(String businesstitle) {
		this.businesstitle = businesstitle;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getCompcount() {
		return compcount;
	}

	public void setCompcount(Integer compcount) {
		this.compcount = compcount;
	}

	public Improve getImprove() {
		return improve;
	}

	public void setImprove(Improve improve) {
		this.improve = improve;
	}
}