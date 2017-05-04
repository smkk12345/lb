package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.ImproveGoalMapper;
import com.longbei.appservice.dao.UserGoalMapper;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.UserGoal;
import com.longbei.appservice.service.GoalService;
import com.longbei.appservice.service.ImproveService;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smkk on 17/2/10.
 */
@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private UserGoalMapper userGoalMapper;

    @Autowired
    private ImproveService improveService;
    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private ImproveGoalMapper improveGoalMapper;

    private static Logger logger = LoggerFactory.getLogger(GoalServiceImpl.class);

    @Override
    public BaseResp<Object> insert(long userid, String goaltag, String ptype, String ispublic, String needwarn, String warntime, String week) {
        BaseResp<Object> baseResp = new BaseResp<>();
        UserGoal userGoal = new UserGoal();
        userGoal.setUserid(userid);
        userGoal.setGoaltag(goaltag);
        userGoal.setPtype(ptype);
        userGoal.setCreatetime(new Date());
        userGoal.setIsdel("0");
        userGoal.setIspublic(ispublic);
        userGoal.setNeedwarn(needwarn);
        userGoal.setWarntime(warntime);
        userGoal.setWeek(week);
        userGoal.setLikes(0);
        userGoal.setFlowers(0);
        userGoal.setIcount(0);
        userGoal.setGoalid(idGenerateService.getUniqueIdAsLong());
        try{
            int n = userGoalMapper.insertSelective(userGoal);
            if(n == 1){
            	baseResp.setData(userGoal);
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch (Exception e){
            logger.error("userGoalMapper.insert error and msg = {}",e);
        }
        return baseResp;
    }

    /**
     * 用户所有的目标进步列表
     * @param userid
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Object> list(long userid, int startNum, int pageSize) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            baseResp = improveService.selectGoalMainImproveList(userid,startNum,pageSize);
        }catch (Exception e){
            logger.error("list error userid={},startNum={},endNum={}",userid,startNum,pageSize,e);
        }
        return baseResp;
    }
    
    /**
     * 单个目标进步列表
     * @param userid
     * @param goalid 目标id
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<List<Improve>> selectListByGoalid(long userid, long goalid, int startNum, int pageSize) {
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        try{
            baseResp = improveService.selectBusinessImproveList(userid + "", goalid + "", "1", startNum,pageSize);
        }catch (Exception e){
            logger.error("list error userid={},startNum={},endNum={}",userid,startNum,pageSize,e);
        }
        return baseResp;
    }
    
    
    /**
     * 获取目标列表
     * @param userid
     * @param startNum
     * @param endNum
     * @return
     */
    @Override
    public BaseResp<List<UserGoal>> selectUserGoalList(long userid, int startNum, int endNum) {
        BaseResp<List<UserGoal>> baseResp = new BaseResp<>();
        try{
        	List<UserGoal> list = userGoalMapper.selectUserGoalList(userid,startNum,endNum);
        	if(null != list && list.size()>0){
//        		int goalCount = 0;
        		for (UserGoal userGoal : list) {
//        			goalCount = improveGoalMapper.selectCountGoal(userGoal.getGoalid(), userid);
//        			userGoal.setGoalCount(goalCount);
        			if(userGoal.getIcount() != 0){
        				Improve improve = improveGoalMapper.selectBeanByGoalId(userGoal.getGoalid());
        				//拼接pickey
        				impItype(improve, userGoal);
        			}
				}
        		baseResp.setData(list);
        	}else{
        		baseResp.setData(new ArrayList<UserGoal>());
        	}
        	baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("list error userid={},startNum={},endNum={}",userid,startNum,endNum,e);
        }
        return baseResp;
    }
    
    /**
     * 获取目标详情
     * @param userid
     * @param goalid 目标id
     * @return
     */
	@Override
	public BaseResp<UserGoal> selectUserGoal(Long userid, long goalid) {
		BaseResp<UserGoal> baseResp = new BaseResp<>();
        try{
        	UserGoal userGoal = userGoalMapper.selectByGoalId(goalid);
        	if(null != userGoal){
//        		int goalCount = 0;
//    			goalCount = improveGoalMapper.selectCountGoal(userGoal.getGoalid(), userid);
//    			userGoal.setGoalCount(goalCount);
    			if(userGoal.getIcount() != 0){
    				Improve improve = improveGoalMapper.selectBeanByGoalId(userGoal.getGoalid());
    				//拼接pickey
    				impItype(improve, userGoal);
    			}
        		baseResp.setData(userGoal);
        	}else{
        		baseResp.setData(new UserGoal());
        	}
        	baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("selectUserGoal userid = {}, goalid = {}",userid, goalid,e);
        }
        return baseResp;
	}
    
    /**
	 * @author yinxc
	 * itype类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
	 * 2017年4月7日
	 */
	private void impItype(Improve improve, UserGoal userGoal){
		if(null != improve){
			//itype类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
			if("0".equals(improve.getItype())){
				//0 文字进步   brief --- 说明
				userGoal.setPickey(improve.getBrief());
			}else if("1".equals(improve.getItype())){
				//1 图片进步   pickey --- 图片的key
                String photos = getFirstPhotos(improve.getPickey());
                userGoal.setPickey(photos);
			}else{
				//2 视频进步 3 音频进步 4 文件    filekey --- 文件key  视频文件  音频文件 普通文件
                String photos = getFirstPhotos(improve.getPickey());
				userGoal.setPickey(photos);
			}
			userGoal.setItype(improve.getItype());
            userGoal.setStarttime(improve.getCreatetime());
		}
	}

	private String getFirstPhotos(String photos){
        if(!StringUtils.isBlank(photos)){
            JSONArray jsonArray = JSONArray.fromObject(photos);
            if(jsonArray.size()>0){
                photos = jsonArray.getString(0);
            }else
                photos = null;
        }
        return photos;
    }

    /**
     * 更新目标title
     * @param goalId
     * @param title
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
    public BaseResp<UserGoal> updateTitle(long goalId, String title) {
        BaseResp<UserGoal> baseResp = new BaseResp<>();
        try{
            int res = userGoalMapper.updateTitle(goalId,title);
            if(res == 1){
            	UserGoal userGoal = userGoalMapper.selectByGoalId(goalId);
            	baseResp.setData(userGoal);
                return baseResp.initCodeAndDesp();
            }
        }catch (Exception e){
            logger.info("goalId={},title={}",goalId,title,e);
        }
        return baseResp;
    }

    /**
     * 删除目标 修改目标状态
     *        修改目标进步中的状态
     * @param goalId
     * @param userid
     * @param gtype 0:不删除目标进步   1：删除目标进步
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
    public BaseResp<Object> delGoal(long goalId, long userid, String gtype) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            int res = userGoalMapper.delGoal(goalId, userid);
            if(res == 1){
            	if("1".equals(gtype)){
            		return improveService.delGoal(goalId,userid);
            	}
            }
            return baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("delGoal goalId={},userid={}",goalId,userid,e);
        }
        return baseResp;
    }


}
