package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.constant.Constant_point;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.UserBehaviourService;
import com.longbei.appservice.service.UserImpCoinDetailService;
import com.longbei.appservice.service.UserLevelService;
import com.longbei.appservice.service.UserMoneyDetailService;
import com.longbei.appservice.service.UserMsgService;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private UserMsgService userMsgService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private UserLevelMapper userLevelMapper;
    @Autowired
    private RankMembersMapper rankMembersMapper;

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
            levelUp(userInfo,point,pType);
            saveUserPointDetail(userInfo,point,pType,operateType);
            putPointToCache(point,userInfo.getUserid(),operateType);
        }
        //进步币发生变化
        int impIcon = 0 ;
        if(!StringUtils.isBlank(origin)){
            impIcon = getImpIcon(userInfo,operateType);
            if(impIcon>0){
            	//进步币 origin 来源   0:签到   1:发进步  2:分享  3：邀请好友  4：榜获奖  5：收到钻石礼物 
           	    //				    6：收到鲜花礼物  7:兑换商品  8：公益抽奖获得进步币  
        	    // 					9：公益抽奖消耗进步币  10.消耗进步币(例如超级用户扣除进步币)
        	    // 					11:取消订单返还龙币     12:兑换鲜花
                //long userid, String origin, int number, long impid, long friendid)
                userImpCoinDetailService.insertPublic(userInfo.getUserid(),origin,impIcon,impid,friendid);
            }
            baseResp.getExpandData().put("impIcon",impIcon);
        }
        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        return baseResp;
    }

    /**
     * 统计每日得分
     * 缓存到当天24点
     * lixb
     */
    private void putPointToCache(int point,long userid,String operateType){
        String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        String key = Constant.RP_USER_PERDAY+"sum"+userid+dateStr;
        if(springJedisDao.hasKey(key)){
            springJedisDao.increment(key,operateType,point);
        }else{
            springJedisDao.increment(key,operateType,point);
            springJedisDao.expire(key,DateUtils.getLastTime());
        }

//        String totalKey = Constant.RP_USER_PERDAY+userid+"_TOTAL"+dateStr;
//        if(springJedisDao.hasKey(totalKey)){
//            springJedisDao.increment(totalKey,dateStr,point);
//        }else{
//            springJedisDao.increment(totalKey,dateStr,point);
//            springJedisDao.expire(totalKey,DateUtils.getLastTime());
//        }

    }

    @Override
    public BaseResp<Object> hasPrivilege(UserInfo userInfo, Constant.PrivilegeType privilegeType,Object o) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
            if(null == userLevel){
                return baseResp.initCodeAndDesp();
            }
            //加榜单个数
            if(privilegeType.equals(Constant.PrivilegeType.joinranknum)){
//                RankMembers rankMembers = new RankMembers();
//                rankMembers.setUserid(userInfo.getUserid());
//                int count = rankMembersMapper.selectCount(rankMembers);
//                if(count < userLevel.getJoinranknum()){
                    return BaseResp.ok();
//                }else{
//                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_14,Constant.RTNINFO_SYS_14);
//                }
            }else {
                //发榜  判断发布榜单个数  暂时不做
                Rank r = (Rank)JSONObject.toBean(JSONObject.fromObject(o),Rank.class);
                if(r.getIspublic().equals("0")){ //公开榜单参与人数限制
                    if(r.getRanklimite() <=  userLevel.getPubrankjoinnum()){
                        return BaseResp.ok();
                    }else{
                        //后面处理
                    }
                }else{//私有榜单参与人数限制
                    if(r.getRanklimite() <=  userLevel.getPrirankjoinnum()){
                        return BaseResp.ok();
                    }else{
                        //operate later
                    }
                }
            }
        }catch(Exception e){
            logger.error("hasPrivilege error msg:{}",e);
        }
        return baseResp.ok();
    }

    @Override
    public BaseResp<Object> userSumInfo(final Constant.UserSumType userSumType,
                                        final long userid,
                                        final Improve improve,
                                        final int count) {

        BaseResp<Object> baseResp = new BaseResp<>();
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int res = 0;
                try{
                    if(userSumType.equals(Constant.UserSumType.addedImprove)){
                        res = userInfoMapper.updateUserSumInfo(userid,1,0,0,0);
                    }else if(userSumType.equals(Constant.UserSumType.removedImprove)){
                        res = userInfoMapper.updateUserSumInfo(userid,-1,improve.getLikes(),0,improve.getFlowers());
                    }else if(userSumType.equals(Constant.UserSumType.addedLike)){
                        res = userInfoMapper.updateUserSumInfo(userid,0,1,0,0);
                    }else if(userSumType.equals(Constant.UserSumType.removedLike)){
                        res = userInfoMapper.updateUserSumInfo(userid,0,-1,0,0);
                    }else if(userSumType.equals(Constant.UserSumType.addedFans)){
                        res = userInfoMapper.updateUserSumInfo(userid,0,0,1,0);
                    }else if(userSumType.equals(Constant.UserSumType.removedFans)){
                        res = userInfoMapper.updateUserSumInfo(userid,0,0,-1,0);
                    }else if(userSumType.equals(Constant.UserSumType.addedFlower)){
                        res = userInfoMapper.updateUserSumInfo(userid,0,0,0,count);
                    }
                }catch (Exception e){
                    logger.error("updateUserSumInfo error and UserSumType={}",userSumType,e);
                }
            }
        });
        return baseResp;
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
    private void updateToUserInfo(UserInfo userInfo,int iPoint) throws Exception {
        userInfo.setPoint(userInfo.getPoint()+iPoint);
        userInfo.setCurpoint(userInfo.getCurpoint()+iPoint);
        if (0 == userInfo.getGrade()){
            throw new Exception("user grade is must can not be 0");
        }
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
                levelPoint = SysRulesCache.pLevelPointMap.get(pType+"&1").getScore();
            }else{
                curPoint = userPlDetail.getScorce();
                level = userPlDetail.getLeve();
                levelPoint = SysRulesCache.pLevelPointMap.get(pType+"&"+(userPlDetail.getLeve())).getScore();
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
                    updateUserPLDetailToplevel(userInfo.getUserid(),pType);
                    saveLevelUpInfo(userInfo,pType,iPoint,userPlDetail.getLeve()+1);
                    springJedisDao.delete(key,dateStr+Constant.PERDAY_POINT+pType);
                }
            }
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
            levelPoint = SysRulesCache.pLevelPointMap.get(pType+"&1").getScore();
        }else{
            point = userPlDetail.getScorce();
            map.put("level",userPlDetail.getLeve());
            levelPoint = SysRulesCache.pLevelPointMap.get(pType+"&"+(userPlDetail.getLeve())).getScore();
        }
        //等级所需分数-当前等级分数-操作分数
        int leftPoint = levelPoint - point - iPoint;
        map.put("leftPoint",leftPoint);

        return map;
    }

    /**
     * @Title: updateUserPLDetailToplevel
     * @Description: 更新是否最高级
     * @param userid
     * @param @return
     * @return boolean 返回类型
     * @auther IngaWu
     * @currentdate:2017年5月3日
     */
    @Override
    public boolean updateUserPLDetailToplevel(long userid,String pType){
        UserPlDetail userPlDetail = new UserPlDetail();
        userPlDetail.setUserid(userid);
        userPlDetail.setPtype(pType);
        userPlDetail.setToplevel("1");
        try{
            int n = userPlDetailMapper.updateUserPLDetailToplevel(userPlDetail);
            if (n>0)
                return true;
        }catch(Exception e){
            logger.error("updateUserPLDetailToplevel error and msg={}",e);
        }

        return false;
    }

    /**
     * 更新当前分数  通过level ptype userid  累加 scorce
     * @param userInfo
     * @param iPoint
     * @param pType
     * @return
     */
    public boolean updateToUserPLDetail(UserInfo userInfo,int iPoint,String pType,int level){
        Date date = new Date();
        UserPlDetail userPlDetail = new UserPlDetail();
        userPlDetail.setScorce(iPoint);
        userPlDetail.setUserid(userInfo.getUserid());
        userPlDetail.setPtype(pType);
        userPlDetail.setLeve(level);
        userPlDetail.setToplevel("1");
        userPlDetail.setUpdatetime(date);

        UserPlDetail userPlDetail2 = new UserPlDetail();
        userPlDetail2.setUserid(userInfo.getUserid());
        userPlDetail2.setScorce(iPoint);
        userPlDetail2.setPtype("a");
        userPlDetail2.setUpdatetime(date);
        try{
            int n = userPlDetailMapper.updateScorce(userPlDetail);
            int m = userPlDetailMapper.updateByPrimaryKeySelective(userPlDetail2);
            if (n>0 && m>0)
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
    private void saveUserPointDetail(UserInfo userInfo,int iPoint,String ptype,String pointtype){
        try{
            UserPointDetail userPointDetail = new UserPointDetail();
            userPointDetail.setUserid(userInfo.getUserid());
            userPointDetail.setDrawdate(new Date());
            userPointDetail.setPoint(iPoint);
            userPointDetail.setPtype(ptype);
            userPointDetail.setPointtype(pointtype);
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
            if (!"a".equals(ptype)) {
                remark = remark.replace("m", SysRulesCache.perfectTenMap.get(ptype));
                //mtype 0 系统消息      msgtype  19：十全十美升级
                userMsgService.insertMsg(Constant.SQUARE_USER_ID, userInfo.getUserid().toString(),
                        "", "6", "", remark, "0", "19", "升级", 0, "", "");
            }
//            levelMsg(userInfo.getUserid(), "19", remark);
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
            String remark = Constant.MSG_USER_LEVEL_MODEL.replace("n", userInfo.getGrade() + "");
            //mtype 0 系统消息      msgtype  18:升龙级
            userMsgService.insertMsg(Constant.SQUARE_USER_ID, userInfo.getUserid().toString(), 
            		"", "6", "", remark, "0", "18", "升级", 0, "", "");
//            levelMsg(userInfo.getUserid(), "18", remark);
        }catch (Exception e){
            logger.error("levelUpAsyn error and msg = {}",e);
        }
    }
    
    /**
	 * @author yinxc
	 * 等级升降级添加消息
	 * 2017年3月8日
	 */
//    private void levelMsg(long userid, String msgtype, String remark){
//    	UserMsg userMsg = new UserMsg();
//        userMsg.setUserid(userid);
//        userMsg.setMtype("0");
//        userMsg.setMsgtype(msgtype);
//        userMsg.setRemark(remark);
//        userMsg.setIsdel("0");
//        userMsg.setIsread("0");
//        //gtype  0:零散 1:目标中 2:榜中  3:圈子中 4.教室中 5:龙群  6:龙级  7:订单  8:认证 9：系统
//        userMsg.setGtype("6");
//        userMsg.setCreatetime(new Date());
//        userMsgMapper.insertSelective(userMsg);
//    }

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
                    if(Constant_Imp_Icon.checkInImpIconMap.containsKey(Integer.parseInt(redisvalue))){
                        return Constant_Imp_Icon.checkInImpIconMap.get(Integer.parseInt(redisvalue));
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
