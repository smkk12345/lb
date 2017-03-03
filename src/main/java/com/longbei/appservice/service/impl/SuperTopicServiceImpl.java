package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.controller.SuperTopicController;
import com.longbei.appservice.dao.ImproveTopicMapper;
import com.longbei.appservice.dao.SuperTopicMapper;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.ImproveTopic;
import com.longbei.appservice.entity.SuperTopic;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.SuperTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private ImproveTopicMapper improveTopicMapper;

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
            List<Improve> list = new ArrayList<>();
            List<ImproveTopic> improveTopics = improveTopicMapper.selectByTopicId(topicId,startNum,endNum);
            if(null == improveTopics){
                return baseResp.initCodeAndDesp();
            }
            for (int i = 0; i < improveTopics.size(); i++) {
                ImproveTopic improveTopic = improveTopics.get(i);
                //Long impid,String userid,String businesstype,String businessid
                Improve improve = improveService.selectImproveByImpid(improveTopic.getImpid(),userid+"",improveTopic.getBusinesstype()+"",improveTopic.getBusinessid()+"");
                list.add(improve);
            }
            baseResp.initCodeAndDesp();
            baseResp.setData(list);
        }catch (Exception e){
            logger.error("topicId={},startNum={},endNum={}",topicId,startNum,endNum,e);
        }
        return baseResp;
    }


}
