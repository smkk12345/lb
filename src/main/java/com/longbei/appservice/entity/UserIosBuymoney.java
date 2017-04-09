package com.longbei.appservice.entity;

public class UserIosBuymoney {
    private Integer id;

    private Integer money;//买花含的进步币数量

    private Integer appmoney;//购买赠送的进步币数量

    private String productid;//ios前端用的一个id

    private String enabled;//是否可以购买 1是可以购买

    private Integer price;//购买金额

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
}