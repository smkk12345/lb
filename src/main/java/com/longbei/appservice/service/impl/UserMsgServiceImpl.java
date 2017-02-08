package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserMsgMapper;
import com.longbei.appservice.entity.UserMsg;
import com.longbei.appservice.service.UserMsgService;

@Service("userMsgService")
public class UserMsgServiceImpl implements UserMsgService {

	@Autowired
	private UserMsgMapper userMsgMapper;
//	@Autowired
//	private UserMongoDao userMongoDao;
	
	private static Logger logger = LoggerFactory.getLogger(UserMsgServiceImpl.class);
	
	@Override
	public BaseResp<Object> deleteByid(Integer id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = delete(id);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByid id={},msg={}",id,e);
		}
		return reseResp;
	}
	
	private boolean delete(Integer id){
		int temp = userMsgMapper.deleteByPrimaryKey(id);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> deleteByUserid(long userid, String mtype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = deleteUserid(userid, mtype);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByUserid userid={},msg={}",userid,e);
		}
		return reseResp;
	}
	
	private boolean deleteUserid(long userid, String mtype){
		int temp = userMsgMapper.deleteByUserid(userid, mtype);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> insertSelective(UserMsg record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean insert(UserMsg record){
		int temp = userMsgMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public UserMsg selectByid(Integer id) {
		UserMsg userMsg = userMsgMapper.selectByPrimaryKey(id);
		return userMsg;
	}

	@Override
	public BaseResp<Object> selectByUserid(long userid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserMsg> list = userMsgMapper.selectByUserid(userid, startNum, endNum);
			if (null != list && list.size()>0) {
//				AppUserMongoEntity mongoEntity = userMongoDao.getAppUser(userid+"");
//				if(null != mongoEntity){
//					
//				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_28, Constant.RTNINFO_SYS_28);
			}
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectByUserid userid={},msg={}",userid,e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> updateByid(UserMsg record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByid record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean update(UserMsg record){
		int temp = userMsgMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> updateIsreadByid(Integer id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateId(id);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsreadByid id={},msg={}",id,e);
		}
		return reseResp;
	}
	
	private boolean updateId(Integer id){
		int temp = userMsgMapper.updateIsreadByid(id);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> updateIsreadByUserid(long userid, String mtype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateUserid(userid, mtype);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsreadByUserid userid = {}, msg = {}", userid, e);
		}
		return reseResp;
	}
	
	private boolean updateUserid(long userid, String mtype){
		int temp = userMsgMapper.updateIsreadByUserid(userid, mtype);
		return temp > 0 ? true : false;
	}

}
