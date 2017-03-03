package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.controller.SuperTopicController;
import com.longbei.appservice.dao.SuperTopicMapper;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.SuperTopic;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.SuperTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 超级话题服务
 * Created by lixb on 2017/3/1.
 */
@Service
public class SuperTopicServiceImpl implements SuperTopicService {

    private static Logger logger = LoggerFactory.getLogger(SuperTopicServiceImpl.class);

    @Autowired
    private SuperTopicMapper superTopicMapper;
    @Autowired
    private ImproveService improveService;

    /**
     * 获取超级话题列表
     * @param startNum
     * @param endNum
     * @return
     */
    @Override
    public BaseResp<Object> selectSuerTopicList(int startNum, int endNum) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            List<SuperTopic> list = superTopicMapper.selectList(startNum,endNum);
            baseResp.setData(list);
            baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("startNum={},endNum={}",startNum,endNum,e);
        }
        return baseResp;
    }

    /**
     * 查询超级话题下的进步列表
     * @param topicId
     * @param startNum
     * @param endNum
     * @return
     */
    @Override
    public BaseResp<Object> selectImprovesByTopicId(long userid,long topicId, int startNum, int endNum) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            List<Improve> list = improveService.selectSuperTopicImproveList(userid,topicId+"",null,startNum,endNum);
            baseResp.initCodeAndDesp();
            baseResp.setData(list);
        }catch (Exception e){
            logger.error("topicId={},startNum={},endNum={}",topicId,startNum,endNum,e);
        }
        return baseResp;
    }


}
