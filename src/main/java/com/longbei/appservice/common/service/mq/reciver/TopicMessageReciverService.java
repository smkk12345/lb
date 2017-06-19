package com.longbei.appservice.common.service.mq.reciver;


import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.constant.Constant_point;
import com.longbei.appservice.common.security.SensitiveWord;
import com.longbei.appservice.dao.BehaviorRuleMapper;
import com.longbei.appservice.entity.BehaviorRule;
import com.longbei.appservice.service.SysSensitiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Set;

/**
 * mq 广播消息处理
 * Created by luye on 2017/1/18.
 */
@Service
public class TopicMessageReciverService implements MessageListener{


    private static Logger logger = LoggerFactory.getLogger(TopicMessageReciverService.class);

    @Autowired
    private BehaviorRuleMapper behaviorRuleMapper;
    @Autowired
    private SysSensitiveService sysSensitiveService;

    @Override
    public void onMessage(Message message) {
        //业务处理
        try {
            String msg = ((TextMessage)message).getText();
            System.out.println(msg);
            if (Constant.UPDATE_RULE.equals(msg)){
                initUserBehaviorRule(0);
                Constant_Imp_Icon.init();
                Constant_point.init();
            }
            if (Constant.UPDATE_SENSITIVE.equals(msg)){
                initSensitiveMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initUserBehaviorRule(int num){

        try {
            BehaviorRule behaviorRule = behaviorRuleMapper.selectOne();
            SysRulesCache.behaviorRule = behaviorRule;
        } catch (Exception e) {
            logger.error("init user behavior rule againnum={} is error:",num,e);
            if (num <= 3){
                initUserBehaviorRule(num++);
            }
        }
    }

    public void initSensitiveMap(){
        try {
            Set<String> list = sysSensitiveService.selectSensitiveWord();
            if (null != list && !list.isEmpty()) {
                SensitiveWord.addSensitiveWordToHashMap(list);
            }
        }catch (Exception e) {
            logger.error("initSensitiveMape is error:",e);
        }
    }
}
