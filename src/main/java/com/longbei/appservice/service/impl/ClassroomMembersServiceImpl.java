package com.longbei.appservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.ClassroomMembersMapper;
import com.longbei.appservice.dao.UserCardMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.ClassroomMembers;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.UserCard;
import com.longbei.appservice.service.ClassroomMembersService;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.UserBehaviourService;
import com.longbei.appservice.service.UserMoneyDetailService;
import com.longbei.appservice.service.UserMsgService;
import com.longbei.appservice.service.UserRelationService;

@Service("classroomMembersService")
public class ClassroomMembersServiceImpl implements ClassroomMembersService {
	
	@Autowired
	private ClassroomMembersMapper classroomMembersMapper;
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private UserMongoDao userMongoDao;
//	@Autowired
//	private ImproveClassroomMapper improveClassroomMapper;
	@Autowired
	private UserBehaviourService userBehaviourService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserMsgService userMsgService;
	@Autowired
	private ImproveService improveService;
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private UserCardMapper userCardMapper;
	@Autowired
	private UserMoneyDetailService userMoneyDetailService;
	
	
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomMembersServiceImpl.class);

	
	/**
    * @Description: 添加教室成员
    * 先判断教室参与人数是否已满
    * @param @param classroomid 教室id 
    * @param @param userid
    * 成员在加入教室之前，如果该教室收费，需先交费后才可加入
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertClassroomMembers(ClassroomMembers record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//成员在加入教室之前，如果该教室收费，需先交费后才可加入
			Classroom classroom = classroomMapper.selectByPrimaryKey(record.getClassroomid());
			if(null == classroom){
				return reseResp;
			}
			UserInfo userInfo = userService.selectJustInfo(record.getUserid());
			//isfree  是否免费。0 免费 1 收费
			//classinvoloed---教室参与人数     classlimited---教室限制人数
			if(classroom.getClassinvoloed() == classroom.getClasslimited()){
				//先判断教室参与人数是否已满
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_36, Constant.RTNINFO_SYS_36);
			}
			if("1".equals(classroom.getIsfree())){
				//需要交费    charge --- 课程价格
				Integer charge = classroom.getCharge();
				//判断用户龙币是否充足
				if(charge > userInfo.getTotalmoney()){
					return reseResp.initCodeAndDesp(Constant.STATUS_SYS_23, Constant.RTNINFO_SYS_23);
				}
				//用户扣费
				userMoneyDetailService.insertPublic(record.getUserid(), "12", charge, 0);
				//创建人教室收益
				userMoneyDetailService.insertPublic(Long.parseLong(Constant.SQUARE_USER_ID), "11", charge, record.getUserid());
				//修改教室总收益
				classroomMapper.updateEarningsByClassroomid(record.getClassroomid(), charge);
				
			}
			//itype 0—加入教室 1—退出教室     为null查全部
			ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(record.getClassroomid(), record.getUserid(), "0");
			if(null != members){
				// 用户已加入教室
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_37, Constant.RTNINFO_SYS_37);
			}
			record.setCusername(userInfo.getUsername());
			boolean temp = insert(record);
			if (temp) {
				//修改教室教室参与人数 classinvoloed
				classroomMapper.updateClassinvoloedByClassroomid(record.getClassroomid(), 1);
				//加入圈子成功获得龙分
				userBehaviourService.pointChange(userInfo,"DAILY_ADDCLASSROOM",classroom.getPtype(),null,0,0);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
			reseResp.setData(record);
		} catch (Exception e) {
			logger.error("insertClassroomMembers record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean insert(ClassroomMembers record){
		int temp = 0;
		//判断当前用户是否退出过教室，若有则修改
		//itype 0—加入教室 1—退出教室     为null查全部
		ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(record.getClassroomid(), record.getUserid(), "1");
		if(null != members){
			temp = classroomMembersMapper.updateItypeByClassroomidAndUserid(members.getClassroomid(), members.getUserid(), "0");
		}else{
			temp = classroomMembersMapper.insertSelective(record);
		}
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<List<ClassroomMembers>> selectListByClassroomid(long classroomid, String userid, int startNum, int endNum) {
		BaseResp<List<ClassroomMembers>> reseResp = new BaseResp<>();
		try {
			List<ClassroomMembers> list = classroomMembersMapper.selectListByClassroomid(classroomid, startNum, endNum);
			if(null != list && list.size()>0){
				for (ClassroomMembers classroomMembers : list) {
					initMsgUserInfoByUserid(classroomMembers, userid);
					//教室所发的微进步总数
//					int allimp = improveClassroomMapper.selectCountByClassroomidAndUserid(classroomid + "", 
//							classroomMembers.getUserid().toString());
//					classroomMembers.setAllimp(allimp);
				}
			}
			reseResp.setData(list);
			if(startNum == 0 && list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_32);
				return reseResp;
			}
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectListByClassroomid classroomid = {}, startNum = {}, endNum = {}", 
					classroomid, startNum, endNum, e);
		}
		return reseResp;
	}

	@Override
	public List<ClassroomMembers> selectInsertByUserid(long userid, int startNum, int endNum) {
		try {
			List<ClassroomMembers> list = classroomMembersMapper.selectInsertByUserid(userid, startNum, endNum);
			if(null != list && list.size()>0){
				for (ClassroomMembers classroomMembers : list) {
					initMsgUserInfoByUserid(classroomMembers, userid + "");
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("selectInsertByUserid userid = {}, startNum = {}, endNum = {}", 
					userid, startNum, endNum, e);
		}
		return null;
	}

	@Override
	public ClassroomMembers selectListByClassroomidAndUserid(long classroomid, long userid, String itype) {
		try {
			ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, itype);
			if(null != classroomMembers){
				String cnickname = "";
				if(!StringUtils.hasBlankParams(classroomMembers.getUserid().toString())){
		    		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(classroomMembers.getUserid()));
		    		if(null != appUserMongoEntity){
		    			cnickname = appUserMongoEntity.getNickname();
		    		}
		    	}
				classroomMembers.setCnickname(cnickname);
			}
			return classroomMembers;
		} catch (Exception e) {
			logger.error("selectListByClassroomidAndUserid classroomid = {}, userid = {}, itype = {}", 
					classroomid, userid, itype, e);
		}
		return null;
	}
	
	/**
	 * @author yinxc
	 * 教室老师剔除成员---推送消息
	 * 2017年7月7日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> quitClassroom(long classroomid, long userid, long currentUserId, String itype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			if(null == classroom){
				return reseResp;
			}
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			if(null == userCard){
				return reseResp;
			}
			//判断当前用户是否是老师
			if(userCard.getUserid() != currentUserId){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1106, Constant.RTNINFO_SYS_1106);
			}
			
			boolean temp = update(classroomid, userid, itype);
			if (temp) {
				//修改教室教室参与人数 classinvoloed
				classroomMapper.updateClassinvoloedByClassroomid(classroomid, -1);
				//推送消息
				String remark = Constant.MSG_CLASSROOM_MODEL;
				userMsgService.insertMsg(Constant.SQUARE_USER_ID, userid + "",
						"", "12", classroomid + "", remark, "2", "54", "教室删除成员", 0, "", "");
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateItypeByClassroomidAndUserid classroomid = {}, userid = {}, itype = {}", 
					classroomid, userid, itype, e);
		}
		return reseResp;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectRoomMemberDetail(Long classroomid, Long userid, Long currentUserId) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(userid+"");
	        //获取好友昵称
	        this.userRelationService.updateFriendRemark(currentUserId,appUserMongoEntity);
	        if(appUserMongoEntity == null) {
	        	return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
	        }
	        Map<String,Object> resultMap = new HashMap<String,Object>();
			ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
			if(members == null) {
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
			}
			if(currentUserId != null){
                if(this.userRelationService.checkIsFans(currentUserId,userid)){
                    resultMap.put("isfans",1);
                }else{
                    resultMap.put("isfans",0);
                }
                if(this.userRelationService.checkIsFriend(currentUserId,userid)){
                    resultMap.put("isFriends",1);
                }else{
                    resultMap.put("isFriends",0);
                }
            }else{
                resultMap.put("isfans",0);
                resultMap.put("isFriends",0);
            }
			resultMap.put("likes", members.getLikes());
            resultMap.put("flowers", members.getFlowers());
            resultMap.put("nickname", appUserMongoEntity.getNickname());
            resultMap.put("avatar", appUserMongoEntity.getAvatar());
            resultMap.put("userid", appUserMongoEntity.getUserid());
            resultMap.put("icount", members.getIcount());
            resultMap.put("vcertification", appUserMongoEntity.getVcertification());
            Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
            if(classroom == null) {
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
			}
            resultMap.put("ranktitle", classroom.getClasstitle());
            resultMap.put("classroomid", classroomid);
			baseResp.setData(resultMap);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectRoomMemberDetail classroomid = {}, userid = {}, currentUserId = {}", 
					classroomid, userid, currentUserId, e);
		}
		return baseResp;
	}

	/**
	 * @author yinxc
	 * 成员退出教室
	 * 2017年2月28日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室
	 */
	@Override
	public BaseResp<Object> updateItypeByClassroomidAndUserid(long classroomid, long userid, String itype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(classroomid, userid, itype);
			if (temp) {
				//修改教室教室参与人数 classinvoloed
				classroomMapper.updateClassinvoloedByClassroomid(classroomid, -1);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateItypeByClassroomidAndUserid classroomid = {}, userid = {}, itype = {}", 
					classroomid, userid, itype, e);
		}
		return reseResp;
	}
	
	private boolean update(long classroomid, long userid, String itype){
		int temp = classroomMembersMapper.updateItypeByClassroomidAndUserid(classroomid, userid, itype);
		return temp > 0 ? true : false;
	}
	
	
	//------------------------公用方法，初始化教室成员用户信息------------------------------------------
	/**
     * 初始化教室成员用户信息 ------Userid
     */
    private void initMsgUserInfoByUserid(ClassroomMembers classroomMembers, String userid){
    	if(!StringUtils.hasBlankParams(classroomMembers.getUserid().toString())){
    		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(classroomMembers.getUserid()));
    		if(null != appUserMongoEntity){
    			if(StringUtils.isNotEmpty(userid)){
    				this.userRelationService.updateFriendRemark(userid,appUserMongoEntity);
    			}
    			classroomMembers.setAppUserMongoEntityUserid(appUserMongoEntity);
            }else{
            	classroomMembers.setAppUserMongoEntityUserid(new AppUserMongoEntity());
            }
    	}
    }

    
    
    
    
    
    //-----------------------------admin调用-------------------------------------
	@Override
	public BaseResp<Page<ClassroomMembers>> selectPcMembersList(ClassroomMembers members, int startNum, int endNum) {
		BaseResp<Page<ClassroomMembers>> reseResp = new BaseResp<>();
		Page<ClassroomMembers> page = new Page<>(startNum/endNum+1, endNum);
		try {
			int totalcount = classroomMembersMapper.selectSearchCount(members);
			Page.setPageNo(startNum/endNum+1,totalcount,endNum);
			List<ClassroomMembers> list = classroomMembersMapper.selectSearchList(members, startNum, endNum);
			if(null != list && list.size()>0){
				String cnickname = "";
				for (ClassroomMembers classroomMembers : list) {
					if(!StringUtils.hasBlankParams(classroomMembers.getUserid().toString())){
			    		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(classroomMembers.getUserid()));
			    		if(null != appUserMongoEntity){
			    			cnickname = appUserMongoEntity.getNickname();
			    		}
			    	}
					classroomMembers.setCnickname(cnickname);
				}
			}
			page.setTotalCount(totalcount);
            page.setList(list);
			reseResp.setData(page);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectPcMembersList members = {}, startNum = {}, endNum = {}", 
					members.getClassroomid(), startNum, endNum, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> updateStatus(String status, String userid, String classroomid, String improveid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		
        try {
//        	Classroom classroom = classroomMapper.selectByPrimaryKey(Long.parseLong(classroomid));
        	//status 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理  4: 已忽略
            if ("1".equals(status)) {
            	improveService.removeImprove(userid, improveid, Constant.IMPROVE_CLASSROOM_TYPE, classroomid);
            	Improve improve = improveService.selectImproveByImpidMuc(Long.parseLong(improveid),
                        userid, Constant.IMPROVE_CLASSROOM_TYPE, classroomid);
                String remark = Constant.MSG_IMP_DEL_MODEL;
                if (null != improve) {
                    if (!StringUtils.isBlank(improve.getBrief())) {
                        if (improve.getBrief().length() >= 20) {
                            //抓取内容20个字
                            String brief = improve.getBrief().substring(0, 20);
                            remark = remark.replace("n", "(" + brief + ")");
                        } else {
                            remark = remark.replace("n", "(" + improve.getBrief() + ")");
                        }
                    } else {
                        remark = remark.replace("n", "");
                    }
                }
                userMsgService.insertMsg(Constant.SQUARE_USER_ID, userid,
                        improveid, "12",
                        classroomid, remark, "0", "59", "删除教室成员进步", 0, "", "", AppserviceConfig.h5_helper);
            }
            reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("remove classroom Improve status = {}, userid = {} , classroomid = {}, improveid = {} ", 
            		status, userid, classroomid, improveid, e);
        }
        return reseResp;
	}

	/**
	 * @author yinxc
	 * 教室老师剔除成员---推送消息
	 * 2017年7月7日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室
	 */
	@Override
	public BaseResp<Object> quitClassroomByPC(long classroomid, long userid, String itype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(classroomid, userid, itype);
			if (temp) {
				//修改教室教室参与人数 classinvoloed
				classroomMapper.updateClassinvoloedByClassroomid(classroomid, -1);
				//推送消息
				String remark = Constant.MSG_CLASSROOM_MODEL;
				userMsgService.insertMsg(Constant.SQUARE_USER_ID, userid + "",
						"", "12", classroomid + "", remark, "2", "54", "教室删除成员", 0, "", "", AppserviceConfig.h5_helper);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateItypeByClassroomidAndUserid classroomid = {}, userid = {}, itype = {}", 
					classroomid, userid, itype, e);
		}
		return reseResp;
	}

}
