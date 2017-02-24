package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserImpCoinDetailMapper;
import com.longbei.appservice.entity.UserImpCoinDetail;
import com.longbei.appservice.service.UserImpCoinDetailService;

@Service("userImpCoinDetailService")
public class UserImpCoinDetailServiceImpl implements UserImpCoinDetailService {
	
	@Autowired
	private UserImpCoinDetailMapper userImpCoinDetailMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserImpCoinDetailServiceImpl.class);

	@Override
	public BaseResp<Object> insertSelective(UserImpCoinDetail record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record = {}, msg = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean insert(UserImpCoinDetail record){
		int temp = userImpCoinDetailMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}
	
	@Override
	public UserImpCoinDetail selectByPrimaryKey(Integer id) {
		UserImpCoinDetail userImpCoinDetail = userImpCoinDetailMapper.selectByPrimaryKey(id);
		return userImpCoinDetail;
	}

	@Override
	public BaseResp<Object> updateByPrimaryKeySelective(UserImpCoinDetail record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByPrimaryKeySelective record = {}, msg = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean update(UserImpCoinDetail record){
		int temp = userImpCoinDetailMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> selectListByUserid(long userid, int pageNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserImpCoinDetail> list = userImpCoinDetailMapper.selectListByUserid(userid, pageNo, pageSize);
			//数据拼接逻辑未写---------------
			
			
			reseResp.setData(list);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectListByUserid userid = {}, pageNo = {}, pageSize = {}, msg = {}", userid, pageNo, pageSize, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> deleteByPrimaryKey(Integer id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = delete(id);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByPrimaryKey id = {}, msg = {}", id, e);
		}
		return reseResp;
	}
	
	private boolean delete(Integer id){
		int temp = userImpCoinDetailMapper.deleteByPrimaryKey(id);
		return temp > 0 ? true : false;
	}

}
