package com.longbei.appservice.config;

import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.security.SensitiveWord;
import com.longbei.appservice.common.service.mq.reciver.AddMessageReceiveService;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.SysSensitiveService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.jms.*;
import javax.jms.Queue;
import java.util.*;
import java.util.List;

/**
 * 规则配置(启动时初始化)
 *
 * @author luye
 * @create 2017-02-04 下午1:59
 **/
@Component
public class InitConfig implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(InitConfig.class);

       //签到规则
    @Autowired
    private SysRuleCheckinMapper sysRuleCheckinMapper;
    //用户特权规则
    @Autowired
    private SysRuleLpMapper sysRuleLpMapper;
    //十全十美等级规则表
    @Autowired
    private SysRulePerfectTenMapper sysRulePerfectTenMapper;
    //用户行为规则
    @Autowired
    private BehaviorRuleMapper behaviorRuleMapper;

    //计分规则
    @Autowired
    private SysScoringRuleMapper sysScoringRuleMapper;
    @Autowired
    private UserLevelMapper userLevelMapper;
    @Autowired
    private SysPerfectInfoMapper sysPerfectInfoMapper;
    @Autowired
    private SysSensitiveService sysSensitiveService;
    @Autowired
    private ConnectionFactory activeMQConnectionFactory;

    @Autowired
    private Queue addqueue;
    @Autowired
    private Queue updatequeue;
    @Autowired
    private AddMessageReceiveService addMessageReceiveService;



    @Override
    public void run(String... strings) throws Exception {
        initUserBehaviorRule(0);
        //初始化相关
        initSysRuleCheckInCache();
        //十全十美每级所需龙分缓存
        initSysRulePLevelPointCache();
        //龙级升级每级所需龙分缓存
        initSysRuleLevelPointCache();
        //十全十美每级详细说明缓存
        initSysPerfectInfoCache();
        //缓存敏感词
        initSensitiveMap();

        Constant_Imp_Icon.init();

        initListener();
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

    /**
     * 添加多个监听
     */
    private void initListener(){
        try {
            Session session = getSession();
            //进步相关监听
            initImproveListener(session,addqueue,addMessageReceiveService);
            //其他监听
        } catch (Exception e) {
            logger.error("initListener ",e);
        }
    }

    private void initImproveListener(Session session,Queue queue,MessageListener mssageListener){
        try {
            MessageConsumer messageConsumer=session.createConsumer(queue); // 创建消息消费者
            messageConsumer.setMessageListener(mssageListener); // 注册消息监听
        }catch (Exception e){
            logger.error("set messageConsumer lister error");
        }

    }

    /**
     * smkk
     * 获取session
     * @return
     */
    private Session getSession(){
        try{
            Connection connection = activeMQConnectionFactory.createConnection();  // 通过连接工厂获取连接
            connection.start(); // 启动连接
            Session session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
            return session;
        }catch (Exception e){
            logger.error("getSession ",e);
        }
        return null;
    }


    /**
     * 初始化签到规则
     */
    private void initSysRuleCheckInCache(){
        Map<Integer,SysRuleCheckin> map = new HashMap<>();
        List<SysRuleCheckin> list =  sysRuleCheckinMapper.selectAll();
        for (int i = 0; i < list.size(); i++) {
            SysRuleCheckin sysRuleCheckin = list.get(i);
            map.put(sysRuleCheckin.getContinues(),sysRuleCheckin);
        }
        //System.out.print(JSONObject.fromObject(map));
        SysRulesCache.sysRuleCheckinMap = map;
    }

    /**
     *十全十美每级所需龙分缓存
     * key  ptype+&+level  value point
     */
    private void initSysRulePLevelPointCache(){
        Map<String,SysRulePerfectTen> map = new HashMap<>();
        List<SysRulePerfectTen> list = sysRulePerfectTenMapper.selectAll();
        for (int i = 0; i < list.size(); i++) {
            SysRulePerfectTen sysRule = list.get(i);
            map.put(sysRule.getPtype()+"&"+sysRule.getPlevel(),sysRule);
        }
        SysRulesCache.pLevelPointMap = map;
    }

    /**
     *龙级升级每级所需龙分缓存
     */
    private void initSysRuleLevelPointCache(){
        Map<Integer,UserLevel> map = new HashMap<>();
        //这里做初始化
        List<UserLevel> list = userLevelMapper.selectAll();
        for (int i = 0; i < list.size(); i++) {
            UserLevel userLevel = list.get(i);
            map.put(userLevel.getGrade(),userLevel);
        }
        SysRulesCache.levelPointMap = map;
    }

    /**
     * 十全十美每级详细说明 key--ptype十全十美类型 value--SysPerfectInfo实体
     */
    private void  initSysPerfectInfoCache() {
        Map<String,SysPerfectInfo> map = new HashMap<>();
        List<SysPerfectInfo> list = sysPerfectInfoMapper.selectAll();
        for (int i = 0; i < list.size(); i++) {
            SysPerfectInfo sysPerfectInfo = list.get(i);
            map.put(sysPerfectInfo.getPtype(),sysPerfectInfo);
        }
        SysRulesCache.sysPerfectInfoMap = map;
    }

    /**
     * 初始化敏感词
     */
    public void initSensitiveMap(){
        Set<String> list = sysSensitiveService.selectSensitiveWord();
        if(null!=list&&!list.isEmpty()){
            SensitiveWord.addSensitiveWordToHashMap(list);
        }
    }


}
