package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

public class RankMembers {
    private Integer id;

    private Long rankid;//榜单id

    private Long userid;//用户id

    private Date createtime;//入榜时间

    private Date updatetime;//下榜时间

    private Integer sortnum;//榜单内排名 榜单结束之后存入

    private String iswinning;//中奖 0 未中间 1 中奖

    private Integer icount;//子进步条数

    private Integer likes;//榜单点赞数

    private Integer flowers;

    private Integer status;//0.待审核 1.同意 已入榜 2.拒绝 已退榜

    private String acceptaward;//0 未领奖 1 领奖 2 发货 3签收

    private String isfashionman; //是否为达人  0 - 不是 1 - 是

    private AppUserMongoEntity appUserMongoEntity;

    private List<AppUserMongoEntity> appUserMongoEntities; //查询使用

    public List<AppUserMongoEntity> getAppUserMongoEntities() {
        return appUserMongoEntities;
    }

    public void setAppUserMongoEntities(List<AppUserMongoEntity> appUserMongoEntities) {
        this.appUserMongoEntities = appUserMongoEntities;
    }

    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
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
     * 榜单id
     * @return rankid 榜单id
     */
    public Long getRankid() {
        return rankid;
    }

    /**
     * 榜单id
     * @param rankid 榜单id
     */
    public void setRankid(Long rankid) {
        this.rankid = rankid;
    }

    /**
     * 用户id
     * @return userid 用户id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 入榜时间
     * @return createtime 入榜时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 入榜时间
     * @param createtime 入榜时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 下榜时间
     * @return updatetime 下榜时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 下榜时间
     * @param updatetime 下榜时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 榜单内排名 榜单结束之后存入
     * @return sortnum 榜单内排名 榜单结束之后存入
     */
    public Integer getSortnum() {
        return sortnum;
    }

    /**
     * 榜单内排名 榜单结束之后存入
     * @param sortnum 榜单内排名 榜单结束之后存入
     */
    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    /**
     * 中奖 0 未中间 1 中奖
     * @return iswinning 中奖 0 未中间 1 中奖
     */
    public String getIswinning() {
        return iswinning;
    }

    /**
     * 中奖 0 未中间 1 中奖
     * @param iswinning 中奖 0 未中间 1 中奖
     */
    public void setIswinning(String iswinning) {
        this.iswinning = iswinning == null ? null : iswinning.trim();
    }

    /**
     * 子进步条数
     * @return icount 子进步条数
     */
    public Integer getIcount() {
        return icount;
    }

    /**
     * 子进步条数
     * @param icount 子进步条数
     */
    public void setIcount(Integer icount) {
        this.icount = icount;
    }

    /**
     * 榜单点赞数
     * @return likes 榜单点赞数
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * 榜单点赞数
     * @param likes 榜单点赞数
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    /**
     * 
     * @return flowers 
     */
    public Integer getFlowers() {
        return flowers;
    }

    /**
     * 
     * @param flowers 
     */
    public void setFlowers(Integer flowers) {
        this.flowers = flowers;
    }

    /**
     * 0 未领奖 1 领奖 2 发货 3签收
     * @return acceptaward 0 未领奖 1 领奖 2 发货 3签收
     */
    public String getAcceptaward() {
        return acceptaward;
    }

    /**
     * 0 未领奖 1 领奖 2 发货 3签收
     * @param acceptaward 0 未领奖 1 领奖 2 发货 3签收
     */
    public void setAcceptaward(String acceptaward) {
        this.acceptaward = acceptaward == null ? null : acceptaward.trim();
    }

    /**
     * 
     * @return isfashionman 
     */
    public String getIsfashionman() {
        return isfashionman;
    }

    /**
     * 
     * @param isfashionman 
     */
    public void setIsfashionman(String isfashionman) {
        this.isfashionman = isfashionman == null ? null : isfashionman.trim();
    }

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };




    public static String test(){
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString().toUpperCase();
    }

    /**
     * 获取入榜状态
     * @return
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 获取入榜状态
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}