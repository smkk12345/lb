package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProductCart implements Serializable {
    private Integer id;//自增id

    private String userid;//用户id

    private String productid;//商品id

    private Integer productcount;//商品数量

    private String enabled;//商品状态  是否下架 0 下架 1 未下架

    private Date createtime;//创建时间

    private Date updatetime;//更新时间
    
    private ProductBasic productBasic; // 商品

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
     * 商品id
     * @return productid 商品id
     */
    @JsonInclude(Include.ALWAYS)
    public String getProductid() {
        return productid;
    }

    /**
     * 商品id
     * @param productid 商品id
     */
    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    /**
     * 商品数量
     * @return productcount 商品数量
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getProductcount() {
        return productcount;
    }

    /**
     * 商品数量
     * @param productcount 商品数量
     */
    public void setProductcount(Integer productcount) {
        this.productcount = productcount;
    }

    /**
     * 商品状态
     * @return enabled 商品状态
     */
    @JsonInclude(Include.ALWAYS)
    public String getEnabled() {
        return enabled;
    }

    /**
     * 商品状态
     * @param enabled 商品状态
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
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
     * @return updatetime 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

	public ProductBasic getProductBasic() {
		return productBasic;
	}

	public void setProductBasic(ProductBasic productBasic) {
		this.productBasic = productBasic;
	}
}