package com.longbei.appservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserIosBuymoney;
import com.longbei.appservice.service.UserIosBuymoneyService;

/** 
* @ClassName: UserIosBuymoneyController 
* @Description: IOS内购
* @author yinxc
* @date 2017年4月9日 下午4:01:36 
*/

@RestController
@RequestMapping(value = "buymoney")
public class UserIosBuymoneyController {

	@Autowired
	private UserIosBuymoneyService userIosBuymoneyService;
	
	private static Logger logger = LoggerFactory.getLogger(UserIosBuymoneyController.class);
	
	
	
	/**
    * @Title: http://ip:port/app_service/buymoney/selectIOSMoney
    * @Description: IOS购买龙币菜单
    * @auther yinxc
    * @currentdate:2017年4月9日
    */
 	@RequestMapping(value = "selectIOSMoney")
    public BaseResp<List<UserIosBuymoney>> selectIOSMoney() {
		BaseResp<List<UserIosBuymoney>> baseResp = new BaseResp<>();
  		try {
  			baseResp = userIosBuymoneyService.selectMoneyAllList();
        } catch (Exception e) {
            logger.error("selectIOSMoney ", e);
        }
  		return baseResp;
    }
	
}
