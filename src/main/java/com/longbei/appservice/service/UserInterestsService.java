package com.longbei.appservice.service;

import java.util.Date;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserInterests;

public interface UserInterestsService {

	BaseResp<Object> updateInterests(int id,String ptype,String perfectname);
}
