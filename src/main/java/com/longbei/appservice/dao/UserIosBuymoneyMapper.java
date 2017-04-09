package com.longbei.appservice.dao;

import java.util.List;

import com.longbei.appservice.entity.UserIosBuymoney;

/** 
* @ClassName: UserIosBuymoneyMapper 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yinxc
* @date 2017年4月9日 下午3:38:57 
*  
*/
public interface UserIosBuymoneyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserIosBuymoney record);

    int insertSelective(UserIosBuymoney record);

    UserIosBuymoney selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserIosBuymoney record);

    int updateByPrimaryKey(UserIosBuymoney record);
    
    /**
    * @Title: selectMoneyAllList 
    * @Description: IOS购买龙币菜单
    * @param @return    设定文件 
    * @return List<UserIosBuymoney>    返回类型
     */
    List<UserIosBuymoney> selectMoneyAllList();
    
}