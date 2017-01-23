package com.longbei.appservice.common.service.mq.reciver;

import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.ActiveMQConfig;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.TimeLine;
import com.longbei.appservice.entity.TimeLineDetail;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.impl.ImproveServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Date;


/**
 * mq 添加消息接收
 * Created by luye on 2017/1/18.
 */
@Service
public class AddMessageReceiveService{

    private static Logger logger = LoggerFactory.getLogger(AddMessageReceiveService.class);

    @Autowired
    private ImproveService improveService;
    @Autowired
    private TimeLineDao timeLineDao;
    @Autowired
    private TimeLineDetailDao timeLineDetailDao;


    @JmsListener(destination="${spring.activemq.queue.name.add}")
    public void receiveMessage(String msg){

        if (StringUtils.isBlank(msg)
                || msg.indexOf(",") == -1
                || msg.split(",").length < 5
                || msg.split(",").length > 5){
            return;
        }
        //id,businesstype,businessid,userid,date
        String []content = msg.split(",");
        //提取消息中的内容
        String id = content[0];
        String businesstype = content[1];
        String businessid = content[2];
        String userid = content[3];
        String date = content[4];

        Improve improve = improveService.selectImproveByImpid(Long.parseLong(id),userid,businesstype,businessid);

        AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
        appUserMongoEntity.setId(userid);
        TimeLineDetail timeLineDetail = new TimeLineDetail();





        System.out.println("监听接收到的消息是:"+msg);//打印队列内的消息
    }

    private void insertTimeLine(String userid, String remark, String messagetype, String ctype, Date createdate){


//        @Id
//        private String id = UUID.randomUUID().toString().replace("-", "_");
//        private String userid;
//        private String remark;
//        @DBRef
//        private TimeLineDetail contents;
//        private String messagetype; // 1 -- improve 2 --- rank 3 --- old award 4 -- new award
//        private String ctype = "1";  //0--广场 1--我的 2--好友，关注，熟人 3-好友 4-关注 5-熟人
//        private Date createdate;


        TimeLine timeLine = new TimeLine();
        timeLine.setUserid(userid);
        timeLine.setRemark(remark);
        timeLine.setMessagetype(messagetype);
        timeLine.setCtype(ctype);
        timeLine.setCreatedate(createdate);

        timeLineDao.save(timeLine);

    }




}
