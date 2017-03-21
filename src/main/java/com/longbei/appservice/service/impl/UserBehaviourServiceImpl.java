package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.constant.Constant_point;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserMsgMapper;
import com.longbei.appservice.dao.UserPlDetailMapper;
import com.longbei.appservice.dao.UserPointDetailMapper;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.entity.UserMsg;
import com.longbei.appservice.entity.UserPlDetail;
import com.longbei.appservice.entity.UserPointDetail;
import com.longbei.appservice.service.UserBehaviourService;
import com.longbei.appservice.service.UserImpCoinDetailService;
import com.longbei.appservice.service.UserMoneyDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by smkk on 17/2/7.
 */
@Service
public class UserBehaviourServiceImpl implements UserBehaviourService {

    @Autowired
    private SpringJedisDao springJedisDao;
    @Autowired
    private UserPlDetailMapper userPlDetailMapper;
    @Autowired
    private UserPointDetailMapper userPointDetailMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserImpCoinDetailService userImpCoinDetailService;
    @Autowired
    private UserMsgMapper userMsgMapper;

    private static Logger logger = LoggerFactory.getLogger(UserBehaviourServiceImpl.class);



    @Override
    public BaseResp<Object> canOperateMore(long userid,UserInfo userInfo, String operateType) {

        BaseResp<Object> baseResp = new BaseResp<>();
        String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        if(null == userInfo){
            userInfo = userInfoMapper.selectByPrimaryKey(userid);
        }
        try{
            switch (operateType){
                case Constant.PERDAY_CHECK_IN:
                    int checkIn = getHashValueFromCache(getPerKey(userInfo.getUserid()), dateStr+Constant.PERDAY_CHECK_IN);
                    if (checkIn == 0){//为空,可以签到
                        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                    }
                    break;
                //发进步
                case Constant.PERDAY_ADD_IMPROVE:
                    int addImproveCount = getHashValueFromCache(getPerKey(userInfo.getUserid()),dateStr+Constant.PERDAY_ADD_IMPROVE);
                    if(addImproveCount < SysRulesCache.sysRules.getMaximprove()){
                        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                    }
                    break;
                //点赞
                case Constant.PERDAY_ADD_LIKE:
                    int addLikeCount = getHashValueFromCache(getPerKey(userInfo.getUserid()),dateStr+Constant.PERDAY_ADD_LIKE);
                    int maxCount = SysRulesCache.sysRules.getLevellike()*userInfo.getGrade()+
                            SysRulesCache.sysRules.getLimitlike();
                    if(addLikeCount < maxCount){
                        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                    }
                    break;
                default:
                    break;
            }
        }catch(Exception e){
            logger.error("canOperateMore error userid={},operateType={},msg={}",userid,operateType,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> pointChange(UserInfo userInfo, String operateType,
                                        String pType,String origin,long impid,long friendid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        int point = getPointByType(userInfo.getUserid(),operateType);
        baseResp.getExpandData().put("point",point);
        if(point > 0){
            String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
            springJedisDao.put(Constant.RP_USER_PERDAY+userInfo.getUserid()+"_TOTAL",dateStr,String.valueOf(point));
            springJedisDao.expire(Constant.RP_USER_PERDAY+userInfo.getUserid()+"_TOTAL", Constant.CACHE_24X60X60);
            levelUp(userInfo.getUserid(),point,pType);
        }
        //进步币发生变化
        int impIcon = 0 ;
        if(!StringUtils.isBlank(origin)){
            impIcon = getImpIcon(userInfo,operateType);
            if(impIcon>0){
                //进步币添加来源   0:签到   1:分享 2：邀请好友注册 3：被送花 4，被送钻石 5 发进步 6 榜单奖品
                //long userid, String origin, int number, long impid, long friendid)
                userImpCoinDetailService.insertPublic(userInfo.getUserid(),origin,impIcon,impid,friendid);
            }
            baseResp.getExpandData().put("impIcon",impIcon);
        }
        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        return baseResp;
    }

    @Override
    public BaseResp<Object> hasPrivilege(long userid, UserInfo userInfo, String operateType) {
        return BaseResp.ok();
    }

    private int getHashValueFromCache(String key,String hashKey){
        String sVaue = springJedisDao.getHashValue(key,hashKey);
        if(!StringUtils.isBlank(sVaue)){
            try{
                return Integer.parseInt(sVaue);
            }catch(Exception e){
                logger.debug("(int)o msg={}",e);
                return 0;
            }
        }
        return 0;
    }

    private BaseResp<Object> levelUp(long userid, int iPoint,String pType) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);//此处通过id获取用户信息
        return levelUp(userInfo,iPoint,pType);
    }

    /**
     * 是否升级以及升级之后的操作
     * 关于用户的龙分 通过计算获取
     * 关于升级所需龙分  当前龙分  不升级均不存储
     * @param userInfo
     * @param iPoint
     * @return
     */
    private BaseResp<Object> levelUp(UserInfo userInfo, int iPoint,String pType) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
            String key = getPerKey(userInfo.getUserid());
            boolean hasKey = springJedisDao.hasKey(key,dateStr+Constant.PERDAY_POINT);
            if(hasKey){
                int point = getHashValueFromCache(key,dateStr+Constant.PERDAY_POINT);
                int leftPoint = point - iPoint;
                if(point > 0 && leftPoint > 0){//未升级
                    springJedisDao.increment(key,dateStr+Constant.PERDAY_POINT,-iPoint);
                    updateToUserInfo(userInfo,iPoint);
                }else{//升级
                    levelUpAsyn(userInfo,iPoint,leftPoint);
                    springJedisDao.delete(key,dateStr+Constant.PERDAY_POINT);
                }
            }else{
                //通过级别获取升级下以及所需分数  进行缓存
                int curpoint = userInfo.getCurpoint();//这里需要改
                UserLevel userLevel = SysRulesCache.levelPointMap.get(userInfo.getGrade()+1);
                int upCount = userLevel.getDiff();
                int leftPoint = upCount -curpoint - iPoint;
                if(leftPoint > 0){
                    springJedisDao.put(key,dateStr+Constant.PERDAY_POINT,leftPoint+"");
                    springJedisDao.expire(key,Constant.CACHE_24X60X60);
                    updateToUserInfo(userInfo,iPoint);
                }else{//升级
                    levelUpAsyn(userInfo,iPoint,leftPoint);
                    springJedisDao.delete(key,dateStr+Constant.PERDAY_POINT);
                }
            }
            //userPointDetailMapper.insert();
            //不管升级不升级  userpldetail  userpoint
            subLevelUp(userInfo,iPoint,pType,dateStr);
        }catch(Exception e){
            logger.error("levelUp error and msg = {}",e);
        }
        return baseResp;
    }

    /**
     * 通过用户id和操作类型获取龙分
     * @param userid 用户id
     * @param operateType 操作类型
     *
     *
     * @return
     */
