package com.longbei.appservice.entity;

import java.util.Date;

public class UserIosBuyflower {
    private Integer id;

    private Integer flower;//购买鲜花数量

    private Integer money;//买花含的进步币数量

    private Integer appflower;//购买赠送的鲜花数量

    private Integer appmoney;//购买赠送的进步币数量

    private String productid;//ios前端用的一个id

    private String enabled;//是否可以购买 1是可以购买

    private Integer price;//购买金额

    private Date buydate;//购买日期

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
     * 购买鲜花数量
     * @return flower 购买鲜花数量
     */
    public Integer getFlower() {
        return flower;
    }

    /**
     * 购买鲜花数量
     * @param flower 购买鲜花数量
     */
    public void setFlower(Integer flower) {
        this.flower = flower;
    }

    /**
     * 买花含的进步币数量
     * @return money 买花含的进步币数量
     */
    public Integer getMoney() {
        return money;
    }

    /**
     * 买花含的进步币数量
     * @param money 买花含的进步币数量
     */
    public void setMoney(Integer money) {
        this.money = money;
    }

    /**
     * 购买赠送的鲜花数量
     * @return appflower 购买赠送的鲜花数量
     */
    public Integer getAppflower() {
        return appflower;
    }

    /**
     * 购买赠送的鲜花数量
     * @param appflower 购买赠送的鲜花数量
     */
    public void setAppflower(Integer appflower) {
        this.appflower = appflower;
    }

    /**
     * 购买赠送的进步币数量
     * @return appmoney 购买赠送的进步币数量
     */
    public Integer getAppmoney() {
        return appmoney;
    }

    /**
     * 购买赠送的进步币数量
     * @param appmoney 购买赠送的进步币数量
     */
    public void setAppmoney(Integer appmoney) {
        this.appmoney = appmoney;
    }

    /**
     * ios前端用的一个id
     * @return productid ios前端用的一个id
     */
    public String getProductid() {
        return productid;
    }

    /**
     * ios前端用的一个id
     * @param productid ios前端用的一个id
     */
    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    /**
     * 是否可以购买 1是可以购买
     * @return enabled 是否可以购买 1是可以购买
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * 是否可以购买 1是可以购买
     * @param enabled 是否可以购买 1是可以购买
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
    }

    /**
     * 购买金额
     * @return price 购买金额
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 购买金额
     * @param price 购买金额
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * 购买日期
     * @return buydate 购买日期
     */
    public Date getBuydate() {
        return buydate;
    }

    /**
     * 购买日期
     * @param buydate 购买日期
     */
    public void setBuydate(Date buydate) {
        this.buydate = buydate;
    }
}