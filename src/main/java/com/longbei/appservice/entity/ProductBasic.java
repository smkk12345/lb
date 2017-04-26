package com.longbei.appservice.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProductBasic {
    private Integer id;

    private String productname;//商品名字

    private String productbriefphotos;//商品简介图片

    private String productbrief;//商品简介

    private String productdetail;//商品详情

    private Double productprice;//商品价值

    private Long productpoint;//兑换商品所需币

    private String productcate;//商品类别

    private Integer productlevel;//商品等级

    private String productcount;//商品数量

    private String enabled;//商品是否下架    0:已下架    1：未下架
    
    private String detailpic; // 商品详情图片

    private Date uptime;//上架时间

    private Date downtime;//下架时间

    private String createtime;//创建时间

    private String updatetime;//更新时间
    
    private Double discount; // 折扣价
    
    private Integer goodsCount; //购物车提交时带的该商品数量

    private Long lowimpicon; //最低进步币要求


    // -------------------------按条件查询商品列表属性--------------------

    private Long productpoint1; //兑换商品所需币1

    private Integer startNum;  //分页起始值

    private Integer pageSize;  // 每页显示条数
    
    private String productcatetitle;//商品类别名称
    
    private String[] photos; //商品图片
    //-----------------------------------------------------------


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
     * 商品名字
     * @return productname 商品名字
     */
    public String getProductname() {
        return productname;
    }

    /**
     * 商品名字
     * @param productname 商品名字
     */
    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    /**
     * 商品简介图片
     * @return productbriefphotos 商品简介图片
     */
    public String getProductbriefphotos() {
        return productbriefphotos;
    }

    /**
     * 商品简介图片
     * @param productbriefphotos 商品简介图片
     */
    public void setProductbriefphotos(String productbriefphotos) {
        this.productbriefphotos = productbriefphotos == null ? null : productbriefphotos.trim();
    }

    /**
     * 商品简介
     * @return productbrief 商品简介
     */
    public String getProductbrief() {
        return productbrief;
    }

    /**
     * 商品简介
     * @param productbrief 商品简介
     */
    public void setProductbrief(String productbrief) {
        this.productbrief = productbrief == null ? null : productbrief.trim();
    }

    /**
     * 商品详情
     * @return productdetail 商品详情
     */
    public String getProductdetail() {
        return productdetail;
    }

    /**
     * 商品详情
     * @param productdetail 商品详情
     */
    public void setProductdetail(String productdetail) {
        this.productdetail = productdetail == null ? null : productdetail.trim();
    }

    /**
     * 商品价值
     * @return productprice 商品价值
     */
    @JsonInclude(Include.ALWAYS)
    public Double getProductprice() {
        return productprice;
    }

    /**
     * 商品价值
     * @param productprice 商品价值
     */
    public void setProductprice(Double productprice) {
        this.productprice = productprice;
    }

    /**
     * 兑换商品所需币
     * @return productpoint 兑换商品所需币
     */
    @JsonInclude(Include.ALWAYS)
    public Long getProductpoint() {
        return productpoint;
    }

    /**
     * 兑换商品所需币
     * @param productpoint 兑换商品所需币
     */
    public void setProductpoint(Long productpoint) {
        this.productpoint = productpoint;
    }

    /**
     * 商品类别
     * @return productcate 商品类别
     */
    @JsonInclude(Include.ALWAYS)
    public String getProductcate() {
        return productcate;
    }

    /**
     * 商品类别
     * @param productcate 商品类别
     */
    public void setProductcate(String productcate) {
        this.productcate = productcate == null ? null : productcate.trim();
    }

    /**
     * 商品等级
     * @return productlevel 商品等级
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getProductlevel() {
        return productlevel;
    }

    /**
     * 商品等级
     * @param productlevel 商品等级
     */
    public void setProductlevel(Integer productlevel) {
        this.productlevel = productlevel;
    }

    /**
     * 商品数量
     * @return productcount 商品数量
     */
    @JsonInclude(Include.ALWAYS)
    public String getProductcount() {
        return productcount;
    }

    /**
     * 商品数量
     * @param productcount 商品数量
     */
    public void setProductcount(String productcount) {
        this.productcount = productcount == null ? null : productcount.trim();
    }

    /**
     * 是否可用 0—不可用 1—可用
     * @return enable 是否可用 0—不可用 1—可用
     */
    @JsonInclude(Include.ALWAYS)
    public String getEnabled() {
        return enabled;
    }

    /**
     * 是否可用 0—不可用 1—可用
     * @param @param enable 是否可用 0—不可用 1—可用
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
    }

    /**
     * 上架时间
     * @return uptime 上架时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUptime() {
        return uptime;
    }

    /**
     * 上架时间
     * @param uptime 上架时间
     */
    public void setUptime(Date uptime) {
        this.uptime = uptime;
    }

    /**
     * 下架时间
     * @return downtime 下架时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getDowntime() {
        return downtime;
    }

    /**
     * 下架时间
     * @param downtime 下架时间
     */
    public void setDowntime(Date downtime) {
        this.downtime = downtime;
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
    public String getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    /**
     * 更新时间
     * @return updatetime 更新时间
     */
    public String getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }

	public String getDetailpic() {
		return detailpic;
	}

	public void setDetailpic(String detailpic) {
		this.detailpic = detailpic;
	}

	@JsonInclude(Include.ALWAYS)
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

    /**
     * 兑换商品所需币1
     * @return setProductpoint1 兑换商品所需币1
     */
    public Long getProductpoint1() {
        return productpoint1;
    }

    /**
     * 兑换商品所需币1
     * @param @param setProductpoint1 兑换商品所需币1
     */
    public void setProductpoint1(Long productpoint1) {
        this.productpoint1 = productpoint1;
    }

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 最低进步币要求
     * @return   lowimpicon
     */
    public Long getLowimpicon() {  //最低进步币要求
        return lowimpicon;
    }
    /**
     * 最低进步币要求
     * @param @param  lowimpicon
     */
    public void setLowimpicon(Long lowimpicon) {//最低进步币要求
        this.lowimpicon = lowimpicon;
    }

	public String getProductcatetitle() {
		return productcatetitle;
	}

	public void setProductcatetitle(String productcatetitle) {
		this.productcatetitle = productcatetitle;
	}

	public String[] getPhotos() {
		return photos;
	}

	public void setPhotos(String[] photos) {
		this.photos = photos;
	}
}