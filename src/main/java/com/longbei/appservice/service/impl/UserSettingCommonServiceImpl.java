package com.longbei.appservice.service.impl;

import java.util.*;

import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserSettingCommonMapper;
import com.longbei.appservice.entity.UserBasic;
import com.longbei.appservice.entity.UserSettingCommon;
import com.longbei.appservice.service.UserSettingCommonService;
import com.longbei.appservice.service.api.userservice.IUserBasicService;

@Service("userSettingCommonService")
public class UserSettingCommonServiceImpl implements UserSettingCommonService {

    @Autowired
    private UserSettingCommonMapper userSettingCommonMapper;
    @Autowired
    private IUserBasicService iUserBasicService;
    @Autowired
    private UserMongoDao userMongoDao;

    private static Logger logger = LoggerFactory.getLogger(UserSettingCommonServiceImpl.class);

    @Override
    public BaseResp<Object> insert(UserSettingCommon record) {
        BaseResp<Object> reseResp = new BaseResp<>();
        try {
            boolean temp = insertUserSettingCommon(record);
            if (temp) {
                reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insert record={},msg={}", record, e);
        }
        return reseResp;
    }

    private boolean insertUserSettingCommon(UserSettingCommon record) {
        int temp = userSettingCommonMapper.insertSelective(record);
        return temp > 0 ? true : false;
    }

    @Override
    public UserSettingCommon selectByid(Integer id) {
        UserSettingCommon userSettingCommon = new UserSettingCommon();
        try {
            userSettingCommon = userSettingCommonMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("selectByid id={},msg={}", id, e);
        }
        return userSettingCommon;
    }

    @Override
    public BaseResp<Object> updateByid(UserSettingCommon record) {
        BaseResp<Object> reseResp = new BaseResp<>();
        try {
            boolean temp = updateUserSettingCommon(record);
            if (temp) {
                reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateByid record={},msg={}", record, e);
        }
        return reseResp;
    }

    private boolean updateUserSettingCommon(UserSettingCommon record) {
        int temp = userSettingCommonMapper.updateByPrimaryKeySelective(record);
        return temp > 0 ? true : false;
    }

    @Override
    public BaseResp<Object> selectByKey(String userid, String key) {
        BaseResp<Object> reseResp = new BaseResp<>();
        try {
            UserSettingCommon userSettingCommon = userSettingCommonMapper.selectByKey(userid, key);
            reseResp.setData(userSettingCommon);
            if (userSettingCommon != null) {
                reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("selectByKey userid={},key={},msg={}", userid, key, e);
        }
        return reseResp;
    }

    @Override
    public BaseResp<Object> selectByUserid(String userid) {
        BaseResp<Object> reseResp = new BaseResp<>();
        try {
            Map<String, Object> expandData = selectMapByUserid(userid);
            //获取是否绑定qq,wx,wb  0:未绑定  1：绑定
            String isqq = "0";
            String iswx = "0";
            String iswb = "0";
            AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(userid);
            BaseResp<UserBasic> resp = iUserBasicService.selectUserByUserName(appUserMongoEntity.getUsername());
            if (ResultUtil.isSuccess(resp)) {
                UserBasic userBasic = resp.getData();
                if (null != userBasic) {
                    if (!StringUtils.isBlank(userBasic.getQq())) {
                        isqq = "1";
                    }
                    if (!StringUtils.isBlank(userBasic.getWx())) {
                        iswx = "1";
                    }
                    if (!StringUtils.isBlank(userBasic.getWb())) {
                        iswb = "1";
                    }
                }
            }
            expandData.put("isqq", isqq);
            expandData.put("iswx", iswx);
            expandData.put("iswb", iswb);
            reseResp.setExpandData(expandData);
            reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectByUserid userid={},msg={}", userid, e);
        }
        return reseResp;
    }

    @Override
    public Map<String, Object> selectMapByUserid(String userid) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<UserSettingCommon> list = userSettingCommonMapper.selectByUserid(userid);
            if (null != list && list.size() > 0) {
                for (UserSettingCommon userSettingCommon : list) {
                    map.put(userSettingCommon.getUkey(), userSettingCommon.getUvalue());
                }
                //兼容注册帐号比较早的用户     2017-06-06   is_newfriendask---新好友申请
                if (!map.containsKey("is_newfriendask")) {
                    //不存在添加
                    UserSettingCommon common7 = new UserSettingCommon(Long.parseLong(userid), "is_newfriendask", "1",
                            "新好友申请", new Date(), new Date());
                    userSettingCommonMapper.insert(common7);
                    map.put("is_newfriendask", "1");
                }
            } else {
//				List<UserSettingCommon> setlist = new ArrayList<UserSettingCommon>();
//				UserSettingCommon common = new UserSettingCommon(Long.parseLong(userid), "is_new_fans", "1",
//						"新粉丝", new Date(), new Date());
//				UserSettingCommon common2 = new UserSettingCommon(Long.parseLong(userid), "is_like", "1",
//						"点赞", new Date(), new Date());
//				UserSettingCommon common3 = new UserSettingCommon(Long.parseLong(userid), "is_flower", "1",
//						"送花", new Date(), new Date());
//				UserSettingCommon common4 = new UserSettingCommon(Long.parseLong(userid), "is_comment", "2",
//						"评论设置", new Date(), new Date());
//				UserSettingCommon common5 = new UserSettingCommon(Long.parseLong(userid), "is_nick_search", "1",
//						"允许通过昵称搜到我", new Date(), new Date());
//				UserSettingCommon common6 = new UserSettingCommon(Long.parseLong(userid), "is_phone_search", "1",
//						"允许通过此手机号搜到我", new Date(), new Date());
//				UserSettingCommon common7 = new UserSettingCommon(Long.parseLong(userid), "is_newfriendask", "1", 
//						"新好友申请", new Date(), new Date());
//				list.add(common);
//				list.add(common2);
//				list.add(common3);
//				list.add(common4);
//				list.add(common5);
//				list.add(common6);
//				list.add(common7);
//				userSettingCommonMapper.insertList(setlist);
//				map.put("is_acquaintance_look","1");
                map.put("is_comment", "1");
                map.put("is_diamond", "1");
                map.put("is_flower", "1");
                map.put("is_like", "1");
                map.put("is_new_fans", "1");
                map.put("is_nick_search", "1");
                map.put("is_page_tool", "1");
                map.put("is_phone_search", "1");
                map.put("is_newfriendask", "1");
            }
        } catch (Exception e) {
            logger.error("selectByUserid userid={},msg={}", userid, e);
        }
        return map;
    }

    @Override
    public boolean isPushMessage(String userid, Constant.userSettingCommen userSettingCommen) {
        if (userSettingCommen == null || StringUtils.isEmpty(userid)) {
            return false;
        }
        Map<String, Object> map = selectMapByUserid(userid);
        String flag = "0";
        switch (userSettingCommen) {
            case comment:
                flag = map.get("is_comment").toString();
                break;
            case diamond:
                flag = map.get("is_diamond").toString();
                break;
            case flower:
                flag = map.get("is_flower").toString();
                break;
            case like:
                flag = map.get("is_like").toString();
                break;
            case new_fans:
                flag = map.get("is_new_fans").toString();
                break;
            case nick_search:
                flag = map.get("is_nick_search").toString();
                break;
            case page_tool:
                flag = map.get("is_page_tool").toString();
                break;
            case phone_search:
                flag = map.get("is_phone_search").toString();
                break;
            case newfriendask:
                flag = map.get("is_newfriendask").toString();
                break;
        }
        if ("1".equals(flag)) {
            return true;
        }
        return false;
    }

    @Override
    public BaseResp<Object> updateByUseridKey(String userid, String key, String value) {
        BaseResp<Object> reseResp = new BaseResp<>();
        try {
            int temp = userSettingCommonMapper.updateByUseridKey(userid, key, value);
            if (temp > 0) {
                reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateByUseridKey userid={},key={},value={},msg={}", userid, key, value, e);
        }
        return reseResp;
    }

    @Override
    public BaseResp<Object> updateByUseridMap(String userid, String value) {
        BaseResp<Object> reseResp = new BaseResp<>();
        try {
            userSettingCommonMapper.updateByUseridKey(userid, "is_new_fans", value);
            userSettingCommonMapper.updateByUseridKey(userid, "is_like", value);
            userSettingCommonMapper.updateByUseridKey(userid, "is_flower", value);
            userSettingCommonMapper.updateByUseridKey(userid, "is_flower", value);
            userSettingCommonMapper.updateByUseridKey(userid, "is_newfriendask", value);
            userSettingCommonMapper.updateByUseridKey(userid, "is_comment", value);
            reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("updateByUseridMap userid = {}, value = {}", userid, value, e);
        }
        return reseResp;
    }

    //map转换成lisy
//	@SuppressWarnings("rawtypes")
//	public static List<UserSettingCommon>  mapTransitionList(Map<String, String> map) {
//		List<UserSettingCommon> list = new ArrayList<UserSettingCommon>();
//		Iterator iter = map.entrySet().iterator();  //获得map的Iterator
//		while(iter.hasNext()) {
//			Entry entry = (Entry)iter.next();
//			UserSettingCommon common = new UserSettingCommon();
//			
//		}
//		return list;
//	}

}
