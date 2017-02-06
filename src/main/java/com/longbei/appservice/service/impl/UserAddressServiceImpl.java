package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserAddressMapper;
import com.longbei.appservice.entity.UserAddress;
import com.longbei.appservice.service.UserAddressService;

@Service("userAddressService")
public class UserAddressServiceImpl implements UserAddressService {

	@Autowired
	private UserAddressMapper userAddressMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserAddressServiceImpl.class);
	
	@Override
	public BaseResp<Object> deleteByPrimaryKey(Integer id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = delete(id);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByPrimaryKey id={},msg={}",id,e);
		}
		return reseResp;
	}
	
	private boolean delete(Integer id){
		int temp = userAddressMapper.deleteByPrimaryKey(id);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> insertSelective(UserAddress record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserAddress> list = userAddressMapper.selectByUserId(record.getUserid().toString(), 0, 10);
			if(null == list){
				//第一次添加收货地址时，设为默认地址
				record.setIsdefault("0");
			}
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean insert(UserAddress record){
		int temp = userAddressMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public UserAddress selectByPrimaryKey(Integer id) {
		UserAddress userAddress = null;
		try {
			userAddress = userAddressMapper.selectByPrimaryKey(id);
		} catch (Exception e) {
			logger.error("selectByPrimaryKey id={},msg={}",id,e);
		}
		return userAddress;
	}

	@Override
	public UserAddress selectDefaultAddressByUserid(String userid) {
		UserAddress userAddress = null;
		try {
			userAddress = userAddressMapper.selectDefaultAddressByUserid(userid);
		} catch (Exception e) {
			logger.error("selectDefaultAddressByUserid userid={},msg={}",userid,e);
		}
		return userAddress;
	}

	@Override
	public BaseResp<Object> updateByPrimaryKeySelective(UserAddress record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int temp = userAddressMapper.updateByPrimaryKeySelective(record);
			if (temp > 0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByPrimaryKeySelective record={},msg={}",record,e);
		}
		return reseResp;
	}

//	@Override
//	public BaseResp<Object> selectUserAddressAll() {
//		BaseResp<Object> reseResp = new BaseResp<>();
//		try {
//			List<UserAddress> list = userAddressMapper.selectUserAddressAll();
//			reseResp.setData(list);
//			if (null != list && list.size() > 0) {
//				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
//			}else {
//				reseResp.initCodeAndDesp(Constant.STATUS_SYS_20, Constant.RTNINFO_SYS_20);
//			}
//		} catch (Exception e) {
//			logger.error("selectUserAddressAll msg={}",e);
//		}
//		return reseResp;
//	}

	@Override
	public BaseResp<Object> selectByUserId(String userid, int pageNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserAddress> list = userAddressMapper.selectByUserId(userid, pageNo, pageSize);
			reseResp.setData(list);
			if (null != list && list.size() > 0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_20, Constant.RTNINFO_SYS_20);
			}
		} catch (Exception e) {
			logger.error("selectByUserId userid={},pageNo={},pageSize={},msg={}",userid,pageNo,pageSize,e);
		}
		return reseResp;
	}

	/**
	 * @author yinxc
	 * 根据id 修改收货地址是否 默认   0 默认  1 非默认   并修改已默认的为非默认状态
	 * 2017年1月18日
	 * return_type
	 */
	@SuppressWarnings("unchecked")
	public BaseResp<Object> updateIsdefaultByAddressId(String id, String userid, String isdefault) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean result = updateNotdefaultByUserId(userid);
			if(!result){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
			}
			int temp = userAddressMapper.updateIsdefaultByAddressId(id, isdefault);
			if (temp>0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsdefaultByAddressId id={},isdefault={},msg={}",id, isdefault, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 修改已默认的为非默认状态
	 * 2017年1月18日
	 * return_type
	 */
	private boolean updateNotdefaultByUserId(String userid){
		int temp = userAddressMapper.updateIsdefaultByUserId(userid);
		return temp > 0 ? true : false;
	}

	/**
	 * @author yinxc
	 * 删除收货地址(假删)
	 * 2017年1月19日
	 * return_type
	 */
	@Override
	public BaseResp<Object> removeIsdel(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int temp = userAddressMapper.removeIsdel(id);
			if (temp>0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("removeIsdel id={},msg={}",id, e);
		}
		return reseResp;
	}

}
