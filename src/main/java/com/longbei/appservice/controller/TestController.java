package com.longbei.appservice.controller;

import com.longbei.appservice.service.UserService;
import com.longbei.appservice.service.impl.UserServiceImpl;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * test
 *
 * @author luye
 * @create 2017-05-25 下午8:12
 **/
@RestController
public class TestController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @RequestMapping("test")
    public List<String> test(){
        return userServiceImpl.getPointOriginate();
    }

}
