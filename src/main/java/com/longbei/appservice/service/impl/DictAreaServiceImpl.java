package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.DictAreaMapper;
import com.longbei.appservice.entity.DictArea;
import com.longbei.appservice.service.DictAreaService;

@Service("dictAreaService")
public class DictAreaServiceImpl implements DictAreaService {

	@Autowired
	private DictAreaMapper dictAreaMapper;
	
	private static Logger logger = LoggerFactory.getLogger(DictAreaServiceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<List<DictArea>> selectCityList(String pid,Integer startNum,Integer pageSize) {
		BaseResp<List<DictArea>> baseResp = new BaseResp<List<DictArea>>();
		try {
			List<DictArea> list = dictAreaMapper.selectCityList(pid,startNum,pageSize);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectCityList error and msg={}",e);
		}
		return baseResp;
	}

	
}
