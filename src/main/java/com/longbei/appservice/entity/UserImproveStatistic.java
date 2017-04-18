package com.longbei.appservice.entity;

/**
 * Created by wangyongzhi 17/4/17.
 */
public class UserImproveStatistic {
    private Integer id;
    private Long userid;
    private String currentday;
    private Integer likecount;
    private Integer improvecount;
    private Integer flowercount;

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
