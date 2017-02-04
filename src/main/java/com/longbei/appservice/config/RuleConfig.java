package com.longbei.appservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 规则配置(启动时初始化)
 *
 * @author luye
 * @create 2017-02-04 下午1:59
 **/
@Component
public class RuleConfig implements CommandLineRunner {

    public static String peried; //周期
    public static int maximprove;//一天最多发的进步数量
    public static int maxrank;//一天最多参加的榜
    public static int refers;//上榜证明人
    public static int register;//注册＋龙分数
    public static int checkin;//签到龙分数
    public static int idcard;//身份证验证龙分数
    public static int email;//邮箱验证龙分数
    public static int invite;//邀请用户注册成功龙分数
    public static int invitemoneyfirst; //邀请用户注册送进步币-一级
    public static int invitemoneysecond; //邀请用户注册送进步币-二级
    public static int invitemoneythird; //邀请用户注册送进步币-三级
    public static int finish;//进步完成龙分数
    public static int gorank;//进步上榜龙分数
    public static int likeother;//给别人点赞自己加龙分
    public static int limitlike;//点赞限制
    public static int levellike;//每一级增加两个点赞数
    public static int improvelike;//同一个进步每个用户只能点赞一次
    public static int proof;//给别人证明  自己＋5
    public static int donatermb;//捐赠一元钱 自己＋100龙分
    public static int donatejinbu;//捐赠一个进步获取100龙分
    public static int maxrate;//最大中奖概率
    public static int minrate;//最小中奖概率
    public static int rate;//默认中奖概率
    public static int slevel1;//一级商家能发布的榜的人数
    public static int slevel2;//二级商家能发布的榜的人数
    public static int slevel3;//三级商家能发布的榜的人数
    public static int slevel4;//四级商家能发布的榜的人数
    public static int slevel5;//五级商家能发布的榜的人数
    public static double star1;//无优惠
    public static double star2;//95
    public static double star3;//90
    public static double star4;//85
    public static double star5;//8
    public static double factor;//鲜花增值因子
    public static String cycle;//增值周期
    public static int bucket;//花篮默认鲜花数量
    public static int bouquet;//花束默认鲜花数量
    public static int pagesize;//默认每页条数
    public static double nearrankdistance = 3;//周边的榜的距离
    public static int improveinvite;//每天同一个进步组同一个人只能邀请一次
    public static int improvecannotinvite;//证明人到达N个之后就不能主动邀请

    public static int liked;//	被点赞送积分数
    public static int flowered;//被送花积分数

    //各种权重
    public static int likeweight = 1;
    public static int flowerweight = 1;
    public static int commentweight = 1;
    public static int complaintsweight = 1;

    @Override
    public void run(String... strings) throws Exception {
        //初始化操作
    }


    public static String getPeried() {
        return peried;
    }

    public static void setPeried(String peried) {
        RuleConfig.peried = peried;
    }

    public static int getMaximprove() {
        return maximprove;
    }

    public static void setMaximprove(int maximprove) {
        RuleConfig.maximprove = maximprove;
    }

    public static int getMaxrank() {
        return maxrank;
    }

    public static void setMaxrank(int maxrank) {
        RuleConfig.maxrank = maxrank;
    }

    public static int getRefers() {
        return refers;
    }

    public static void setRefers(int refers) {
        RuleConfig.refers = refers;
    }

    public static int getRegister() {
        return register;
    }

    public static void setRegister(int register) {
        RuleConfig.register = register;
    }

    public static int getCheckin() {
        return checkin;
    }

    public static void setCheckin(int checkin) {
        RuleConfig.checkin = checkin;
    }

    public static int getIdcard() {
        return idcard;
    }

    public static void setIdcard(int idcard) {
        RuleConfig.idcard = idcard;
    }

    public static int getEmail() {
        return email;
    }

    public static void setEmail(int email) {
        RuleConfig.email = email;
    }

    public static int getInvite() {
        return invite;
    }

    public static void setInvite(int invite) {
        RuleConfig.invite = invite;
    }

    public static int getInvitemoneyfirst() {
        return invitemoneyfirst;
    }

    public static void setInvitemoneyfirst(int invitemoneyfirst) {
        RuleConfig.invitemoneyfirst = invitemoneyfirst;
    }

    public static int getInvitemoneysecond() {
        return invitemoneysecond;
    }

    public static void setInvitemoneysecond(int invitemoneysecond) {
        RuleConfig.invitemoneysecond = invitemoneysecond;
    }

    public static int getInvitemoneythird() {
        return invitemoneythird;
    }

    public static void setInvitemoneythird(int invitemoneythird) {
        RuleConfig.invitemoneythird = invitemoneythird;
    }

    public static int getFinish() {
        return finish;
    }

    public static void setFinish(int finish) {
        RuleConfig.finish = finish;
    }

    public static int getGorank() {
        return gorank;
    }

    public static void setGorank(int gorank) {
        RuleConfig.gorank = gorank;
    }

    public static int getLikeother() {
        return likeother;
    }

    public static void setLikeother(int likeother) {
        RuleConfig.likeother = likeother;
    }

    public static int getLimitlike() {
        return limitlike;
    }

