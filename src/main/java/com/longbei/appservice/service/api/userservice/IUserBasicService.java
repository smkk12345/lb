package com.longbei.appservice.service.api.userservice;


import com.longbei.appservice.common.BaseResp;

import feign.Param;
import feign.RequestLine;

public interface IUserBasicService {
	/**
	* @Title: add
	* @Description:  注册登统中心
	* @param @param userid
	* @param @param username
	* @param @param password
	* @auther smkk
	* @currentdate:2017年1月16日
	 */
	 @RequestLine("GET /user/add?userid={userid}&username={username}&password={password}")
	 BaseResp<Object> add(@Param("userid") Long userid,@Param("username") String username,@Param("password") String password);
	 /**
	 * @Title: gettoken
	 * @Description: 移动端调用api请求需要的token
	 * @param @param userid
	 * @param @param username
	 * @param @param password
	 * @param @return
	 * @auther smkk
	 * @currentdate:2017年1月16日
	  */
	 @RequestLine("GET /user/gettoken?username={username}&password={password}")
	 BaseResp<Object> gettoken(@Param("username") String username,@Param("password") String password);
	 /**
	 * @Title: getapitoken
	 * @Description: service 之间api调用的令牌
	 * @auther smkk
	 * @currentdate:2017年1月16日
	  */
	 @RequestLine("GET /common/getServiceToken?servicename={servicename}&exp={exp}")
	 BaseResp<Object> getServiceToken(@Param("servicename")String servicename,@Param("exp")long exp);
	 /**
	 * @Title: updatepwd
	 * @Description: 更新登统中心密码
	 * @param @param servicename
	 * @param @param exp
	 * @auther smkk
	 * @currentdate:2017年1月16日
	 */
	 @RequestLine("GET /user/updatepwd?username={username}&oldpwd={oldpwd}&newpwd={newpwd}")
	 BaseResp<Object> updatepwd(@Param("username")String username,@Param("oldpwd")String oldpwd,@Param("newpwd")String newpwd);

	@RequestLine("GET /user/updatepwdById?userid={userid}&oldpwd={oldpwd}&newpwd={newpwd}")
	BaseResp<Object> updatepwdById(@Param("userid")long userid,@Param("oldpwd")String oldpwd,@Param("newpwd")String newpwd);

	/**
	 * @Title: thirdlogin
	 * @Description: 第三方登录
	 * @param @param openid
	 * @param @param utype
	 * @auther smkk
	 * @currentdate:2017年1月18日
	 */
	 @RequestLine("GET /user/thirdlogin?openid={openid}&utype={utype}")
	 BaseResp<Object> thirdlogin(@Param("openid")String openid,@Param("utype")String utype);
	 /**
	 * @Title: bindingThird
	 * @Description: 绑定帐号
	 * @param @param openid
	 * @param @param utype
	 * @param @param userid
	 * @param @return
	 * @auther smkk
	 * @currentdate:2017年1月18日
	  */
	 @RequestLine("GET /user/bindingThird?openid={openid}&utype={utype}&userid={userid}")
	 BaseResp<Object> bindingThird(@Param("openid")String openid,@Param("utype")String utype,@Param("userid")Long userid);
	 /**
	 * @Title: hasbindingThird
	 * @Description: 判断是否绑定第三方信息
	 * @param @param openid
	 * @param @param utype
	 * @param @param username
	 * @auther smkk
	 * @currentdate:2017年1月19日
	  */
	 @RequestLine("GET /user/hasbindingThird?openid={openid}&utype={utype}&username={username}")
	 BaseResp<Object> hasbindingThird(@Param("openid")String openid,@Param("utype")String utype,@Param("username")String username);
	 
}
