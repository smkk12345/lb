package com.longbei.appservice.common.constant;

import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.entity.SysRules;
import org.apache.commons.collections.map.FixedSizeMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by smkk on 17/2/24.
 * 龙分  每日上线
 */
public class Constant_point {


    public static void init(){
        NEW_REGISTER = SysRulesCache.behaviorRule.getRegistergoals();//用户注册
        NEW_LOGIN_QQ = SysRulesCache.behaviorRule.getQqbindgoals();//绑定qq
        NEW_LOGIN_WX = SysRulesCache.behaviorRule.getWechatbindgoals();//绑定微信
        NEW_LOGIN_WB = SysRulesCache.behaviorRule.getWebbindgoals();//绑定微博
        NEW_BINDING_EMAIL = 10;//绑定邮箱
        NEW_CERTIFY_USERCARD = SysRulesCache.behaviorRule.getCertificationgoals();//身份证认证
        NEW_CERTIFY_ACADEMIC = 30;//学历认证 academic
        NEW_CERTIFY_JOB = 30;//用作认证
        NEW_USERINFO = SysRulesCache.behaviorRule.getInfocompletegoals();//更新用户信息
        DAILY_CHECKIN = SysRulesCache.behaviorRule.getLikegoals();//点赞
        DAILY_SHARE= SysRulesCache.behaviorRule.getInfocompletegoals();//每日分享
        DAILY_SHARE_OUT= SysRulesCache.behaviorRule.getOutsharegoals();//每日分享(站外)
        DAILY_SHARE_LIMIT = SysRulesCache.behaviorRule.getInnersharegoalsmax();//每日分享上限限制
        DAILY_SHARE_OUT_LIMIT = SysRulesCache.behaviorRule.getOutshareawardtimes();//每日分享(站外)
        DAILY_ADDFRIEND = SysRulesCache.behaviorRule.getAddfriendgoals();//每日添加好友
        DAILY_ADDFRIEND_LIMIT = SysRulesCache.behaviorRule.getAddfriendgoalsmax();//每日添加好友限制
        DAILY_LIKE = SysRulesCache.behaviorRule.getLikegoals();//点赞
        DAILY_LIKE_LIMIT = SysRulesCache.behaviorRule.getLikegoalsmax();//点赞限制
        DAILY_COMMENT = SysRulesCache.behaviorRule.getImpcommentgoals();//评论
        DAILY_COMMENT_LIMIT = SysRulesCache.behaviorRule.getImpcommentgoalsmax();//评论限制
        DAILY_FLOWER = SysRulesCache.behaviorRule.getFlowergoals();//送花
        DAILY_FLOWER_LIMIT = SysRulesCache.behaviorRule.getFlowergoalsmax();//送花限制
        DAILY_IMPFLOWER_LIMIT = SysRulesCache.behaviorRule.getEachimproveflowersmax();
        LOTTERY = SysRulesCache.behaviorRule.getLotterygoals();//lottery 抽奖
        INVITE_LEVEL1 = SysRulesCache.behaviorRule.getFriendregistergoals();//邀请好友
        INVITE_LEVEL1_LIMIT = SysRulesCache.behaviorRule.getFriendregistergoalsmax();//邀请好友限制
        DAILY_ADDRANK = SysRulesCache.behaviorRule.getJoinrankgoals();//加榜
        DAILY_ADDRANK_LIMIT = SysRulesCache.behaviorRule.getJoinrankgoalstimes()*DAILY_ADDRANK;//加榜限制
        fieldsMap = null;
    }

    /**
     * 新手任务
     */
    public static int NEW_REGISTER = SysRulesCache.behaviorRule.getRegistergoals();//用户注册
    public static int NEW_LOGIN_QQ = SysRulesCache.behaviorRule.getQqbindgoals();//绑定qq
    public static int NEW_LOGIN_WX = SysRulesCache.behaviorRule.getWechatbindgoals();//绑定微信
    public static int NEW_LOGIN_WB = SysRulesCache.behaviorRule.getWebbindgoals();//绑定微博
    public static int NEW_BINDING_EMAIL = 10;//绑定邮箱
    public static int NEW_CERTIFY_USERCARD = SysRulesCache.behaviorRule.getCertificationgoals();//身份证认证
    public static int NEW_CERTIFY_ACADEMIC = 30;//学历认证 academic
    public static int NEW_CERTIFY_JOB = 30;//用作认证
    public static int NEW_USERINFO = SysRulesCache.behaviorRule.getInfocompletegoals();//更新用户信息

