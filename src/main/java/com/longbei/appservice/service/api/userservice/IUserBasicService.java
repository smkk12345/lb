package com.longbei.appservice.service.api.userservice;


import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserBasic;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("userServiceYXC")
@RequestMapping("userService")
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

	@RequestMapping(method = RequestMethod.GET, value = "/user/add")
	BaseResp<Object> add(@RequestParam("userid") Long userid,
						 @RequestParam("username") String username,
						 @RequestParam("password") String password);
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
	@RequestMapping(method = RequestMethod.GET, value = "/user/gettoken")
	BaseResp<Object> gettoken(@RequestParam("username") String username,
							  @RequestParam("password") String password);

	/**
	 * 获取token之通过手机号
	 * @param username
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/user/gettokenWithoutPwd")
	BaseResp<Object> gettokenWithoutPwd(@RequestParam("username") String username);

	/**
	 * @Title: getapitoken
	 * @Description: service 之间api调用的令牌
	 * @auther smkk
	 * @currentdate:2017年1月16日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/common/getServiceToken")
	BaseResp<Object> getServiceToken(@RequestParam("servicename") String servicename,
									 @RequestParam("exp") long exp);
	/**
	 * @Title: updatepwd
	 * @Description: 更新登统中心密码
	 * @param @param servicename
	 * @param @param exp
	 * @auther smkk
	 * @currentdate:2017年1月16日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/user/updatepwd")
	BaseResp<Object> updatepwd(@RequestParam("username") String username,
							   @RequestParam("oldpwd") String oldpwd,
							   @RequestParam("newpwd") String newpwd);

	@RequestMapping(method = RequestMethod.GET, value = "/user/updatepwdById")
	BaseResp<Object> updatepwdById(@RequestParam("userid") long userid,
								   @RequestParam("oldpwd") String oldpwd,
								   @RequestParam("newpwd") String newpwd);

	/**
	 * @Title: thirdlogin
	 * @Description: 第三方登录
	 * @param @param openid
	 * @param @param utype
	 * @auther smkk
	 * @currentdate:2017年1月18日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/user/thirdlogin")
	BaseResp<Object> thirdlogin(@RequestParam("openid") String openid,
								@RequestParam("utype") String utype);
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
	@RequestMapping(method = RequestMethod.GET, value = "/user/bindingThird")
	BaseResp<Object> bindingThird(@RequestParam("openid") String openid,
								  @RequestParam("utype") String utype,
								  @RequestParam("userid") Long userid);
	/**
	 * @Title: hasbindingThird
	 * @Description: 判断是否绑定第三方信息
	 * @param @param openid
	 * @param @param utype
	 * @param @param username
	 * @auther smkk
	 * @currentdate:2017年1月19日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/user/hasbindingThird")
	BaseResp<Object> hasbindingThird(@RequestParam("openid") String openid,
									 @RequestParam("utype") String utype,
									 @RequestParam("username") String username);
	
	/**
	* @Title: selectUserByUserid 
	* @Description: 获取用户信息
	* @param @param userid
	* @param @return    设定文件 
	* @return BaseResp<UserBasic>    返回类型
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/user/selectUserByUserid")
	BaseResp<UserBasic> selectUserByUserid(@RequestParam("userid") long userid);

}
