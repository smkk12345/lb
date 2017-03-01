package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserSponsor;

import java.util.Date;

public interface UserSponsorService {
	/**
	 * @Title: insertSponsor
	 * @Description: 添加赞助
	 * @param @param userid 赞助人
	 * @param @param number 赞助龙币或进步币数量
	 * @param @param bid榜单id
	 * @param @param ptype赞助类型 0龙币 1进步币
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年2月27日
	 */
	BaseResp<Object> insertSponsor(long userid,int number,long bid,String ptype);
	/**
	 * @Title: selectSponsorList
	 * @Description: 查询赞助记录列表
	 * @param @param startNum分页起始值 pageSize每页显示条数
	 * @auther IngaWu
	 * @currentdate:2017年2月27日
	 */
	BaseResp<Object> selectSponsorList(int startNum, int pageSize);
	/**
	 * @Title: updateSponsor
	 * @Description: 修改赞助，追加数量
	 * @param @param userid 赞助人
	 * @param @param number 赞助龙币或进步币数量
	 * @param @param bid榜单
	 * @param @param ptype赞助类型 0龙币 1进步币
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年2月27日
	 */
	BaseResp<Object> updateSponsor(long userid,int number,long bid,String ptype);
	/**
	 * @Title: selectByUseridAndBid
	 * @Description: 查询赞助记录
	 * @param @param userid 赞助人
	 * @param @param bid榜单
	 * @auther IngaWu
	 * @currentdate:2017年2月27日
	 */
	UserSponsor selectByUseridAndBid(long userid,long bid);

}
