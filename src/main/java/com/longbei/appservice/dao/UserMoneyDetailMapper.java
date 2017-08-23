package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserMoneyDetail;

public interface UserMoneyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMoneyDetail record);

    int insertSelective(UserMoneyDetail record);

    UserMoneyDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMoneyDetail record);

    int updateByPrimaryKey(UserMoneyDetail record);
    
    /**
	 * @author yinxc
	 * 获取龙币明细列表
	 * 2017年2月27日
	 * return_type
	 */
    List<UserMoneyDetail> selectListByUserid(@Param("userid") long userid, @Param("pageNo") int pageNo, 
    		@Param("pageSize") int pageSize);

    /**
     * 获取龙币总量
     * @param userid  用户id
     * @param origin  //origin： 来源   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用龙币
    //3：发布龙榜    4：赞助榜单    5：赞助教室  6:取消订单返还龙币 7 龙榜审核未通过返回。8 龙榜撤回。   9 龙榜奖品剩余
    // 10.榜单提交审核  11：教室收益   12：教室收费  13：送礼物支出
     * @return
     */
    String selectMoneyNum(@Param("userid") long userid,@Param("origin") String origin);
    
}