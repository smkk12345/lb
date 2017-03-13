package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.DictArea;

import java.util.List;

public interface DictAreaService {

	/**
	 * @Title: selectCityList
	 * @Description: 查找城市信息
	 * @param @param pid 父级城市编号 (通过pid=null可查中国全部省份，通过省份可查其全部市，通过市可查其全部县)
	 * @param @param startNum分页起始值，pageSize每页显示条数
	 * @auther IngaWu
	 * @currentdate:2017年2月23日
	 */
	BaseResp<List<DictArea>> selectCityList(String pid, Integer startNum, Integer pageSize);

	/**
	 * @Title: readCityTxt
	 * @Description: 读取城市信息txt,若没有则新建
	 * @auther IngaWu
	 * @currentdate:2017年3月9日
	 */
	BaseResp<Object> readCityTxt();

}
