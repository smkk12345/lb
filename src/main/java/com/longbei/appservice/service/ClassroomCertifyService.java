package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.ClassroomCertify;


public interface ClassroomCertifyService {

    /**
     * 获取老师认证列表
     * @title selectClassroomCertifyList
     * @author IngaWu
     * @currentdate:2017年8月9日
     */
    Page<ClassroomCertify> selectClassroomCertifyList(ClassroomCertify classroomCertify, Integer startNum, Integer pageSize);

    /**
     * @Title: selectClassroomCertifyByUserid
     * @Description: 通过用户id查看老师认证详情
     * @param userid 用户id
     * @auther IngaWu
     * @currentdate:2017年8月9日
     */
    BaseResp<ClassroomCertify> selectClassroomCertifyByUserid(Long userid);

    /**
     * 添加老师认证
     * @title insertClassroomCertify
     * @author IngaWu
     * @currentdate:2017年8月9日
     */
    BaseResp<Object> insertClassroomCertify(ClassroomCertify classroomCertify);

    /**
     * 编辑老师认证
     * @title updateClassroomCertifyByUserid
     * @author IngaWu
     * @currentdate:2017年8月9日
     */
    BaseResp<Object> updateClassroomCertifyByUserid(ClassroomCertify classroomCertify);
}
