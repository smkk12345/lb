package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserAddress;

public interface UserAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAddress record);

    int insertSelective(UserAddress record);
    
    List<UserAddress> selectUserAddressAll();
    
    /**
	 * @author yinxc
	 * 获取订单地址列表(分页)
	 * 2017年1月18日
	 * return_type
	 */
    List<UserAddress> selectByUserId(@Param("userid") String userid, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    UserAddress selectByPrimaryKey(Integer id);
    
    /**
	 * @author yinxc
	 * 获取默认状态的收货地址
	 * 2017年1月18日
	 * return_type
	 */
    UserAddress selectDefaultAddressByUserid(@Param("userid") String userid);

    int updateByPrimaryKeySelective(UserAddress record);

    int updateByPrimaryKey(UserAddress record);
    
    /**
	 * @author yinxc
	 * 根据id 修改收货地址是否 默认   0 默认  1 非默认
	 * 2017年1月18日
	 * return_type
	 */
    int updateIsdefaultByAddressId(@Param("id") String id, @Param("isdefault") String isdefault);
    
    /**
	 * @author yinxc
	 * 修改收货地址非默认
	 * 2017年1月19日
	 * return_type
	 */
    int updateIsdefaultByUserId(@Param("userid") String userid);
    
    /**
	 * @author yinxc
	 * 删除收货地址(假删)
	 * 2017年1月19日
	 * return_type
	 */
    int removeIsdel(@Param("id") String id);
    
}