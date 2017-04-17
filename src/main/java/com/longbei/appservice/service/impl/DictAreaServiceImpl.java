package com.longbei.appservice.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.longbei.appservice.controller.api.TestApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.DictAreaMapper;
import com.longbei.appservice.entity.DictArea;
import com.longbei.appservice.service.DictAreaService;

@Service("dictAreaService")
public class DictAreaServiceImpl implements DictAreaService {

	@Autowired
	private DictAreaMapper dictAreaMapper;
	@Autowired
	private TestApiController testApiController;
	
	private static Logger logger = LoggerFactory.getLogger(DictAreaServiceImpl.class);
	
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

	@Override
	public BaseResp<Object> readCityTxt() {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		File file = new File("src/City.txt");
		FileWriter fw = null;
		BufferedReader reader=null;
			try {
				if (!file.exists()) {
					file.createNewFile();
					fw = new FileWriter(file);
					String a = testApiController.dictArea();
					fw.write(a);
				}
				reader = new BufferedReader(new FileReader(file));
				List<String> list = new ArrayList<String>();
				String tempString;
				while ((tempString = reader.readLine()) != null) {
					list.add(tempString);
				}
				baseResp.initCodeAndDesp();
				baseResp.setData(list);
			}
			catch (Exception e) {
				logger.error("readCityTxt ", e);
			}
			finally {
				if (null != fw) {
					try {
						fw.close();
					} catch (IOException e) {
						logger.error("readCityTxt ", e);
					}
			}
				if (null != reader) {
					try {
						reader.close();
					} catch (IOException e) {
						logger.error("readCityTxt ", e);
					}
			}
			return baseResp;
    	}
	}
}