    /**
     * 日常行为
     */
    public static int DAILY_CHECKIN = SysRulesCache.behaviorRule.getLikegoals();//点赞
    public static int DAILY_SHARE= SysRulesCache.behaviorRule.getInfocompletegoals();//每日分享
    public static int DAILY_SHARE_OUT= SysRulesCache.behaviorRule.getOutsharegoals();//每日分享(站外)
    public static int DAILY_SHARE_LIMIT = SysRulesCache.behaviorRule.getInnersharegoalsmax();//每日分享上限限制
    public static int DAILY_SHARE_OUT_LIMIT = SysRulesCache.behaviorRule.getOutshareawardtimes();//每日分享(站外)
    public static int DAILY_ADDFRIEND = SysRulesCache.behaviorRule.getAddfriendgoals();//每日添加好友
    public static int DAILY_ADDFRIEND_LIMIT = SysRulesCache.behaviorRule.getAddfriendgoalsmax();//每日添加好友限制
//    public static int DAILY_FUN = SysRulesCache.behaviorRule.;//每日关注
//    public static int DAILY_FUN_LIMIT = SysRulesCache.behaviorRule.;//每日关注限制
    /**
     * 互动行为
     */
    public static int DAILY_LIKE = SysRulesCache.behaviorRule.getLikegoals();//点赞
    public static int DAILY_LIKE_LIMIT = SysRulesCache.behaviorRule.getLikegoalsmax();//点赞限制
    public static int DAILY_COMMENT = SysRulesCache.behaviorRule.getImpcommentgoals();//评论
    public static int DAILY_COMMENT_LIMIT = SysRulesCache.behaviorRule.getImpcommentgoalsmax();//评论限制
    public static int DAILY_FLOWER = SysRulesCache.behaviorRule.getFlowergoals();//送花
    public static int DAILY_FLOWER_LIMIT = SysRulesCache.behaviorRule.getFlowergoalsmax();//送花限制
    public static int DAILY_IMPFLOWER_LIMIT = SysRulesCache.behaviorRule.getEachimproveflowersmax();//单个用户对单条微进步，最多可献N朵花
//    public static int DAILY_DIAMOND = 10;//送钻
//    public static int DAILY_DIAMOND_LIMIT = 100;//送钻限制
    /**
     * 进步行为
     */
    public static int DAILY_ADDIMP = SysRulesCache.behaviorRule.getImprovegoals();//发进步
    public static int DAILY_ADDIMP_LIMIT = SysRulesCache.behaviorRule.getImprovegoalsmax();//发进步限制
    public static int DAILY_ADDRANK = SysRulesCache.behaviorRule.getJoinrankgoals();//加榜
    public static int DAILY_ADDRANK_LIMIT = SysRulesCache.behaviorRule.getJoinrankgoalstimes()*DAILY_ADDRANK;//加榜限制
    public static int DAILY_ADDCLASSROOM = SysRulesCache.behaviorRule.getJoinclassgoals();//加教室
    public static int DAILY_ADDCLASSROOM_LIMIT = 35;//加教室限制
    public static int DAILY_ADDCIRCLE = SysRulesCache.behaviorRule.getJoincirclegoals();//加圈子
    public static int DAILY_ADDCIRCLE_LIMIT = SysRulesCache.behaviorRule.getJoincirclegoalstimes();//加圈子限制
    /**
     * 金融消费
     */
//    public static int SPONSOR = 1;//赞助
//    public static int APPRECIATE = 1;//appreciate 赞赏
    public static int LOTTERY = SysRulesCache.behaviorRule.getLotterygoals();//lottery 抽奖

    /**
     *
     * 邀请好友注册赠送的龙分
     */
    public static int INVITE_LEVEL1 = SysRulesCache.behaviorRule.getFriendregistergoals();

    public static int INVITE_LEVEL1_LIMIT = SysRulesCache.behaviorRule.getFriendregistergoalsmax();

    

    private static Map<String,Integer> fieldsMap = null;

    public static boolean hasContain(String fieldName){
        initFieldMap();
        return fieldsMap.containsKey(fieldName);
    }

    public static Integer getStaticProperty(String fieldName){
        initFieldMap();
        return fieldsMap.get(fieldName);
    }

    private static void initFieldMap(){
        try{
            if(null == fieldsMap){
                fieldsMap = new HashMap<>();
                Class ownerClass = Class.forName(Constant_point.class.getName());
                Field[] fields = ownerClass.getFields();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if(field.getType().getName().equals("int")){
                        fieldsMap.put(field.getName(), new Integer(field.getInt(field.getName())));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
