package com.longbei.appservice.entity;

public class BehaviorRule {
    /**
     *
     */
    private Long id;

    /**
     * 注册奖励N个龙分；仅一次
     */
    private Integer registergoals;

    /**
     * 绑定QQ奖励N个龙分；仅一次
     */
    private Integer qqbindgoals;

    /**
     * 绑定微博奖励N个龙分；仅一次
     */
    private Integer webbindgoals;

    /**
     * 绑定微信奖励N个龙分；仅一次
     */
    private Integer wechatbindgoals;

    /**
     * 实名认证奖励N个龙分；仅一次
     */
    private Integer certificationgoals;

    /**
     * 完善个人信息，每一项奖励N个龙分；昵称、性别、所在地、简介、生日、星座、血型、感情状况、工作、学历、兴趣标签 共11项，每项均奖励一次，删除不扣分
     */
    private Integer infocompletegoals;

    /**
     * 用户签到奖励N个龙分
     */
    private Integer signgoals;

    /**
     * 用户首次签到得N个进步币
     */
    private Integer firstsignimpcoins;

    /**
     * 用户首次签到得N个进步币，每多连续签到1天，每次签到多得X个进步币 其中，N+X最大为7
     */
    private Integer continuesignimpcoinsmore;

    /**
     * 分享（站内）可获得N个龙分，N不超过10/天
     */
    private Integer innersharegoals;

    /**
     * 分享（站外）可获得N个龙分，N不超过10/天
     */
    private Integer outsharegoals;

    /**
     * 分享（站外）可获得X个进步币；X范围5至10，随机；奖励前5次
     */
    private Integer outshareimpcoins;

    /**
     * 邀请好友注册成功可得N个龙分；N不超过1000/天
     */
    private Integer friendregistergoals;

    /**
     * 邀请好友注册成功可得X个进步币；X不超过2000/天
     */
    private Integer friendregisterimpcoins;

    /**
     * 添加好友可得N个龙分；N不超过20/天
     */
    private Integer addfriendgoals;

    /**
     * 添加好友可得X个进步币；
     */
    private Integer addfriendimpcoins;

    /**
     * 每人，每抽1次公益抽奖，可获得N个龙分；无上限
     */
    private Integer lotterygoals;

    /**
     * 单个用户每捐赠1个龙币，可获得X个龙分
     */
    private Integer coindonategoals;

    /**
     * 单个用户每捐赠1个进步币，可获得X个龙分；可为0.1等小数
     */
    private Double impcoindonategoals;

    /**
     * 单个用户对单条微进步，最多可献N朵花
     */
    private Integer eachimproveflowersmax;

    /**
     * 给他人点赞，可得N个龙分；不超过30/天
     */
    private Integer likegoals;

    /**
     * 给他人微进步评论和回复他人的评论，可得N个龙分；不超过40/天
     */
    private Integer impcommentgoals;

    /**
     * 给他人送1朵花，可得N个龙分；不超过100/天
     */
    private Double flowergoals;

    /**
     * 单个用户每发1条微进步，可得N个龙分，不超过50/天
     */
    private Integer improvegoals;

    /**
     * 单个用户每发1条微进步，可得X个进步币；3-10随机，奖励前10条
     */
    private Integer improveimpcoins;

    /**
     * 单个用户在单个龙榜中，每天最多发微进步数；榜主可自定义为X，X不超过系统值初始值
     */
    private Integer rankimprovesdaymax;

    /**
     * 单个用户在单个龙榜中，总微进步数的最低值；榜主可自定义为X，X不超过系统值初始值
     */
    private Integer rankimprovestotalmin;

    /**
     * 单个用户在单个龙榜中，总微进步数的最大值； -1为不限制，榜主可自定义为X
     */
    private Integer rankimprovestotalmax;

    /**
     * 用户所建龙榜的天数，最低不得少于N天；-1为不限制
     */
    private Integer rankdaysmin;

    /**
     * 用户所建龙榜的天数，最高不得多于X天；
     */
    private Integer rankdaysmax;

