package com.longbei.appservice.entity;

import java.io.Serializable;

/**
 * Created by wangyongzhi 17/4/17.
 */
public class UserImproveStatistic implements Serializable {
    private Integer id;
    private Long userid;//用户id
    private String currentday;//统计的日期
    private Integer likecount;//赞的数量
    private Integer improvecount;//发表的进步数量
    private Integer flowercount;//送花的数量

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getLikecount() {
        return likecount;
    }

    public void setLikecount(Integer likecount) {
        this.likecount = likecount;
    }

    public Integer getImprovecount() {
        return improvecount;
    }

    public void setImprovecount(Integer improvecount) {
        this.improvecount = improvecount;
    }

    public Integer getFlowercount() {
        return flowercount;
    }

    public void setFlowercount(Integer flowercount) {
        this.flowercount = flowercount;
    }

    public String getCurrentday() {
        return currentday;
    }

    public void setCurrentday(String currentday) {
        this.currentday = currentday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
