package com.longbei.appservice.entity;

import java.util.Date;

public class Improve {
    private Long id;

    private Long impid;//微进步id

    private String itype;//类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件

    private String brief;//说明

    private String pickey;//图片的key


    private String filekey;//文件key  视频文件  音频文件 普通文件

    private Long userid;//用户id


    private Long businessid;//业务id  榜单id  圈子id 教室id

    private String businesstype;//微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室

    private Integer likes;//点赞数量

    private Integer flowers;//花数量


    private Integer diamonds;//钻石

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private Integer indexnum;//子进步的排序好

    private String isdel;//假删  0 未删除  1 删除

    private String ismainimp;//0 普通微进步  1 最新微进步

    private String ptype;//十全十美id

    private String ispublic;//可见程度  0 私密 1 好友可见 2 全部可见

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 微进步id
     * @return impid 微进步id
     */
    public Long getImpid() {
        return impid;
    }

    /**
     * 微进步id
     * @param impid 微进步id
     */
    public void setImpid(Long impid) {
        this.impid = impid;
    }

    /**
     * 类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
     * @return itype 类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
     */
    public String getItype() {
        return itype;
    }

    /**
     * 类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
     * @param itype 类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
     */
    public void setItype(String itype) {
        this.itype = itype == null ? null : itype.trim();
    }

    /**
     * 说明
     * @return brief 说明
     */
    public String getBrief() {
        return brief;
    }

    /**
     * 说明
     * @param brief 说明
     */
    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }

    /**
     * 图片的key 
     * @return pickey 图片的key 
     */
    public String getPickey() {
        return pickey;
    }

    /**
     * 图片的key 
     * @param pickey 图片的key 
     */
    public void setPickey(String pickey) {
        this.pickey = pickey == null ? null : pickey.trim();
    }

    /**
     * 文件key  视频文件  音频文件 普通文件
     * @return filekey 文件key  视频文件  音频文件 普通文件
     */
    public String getFilekey() {
        return filekey;
    }

    /**
     * 文件key  视频文件  音频文件 普通文件
     * @param filekey 文件key  视频文件  音频文件 普通文件
     */
    public void setFilekey(String filekey) {
        this.filekey = filekey == null ? null : filekey.trim();
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
     * 业务id  榜单id  圈子id 教室id
     * @return businessid 业务id  榜单id  圈子id 教室id
     */
    public Long getBusinessid() {
        return businessid;
    }

    /**
     * 业务id  榜单id  圈子id 教室id
     * @param businessid 业务id  榜单id  圈子id 教室id
     */
    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    /**
     * 微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     * @return businesstype 微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     */
    public String getBusinesstype() {
        return businesstype;
    }

    /**
     * 微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     * @param businesstype 微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     */
    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype == null ? null : businesstype.trim();
    }

    /**
     * 点赞数量
     * @return likes 点赞数量
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * 点赞数量
     * @param likes 点赞数量
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    /**
     * 花数量 
     * @return flowers 花数量 
     */
    public Integer getFlowers() {
        return flowers;
    }

    /**
     * 花数量 
     * @param flowers 花数量 
     */
    public void setFlowers(Integer flowers) {
        this.flowers = flowers;
    }

    /**
     * 钻石
     * @return diamonds 钻石
     */
    public Integer getDiamonds() {
        return diamonds;
    }

    /**
     * 钻石
     * @param diamonds 钻石
     */
    public void setDiamonds(Integer diamonds) {
        this.diamonds = diamonds;
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
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

    /**
     * 子进步的排序好
     * @return indexnum 子进步的排序好
     */
    public Integer getIndexnum() {
        return indexnum;
    }

    /**
     * 子进步的排序好
     * @param indexnum 子进步的排序好
     */
    public void setIndexnum(Integer indexnum) {
        this.indexnum = indexnum;
    }

    /**
     * 假删  0 未删除  1 删除
     * @return isdel 假删  0 未删除  1 删除
     */
    public String getIsdel() {
        return isdel;
    }

    /**
     * 假删  0 未删除  1 删除
     * @param isdel 假删  0 未删除  1 删除
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
    }

    /**
     * 0 普通微进步  1 最新微进步
     * @return ismainimp 0 普通微进步  1 最新微进步
     */
    public String getIsmainimp() {
        return ismainimp;
    }

    /**
     * 0 普通微进步  1 最新微进步
     * @param ismainimp 0 普通微进步  1 最新微进步
     */
    public void setIsmainimp(String ismainimp) {
        this.ismainimp = ismainimp == null ? null : ismainimp.trim();
    }

    /**
     * 十全十美id
     * @return ptype 十全十美id
     */
    public String getPtype() {
        return ptype;
    }

    /**
     * 十全十美id
     * @param ptype 十全十美id
     */
    public void setPtype(String ptype) {
        this.ptype = ptype == null ? null : ptype.trim();
    }

    /**
     * 可见程度  0 私密 1 好友可见 2 全部可见
     * @return ispublic 可见程度  0 私密 1 好友可见 2 全部可见
     */
    public String getIspublic() {
        return ispublic;
    }

    /**
     * 可见程度  0 私密 1 好友可见 2 全部可见
     * @param ispublic 可见程度  0 私密 1 好友可见 2 全部可见
     */
    public void setIspublic(String ispublic) {
        this.ispublic = ispublic == null ? null : ispublic.trim();
    }
}