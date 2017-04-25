package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.SuperTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lixb on 2017/3/1.
 * 超级话题
 */
@RestController
@RequestMapping(value = "/topic")
public class SuperTopicController {

    private static Logger logger = LoggerFactory.getLogger(SuperTopicController.class);

    @Autowired
    private SuperTopicService superTopicService;

    /**
     * topic/list
     * 获取超级话题列表
     * @param startNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseResp<Object> list(int startNum,int pageSize) {
        BaseResp<Object> baseResp = new BaseResp<>();
        if(pageSize == 0){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp  = superTopicService.selectSuerTopicList(startNum,pageSize);
        } catch (Exception e) {
            logger.error("list startNum = {},pageSize={} ", startNum,pageSize, e);
        }
        return baseResp;
    }

    /**
     * topic／selectImprovesById   获取进步列表  通过超级话题id
     * @param topicId
     * @param userid
     * @param startNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/selectImprovesById")
    public BaseResp<Object> selectImprovesById(String topicId,String userid,int startNum,int pageSize){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(topicId,userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try{
            baseResp = superTopicService.selectImprovesByTopicId(Long.parseLong(userid),Long.parseLong(topicId),startNum,pageSize);
        }catch (Exception e){
            logger.error("selectImprovesById startNum = {},endNum={},topicid={},userid={} ", startNum,pageSize,topicId,userid, e);
        }
        return baseResp;
    }




}
