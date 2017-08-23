package com.longbei.appservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserClassroomCertifyMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.ClassroomCertify;
import com.longbei.appservice.entity.UserIdcard;
import com.longbei.appservice.service.ClassroomCertifyService;
import com.longbei.appservice.service.JPushService;
import com.longbei.appservice.service.UserIdcardService;
import com.longbei.appservice.service.UserMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClassroomCertifyServiceImpl implements ClassroomCertifyService {

    private static Logger logger = LoggerFactory.getLogger(ClassroomCertifyServiceImpl.class);

    @Autowired
    private UserClassroomCertifyMapper userUserClassroomCertifyMapper;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private UserIdcardService userIdcardService;
    @Autowired
    private UserMsgService userMsgService;
    @Autowired
    private JPushService jPushService;

    @Override
    public Page<ClassroomCertify> selectClassroomCertifyList(ClassroomCertify classroomCertify, Integer startNum, Integer pageSize){
        Page<ClassroomCertify> page = new Page<>(startNum/pageSize+1,pageSize);
        try {
            int totalcount = userUserClassroomCertifyMapper.selectClassroomCertifyListCount(classroomCertify);
            Page.setPageNo(startNum/pageSize+1,totalcount,pageSize);
            List<ClassroomCertify> classroomCertifyList = new ArrayList<ClassroomCertify>();
            classroomCertifyList = userUserClassroomCertifyMapper.selectClassroomCertifyList(classroomCertify,startNum,pageSize);
            for(int i= 0;i<classroomCertifyList.size();i++) {
                AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(classroomCertifyList.get(i).getUserid()));
                if(null != appUserMongoEntity) {
                    classroomCertifyList.get(i).setUser(appUserMongoEntity);
                }
                UserIdcard userIdcard = userIdcardService.selectByUserid(classroomCertifyList.get(i).getUserid()+"");
                if(null != userIdcard) {
                    classroomCertifyList.get(i).setUserIdcard(userIdcard);
                }
                //时间格式
                if(StringUtils.isNotBlank(classroomCertifyList.get(i).getCreatetime())) {
                    Date createtime = DateUtils.formatDate(classroomCertifyList.get(i).getCreatetime(), "yyyy-MM-dd HH:mm:ss");
                    classroomCertifyList.get(i).setCreatetime(DateUtils.formatDateTime1(createtime));
                }
                if(StringUtils.isNotBlank(classroomCertifyList.get(i).getChecktime())) {
                    Date uptime = DateUtils.formatDate(classroomCertifyList.get(i).getChecktime(), "yyyy-MM-dd HH:mm:ss");
                    classroomCertifyList.get(i).setChecktime(DateUtils.formatDateTime1(uptime));
                }
                if(StringUtils.isNotBlank(classroomCertifyList.get(i).getUpdatetime())) {
                    Date downtime = DateUtils.formatDate(classroomCertifyList.get(i).getUpdatetime(), "yyyy-MM-dd HH:mm:ss");
                    classroomCertifyList.get(i).setUpdatetime(DateUtils.formatDateTime1(downtime));
                }
            }
            page.setTotalCount(totalcount);
            page.setList(classroomCertifyList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    @Override
    public BaseResp<Object> selectClassroomCertifyNum(ClassroomCertify classroomCertify){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            int totalcount = userUserClassroomCertifyMapper.selectClassroomCertifyListCount(classroomCertify);
            baseResp.setData(totalcount);
            baseResp.initCodeAndDesp();
        } catch (Exception e) {
            logger.error("selectClassroomCertifyNum for adminservice and classroomCertify ={}", JSON.toJSONString(classroomCertify), e);
        }
        return  baseResp;
    }

    @Override
    public BaseResp<ClassroomCertify> selectClassroomCertifyByUserid(Long userid) {
        BaseResp<ClassroomCertify> baseResp = new BaseResp<ClassroomCertify>();
        try {
            ClassroomCertify classroomCertify = userUserClassroomCertifyMapper.selectClassroomCertifyByUserid(userid);
            AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(classroomCertify.getUserid()));
            if(null != appUserMongoEntity) {
                classroomCertify.setUser(appUserMongoEntity);
            }
            UserIdcard userIdcard = userIdcardService.userSafety(classroomCertify.getUserid()).getData();
            if(null != userIdcard) {
                classroomCertify.setUserIdcard(userIdcard);
            }
            //时间格式
            if(StringUtils.isNotBlank(classroomCertify.getCreatetime())) {
                Date createtime = DateUtils.formatDate(classroomCertify.getCreatetime(), "yyyy-MM-dd HH:mm:ss");
                classroomCertify.setCreatetime(DateUtils.formatDateTime1(createtime));
            }
            if(StringUtils.isNotBlank(classroomCertify.getChecktime())) {
                Date uptime = DateUtils.formatDate(classroomCertify.getChecktime(), "yyyy-MM-dd HH:mm:ss");
                classroomCertify.setChecktime(DateUtils.formatDateTime1(uptime));
            }
            if(StringUtils.isNotBlank(classroomCertify.getUpdatetime())) {
                Date downtime = DateUtils.formatDate(classroomCertify.getUpdatetime(), "yyyy-MM-dd HH:mm:ss");
                classroomCertify.setUpdatetime(DateUtils.formatDateTime1(downtime));
            }
            baseResp.setData(classroomCertify);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        catch (Exception e) {
            logger.error("selectClassroomCertifyByUserid error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertClassroomCertify(ClassroomCertify classroomCertify){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        classroomCertify.setCreatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        classroomCertify.setStatus("0");
        int n ;
        try {
            ClassroomCertify classroomCertify1 = userUserClassroomCertifyMapper.selectClassroomCertifyByUserid(classroomCertify.getUserid());
            if(null == classroomCertify1) {
                n = userUserClassroomCertifyMapper.insertClassroomCertify(classroomCertify);
            }else{
                classroomCertify.setChecktime(null);
                classroomCertify.setUpdatetime(null);
                classroomCertify.setRemark(null);
                classroomCertify.setOperateuid(null);
                n = userUserClassroomCertifyMapper.updateClassroomCertifyByUserid(classroomCertify);
            }
            if(n == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertClassroomCertify error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public 	BaseResp<Object> updateClassroomCertifyByUserid(ClassroomCertify classroomCertify) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        String date= DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        classroomCertify.setUpdatetime(date);
        if(StringUtils.isNotBlank(classroomCertify.getStatus())) {
                classroomCertify.setChecktime(date);
        }
        try {
            int n = userUserClassroomCertifyMapper.updateClassroomCertifyByUserid(classroomCertify);
            if(n >= 1){
                if ("1".equals(classroomCertify.getStatus())){
                    String remark = Constant.MSG_CLASSROOM_CERTIFY_SUCCESS_MODEL;
                    userMsgService.insertMsg(Constant.SQUARE_USER_ID, String.valueOf(classroomCertify.getUserid())
                            ,null,"9",null,remark,"2","65", "教室资格认证",0, "", "");
                    this.jPushService.pushMessage("消息标识",classroomCertify.getUserid()+"","教室资格认证审核",
                            "恭喜，您的老师认证审核通过！","",Constant.JPUSH_TAG_COUNT_1306);
                } else if ("2".equals(classroomCertify.getStatus())) {
                    String remark = Constant.MSG_CLASSROOM_CERTIFY_FAIL_MODEL + classroomCertify.getRemark();
                    userMsgService.insertMsg(Constant.SQUARE_USER_ID, String.valueOf(classroomCertify.getUserid())
                            ,null,"9",null,remark,"2","69", "教室资格认证",0, "", "");
                    this.jPushService.pushMessage("消息标识",classroomCertify.getUserid()+"","教室资格认证审核",
                            "您的老师认证审核未通过","",Constant.JPUSH_TAG_COUNT_1307);
                } else if ("3".equals(classroomCertify.getStatus())){
//                    String remark = "老师认证被撤销";
                	String remark = Constant.MSG_CLASSROOM_CERTIFY_FAIL_MODEL + classroomCertify.getRemark();
                    userMsgService.insertMsg(Constant.SQUARE_USER_ID, String.valueOf(classroomCertify.getUserid())
                            ,null,"9",null,remark,"2","69", "教室资格认证",0, "", "");
                    this.jPushService.pushMessage("消息标识",classroomCertify.getUserid()+"","教室资格认证审核",
                            "您的教室资格认证审核被撤销","",Constant.JPUSH_TAG_COUNT_1307);
                }
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateClassroomCertifyByUserid error and msg={}",e);
        }
        return baseResp;
    }

}
