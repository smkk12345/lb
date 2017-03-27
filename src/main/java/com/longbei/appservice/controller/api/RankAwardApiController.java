package com.longbei.appservice.controller.api;

import com.longbei.appservice.entity.RankMembers;
import com.longbei.appservice.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 榜单领奖
 *
 * @author luye
 * @create 2017-03-21 上午10:18
 **/
@RestController
@RequestMapping(value = "rankaward")
public class RankAwardApiController {

    @Autowired
    private RankService rankService;







}
