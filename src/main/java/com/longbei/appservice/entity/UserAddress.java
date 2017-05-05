package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UserAddress {
    private Integer id;

    private Long userid;

    private String region;//区域

    private String address;//收获地址

    private String isdefault;//是否 默认   1 默认收货地址  0 非默认

    private String mobile;//联系人手机号

    private String receiver;//收获人

    private String isdel;//0 未删除 1 删除

    private Date createtime;

    private Date updatetime;
    
    public UserAddress(){
    	super();
    }

    public UserAddress(long userid, String region, String address, String isdefault, String mobile, String receiver,
    		String isdel) {
		super();
		this.userid = userid;
		this.region = region;
		this.address = address;
		this.isdefault = isdefault;
		this.mobile = mobile;
		this.receiver = receiver;
		this.isdel = isdel;
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
     * 区域
     * @return region 区域
     */
    public String getRegion() {
        return region;
    }

    /**
     * 区域
     * @param region 区域
     */
    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    /**
     * 收获地址
     * @return addresses 收获地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 收获地址
     * @param addresses 收获地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 是否 默认   0 默认  1 非默认
     * @return isdefault 是否 默认  1 默认收货地址  0 非默认
     */
    @JsonInclude(Include.ALWAYS)
    public String getIsdefault() {
        return isdefault;
    }

    /**
     * 是否 默认   0 默认  1 非默认
     * @param isdefault 是否 默认   1 默认收货地址  0 非默认
     */
    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault == null ? null : isdefault.trim();
    }

    /**
     * 联系人手机号
     * @return mobile 联系人手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 联系人手机号
     * @param mobile 联系人手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 收获人
     * @return reveiver 收获人
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * 收获人
     * @param reveiver 收获人
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    /**
     * 0 未删除 1 删除
     * @return isdel
     */
    @JsonInclude(Include.ALWAYS)
    public String getIsdel() {
		return isdel;
	}

    /**
     * 0 未删除 1 删除
     * @param isdel
     */
	public void setIsdel(String isdel) {
		this.isdel = isdel;
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

    /**
     * 
     * @return updatetime 
     */
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
}