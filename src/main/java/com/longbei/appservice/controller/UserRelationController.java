/**   
* @Title: UserRelationController.java 
* @Package com.longbei.appservice.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月20日 下午3:46:47 
* @version V1.0   
*/
package com.longbei.appservice.controller;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.SnsFans;
import com.longbei.appservice.service.UserImpCoinDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.service.UserRelationService;

import java.util.Date;
import java.util.List;

/**
 * @author smkk
 * 关系控制器，好友  熟人  关注的人
 */
@RestController
@RequestMapping(value = "/user")
public class UserRelationController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(UserRelationController.class);
	
	 @Autowired
	 private UserRelationService userRelationService;
	@Autowired
	private UserImpCoinDetailService userImpCoinDetailService;
	
	//－－－－－－－－－－－sns_frined－start－－－－－－－－－－－－
	 
	 /**
	 * @Title: http://ip:port/appservice/user/searchLongRange
     * @Description: 通讯录远程搜索(手机号和昵称搜索) 分页  startNum,endNum
     * @param @param userid
	 * @param @param nickname
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年2月7日
	 */
	 @SuppressWarnings("unchecked")
     @RequestMapping(value = "searchLongRange")
	 public BaseResp<Object> searchLongRange(String userid, String nickname, int startNum, int pageSize){
		 logger.info("seachLongRange params userid={},nickname={},startNum={},pageSize={}",userid,nickname,startNum,pageSize);
		 BaseResp<Object> baseResp = new BaseResp<>();
		 if (StringUtils.hasBlankParams(userid, nickname)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
         }
		 try {
			 return userRelationService.selectLongRangeListByUnameAndNname(Long.parseLong(userid), nickname, startNum, pageSize);
		 } catch (Exception e) {
			 logger.error("searchLongRange userid = {}, nickname = {}, msg = {}", userid, nickname, e);
		 }
		 return baseResp;
	 }
	 
	 /**
	 * @Title: http://ip:port/appservice/user/searchLocal
     * @Description: 通讯录本地搜索(手机号和昵称搜索)  (没用到接口，3.0版本客户端做本地搜索)
     *  分页  startNum,endNum
     * @param @param userid
	 * @param @param nickname
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年2月7日
	 */
	 @SuppressWarnings("unchecked")
     @RequestMapping(value = "searchLocal")
	 public BaseResp<Object> searchLocal(String userid, String nickname, int startNum, int endNum){
		 logger.info("searchLocal params userid={},nickname={},startNum={},endNum={}",userid,nickname,startNum,endNum);
		 BaseResp<Object> baseResp = new BaseResp<>();
		 if (StringUtils.hasBlankParams(userid, nickname)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
         }
		 try {
			 return userRelationService.selectLocalListByUnameAndNname(Long.parseLong(userid), nickname, startNum, endNum);
		 } catch (Exception e) {
			 logger.error("searchLocal userid = {}, nickname = {}, msg = {}", userid, nickname, e);
		 }
		 return baseResp;
	 }
	 
	/**
	* @Title: http://ip:port/app_service/user/insertFriend
	* @Description: 添加好友
	* @param @param userid
	* @param @param friendid
	* @auther smkk
	* @currentdate:2017年1月20日
	*/
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "insertFriend")
	public BaseResp<Object> insertFriend(String userid, String friendid){
		logger.info("insertfriend params userid={},friendid={}",userid,friendid);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid, friendid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
		try {
			return userRelationService.insertFriend(Long.parseLong(userid), Long.parseLong(friendid));
		} catch (Exception e) {
			logger.error("insertfriend error and msg={}",e);
		}
		return baseResp;
	}
	/**
	* @Title: http://ip:port/app_service/user/selectListByUserId
	* @Description: 查询好友列表 通过好友id
	* @param @param userid
	* @param updateTime 上次同步的时间,如果是获取整个好友列表,则不需要传该参数或仅限于传0
	 *                  如果传入updateTime ,请传入格式为 2017-03-16 10:00:00 的时间格式
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 * @update by smkk on 2017-05-12  该方法暂时不用了
	 */
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "selectListByUserId")
	public BaseResp<Object> selectListByUserId(String userid ,Integer startNum,Integer pageSize,String updateTime){
		logger.info("userid={},startNum={},pageSize={},updateTime={}",userid,startNum,pageSize,updateTime);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
		if(startNum != null && startNum < 0){
			startNum = 0;
		}
		if(null == pageSize){
			pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		}


		Date updateDate = null;
		if(StringUtils.isNotEmpty(updateTime) && !"0".equals(updateTime)){
			try{
				updateDate = DateUtils.formatDate(updateTime,null);
			}catch (Exception e){
				logger.error("select friend list by userId userId:{} startNum:{} pageSize:{} updateTime;{}",userid,startNum,pageSize,updateTime);
				updateDate = null;
			}
		}
		try {
			return userRelationService.selectListByUserId(Long.parseLong(userid), startNum, pageSize,updateDate);
		} catch (Exception e) {
			logger.error("selectListByUserId userid = {}, startNum = {}, pageSize = {}", userid, startNum, pageSize, e);
		}
		return baseResp;
	}
	/**
	* @Title: http://ip:port/app_service/user/delete
	* @Description: 删除好友
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "delete")
	public BaseResp<Object> delete(String userid, String friendid){
		logger.info("delete params userid={},friendid={}",userid,friendid);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid, friendid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
		try {
			return userRelationService.delete(Long.parseLong(userid), Long.parseLong(friendid));
		} catch (Exception e) {
			logger.error("delete error and msg={}",e);
		}
		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/appservice/user/updateRemark
    * @Description: 修改好友备注
    * @param @param userid
    * @param @param friendid
    * @param @param remark
    * @param @param code 0
    * @auther yinxc
    * @currentdate:2017年2月7日
    */
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "updateRemark")
	public BaseResp<Object> updateRemark(String userid, String friendid, String remark){
		logger.info("updateRemark params userid = {}, friendid = {}, remark = {}", userid, friendid, remark);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid, friendid, remark)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
		try {
			return userRelationService.updateRemarkByUidAndFid(Long.parseLong(userid), Long.parseLong(friendid), remark);
		} catch (Exception e) {
			logger.error("delete error and msg={}",e);
		}
		return baseResp;
	}
	
	//－－－－－－－－－－－sns_frined－end－－－－－－－－－－－－－-
	//－－－－－－－－－－－sns_fans－start－－－－－－－－－－－－
	/**
	* @Title: http://ip:port/app_service/user/insertFans
	* @Description: 添加关注
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "insertFans")
	public BaseResp<Object> insertFans(String userid, String friendid){
		logger.info("insertFans params userid={},friendid={}",userid,friendid);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid, friendid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
		try {
			return userRelationService.insertFans(Long.parseLong(userid), Long.parseLong(friendid));
		} catch (Exception e) {
			logger.error("insertFans error and msg={}",e);
		}
		return baseResp;
	}
	/**
	* @Title: http://ip:port/app_service/user/deleteFans
	* @Description: 删除粉丝
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "deleteFans")
	public BaseResp<Object> deleteFans(String userid, String friendid){
		logger.info("deleteFans params userid={},friendid={}",userid,friendid);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid, friendid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
		try {
			return userRelationService.deleteFans(Long.parseLong(userid), Long.parseLong(friendid));
		} catch (Exception e) {
			logger.error("deleteFans error and msg={}",e);
		}
		return baseResp;
	}
	/**
	* @Title: http://ip:port/app_service/user/selectFansListByUserId
	* @Description: 查询关注的人员列表：粉丝列表
	* @param @param userid
	 *@param @param friendid 当前访问者id
	* @param ftype 0:查询关注列表   1：粉丝列表
	* @param @param startNum pageSize
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "selectFansListByUserId")
	public BaseResp<List<SnsFans>> selectFansListByUserId(String userid, String friendid, String ftype,
														  Integer startNum, Integer pageSize){
		logger.info("userid={},ftype={}startNum={},pageSize={}",userid,ftype,startNum,pageSize);
		BaseResp<List<SnsFans>> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid, friendid, ftype)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
		try {
			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
			if(null != startNum){
				sNo = startNum.intValue();
			}
			if(null != pageSize){
				sSize = pageSize.intValue();
			}
			return userRelationService.selectFansListByUserId(Long.parseLong(userid), Long.parseLong(friendid), ftype, sNo, sSize);
		} catch (Exception e) {
			logger.error("selectFansListByUserId error and msg={}",e);
		}
		return baseResp;
	}

	/**
	 * 加载推荐的达人
	 * @url http://ip:port/app_service/user/selectFashionManUser
	 * @param userid 
	 * @param startNum 开始页码
	 * @param pageSize 结束页码
     * @return
     */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "selectFashionManUser")
	public BaseResp<Object> selectFashionManUser(String userid, Integer startNum, Integer pageSize ){
		logger.info("userid={},startNum={},pageSize={}",userid,startNum,pageSize);
		BaseResp<Object> baseResp = new BaseResp<Object>();
		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
		if(startNum == null || startNum < 0){
			startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
		}
		if(null == pageSize){
			pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		}
		baseResp = this.userRelationService.selectFashionManUser(Long.parseLong(userid), startNum, pageSize);
		return baseResp;
	}
	//－－－－－－－－－－－sns_fans－end－－－－－－－－－－－－－-
	//－－－－－－－－－－－sns_frined－start－－－－－－－－－－－－
	
	//－－－－－－－－－－－sns_frined－end－－－－－－－－－－－－－-

	/**
	 *
	 * @return
     */
	@RequestMapping(value="test")
	public BaseResp<Object> test(){
		return this.userRelationService.selectFansListByLikeUserid(Long.parseLong("17012211590782000"),false,null,null);
	}

	/**
	 * @url user/selectRelationList
	 * 获取关系用户数据变化接口 用户关系同步接口
	 * @param userid
	 * @param updateTime  2017-05-11 00：00：00
	 * @return
	 */
	@RequestMapping(value="selectRelationList")
	public BaseResp<List<AppUserMongoEntity>> selectRelationList(String userid, String updateTime){
		logger.info("selectRelationList userid={},dataStr={}",userid,updateTime);
		BaseResp<List<AppUserMongoEntity>> baseResp = new BaseResp<>();
		if("0".equals(updateTime)){
			updateTime = null;
		}
		try{
			baseResp = userRelationService.selectRelationList(userid,updateTime);
		}catch (Exception e){
			logger.error("selectRelationList userid={},dataStr={}",userid,updateTime,e);
		}
		return baseResp;
	}

}
