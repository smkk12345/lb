package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserIdcardMapper;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.UserIdcard;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserIdcardService;

import java.util.List;

@Service("userIdcardService")
public class UserIdcardServiceImpl implements UserIdcardService {

	@Autowired
	private UserIdcardMapper userIdcardMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserIdcardServiceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insert(UserIdcard record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			UserIdcard userIdcard = selectByUserid(record.getUserid().toString());
			if(userIdcard != null){
				//存在    修改
				boolean temp = updateUserIdcard(record);
				if (temp) {
					reseResp.setData(record);
					return reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				}
			}
			//添加
			boolean temp = insertUserIdcard(record);
			if (temp) {
				reseResp.setData(record);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insert record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean insertUserIdcard(UserIdcard record){
		int temp = userIdcardMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public UserIdcard selectByUserid(String userid) {
		if(StringUtils.hasBlankParams(userid)){
			return null;
		}
		return userIdcardMapper.selectByUserid(Long.parseLong(userid));
	}
	
	/**
	 * @author yinxc
	 * 帐号与安全(获取身份验证状态)
	 * 2017年2月24日
	 * return_type
	 * UserIdcardService
	 */
	@Override
	public BaseResp<UserIdcard> userSafety(long userid) {
		BaseResp<UserIdcard> reseResp = new BaseResp<>();
		try {
//			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
			UserIdcard userIdcard = userIdcardMapper.selectByUserid(userid);
			if(null != userIdcard){
				String imgStr = userIdcard.getIdcardimage().replaceAll("&quot;", "");
				if(imgStr.indexOf("[") != -1){
					imgStr = imgStr.substring(1, imgStr.length()-1);
				}
				String[] imgArr = imgStr.split(",");
				if (imgArr.length == 2) {
					userIdcard.setFrontidcardimage(Constant.OSS_CDN + imgArr[0]);
					userIdcard.setOppositeidcardimage(Constant.OSS_CDN + imgArr[1]);
                } else {
                	userIdcard.setFrontidcardimage(imgStr);
                	userIdcard.setOppositeidcardimage(imgStr);
                }
			}else{
				userIdcard = new UserIdcard();
				userIdcard.setValidateidcard("0");
			}
//			userIdcard.setRealname(userInfo.getRealname());
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			reseResp.setData(userIdcard);
			reseResp.getExpandData().put("osspath",Constant.OSS_MEDIA);
		} catch (Exception e) {
			logger.error("userSafety userid = {}, msg = {}", userid, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> update(UserIdcard record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateUserIdcard(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("update record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean updateUserIdcard(UserIdcard record){
		int temp = userIdcardMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Page<UserIdcard>> selectUserIdCardListPage(UserIdcard userIdcard, Integer pageno, Integer pagesize) {

		BaseResp<Page<UserIdcard>> baseResp = new BaseResp<>();
		Page<UserIdcard> page = new Page<>(pageno,pagesize);

		try {
			int totalcount = userIdcardMapper.selectCount(userIdcard);
			List<UserIdcard> userIdcards = userIdcardMapper.selectList(userIdcard,pagesize*(pageno-1),pagesize);
			page.setTotalCount(totalcount);
			page.setList(userIdcards);
			baseResp = BaseResp.ok();
			baseResp.setData(page);
		} catch (Exception e) {
			logger.error("select user id card list for pc is error:",e);
		}

		return baseResp;
	}
}