    public static void setLimitlike(int limitlike) {
        RuleConfig.limitlike = limitlike;
    }

    public static int getLevellike() {
        return levellike;
    }

    public static void setLevellike(int levellike) {
        RuleConfig.levellike = levellike;
    }

    public static int getImprovelike() {
        return improvelike;
    }

    public static void setImprovelike(int improvelike) {
        RuleConfig.improvelike = improvelike;
    }

    public static int getProof() {
        return proof;
    }

    public static void setProof(int proof) {
        RuleConfig.proof = proof;
    }

    public static int getDonatermb() {
        return donatermb;
    }

    public static void setDonatermb(int donatermb) {
        RuleConfig.donatermb = donatermb;
    }

    public static int getDonatejinbu() {
        return donatejinbu;
    }

    public static void setDonatejinbu(int donatejinbu) {
        RuleConfig.donatejinbu = donatejinbu;
    }

    public static int getMaxrate() {
        return maxrate;
    }

    public static void setMaxrate(int maxrate) {
        RuleConfig.maxrate = maxrate;
    }

    public static int getMinrate() {
        return minrate;
    }

    public static void setMinrate(int minrate) {
        RuleConfig.minrate = minrate;
    }

    public static int getRate() {
        return rate;
    }

    public static void setRate(int rate) {
        RuleConfig.rate = rate;
    }

    public static int getSlevel1() {
        return slevel1;
    }

    public static void setSlevel1(int slevel1) {
        RuleConfig.slevel1 = slevel1;
    }

    public static int getSlevel2() {
        return slevel2;
    }

    public static void setSlevel2(int slevel2) {
        RuleConfig.slevel2 = slevel2;
    }

    public static int getSlevel3() {
        return slevel3;
    }

    public static void setSlevel3(int slevel3) {
        RuleConfig.slevel3 = slevel3;
    }

    public static int getSlevel4() {
        return slevel4;
    }

    public static void setSlevel4(int slevel4) {
        RuleConfig.slevel4 = slevel4;
    }

    public static int getSlevel5() {
        return slevel5;
    }

    public static void setSlevel5(int slevel5) {
        RuleConfig.slevel5 = slevel5;
    }

    public static double getStar1() {
        return star1;
    }

    public static void setStar1(double star1) {
        RuleConfig.star1 = star1;
    }

    public static double getStar2() {
        return star2;
    }

    public static void setStar2(double star2) {
        RuleConfig.star2 = star2;
    }

    public static double getStar3() {
        return star3;
    }

    public static void setStar3(double star3) {
        RuleConfig.star3 = star3;
    }

    public static double getStar4() {
        return star4;
    }

    public static void setStar4(double star4) {
        RuleConfig.star4 = star4;
    }

    public static double getStar5() {
        return star5;
    }

    public static void setStar5(double star5) {
        RuleConfig.star5 = star5;
    }

    public static double getFactor() {
        return factor;
    }

    public static void setFactor(double factor) {
        RuleConfig.factor = factor;
    }

    public static String getCycle() {
        return cycle;
    }

    public static void setCycle(String cycle) {
        RuleConfig.cycle = cycle;
    }

    public static int getBucket() {
        return bucket;
    }

    public static void setBucket(int bucket) {
        RuleConfig.bucket = bucket;
    }

    public static int getBouquet() {
        return bouquet;
    }

    public static void setBouquet(int bouquet) {
        RuleConfig.bouquet = bouquet;
    }

    public static int getPagesize() {
        return pagesize;
    }

    public static void setPagesize(int pagesize) {
        RuleConfig.pagesize = pagesize;
    }

    public static double getNearrankdistance() {
        return nearrankdistance;
    }

    public static void setNearrankdistance(double nearrankdistance) {
        RuleConfig.nearrankdistance = nearrankdistance;
    }

    public static int getImproveinvite() {
        return improveinvite;
    }

    public static void setImproveinvite(int improveinvite) {
        RuleConfig.improveinvite = improveinvite;
    }

    public static int getImprovecannotinvite() {
        return improvecannotinvite;
    }

    public static void setImprovecannotinvite(int improvecannotinvite) {
        RuleConfig.improvecannotinvite = improvecannotinvite;
    }

    public static int getLiked() {
        return liked;
    }

    public static void setLiked(int liked) {
        RuleConfig.liked = liked;
    }

    public static int getFlowered() {
        return flowered;
    }

    public static void setFlowered(int flowered) {
        RuleConfig.flowered = flowered;
    }

    public static int getLikeweight() {
        return likeweight;
    }

    public static void setLikeweight(int likeweight) {
        RuleConfig.likeweight = likeweight;
    }

    public static int getFlowerweight() {
        return flowerweight;
    }

    public static void setFlowerweight(int flowerweight) {
        RuleConfig.flowerweight = flowerweight;
    }

    public static int getCommentweight() {
        return commentweight;
    }

    public static void setCommentweight(int commentweight) {
        RuleConfig.commentweight = commentweight;
    }

    public static int getComplaintsweight() {
        return complaintsweight;
    }

    public static void setComplaintsweight(int complaintsweight) {
        RuleConfig.complaintsweight = complaintsweight;
    }
}
