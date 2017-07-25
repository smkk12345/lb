package com.longbei.appservice.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.format.annotation.DateTimeFormat;

public class UserIdcard {
    private Integer id;

    private String idcard;

    private String validateemail;

    private String validateidcard;//是否验证了身份证号码  0-验证中 1-验证通过 2-验证不通过

    private String idcardimage;
    
    private Long checkuserid;

    private String checkoption;

    private Date checkdate;

    private Long userid;

    private Date applydate;
    
    private String realname;  //真实姓名

    private Date createtime;

    private Date updatetime;

    private List<String> userids;

    private AppUserMongoEntity user;

    public List<String> getUserids() {
        return userids;
    }

    public void setUserids(List<String> userids) {
        this.userids = userids;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public AppUserMongoEntity getUser() {
        return user;
    }

    public void setUser(AppUserMongoEntity user) {
        this.user = user;
    }
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
     * 是否验证了身份证号码 0-验证中 1-验证通过 2-验证不通过
     * @return validateidcard 是否验证了身份证号码 0-验证中 1-验证通过 2-验证不通过
     */
    @JsonInclude(Include.ALWAYS)
    public String getValidateidcard() {
        return validateidcard;
    }

    /**
     * 是否验证了身份证号码 0-验证中 1-验证通过 2-验证不通过
     * @param validateidcard 是否验证了身份证号码 0-验证中 1-验证通过 2-验证不通过
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
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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