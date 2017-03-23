package com.longbei.appservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProductOrders {
    private Integer id;//自增id

    private String orderid;//订单id
    
    private String username; //用户手机号    冗余字段

    private String ordernum;//订单号

    private String userid;//用户id

    private String receiver;//收件人

    private String address;//收件人地址

    private String mobile;//收件人手机
    
    private Double impiconprice;//成交价格---进步币
    
    private Double moneyprice;  //成交价格---龙币

    private String paytype;//支付方式  0：龙币支付 1：微信支付 2：支付宝支付

    private String orderstatus;//订单状态   0：待付款   1：待发货   2：待收货  3：已完成     4：已取消
    
    private String otype; //订单类型。0 龙币 1 进步币 2 混排

    private String isdel;//是否取消订单   0：不取消 1:取消
    
    private String isexception; //0 正常 1 异常   

    private String remark;//备注

    private String logisticscode;//物流编号

    private String logisticscompany;//物流公司

    private Date sendgoodsdate;//发货日期

    private Date createtime;//创建日期

    private Date updatetime;//更新日期
    
    
    private List<ProductOrderInfo> orderInfoList = new ArrayList<ProductOrderInfo>(); //订单详情
    
    private AppUserMongoEntity appUserMongoEntity; //用户信息----Userid
    
    private int sumNum = 0; //订单总共有商品数量

    /**
     * 自增id
     * @return id 自增id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 自增id
     * @param id 自增id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 订单id
     * @return orderid 订单id
     */
    @JsonInclude(Include.ALWAYS)
    public String getOrderid() {
        return orderid;
    }

    /**
     * 订单id
     * @param orderid 订单id
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    /**
     * 订单号
     * @return ordernum 订单号
     */
    @JsonInclude(Include.ALWAYS)
    public String getOrdernum() {
        return ordernum;
    }

    /**
     * 订单号
     * @param ordernum 订单号
     */
    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum == null ? null : ordernum.trim();
    }

    /**
     * 用户id
     * @return userid 用户id
     */
    @JsonInclude(Include.ALWAYS)
    public String getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 收件人
     * @return receiver 收件人
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * 收件人
     * @param receiver 收件人
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    /**
     * 收件人地址
     * @return address 收件人地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 收件人地址
     * @param address 收件人地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 收件人手机
     * @return mobile 收件人手机
     */
    @JsonInclude(Include.ALWAYS)
    public String getMobile() {
        return mobile;
    }

    /**
     * 收件人手机
     * @param mobile 收件人手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    @JsonInclude(Include.ALWAYS)
    public Double getImpiconprice() {
		return impiconprice;
	}

	public void setImpiconprice(Double impiconprice) {
		this.impiconprice = impiconprice;
	}

	@JsonInclude(Include.ALWAYS)
	public Double getMoneyprice() {
		return moneyprice;
	}

	public void setMoneyprice(Double moneyprice) {
		this.moneyprice = moneyprice;
	}

	/**
     * 支付方式
     * @return paytype 支付方式
     */
    @JsonInclude(Include.ALWAYS)
    public String getPaytype() {
        return paytype;
    }

    /**
     * 支付方式
     * @param paytype 支付方式
     */
    public void setPaytype(String paytype) {
        this.paytype = paytype == null ? null : paytype.trim();
    }

    /**
     * 订单状态
     * @return orderstatus 订单状态
     */
    @JsonInclude(Include.ALWAYS)
    public String getOrderstatus() {
        return orderstatus;
    }

    /**
     * 订单状态
     * @param orderstatus 订单状态
     */
    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus == null ? null : orderstatus.trim();
    }

    /**
     * 是否取消订单   0：不取消 1:取消
     * @return isdel 是否取消订单   0：不取消 1:取消
     */
    @JsonInclude(Include.ALWAYS)
    public String getIsdel() {
        return isdel;
    }

    /**
     * 是否取消订单   0：不取消 1:取消
     * @param isdel 是否取消订单   0：不取消 1:取消
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 物流编号
     * @return logisticscode 物流编号
     */
    @JsonInclude(Include.ALWAYS)
    public String getLogisticscode() {
        return logisticscode;
    }

    /**
     * 物流编号
     * @param logisticscode 物流编号
     */
    public void setLogisticscode(String logisticscode) {
        this.logisticscode = logisticscode == null ? null : logisticscode.trim();
    }

    /**
     * 物流公司
     * @return logisticscompany 物流公司
     */
    public String getLogisticscompany() {
        return logisticscompany;
    }

    /**
     * 物流公司
     * @param logisticscompany 物流公司
     */
    public void setLogisticscompany(String logisticscompany) {
        this.logisticscompany = logisticscompany == null ? null : logisticscompany.trim();
    }

    /**
     * 发货日期
     * @return sendgoodsdate 发货日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getSendgoodsdate() {
        return sendgoodsdate;
    }

    /**
     * 发货日期
     * @param sendgoodsdate 发货日期
     */
    public void setSendgoodsdate(Date sendgoodsdate) {
        this.sendgoodsdate = sendgoodsdate;
    }

    /**
     * 创建日期
     * @return createtime 创建日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 创建日期
     * @param createtime 创建日期
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 更新日期
     * @return updatetime 更新日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新日期
     * @param updatetime 更新日期
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	public List<ProductOrderInfo> getOrderInfoList() {
		return orderInfoList;
	}

	public void setOrderInfoList(List<ProductOrderInfo> orderInfoList) {
		this.orderInfoList = orderInfoList;
	}

	@JsonInclude(Include.ALWAYS)
	public int getSumNum() {
		return sumNum;
	}

	public void setSumNum(int sumNum) {
		this.sumNum = sumNum;
	}

	@JsonInclude(Include.ALWAYS)
	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	@JsonInclude(Include.ALWAYS)
	public String getIsexception() {
		return isexception;
	}

	public void setIsexception(String isexception) {
		this.isexception = isexception;
	}

	@JsonInclude(Include.ALWAYS)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public AppUserMongoEntity getAppUserMongoEntity() {
		return appUserMongoEntity;
	}

	public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
		this.appUserMongoEntity = appUserMongoEntity;
	}
}