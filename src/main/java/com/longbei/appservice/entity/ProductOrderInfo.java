package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

public class ProductOrderInfo implements Serializable {
    private Integer pid;

    private String id;//编码

    private String orderid;//订单id

    private Integer productid;//商品id

    private String productname;//商品名称

    private Integer number;//数量

    private Double price;//单价

    private String paytype;//支付方式

    private Double originprice; //原价

    private String productpic;//商品图片

    private String enabled;//是否下架
    
    private ProductBasic productBasic = new ProductBasic(); //商品信息

    /**
     * 
     * @return pid 
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 
     * @param pid 
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 编码
     * @return id 编码
     */
    public String getId() {
        return id;
    }

    /**
     * 编码
     * @param id 编码
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
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
     * 商品id
     * @return productid 商品id
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getProductid() {
        return productid;
    }

    /**
     * 商品id
     * @param productid 商品id
     */
    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    /**
     * 商品名称
     * @return productname 商品名称
     */
    public String getProductname() {
        return productname;
    }

    /**
     * 商品名称
     * @param productname 商品名称
     */
    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    /**
     * 数量
     * @return number 数量
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getNumber() {
        return number;
    }

    /**
     * 数量
     * @param number 数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 单价
     * @return price 单价
     */
    @JsonInclude(Include.ALWAYS)
    public Double getPrice() {
        return price;
    }

    /**
     * 单价
     * @param price 单价
     */
    public void setPrice(Double price) {
        this.price = price;
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
     * 
     * @return originprice 
     */
    @JsonInclude(Include.ALWAYS)
    public Double getOriginprice() {
        return originprice;
    }

    /**
     * 
     * @param originprice 
     */
    public void setOriginprice(Double originprice) {
        this.originprice = originprice;
    }

    /**
     * 商品图片
     * @return productpic 商品图片
     */
    public String getProductpic() {
        return productpic;
    }

    /**
     * 商品图片
     * @param productpic 商品图片
     */
    public void setProductpic(String productpic) {
        this.productpic = productpic == null ? null : productpic.trim();
    }

    /**
     * 是否下架
     * @return enabled 是否下架
     */
    @JsonInclude(Include.ALWAYS)
    public String getEnabled() {
        return enabled;
    }

    /**
     * 是否下架
     * @param enabled 是否下架
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
    }

	public ProductBasic getProductBasic() {
		return productBasic;
	}

	public void setProductBasic(ProductBasic productBasic) {
		this.productBasic = productBasic;
	}
}