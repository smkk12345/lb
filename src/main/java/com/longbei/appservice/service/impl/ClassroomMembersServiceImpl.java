package com.longbei.appservice.service.impl;

import java.util.List;

import com.longbei.appservice.common.constant.Constant_Perfect;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.ClassroomMembersMapper;
import com.longbei.appservice.dao.ImproveClassroomMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.ClassroomMembers;
import com.longbei.appservice.service.ClassroomMembersService;
import com.longbei.appservice.service.UserBehaviourService;

@Service("classroomMembersService")
public class ClassroomMembersServiceImpl implements ClassroomMembersService {
	
	@Autowired
	private ClassroomMembersMapper classroomMembersMapper;
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private ImproveClassroomMapper improveClassroomMapper;
	@Autowired
	private UserBehaviourService userBehaviourService;
	@Autowired
	private UserService userService;
	
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
			//isfree  是否免费。0 免费 1 收费
			if(null != classroom){
				//classinvoloed---教室参与人数     classlimited---教室限制人数
				if(classroom.getClassinvoloed() == classroom.getClasslimited()){
					//先判断教室参与人数是否已满
					return reseResp.initCodeAndDesp(Constant.STATUS_SYS_36, Constant.RTNINFO_SYS_36);
				}
				if("1".equals(classroom.getIsfree())){
					//需要交费    charge --- 课程价格
					
					
					
					
					
				}
			}
			//itype 0—加入教室 1—退出教室     为null查全部
			ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(record.getClassroomid(), record.getUserid(), "0");
			if(null != members){
				// 用户已加入教室
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_37, Constant.RTNINFO_SYS_37);
			}
			boolean temp = insert(record);
			if (temp) {
				//修改教室教室参与人数 classinvoloed
				classroomMapper.updateClassinvoloedByClassroomid(record.getClassroomid(), 1);
				//加入圈子成功获得龙分
				UserInfo userInfo = userService.selectJustInfo(record.getUserid());
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
			members.setItype(0);
			temp = classroomMembersMapper.updateByPrimaryKeySelective(members);
		}else{
			temp = classroomMembersMapper.insertSelective(record);
		}
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> selectListByClassroomid(long classroomid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<ClassroomMembers> list = classroomMembersMapper.selectListByClassroomid(classroomid, startNum, endNum);
			if(null != list && list.size()>0){
				for (ClassroomMembers classroomMembers : list) {
					initMsgUserInfoByUserid(classroomMembers);
					//教室所发的微进步总数
					int allimp = improveClassroomMapper.selectCountByClassroomidAndUserid(classroomid, classroomMembers.getUserid());
					classroomMembers.setAllimp(allimp);
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
					initMsgUserInfoByUserid(classroomMembers);
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
			return classroomMembers;
		} catch (Exception e) {
			logger.error("selectListByClassroomidAndUserid classroomid = {}, userid = {}, itype = {}", 
					classroomid, userid, itype, e);
		}
		return null;
	}

	/**
	 * @author yinxc
	 * 教室剔除成员
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
    private void initMsgUserInfoByUserid(ClassroomMembers classroomMembers){
    	if(!StringUtils.hasBlankParams(classroomMembers.getUserid().toString())){
    		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(classroomMembers.getUserid()));
    		classroomMembers.setAppUserMongoEntityUserid(appUserMongoEntity);;
    	}
    }

}