    /**
     * 用户建“公开龙榜”，最低奖品价值总额；-1为不限制;初始值为1000龙币
     */
    private Integer publicrankpricetotalmin;

    /**
     * 用户建“定制私密龙榜”，最低奖品价值总额；-1为不限制
     */
    private Integer privaterankpricetotalmin;

    /**
     * 用户参加龙榜，可得N个龙分；1次／天；退出不扣分
     */
    private Integer joinrankgoals;

    /**
     * 用户参加圈子，可得N个龙分；1次／天；退出不扣分
     */
    private Integer joincirclegoals;

    /**
     * 用户参加教室，可得N个龙分；1次／天；退出不扣分
     */
    private Integer joinclassgoals;

    /**
     * 分享（站内）可获得N个龙分；上限值
     */
    private Integer innersharegoalsmax;

    /**
     * 用户签到奖励N个龙分,X个进步币；N+X的上限
     */
    private Integer signimpcoinsmax;

    /**
     * 分享（站外）可获得N个龙分，上限值
     */
    private Integer outsharegoalsmax;

    /**
     * 分享（站外）可获得X个进步币，随机最小值
     */
    private Integer outshareimpcoinsmin;

    /**
     * 分享（站外）可获得X个进步币，随机最大值
     */
    private Integer outshareimpcoinsmax;

    /**
     * 分享（站外）奖励次数，上限值
     */
    private Integer outshareawardtimes;

    /**
     * 邀请好友注册成功可得N个龙分，上限值
     */
    private Integer friendregistergoalsmax;

    /**
     * 邀请好友注册成功可得X个进步币，上限值
     */
    private Integer friendregisterimpcoinsmax;

    /**
     * 添加好友可得N个龙分，上限值
     */
    private Integer addfriendgoalsmax;

    /**
     * 添加好友可得X个进步币，上限值
     */
    private Integer addfriendimpcoinsmax;

    /**
     * 给他人点赞，可得N个龙分，上限值
     */
    private Integer likegoalsmax;

    /**
     * 给他人微进步评论和回复他人的评论，可得N个龙分，上限值
     */
    private Integer impcommentgoalsmax;

    /**
     * 给他人送1朵花，可得N个龙分，上限值
     */
    private Integer flowergoalsmax;

    /**
     * 单个用户每发1条微进步，可得N个龙分，上限值
     */
    private Integer improvegoalsmax;

    /**
     * 单个用户每发1条微进步，可得X个进步币，随机最小值
     */
    private Integer improveimpcoinsmin;

    /**
     * 单个用户每发1条微进步，可得X个进步币，随机最大值
     */
    private Integer improveimpcoinsmax;

    /**
     * 单个用户每发1条微进步，可得进步币次数，上限值
     */
    private Integer improveimpcoinstimes;

    /**
     * 用户参加龙榜，可得龙分次数，上限值
     */
    private Integer joinrankgoalstimes;

    /**
     * 用户参加圈子，可得龙分次数，上限值
     */
    private Integer joincirclegoalstimes;

    /**
     * 用户参加教室，可得龙分次数，上限值
     */
    private Integer aoinclassgoalstimes;

    private Integer changedeveicelimitperday = 3;//同一设备每天切换帐号数量

    private Integer registerdevicelimit = 5;//同一设备注册帐号数

    private Integer shareMaxCount;//单次分享的最大个数限制

    private Integer shareGroupCount = 1;//单次内部分享到群限制

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRegistergoals() {
        return registergoals;
    }

    public void setRegistergoals(Integer registergoals) {
        this.registergoals = registergoals;
    }

    public Integer getQqbindgoals() {
        return qqbindgoals;
    }

    public void setQqbindgoals(Integer qqbindgoals) {
        this.qqbindgoals = qqbindgoals;
    }

    public Integer getWebbindgoals() {
        return webbindgoals;
    }

    public void setWebbindgoals(Integer webbindgoals) {
        this.webbindgoals = webbindgoals;
    }

    public Integer getWechatbindgoals() {
        return wechatbindgoals;
    }