//    @Override
    public int getPointByType(long userid, String operateType) {
        BaseResp<Object> baseResp = new BaseResp<>();
        //如果有限制  去redis中去找
        String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        int result = Constant_point.getStaticProperty(operateType);
        String limitField = operateType+"_LIMIT";
        String key = getPerKey(userid);
        if(Constant_point.hasContain(limitField)){
            int limitValue = Constant_point.getStaticProperty(limitField);
            String value = springJedisDao.getHashValue(key, dateStr+limitField);
            if(StringUtils.isBlank(value)){
                springJedisDao.put(key, dateStr+limitField,result+"");
                return result;
            }else{
                int curValue = Integer.parseInt(value);
                if(curValue+result > limitValue){//就不给了
                    return 0;
                }else{
                    springJedisDao.put(key, dateStr+limitField,result+"");
                    return result;
                }
            }
        }else{
            //签到的时候把过期时间添加上
            if(operateType.equals(Constant_point.DAILY_CHECKIN)){
                springJedisDao.expire(key,Constant.CACHE_24X60X60);
            }
            return result;
        }
    }



    /**
     * 更新用户当前龙分  总龙分
     * @param userInfo
     * @param iPoint
     */
    private void updateToUserInfo(UserInfo userInfo,int iPoint){
        userInfo.setPoint(userInfo.getPoint()+iPoint);
        userInfo.setCurpoint(userInfo.getCurpoint()+iPoint);
        userInfoMapper.updatePointByUserid(userInfo);
    }

    private BaseResp<Object> subLevelUp(UserInfo userInfo, int iPoint,String pType,String dateStr){
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            String key = getPerKey(userInfo.getUserid());
            boolean hasKey = springJedisDao.hasKey(key,dateStr+Constant.PERDAY_POINT+pType);
            UserPlDetail userPlDetail = userPlDetailMapper.selectByUserIdAndType(userInfo.getUserid(),pType);

            int level=0;//等级
            int levelPoint = 0;
            int curPoint = 0;
            if(null == userPlDetail){
                //如果是空 说明当前用户无十大分类信息 积分从缓存中获取 0级别
                level = 1;
                levelPoint = SysRulesCache.pLevelPointMap.get(pType+"&1");
            }else{
                curPoint = userPlDetail.getScorce();
                level = userPlDetail.getLeve();
                levelPoint = SysRulesCache.pLevelPointMap.get(pType+"&"+(userPlDetail.getLeve()));
            }
            if(hasKey){
                int point = getHashValueFromCache(key,dateStr+Constant.PERDAY_POINT+pType);
                int leftPoint = point - iPoint;
                if(point > 0 && leftPoint > 0){//未升级
                    springJedisDao.increment(key,dateStr+Constant.PERDAY_POINT+pType,-iPoint);
                    //没有升级 更新userPlDetail数据
                    updateToUserPLDetail(userInfo,iPoint,pType,level);
                }else{//升级
                    saveLevelUpInfo(userInfo,pType,iPoint,userPlDetail.getLeve()+1);
                    springJedisDao.delete(key,dateStr+Constant.PERDAY_POINT+pType);
                }
            }else{
                //这里通过10大分类获取用户point分数
                int leftPoint = levelPoint - curPoint - iPoint;
                if(leftPoint > 0){
                    springJedisDao.put(key,dateStr+Constant.PERDAY_POINT+pType,leftPoint+"");
                    updateToUserPLDetail(userInfo,iPoint,pType,level);
                }else{//升级
                    saveLevelUpInfo(userInfo,pType,iPoint,userPlDetail.getLeve()+1);
                    springJedisDao.delete(key,dateStr+Constant.PERDAY_POINT+pType);
                }
            }
            saveUserPointDetail(userInfo,iPoint,pType);
        }catch (Exception e){
            logger.error("subLevelUp error and msg = {}",e);
        }
        return baseResp.initCodeAndDesp();
    }

    private Map<String,Integer> getLeftPointAndLevel(UserInfo userInfo,String pType,int iPoint){
        Map<String,Integer> map = new HashMap<String,Integer>();
        //这里通过10大分类获取用户point分数
        UserPlDetail userPlDetail = userPlDetailMapper.selectByUserIdAndType(userInfo.getUserid(),pType);
        int point = 0;
        int levelPoint = 0;
        if(null == userPlDetail){
            //如果是空 说明当前用户无十大分类信息 积分从缓存中获取 0级别
            map.put("level",1);
            levelPoint = SysRulesCache.pLevelPointMap.get(pType+"&1");
        }else{
            point = userPlDetail.getScorce();
            map.put("level",userPlDetail.getLeve());
            levelPoint = SysRulesCache.pLevelPointMap.get(pType+"&"+(userPlDetail.getLeve()));
        }
        //等级所需分数-当前等级分数-操作分数
        int leftPoint = levelPoint - point - iPoint;
        map.put("leftPoint",leftPoint);

        return map;
    }

    /**
     * 更新当前分数  通过level ptype userid  累加 scorce
     * @param userInfo
     * @param iPoint
     * @param pType
     * @return
     */
    public boolean updateToUserPLDetail(UserInfo userInfo,int iPoint,String pType,int level){
        UserPlDetail userPlDetail = new UserPlDetail();
        userPlDetail.setScorce(iPoint);
        userPlDetail.setUserid(userInfo.getUserid());
        userPlDetail.setPtype(pType);
        userPlDetail.setLeve(level);
        try{
            int n = userPlDetailMapper.updateScorce(userPlDetail);
            if (n>0)
                return true;
        }catch(Exception e){
            logger.error("userPlDetailMapper.updateScorce error and msg={}",e);
        }

        return false;
    }

    /**
     * 存储用户积分明细
     * @param userInfo
     * @param iPoint
     * @param ptype
     */
    private void saveUserPointDetail(UserInfo userInfo,int iPoint,String ptype){
        try{
            UserPointDetail userPointDetail = new UserPointDetail();
            userPointDetail.setUserid(userInfo.getUserid());
            userPointDetail.setDrawdate(new Date());
            userPointDetail.setPoint(iPoint);
            userPointDetail.setPtype(ptype);
            userPointDetailMapper.insert(userPointDetail);
        }catch (Exception e){
            logger.error("userPointDetailMapper.insert error and msg={}",e);
        }
    }

    private String getPerKey(long userid){
        return Constant.RP_USER_PERDAY+userid;
    }

    private void saveLevelUpInfo(UserInfo userInfo,String ptype,int iPoint,int level){
        try{
            UserPlDetail userPlDetail = new UserPlDetail();
            userPlDetail.setCreatetime(new Date());
            userPlDetail.setLeve(level);
            if(iPoint == 0){
                userPlDetail.setScorce(userInfo.getPoint());
            }
            else{
                userPlDetail.setScorce(userInfo.getPoint()+iPoint);
            }
            userPlDetail.setPtype(ptype);

            userPlDetail.setUserid(userInfo.getUserid());
            userPlDetailMapper.insert(userPlDetail);
            
            //推送一条消息
            String remark = Constant.MSG_USER_PL_LEVEL_MODEL.replace("n", level + "");
            remark = remark.replace("m", SysRulesCache.perfectTenMap.get(ptype));
            //mtype 0 系统消息      msgtype  19：十全十美升级
            levelMsg(userInfo.getUserid(), "19", remark);
        }catch (Exception e){
            logger.error("subLevelUpAsyn error and msg = {}",e);
        }
    }


    //异步线程跑升级
    private void levelUpAsyn(UserInfo userInfo,int iPoint,int leftpoint){
        try{
            //等级升级  update UserInfo
            userInfo.setGrade(userInfo.getGrade()+1);
            userInfo.setPoint(userInfo.getPoint()+iPoint);
            userInfo.setCurpoint(leftpoint);
            userInfoMapper.updatePointByUserid(userInfo);
            //插入一条 等级升级消息  不升级就不插入这个表
            saveLevelUpInfo(userInfo,"a",0,userInfo.getGrade());
            //推送一条消息
            String remark = Constant.MSG_USER_LEVEL_MODEL.replace("n", userInfo.getGrade().toString());
            //mtype 0 系统消息      msgtype  18:升龙级
            levelMsg(userInfo.getUserid(), "18", remark);
        }catch (Exception e){
            logger.error("levelUpAsyn error and msg = {}",e);
        }
    }
    
    /**
	 * @author yinxc
	 * 等级升降级添加消息
	 * 2017年3月8日
	 */
    private void levelMsg(long userid, String msgtype, String remark){
    	UserMsg userMsg = new UserMsg();
        userMsg.setUserid(userid);
        userMsg.setMtype("0");
        userMsg.setMsgtype(msgtype);
        userMsg.setRemark(remark);
        userMsg.setIsdel("0");
        userMsg.setIsread("0");
        userMsg.setCreatetime(new Date());
        userMsgMapper.insertSelective(userMsg);
    }

    /**
     *  进步币发生变化
     *
     */
    private int getImpIcon(UserInfo userInfo, String operateType){
        String operateTypeRandom = operateType+"_RANDOM";
        String key = getPerKey(userInfo.getUserid());
        String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        int result = 0;
        if(Constant_Imp_Icon.hasContain(operateType)){ //key 直接存在  目前有两种情况 签到  邀请好友注册
            if(operateType.equals("INVITE_LEVEL1")){
                result = Constant_Imp_Icon.getStaticProperty(operateType);
                //
                return result;
            }else if(operateType.equals("DAILY_CHECKIN")){
                String redisvalue = springJedisDao.getHashValue(Constant.RP_USER_CHECK + userInfo.getUserid(),
                        Constant.RP_USER_CHECK_VALUE + userInfo.getUserid());
                if(StringUtils.isBlank(redisvalue)){

                    return Constant_Imp_Icon.checkInImpIconMap.get(1);
                }else{
                    if(Constant_Imp_Icon.checkInImpIconMap.containsKey(redisvalue)){
                        return Constant_Imp_Icon.checkInImpIconMap.get(redisvalue);
                    }else{
                        return Constant_Imp_Icon.checkInImpIconMap.get(7);
                    }
                }
            }else {
                return 0;
            }
            //随机进步币   次数限制
        }else if(Constant_Imp_Icon.hasContain(operateTypeRandom)){
            String operateTypeLimit = operateType+"_LIMIT";
            //次数有限制
            if(Constant_Imp_Icon.hasContain(operateTypeLimit)){
                int limit = Constant_Imp_Icon.getStaticProperty(operateTypeLimit);
                String cacheStr = springJedisDao.getHashValue(key,dateStr+operateTypeLimit);
                int cacheTime = 0;
                if(!StringUtils.isBlank(cacheStr)){
                    cacheTime = Integer.parseInt(cacheStr);
                }
                if(limit < cacheTime){//还送
                    int randomRule = Constant_Imp_Icon.getStaticProperty(operateTypeRandom);
                    int randomCode = Constant_Imp_Icon.getRandomCode(randomRule);
                    springJedisDao.put(key,dateStr+operateTypeLimit,(cacheTime+1)+"");
                    return randomCode;
                }else{//不送了
                    return 0;
                }
            }else{//没有限制  目前没有这种情况
                //do nothing
            }
        }else{
            //do nothing
        }
        return 0;
    }

    /**
     *  龙币发生变化
     */



}
