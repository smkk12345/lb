package com.longbei.appservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserIdcardMapper;
import com.longbei.appservice.entity.UserIdcard;
import com.longbei.appservice.service.UserIdcardService;

@Service("userIdcardService")
public class UserIdcardServiceImpl implements UserIdcardService {

	@Autowired
	private UserIdcardMapper userIdcardMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserIdcardServiceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insert(UserIdcard record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			UserIdcard userIdcard = selectByUserid(record.getUserid().toString());
			if(userIdcard != null){
				//存在    修改
				boolean temp = updateUserIdcard(record);
				if (temp) {
					return reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				}
			}
			//添加
			boolean temp = insertUserIdcard(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insert record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean insertUserIdcard(UserIdcard record){
		int temp = userIdcardMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public UserIdcard selectByUserid(String userid) {
		if(StringUtils.hasBlankParams(userid)){
			return null;
		}
		return userIdcardMapper.selectByUserid(userid);
	}

	@Override
	public BaseResp<Object> update(UserIdcard record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateUserIdcard(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("update record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean updateUserIdcard(UserIdcard record){
		int temp = userIdcardMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

}
