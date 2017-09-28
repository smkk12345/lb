package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.LiveGiftService;
import com.longbei.appservice.service.UserMoneyDetailService;
import com.longbei.appservice.service.UserRelationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lixb on 2017/8/11.
 */
@Service
public class LiveGiftServiceImpl implements LiveGiftService {

    @Autowired
    private LiveGiftMapper liveGiftMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private LiveGiftDetailMapper liveGiftDetailMapper;
    @Autowired
    private UserMoneyDetailService userMoneyDetailService;
    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private UserInComeService userInComeService;
    @Autowired
    private LiveInfoMongoMapper liveInfoMongoMapper;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private UserRelationService userRelationService;
//    @Autowired
//    private LiveCache liveCache;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private UserCardMapper userCardMapper;
    

    private static Logger logger = LoggerFactory.getLogger(LiveGiftServiceImpl.class);

    @Override
    public BaseResp<List<LiveGift>> selectList(Integer startNum, Integer endNum) {
        BaseResp<List<LiveGift>> baseResp = new BaseResp<>();
        try{
            List<LiveGift> list = liveGiftMapper.selectListByIsdel(0, 500);
            baseResp.setData(list);
            baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("selectList error and startNum={},endNum={}",startNum,endNum,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> giveGift(long giftId, long fromUid, int num, long toUId, long liveid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        LiveGift liveGift = liveGiftMapper.selectLiveGiftByGiftId(giftId);
        UserInfo userInfo = userInfoMapper.selectByUserid(fromUid);
        if(null == liveGift||null == userInfo){
            return baseResp;
        }
        LiveInfo liveInfo = liveInfoMongoMapper.selectLiveInfoByLiveid(liveid);
        if(null == liveInfo){
            return baseResp;
        }
        long classroomid = liveInfo.getClassroomid();
        if(hasEnoughMoney(liveGift,userInfo,num)){
            //扣除龙币 生成记录
            //1，兑换礼物 2，送礼物
            //插入一条明细
        	//gtype  礼物类型 0 直播礼物 1 非直播礼物
            int n = insertLiveGiftDetail(fromUid,toUId,num,liveGift,classroomid,Constant.IMPROVE_CLASSROOM_TYPE, "0");
            if(n > 0){ //String origin, int number, long friendid
                int giveMoney = liveGift.getPrice()*num;
                userMoneyDetailService.insertPublic(userInfo,"13",-giveMoney,toUId);
                //添加教室收益
                userInComeService.updateUserInCome(String.valueOf(classroomid),String.valueOf(toUId),
                        String.valueOf(fromUid),"1","0",giveMoney,null);
            }
            baseResp.initCodeAndDesp();
            baseResp.setData(userInfo.getTotalmoney()-num*liveGift.getPrice());
        }else {
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_1301, Constant.RTNINFO_SYS_1301);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> giveGift(long giftId, long fromUid, int num, long toUId,
                                     long classroomid,String businesstype) {
        BaseResp<Object> baseResp = new BaseResp<>();
        LiveGift liveGift = liveGiftMapper.selectLiveGiftByGiftId(giftId);
        UserInfo userInfo = userInfoMapper.selectByUserid(fromUid);
        if(null == liveGift||null == userInfo){
            return baseResp;
        }
        if(hasEnoughMoney(liveGift,userInfo,num)){
            //扣除龙币 生成记录
            //1，兑换礼物 2，送礼物
            //插入一条明细
        	//gtype  礼物类型 0 直播礼物 1 非直播礼物
            int n = insertLiveGiftDetail(fromUid,toUId,num,liveGift,classroomid,businesstype, "1");
            if(n > 0){ //String origin, int number, long friendid
                int giveMoney = liveGift.getPrice()*num;
                userMoneyDetailService.insertPublic(userInfo,"13",-giveMoney,toUId);
                //添加教室收益
                userInComeService.updateUserInCome(String.valueOf(classroomid),String.valueOf(toUId),
                        String.valueOf(fromUid),"7","0",giveMoney,null);
            }
            baseResp.initCodeAndDesp();
            baseResp.setData(userInfo.getTotalmoney()-num*liveGift.getPrice());
        }else {
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_1301, Constant.RTNINFO_SYS_1301);
        }
        return baseResp;
    }

    /**
     * 查询用户收到的礼物列表
     * @param userid 当前登录者id
     * @param classroomid
     * @param startNum
     * @param endNum
     * @return
     */
    @Override
    public BaseResp<List<LiveGiftDetail>> selectOwnGiftList(Long userid, String classroomid, 
    		Integer startNum, Integer endNum) {
    	BaseResp<List<LiveGiftDetail>> baseResp = new BaseResp<>();
        try{
            List<LiveGiftDetail> list = null;
            if(StringUtils.isBlank(classroomid)){
                //查个人中心的总明细
                list = liveGiftDetailMapper.selectOwnGiftList(userid,
                        null, null, startNum, endNum);
            }else{
                //查教室中的礼物明细
                list = liveGiftDetailMapper.selectOwnGiftList(null,
                        classroomid, "4", startNum, endNum);
            }
            if(null != list && list.size()>0){
            	for (LiveGiftDetail liveGiftDetail : list) {
            		initLiveGiftDetailByUserid(liveGiftDetail, userid.toString());
    			}
            }
            baseResp.setData(list);
            baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("selectOwnGiftList userid = {}, startNum = {}, endNum = {}", 
            		userid, startNum, endNum, e);
        }
        return baseResp;
    }
    

	@Override
	public BaseResp<List<LiveGiftDetail>> selectGiftListByGiftid(Long userid, String classroomid, 
			Long giftid, Integer startNum, Integer endNum) {
		BaseResp<List<LiveGiftDetail>> baseResp = new BaseResp<>();
        try{
            List<LiveGiftDetail> list = null;
            logger.info("selectGiftListByGiftid classroomid={},giftid={},cuserid={}",classroomid,giftid,cuserid);
        	if(StringUtils.isBlank(classroomid)){
                //查询用户制定礼物的礼物明细
                list = liveGiftDetailMapper.selectGiftListByGiftid(userid, null, null,
                        giftid, startNum, endNum);
            }else{
                //查询教室中制定礼物的礼物明细
                list = liveGiftDetailMapper.selectGiftListByGiftid(null, classroomid, "4",
                        giftid, startNum, endNum);
            }

            if(null != list && list.size()>0){
            	for (LiveGiftDetail liveGiftDetail : list) {
            		initLiveGiftDetailByUserid(liveGiftDetail, userid.toString());
    			}
            }
            baseResp.setData(list);
            baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("selectGiftListByGiftid userid = {}, giftid = {}, startNum = {}, endNum = {}", 
            		userid, giftid, startNum, endNum, e);
        }
        return baseResp;
	}

    
    /**
     * 初始化礼物中用户信息 ------Userid
     */
    private void initLiveGiftDetailByUserid(LiveGiftDetail liveGiftDetail, String friendid){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(liveGiftDetail.getFromuid()));
        if(null != appUserMongoEntity){
			if(StringUtils.isNotEmpty(friendid)){
				this.userRelationService.updateFriendRemark(friendid,appUserMongoEntity);
			}
			liveGiftDetail.setAppUserMongoEntity(appUserMongoEntity);
        }else{
        	liveGiftDetail.setAppUserMongoEntity(new AppUserMongoEntity());
        }
    }
    

    /**
     * 查询用户收到的各礼物类型总数
     * @param userid
     */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<List<Map<String,String>>> selectGiftSumList(long userid, String classroomid) {
		BaseResp<List<Map<String,String>>> baseResp = new BaseResp<>();
        try{
            List<Map<String,String>> resultList = new ArrayList<>();
            List<LiveGiftDetail> list = null;
            if(!StringUtils.isBlank(classroomid)){
                //查询教室的礼物
                list = liveGiftDetailMapper.selectGiftSumList(null, classroomid, "4");
            }else {
                //查询用户的礼物
                list = liveGiftDetailMapper.selectGiftSumList(userid, null, null);
            }
            if(null == list||list.size()==0){
                return baseResp.initCodeAndDesp();
            }
            Map<Long,Integer> map = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                LiveGiftDetail detail = list.get(i);
                map.put(detail.getGiftid(),detail.getNum());
            }
            List<LiveGift> giftList = liveGiftMapper.selectList(0, 500);
            for (int i = 0; i < giftList.size(); i++) {
                LiveGift gift = giftList.get(i);
                if(map.containsKey(gift.getGiftid())){
                    Map<String,String> reMap = new HashMap<>();
                    reMap.put("num",map.get(gift.getGiftid())+"");
                    reMap.put("gifttitle",gift.getTitle());
                    reMap.put("picurl",gift.getPicurl());
                    reMap.put("giftid",gift.getGiftid()+"");
                    resultList.add(reMap);
                }
            }
            baseResp.setData(resultList);
            baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("selectGiftSumList userid = {}", userid, e);
        }
        return baseResp;
	}

    @Override
    public BaseResp<List<LiveGiftDetail>> selectGiftList(long userid, Integer startNum, Integer endNum) {
        return null;
    }

    /**
     * 判断用户是否有足够的龙币支付礼物
     * @param liveGift
     * @param userInfo
     * @param num
     * @return
     */
    private boolean hasEnoughMoney(LiveGift liveGift,
                                   UserInfo userInfo,
                                   int num){
        if(userInfo.getTotalmoney() >= liveGift.getPrice()*num){
            return true;
        }
        return false;
    }


    private int insertLiveGiftDetail(long fromuid,long touid
                                     ,int num,LiveGift liveGift,
                                     Long businessid,
                                     String businesstype, String gtype){
        int result = 0;
        LiveGiftDetail liveGiftDetail = new LiveGiftDetail(fromuid,
                touid,liveGift.getGiftid(),liveGift.getTitle(),
                num,businessid,businesstype);
        Date date = new Date();
        liveGiftDetail.setCreatetime(date);
        liveGiftDetail.setUpdatetime(date);
        liveGiftDetail.setGtype(gtype);
        try{
            result = liveGiftDetailMapper.insertGiftDetail(liveGiftDetail);
        }catch (Exception e){
            logger.error("insertLiveGiftDetail error",e);
        }
        return result;
    }


    @Override
    public Page<LiveGift> selectLiveGiftList(LiveGift liveGift, Integer startNum, Integer pageSize){
        Page<LiveGift> page = new Page<>(startNum/pageSize+1,pageSize);
        try {
            int totalcount = liveGiftMapper.selectLiveGiftListCount(liveGift);
            Page.setPageNo(startNum/pageSize+1,totalcount,pageSize);
            List<LiveGift> liveGiftList = new ArrayList<LiveGift>();
            liveGiftList = liveGiftMapper.selectLiveGiftList(liveGift,startNum,pageSize);
            page.setTotalCount(totalcount);
            page.setList(liveGiftList);
        } catch (Exception e) {
            logger.error("selectLiveGiftList error and msg={}",e);
        }
        return page;
    }

    @Override
    public BaseResp<LiveGift> selectLiveGiftByGiftId(Long giftId) {
        BaseResp<LiveGift> baseResp = new BaseResp<LiveGift>();
        try {
            LiveGift liveGift = liveGiftMapper.selectLiveGiftByGiftId(giftId);
            baseResp.setData(liveGift);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        catch (Exception e) {
            logger.error("selectLiveGiftByGiftId error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> removeLiveGiftByGiftId(Long giftId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            int m = liveGiftMapper.removeLiveGiftByGiftId(giftId);
            if(m == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("removeLiveGiftByGiftId error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertLiveGift(LiveGift liveGift){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        liveGift.setGiftid(idGenerateService.getUniqueIdAsLong());
        Date date = new Date();
        liveGift.setCreatetime(date);
        liveGift.setUpdatetime(date);
        try {
            int n = liveGiftMapper.insertLiveGift(liveGift);
            if(n == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertLiveGift error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public 	BaseResp<Object> updateLiveGiftByGiftId(LiveGift liveGift) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        liveGift.setUpdatetime(new Date());
        try {
            int n = liveGiftMapper.updateLiveGiftByGiftId(liveGift);
            if(n >= 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateLiveGiftByGiftId error and msg={}",e);
        }
        return baseResp;
    }

}
