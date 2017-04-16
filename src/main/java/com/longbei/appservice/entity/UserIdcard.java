package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UserIdcard {
    private Integer id;

    private String idcard;

    private String validateemail;

    private String validateidcard;//是否验证了身份证号码 0  是未提交信息 1  是验证中 2 验证通过 3  验证不通过

    private String idcardimage;
    
    private Long checkuserid;

    private String checkoption;

    private Date checkdate;

    private Long userid;

    private Date applydate;
    
    private String realname;  //真实姓名

    //---------------------扩展字段------------

    private String frontidcardimage;//正面

    private String oppositeidcardimage;//反面

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
     * @return idcard 
     */
    @JsonInclude(Include.ALWAYS)
    public String getIdcard() {
        return idcard;
    }

    /**
     * 
     * @param idcard 
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    /**
     * 
     * @return validateemail 
     */
    @JsonInclude(Include.ALWAYS)
    public String getValidateemail() {
        return validateemail;
    }

    /**
     * 
     * @param validateemail 
     */
    public void setValidateemail(String validateemail) {
        this.validateemail = validateemail == null ? null : validateemail.trim();
    }

    /**
     * 是否验证了身份证号码 0  是未提交信息 1  是验证中 2 验证通过 3  验证不通过
     * @return validateidcard 是否验证了身份证号码 0  是未提交信息 1  是验证中 2 验证通过 3  验证不通过
     */
    @JsonInclude(Include.ALWAYS)
    public String getValidateidcard() {
        return validateidcard;
    }

    /**
     * 是否验证了身份证号码 0  是未提交信息 1  是验证中 2 验证通过 3  验证不通过
     * @param validateidcard 是否验证了身份证号码 0  是未提交信息 1  是验证中 2 验证通过 3  验证不通过
     */
    public void setValidateidcard(String validateidcard) {
        this.validateidcard = validateidcard == null ? null : validateidcard.trim();
    }

    /**
     * 
     * @return idcardimage 
     */
    @JsonInclude(Include.ALWAYS)
    public String getIdcardimage() {
        return idcardimage;
    }

    /**
     * 
     * @param idcardimage 
     */
    public void setIdcardimage(String idcardimage) {
        this.idcardimage = idcardimage == null ? null : idcardimage.trim();
    }

    /**
     * 
     * @return checkuserid 
     */
    public Long getCheckuserid() {
        return checkuserid;
    }

    /**
     * 
     * @param checkuserid 
     */
    public void setCheckuserid(Long checkuserid) {
        this.checkuserid = checkuserid;
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
     * @return checkdate 
     */
    public Date getCheckdate() {
        return checkdate;
    }

    /**
     * 
     * @param checkdate 
     */
    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    /**
     * 
     * @return userid 
     */
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
     * @return applydate 
     */
    public Date getApplydate() {
        return applydate;
    }

    /**
     * 
     * @param applydate 
     */
    public void setApplydate(Date applydate) {
        this.applydate = applydate;
    }

	public String getFrontidcardimage() {
		return frontidcardimage;
	}

	public void setFrontidcardimage(String frontidcardimage) {
		this.frontidcardimage = frontidcardimage;
	}

	public String getOppositeidcardimage() {
		return oppositeidcardimage;
	}

	public void setOppositeidcardimage(String oppositeidcardimage) {
		this.oppositeidcardimage = oppositeidcardimage;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
}