package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserInterestsMapper;
import com.longbei.appservice.entity.UserInterests;
import com.longbei.appservice.service.UserInterestsService;

@Service("userInterestsService")
public class UserInterestsServiceImpl implements UserInterestsService {

	@Autowired
	private UserInterestsMapper userInterestsMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserInterestsServiceImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertInterests(String userid,String ids) {
		BaseResp<Object> reseResp = new BaseResp<>();
		UserInterests data = new UserInterests();
		Date date = new Date();
		String idsString[] = ids.split(",");
			for (String id : idsString) {
				data.setUserid(userid);
				data.setPtype(id);
				data.setCreatetime(date);
				try {
				   int temp = userInterestsMapper.insert(data);
				   if(temp > 0 ){
				 	reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				   }
				} catch (Exception e){
					logger.error("insertInterests userid = {},ptype = {}",userid,id, e);
				};
			}
		return reseResp;
	}
}