    public void setWechatbindgoals(Integer wechatbindgoals) {
        this.wechatbindgoals = wechatbindgoals;
    }

    public Integer getCertificationgoals() {
        return certificationgoals;
    }

    public void setCertificationgoals(Integer certificationgoals) {
        this.certificationgoals = certificationgoals;
    }

    public Integer getInfocompletegoals() {
        return infocompletegoals;
    }

    public void setInfocompletegoals(Integer infocompletegoals) {
        this.infocompletegoals = infocompletegoals;
    }

    public Integer getSigngoals() {
        return signgoals;
    }

    public void setSigngoals(Integer signgoals) {
        this.signgoals = signgoals;
    }

    public Integer getFirstsignimpcoins() {
        return firstsignimpcoins;
    }

    public void setFirstsignimpcoins(Integer firstsignimpcoins) {
        this.firstsignimpcoins = firstsignimpcoins;
    }

    public Integer getContinuesignimpcoinsmore() {
        return continuesignimpcoinsmore;
    }

    public void setContinuesignimpcoinsmore(Integer continuesignimpcoinsmore) {
        this.continuesignimpcoinsmore = continuesignimpcoinsmore;
    }

    public Integer getInnersharegoals() {
        return innersharegoals;
    }

    public void setInnersharegoals(Integer innersharegoals) {
        this.innersharegoals = innersharegoals;
    }

    public Integer getOutsharegoals() {
        return outsharegoals;
    }

    public void setOutsharegoals(Integer outsharegoals) {
        this.outsharegoals = outsharegoals;
    }

    public Integer getOutshareimpcoins() {
        return outshareimpcoins;
    }

    public void setOutshareimpcoins(Integer outshareimpcoins) {
        this.outshareimpcoins = outshareimpcoins;
    }

    public Integer getFriendregistergoals() {
        return friendregistergoals;
    }

    public void setFriendregistergoals(Integer friendregistergoals) {
        this.friendregistergoals = friendregistergoals;
    }

    public Integer getFriendregisterimpcoins() {
        return friendregisterimpcoins;
    }

    public void setFriendregisterimpcoins(Integer friendregisterimpcoins) {
        this.friendregisterimpcoins = friendregisterimpcoins;
    }

    public Integer getAddfriendgoals() {
        return addfriendgoals;
    }

    public void setAddfriendgoals(Integer addfriendgoals) {
        this.addfriendgoals = addfriendgoals;
    }

    public Integer getAddfriendimpcoins() {
        return addfriendimpcoins;
    }

    public void setAddfriendimpcoins(Integer addfriendimpcoins) {
        this.addfriendimpcoins = addfriendimpcoins;
    }

    public Integer getLotterygoals() {
        return lotterygoals;
    }

    public void setLotterygoals(Integer lotterygoals) {
        this.lotterygoals = lotterygoals;
    }

    public Integer getCoindonategoals() {
        return coindonategoals;
    }

    public void setCoindonategoals(Integer coindonategoals) {
        this.coindonategoals = coindonategoals;
    }

    public Double getImpcoindonategoals() {
        return impcoindonategoals;
    }

    public void setImpcoindonategoals(Double impcoindonategoals) {
        this.impcoindonategoals = impcoindonategoals;
    }

    public Integer getEachimproveflowersmax() {
        return eachimproveflowersmax;
    }

    public void setEachimproveflowersmax(Integer eachimproveflowersmax) {
        this.eachimproveflowersmax = eachimproveflowersmax;
    }

    public Integer getLikegoals() {
        return likegoals;
    }

    public void setLikegoals(Integer likegoals) {
        this.likegoals = likegoals;
    }

    public Integer getImpcommentgoals() {
        return impcommentgoals;
    }

    public void setImpcommentgoals(Integer impcommentgoals) {
        this.impcommentgoals = impcommentgoals;
    }

    public Double getFlowergoals() {
        return flowergoals;
    }

    public void setFlowergoals(Double flowergoals) {
        this.flowergoals = flowergoals;
    }

    public Integer getImprovegoals() {
        return improvegoals;
    }

