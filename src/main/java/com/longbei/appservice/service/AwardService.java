package com.longbei.appservice.service;

import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Award;

import java.util.List;

/**
 * 奖品相关操作
 *
 * @author luye
 * @create 2017-02-27 下午4:05
 **/
public interface AwardService {


    public Page<Award> selectAwardListWithPage(Award award,int startno,int pagesize);


}
