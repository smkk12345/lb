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
	public BaseResp<Object> selectCityList(String pid) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			List<DictArea> list = dictAreaMapper.selectCityList(pid);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(JSONArray.toJSON(list));	
		} catch (Exception e) {
			logger.error("selectCityList error and msg={}",e);
		}
		return baseResp;
	}

	
}
