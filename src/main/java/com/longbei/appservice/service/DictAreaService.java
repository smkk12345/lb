package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.DictArea;

public interface DictAreaService {

	BaseResp<Object> selectCityList(String pid);

}