    public void setImprovegoals(Integer improvegoals) {
        this.improvegoals = improvegoals;
    }

    public Integer getImproveimpcoins() {
        return improveimpcoins;
    }

    public void setImproveimpcoins(Integer improveimpcoins) {
        this.improveimpcoins = improveimpcoins;
    }

    public Integer getRankimprovesdaymax() {
        return rankimprovesdaymax;
    }

    public void setRankimprovesdaymax(Integer rankimprovesdaymax) {
        this.rankimprovesdaymax = rankimprovesdaymax;
    }

    public Integer getRankimprovestotalmin() {
        return rankimprovestotalmin;
    }

    public void setRankimprovestotalmin(Integer rankimprovestotalmin) {
        this.rankimprovestotalmin = rankimprovestotalmin;
    }

    public Integer getRankimprovestotalmax() {
        return rankimprovestotalmax;
    }

    public void setRankimprovestotalmax(Integer rankimprovestotalmax) {
        this.rankimprovestotalmax = rankimprovestotalmax;
    }

    public Integer getRankdaysmin() {
        return rankdaysmin;
    }

    public void setRankdaysmin(Integer rankdaysmin) {
        this.rankdaysmin = rankdaysmin;
    }

    public Integer getRankdaysmax() {
        return rankdaysmax;
    }

    public void setRankdaysmax(Integer rankdaysmax) {
        this.rankdaysmax = rankdaysmax;
    }

    public Integer getPublicrankpricetotalmin() {
        return publicrankpricetotalmin;
    }

    public void setPublicrankpricetotalmin(Integer publicrankpricetotalmin) {
        this.publicrankpricetotalmin = publicrankpricetotalmin;
    }

    public Integer getPrivaterankpricetotalmin() {
        return privaterankpricetotalmin;
    }

    public void setPrivaterankpricetotalmin(Integer privaterankpricetotalmin) {
        this.privaterankpricetotalmin = privaterankpricetotalmin;
    }

    public Integer getJoinrankgoals() {
        return joinrankgoals;
    }

    public void setJoinrankgoals(Integer joinrankgoals) {
        this.joinrankgoals = joinrankgoals;
    }

    public Integer getJoincirclegoals() {
        return joincirclegoals;
    }

    public void setJoincirclegoals(Integer joincirclegoals) {
        this.joincirclegoals = joincirclegoals;
    }

    public Integer getJoinclassgoals() {
        return joinclassgoals;
    }

    public void setJoinclassgoals(Integer joinclassgoals) {
        this.joinclassgoals = joinclassgoals;
    }

    public Integer getInnersharegoalsmax() {
        return innersharegoalsmax;
    }

    public void setInnersharegoalsmax(Integer innersharegoalsmax) {
        this.innersharegoalsmax = innersharegoalsmax;
    }

    public Integer getSignimpcoinsmax() {
        return signimpcoinsmax;
    }

    public void setSignimpcoinsmax(Integer signimpcoinsmax) {
        this.signimpcoinsmax = signimpcoinsmax;
    }

    public Integer getOutsharegoalsmax() {
        return outsharegoalsmax;
    }

    public void setOutsharegoalsmax(Integer outsharegoalsmax) {
        this.outsharegoalsmax = outsharegoalsmax;
    }

    public Integer getOutshareimpcoinsmin() {
        return outshareimpcoinsmin;
    }

    public void setOutshareimpcoinsmin(Integer outshareimpcoinsmin) {
        this.outshareimpcoinsmin = outshareimpcoinsmin;
    }

    public Integer getOutshareimpcoinsmax() {
        return outshareimpcoinsmax;
    }

    public void setOutshareimpcoinsmax(Integer outshareimpcoinsmax) {
        this.outshareimpcoinsmax = outshareimpcoinsmax;
    }

    public Integer getOutshareawardtimes() {
        return outshareawardtimes;
    }

    public void setOutshareawardtimes(Integer outshareawardtimes) {
        this.outshareawardtimes = outshareawardtimes;
    }

    public Integer getFriendregistergoalsmax() {
        return friendregistergoalsmax;
    }

