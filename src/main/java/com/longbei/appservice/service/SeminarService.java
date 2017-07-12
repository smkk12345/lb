package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Module;
import com.longbei.appservice.entity.Seminar;
import com.longbei.appservice.entity.SeminarModule;

import java.util.List;

/**
 * 专题
 *
 * @author luye
 * @create 2017-07-05 上午11:17
 **/
public interface SeminarService {


    BaseResp<String> insertSeminar(Seminar seminar);


    BaseResp<Object> insertSeminarModule(List<SeminarModule> seminarModules);


    BaseResp<Object> deleteSeminar(String seminarid);


    BaseResp<Object> updateSeminar(Seminar seminar);


    BaseResp<Object> updateSeminarModule(String seminarid,List<SeminarModule> seminarModules);


    BaseResp<Seminar> selectSeminar(String seminarid);


    BaseResp<List<SeminarModule>> selectSeminarModules(String seminar);


    BaseResp<Page<Seminar>> selectSeminars(Seminar seminar, Integer pageNo, Integer pageSize);


    BaseResp<List<Module>> selectModules();

    BaseResp<Seminar> selectSeminarAllDetail(String seminarid);
}
