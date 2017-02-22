package com.longbei.appservice.service;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserAddress;

/**
 * @author yinxc
 * UserAddress  收货地址
 * 2017年1月18日
 */
public interface UserAddressService {

	/**
	 * @author yinxc
	 * 删除收货地址
	 * 2017年1月18日
	 * return_type
	 */
	BaseResp<Object> deleteByPrimaryKey(Integer id);
	
	/**
	 * @author yinxc
	 * 添加收货地址
	 * 2017年1月18日
	 * return_type
	 */
	BaseResp<Object> insertSelective(UserAddress record);
	
	/**
	 * @author yinxc
	 * 根据id查询收货地址信息
	 * 2017年1月18日
	 * return_type
	 */
	UserAddress selectByPrimaryKey(Integer id);
	
	/**
	 * @author yinxc
	 * 获取默认状态的收货地址
	 * 2017年1月18日
	 * return_type
	 */
	UserAddress selectDefaultAddressByUserid(@Param("userid") long userid);
	
	/**
	 * @author yinxc
	 * 修改
	 * 2017年1月18日
	 * return_type
	 */
	BaseResp<Object> updateByPrimaryKeySelective(UserAddress record);
	
	/**
	 * @author yinxc
	 * 获取所有的订单地址
	 * 2017年1月18日
	 * return_type
	 */
//	BaseResp<Object> selectUserAddressAll();
	
	/**
	 * @author yinxc
	 * 获取订单地址列表(分页)
	 * 2017年1月18日
	 * return_type
	 */
	BaseResp<Object> selectByUserId(long userid, int pageNo, int pageSize);
	
	/**
	 * @author yinxc
	 * 根据id 修改收货地址是否 默认   0 默认  1 非默认
	 * 2017年1月18日
	 * return_type
	 */
	BaseResp<Object> updateIsdefaultByAddressId(String id, long userid, String isdefault);
	
	/**
	 * @author yinxc
	 * 修改收货地址非默认
	 * 2017年1月19日
	 * return_type
	 */
//	BaseResp<Object> updateIsdefaultByUserId(@Param("userid") String userid);
	
	/**
	 * @author yinxc
	 * 删除收货地址(假删)
	 * 2017年1月19日
	 * return_type
	 */
	BaseResp<Object> removeIsdel(String id);
}