    public void setFriendregistergoalsmax(Integer friendregistergoalsmax) {
        this.friendregistergoalsmax = friendregistergoalsmax;
    }

    public Integer getFriendregisterimpcoinsmax() {
        return friendregisterimpcoinsmax;
    }

    public void setFriendregisterimpcoinsmax(Integer friendregisterimpcoinsmax) {
        this.friendregisterimpcoinsmax = friendregisterimpcoinsmax;
    }

    public Integer getAddfriendgoalsmax() {
        return addfriendgoalsmax;
    }

    public void setAddfriendgoalsmax(Integer addfriendgoalsmax) {
        this.addfriendgoalsmax = addfriendgoalsmax;
    }

    public Integer getAddfriendimpcoinsmax() {
        return addfriendimpcoinsmax;
    }

    public void setAddfriendimpcoinsmax(Integer addfriendimpcoinsmax) {
        this.addfriendimpcoinsmax = addfriendimpcoinsmax;
    }

    public Integer getLikegoalsmax() {
        return likegoalsmax;
    }

    public void setLikegoalsmax(Integer likegoalsmax) {
        this.likegoalsmax = likegoalsmax;
    }

    public Integer getImpcommentgoalsmax() {
        return impcommentgoalsmax;
    }

    public void setImpcommentgoalsmax(Integer impcommentgoalsmax) {
        this.impcommentgoalsmax = impcommentgoalsmax;
    }

    public Integer getFlowergoalsmax() {
        return flowergoalsmax;
    }

    public void setFlowergoalsmax(Integer flowergoalsmax) {
        this.flowergoalsmax = flowergoalsmax;
    }

    public Integer getImprovegoalsmax() {
        return improvegoalsmax;
    }

    public void setImprovegoalsmax(Integer improvegoalsmax) {
        this.improvegoalsmax = improvegoalsmax;
    }

    public Integer getImproveimpcoinsmin() {
        return improveimpcoinsmin;
    }

    public void setImproveimpcoinsmin(Integer improveimpcoinsmin) {
        this.improveimpcoinsmin = improveimpcoinsmin;
    }

    public Integer getImproveimpcoinsmax() {
        return improveimpcoinsmax;
    }

    public void setImproveimpcoinsmax(Integer improveimpcoinsmax) {
        this.improveimpcoinsmax = improveimpcoinsmax;
    }

    public Integer getImproveimpcoinstimes() {
        return improveimpcoinstimes;
    }

    public void setImproveimpcoinstimes(Integer improveimpcoinstimes) {
        this.improveimpcoinstimes = improveimpcoinstimes;
    }

    public Integer getJoinrankgoalstimes() {
        return joinrankgoalstimes;
    }

    public void setJoinrankgoalstimes(Integer joinrankgoalstimes) {
        this.joinrankgoalstimes = joinrankgoalstimes;
    }

    public Integer getJoincirclegoalstimes() {
        return joincirclegoalstimes;
    }

    public void setJoincirclegoalstimes(Integer joincirclegoalstimes) {
        this.joincirclegoalstimes = joincirclegoalstimes;
    }

    public Integer getAoinclassgoalstimes() {
        return aoinclassgoalstimes;
    }

    public void setAoinclassgoalstimes(Integer aoinclassgoalstimes) {
        this.aoinclassgoalstimes = aoinclassgoalstimes;
    }

    public void setChangedeveicelimitperday(Integer changedeveicelimitperday) {
        this.changedeveicelimitperday = changedeveicelimitperday;
    }

    public void setRegisterdevicelimit(Integer registerdevicelimit) {
        this.registerdevicelimit = registerdevicelimit;
    }

    public Integer getChangedeveicelimitperday() {
        return changedeveicelimitperday;
    }

    public Integer getRegisterdevicelimit() {
        return registerdevicelimit;
    }

    public Integer getShareMaxCount() {
        return shareMaxCount;
    }

    public void setShareMaxCount(Integer shareMaxCount) {
        this.shareMaxCount = shareMaxCount;
    }
}