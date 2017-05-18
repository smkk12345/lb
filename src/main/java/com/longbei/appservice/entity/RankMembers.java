package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

public class RankMembers {
    public static final Integer maxHour = 1;//最大多长时间不发进步,则会被其他成员挤走

    private Integer id;

    private Long rankid;//榜单id

    private Long userid;//用户id

    private Date createtime;//入榜时间

    private Date updatetime;//下榜时间

    private Integer sortnum;//榜单内排名 榜单结束之后存入

    private String iswinning;//中奖 0 未中间 1 中奖 2 审核未通过

    private Integer icount;//子进步条数

    private Integer likes;//榜单点赞数

    private Integer flowers;

    private Integer status;//0.待审核 1.同意 已入榜 2.拒绝 已退榜 3.被榜主踢出榜 无法再次申请加入榜

    private String acceptaward;//0 未领奖 1 领奖 2 发货 3签收

    private String isfashionman; //是否为达人  0 - 不是 1 - 是

    private String checkstatus; //审核结果  0 未审核 1 - 机器审核不通过 2 - 人工不通过 3 - 通过

    private String checkresult; //审s核不通过原因

    private Integer complaintotalcount; //投诉次数

    private Integer awardid;//奖品id

    private Integer awardlevel;//奖品等级

    private Date spublishawarddate; //搜索使用

    private Date publishawarddate; //获奖发布时间

    private Date epublishawarddate; //搜索使用

    private Rank rank; //pc用

    private List<Rank> ranks; //查询使用

    private List<Award> awards; //查询使用

    private AppUserMongoEntity appUserMongoEntity;

    private UserInfo userInfo; // pc使用

    private List<AppUserMongoEntity> appUserMongoEntities; //查询使用

    private String checkuserid; //审核人id

    private Date checkdate; //审核日期

    private String receivecode; //领奖口令

    private RankAward rankAward;

    private Award award;

    private Date upfashionmantime; //设置为达人时间

    private Date downfashionmantime; //取消达人时间

    private Integer sort; //达人排序

    private Integer rankfinishlikes = 0;

    private Integer rankfinishflower = 0;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpfashionmantime() {
        return upfashionmantime;
    }

    public void setUpfashionmantime(Date upfashionmantime) {
        this.upfashionmantime = upfashionmantime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getDownfashionmantime() {
        return downfashionmantime;
    }

    public void setDownfashionmantime(Date downfashionmantime) {
        this.downfashionmantime = downfashionmantime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getSpublishawarddate() {
        return spublishawarddate;
    }

    public void setSpublishawarddate(Date spublishawarddate) {
        this.spublishawarddate = spublishawarddate;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getEpublishawarddate() {
        return epublishawarddate;
    }

    public void setEpublishawarddate(Date epublishawarddate) {
        this.epublishawarddate = epublishawarddate;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getPublishawarddate() {
        return publishawarddate;
    }

    public void setPublishawarddate(Date publishawarddate) {
        this.publishawarddate = publishawarddate;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(List<Rank> ranks) {
        this.ranks = ranks;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public String getReceivecode() {
        return receivecode;
    }

    public void setReceivecode(String receivecode) {
        this.receivecode = receivecode;
    }

    public RankAward getRankAward() {
        return rankAward;
    }

    public void setRankAward(RankAward rankAward) {
        this.rankAward = rankAward;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    public String getCheckuserid() {
        return checkuserid;
    }

    public void setCheckuserid(String checkuserid) {
        this.checkuserid = checkuserid;
    }

    public String getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(String checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getCheckresult() {
        return checkresult;
    }

    public void setCheckresult(String checkresult) {
        this.checkresult = checkresult;
    }

    public Integer getComplaintotalcount() {
        return complaintotalcount;
    }

    public void setComplaintotalcount(Integer complaintotalcount) {
        this.complaintotalcount = complaintotalcount;
    }

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
     * 中奖 0 未中间 1 中奖 2 审核未通过
     * @return iswinning 中奖 0 未中间 1 中奖 2 审核未通过
     */
    public String getIswinning() {
        return iswinning;
    }

    /**
     * 中奖 0 未中间 1 中奖 2 审核未通过
     * @param iswinning 中奖 0 未中间 1 中奖 2 审核未通过
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

    /**
     * 获取奖品id
     * @return
     */
    public Integer getAwardid() {
        return awardid;
    }

    /**
     * 设置奖品id
     * @param awardid
     */
    public void setAwardid(Integer awardid) {
        this.awardid = awardid;
    }

    /**
     * 获取奖品等级
     * @return
     */
    public Integer getAwardlevel() {
        return awardlevel;
    }

    /**
     * 设置奖品等级
     * @param awardlevel
     */
    public void setAwardlevel(Integer awardlevel) {
        this.awardlevel = awardlevel;
    }

    public void setRankfinishflower(Integer rankfinishflower) {
        this.rankfinishflower = rankfinishflower;
    }

    public void setRankfinishlikes(Integer rankfinishlikes) {
        this.rankfinishlikes = rankfinishlikes;
    }

    public Integer getRankfinishflower() {
        return rankfinishflower;
    }

    public Integer getRankfinishlikes() {
        return rankfinishlikes;
    }
}