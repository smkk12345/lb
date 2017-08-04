package com.longbei.appservice.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HomePoster {
    private Integer id;

    private String title;

    private String photos;

    private String contenttype;//关联 内容类型 0 - 龙榜 1 - 教室  2 - 专题   3 - 达人  4 - 商品

    private String isup;//否上线 0 - 未上线 1 - 上线     默认0

    private Date uptime;

    private Date downtime;

    private Date createtime;

    private String cuserid;

    private String href;  //id
    
    private String hreftitle; //内容类型对应的名称

    private String isdel;//0 未删除 1 已经删除

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
     * @return title 
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title 
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 
     * @return photos 
     */
    public String getPhotos() {
        return photos;
    }

    /**
     * 
     * @param photos 
     */
    public void setPhotos(String photos) {
        this.photos = photos == null ? null : photos.trim();
    }

    /**
     * 关联 内容类型 0 - 龙榜 1 - 教室  2 - 专题   3 - 达人  4 - 商品
     * @return contenttype 关联 内容类型 0 - 龙榜 1 - 教室  2 - 专题   3 - 达人  4 - 商品
     */
    public String getContenttype() {
        return contenttype;
    }

    /**
     * 关联 内容类型 0 - 龙榜 1 - 教室  2 - 专题   3 - 达人  4 - 商品
     * @param contenttype 关联 内容类型 0 - 龙榜 1 - 教室  2 - 专题   3 - 达人  4 - 商品
     */
    public void setContenttype(String contenttype) {
        this.contenttype = contenttype == null ? null : contenttype.trim();
    }

    /**
     * 否上线 0 - 未上线 1 - 上线     默认0
     * @return isup 否上线 0 - 未上线 1 - 上线     默认0
     */
    public String getIsup() {
        return isup;
    }

    /**
     * 否上线 0 - 未上线 1 - 上线     默认0
     * @param isup 否上线 0 - 未上线 1 - 上线     默认0
     */
    public void setIsup(String isup) {
        this.isup = isup == null ? null : isup.trim();
    }

    /**
     * 
     * @return uptime 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUptime() {
        return uptime;
    }

    /**
     * 
     * @param uptime 
     */
    public void setUptime(Date uptime) {
        this.uptime = uptime;
    }

    /**
     * 
     * @return downtime 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getDowntime() {
        return downtime;
    }

    /**
     * 
     * @param downtime 
     */
    public void setDowntime(Date downtime) {
        this.downtime = downtime;
    }

    /**
     * 
     * @return createtime 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
     * @return cuserid 
     */
    public String getCuserid() {
        return cuserid;
    }

    /**
     * 
     * @param cuserid 
     */
    public void setCuserid(String cuserid) {
        this.cuserid = cuserid == null ? null : cuserid.trim();
    }

    /**
     * 
     * @return href 
     */
    public String getHref() {
        return href;
    }

    /**
     * 
     * @param href 
     */
    public void setHref(String href) {
        this.href = href == null ? null : href.trim();
    }

    /**
     * 0 未删除 1 已经删除
     * @return isdel 0 未删除 1 已经删除
     */
    public String getIsdel() {
        return isdel;
    }

    /**
     * 0 未删除 1 已经删除
     * @param isdel 0 未删除 1 已经删除
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
    }

	public String getHreftitle() {
		return hreftitle;
	}

	public void setHreftitle(String hreftitle) {
		this.hreftitle = hreftitle;
	}
}