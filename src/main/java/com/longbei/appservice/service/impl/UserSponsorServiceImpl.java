package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.dao.UserSponsorMapper;
import com.longbei.appservice.dao.UserMoneyDetailMapper;
import com.longbei.appservice.entity.UserSponsor;
import com.longbei.appservice.service.UserSponsorService;
import com.longbei.appservice.service.UserMoneyDetailService;
import com.longbei.appservice.service.RankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("serSponsorService")
public class UserSponsorServiceImpl implements UserSponsorService {

	@Autowired
	private UserSponsorMapper userSponsorMapper;
	@Autowired
	private UserMoneyDetailMapper userMoneyDetailMapper;
	@Autowired
	private UserMoneyDetailService userMoneyDetailService;
	@Autowired
	private RankService rankService;
	
	private static Logger logger = LoggerFactory.getLogger(UserSponsorServiceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertSponsor(long userid,int number,long bid,String ptype) {
		UserSponsor data = new UserSponsor();
		data.setUserid(userid);
		data.setNumber(number);
		data.setBid(bid);
		data.setPtype(ptype);
		Date date = new Date();
		data.setCreatetime(date);
		try {
			String origin = "4";
			long friendid = 0;
			BaseResp baseResp2 = userMoneyDetailService.insertPublic(userid,origin, number,friendid);
			int m = userSponsorMapper.insertSponsor(data);
			boolean n = rankService.updateSponsornumAndSponsormoney( );
			if(ResultUtil.isSuccess(baseResp2)&&m == 1){
				return baseResp2;
			 }
		} catch (Exception e) {
			logger.error("insertSponsor error and msg={}",e);
		}
		return BaseResp.fail();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectSponsorList(int startNum,int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			List<UserSponsor> list = userSponsorMapper.selectSponsorList(startNum,pageSize);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectSponsorList error and msg={}",e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateSponsor(long userid,int number,long bid,String ptype) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		UserSponsor data = new UserSponsor();
		data.setUserid(userid);
		data.setNumber(number);
		data.setBid(bid);
		data.setPtype(ptype);
		Date date = new Date();
		data.setCreatetime(date);
		try {
			String origin = "4";
			long friendid = 0;
			baseResp = userMoneyDetailService.insertPublic(userid,origin, number,friendid);
			int m = userSponsorMapper.updateSponsor(data);
			boolean n = rankService.updateSponsornumAndSponsormoney( );
			if(ResultUtil.isSuccess(baseResp)&&m == 1){
				return baseResp;
			}
		} catch (Exception e) {
			logger.error("updateSponsor error and msg={}",e);
		}
		return BaseResp.fail();
	}

	@Override
	public UserSponsor selectByUseridAndBid(long userid,long bid){
		UserSponsor userSponsor = userSponsorMapper.selectByUseridAndBid(userid,bid);
		return userSponsor;
	}

}


