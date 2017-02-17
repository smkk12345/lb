package com.longbei.appservice.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Improve {

    protected Long id;

    protected Long impid;//微进步id

    protected String itype;//类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件

    protected String brief;//说明

    protected String pickey;//图片的key


    protected String filekey;//文件key  视频文件  音频文件 普通文件

    protected Long userid;//用户id


    protected Long businessid;//业务id  榜单id  圈子id 教室id

    protected String businesstype;//微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室

    protected Integer likes = 0;//点赞数量

    protected Integer flowers = 0;//花数量


    protected Integer diamonds = 0;//钻石

    protected Date createtime;//创建时间

    protected Date updatetime;//更新时间

    protected Integer indexnum = 0;//子进步的排序好

    protected String isdel;//假删  0 未删除  1 删除

    protected String ismainimp;//0 普通微进步  1 最新微进步

    protected String ptype;//十全十美id

    protected String ispublic;//可见程度  0 私密 1 好友可见 2 全部可见
    
    protected Integer goalid;//进步组id
    
    protected Integer rankid; //榜id

    protected Integer commentnum = 0; //评论数

    /**
     * 以下扩展返回信息
     * 进步人信息
     * 是否点过赞送过花 送过钻
     * 是否已经收藏
     * 点赞送花送钻用户列表
     * 点赞送花送钻人数
     * 关联的超级话题
     * @return
     */
    protected AppUserMongoEntity appUserMongoEntity; //进步用户信息

    private List<AppUserMongoEntity> appUserList;//点赞送花送钻用户列表 只取前5条

    private Integer lfdcount = 0;//点赞送花送钻 数量

    private String haslike = "0";//是否点赞
    private String hasflower = "0";//是否送花
    private String hasdiamond = "0";//时候送钻
    private String hascollect = "0";//是否收藏

    private SuperTopic superTopic;//超级话题

    public List<AppUserMongoEntity> getAppUserList() {
        return appUserList;
    }

    public void setAppUserList(List<AppUserMongoEntity> appUserList) {
        this.appUserList = appUserList;
    }

    public Integer getLfdcount() {
        return lfdcount;
    }

    public void setLfdcount(Integer lfdcount) {
        this.lfdcount = lfdcount;
    }

    public String getHaslike() {
        return haslike;
    }

    public void setHaslike(String haslike) {
        this.haslike = haslike;
    }

    public String getHasflower() {
        return hasflower;
    }

    public void setHasflower(String hasflower) {
        this.hasflower = hasflower;
    }

    public String getHasdiamond() {
        return hasdiamond;
    }

    public void setHasdiamond(String hasdiamond) {
        this.hasdiamond = hasdiamond;
    }

    public String getHascollect() {
        return hascollect;
    }

    public void setHascollect(String hascollect) {
        this.hascollect = hascollect;
    }

    public SuperTopic getSuperTopic() {
        return superTopic;
    }

    public void setSuperTopic(SuperTopic superTopic) {
        this.superTopic = superTopic;
    }

    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
    }

    @JsonInclude(Include.ALWAYS)
    public Integer getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(Integer commentnum) {
        this.commentnum = commentnum;
    }

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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
    public String getBusinesstype() {
        return businesstype;
    }

    /**
     * 微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     * @param businesstype 微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     */
    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    /**
     * 点赞数量
     * @return likes 点赞数量
     */
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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
    @JsonInclude(Include.ALWAYS)
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

    @JsonInclude(Include.ALWAYS)
	public Integer getGoalid() {
		return goalid;
	}

	public void setGoalid(Integer goalid) {
		this.goalid = goalid;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getRankid() {
		return rankid;
	}

	public void setRankid(Integer rankid) {
		this.rankid = rankid;
	}


    
}