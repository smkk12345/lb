package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.CircleMapper;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.ImpComplaintsMapper;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Circle;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.ImpComplaints;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.service.ImpComplaintsService;
import com.longbei.appservice.service.ImproveService;

@Service("impComplaintsService")
public class ImpComplaintsServiceImpl implements ImpComplaintsService {

	@Autowired
	private ImpComplaintsMapper impComplaintsMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private RankMapper rankMapper;
	@Autowired
	private CircleMapper circleMapper;
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private ImproveService improveService;
//	@Autowired
//	private RankService rankService;
	
	
	private static Logger logger = LoggerFactory.getLogger(ImpComplaintsServiceImpl.class);
	
	@Override
	public BaseResp<ImpComplaints> insertSelective(ImpComplaints record, long friendid) {
		BaseResp<ImpComplaints> reseResp = new BaseResp<>();
		try {
			String username = initMsgUserInfoByFriendid(record.getUserid());
			String cusername = initMsgUserInfoByFriendid(friendid);
			record.setUsername(username);
			record.setCusername(cusername);
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean insert(ImpComplaints record){
		int temp = impComplaintsMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public Page<ImpComplaints> selectListByStatus(String status, int pageNo, int pageSize) {
		Page<ImpComplaints> page = new Page<>(pageNo, pageSize);
		try {
            int totalcount = impComplaintsMapper.selectCountByStatus(status);
//            pageNo = Page.setPageNo(pageNo, totalcount, pageSize);
            List<ImpComplaints> list = impComplaintsMapper.selectListByStatus(status, pageNo, pageSize);
            if(null != list && list.size()>0){
            	for (ImpComplaints impComplaints : list) {
            		//获取各类型标题
            		getImpComplaintsTitle(impComplaints);
            		//各类型进步被投诉次数
            		Integer compcount = impComplaintsMapper.selectCountByImpid(status, impComplaints.getImpid(), 
            				impComplaints.getBusinessid(), impComplaints.getBusinesstype());
            		impComplaints.setCompcount(compcount);
            		//获取选择的投诉类型
            		getContenttype(impComplaints);
            		//实例化用户信息
            		selectUserNickname(impComplaints);
				}
            }
            page.setTotalCount(totalcount);
            page.setList(list);
        } catch (Exception e) {
            logger.error("selectListByStatus status = {}, pageNo = {}, pageSize = {}", 
            		status, pageNo, pageSize, e);
        }
        return page;
	}
	
	@Override
	public Page<ImpComplaints> searchList(String status, String username, String businesstype, String sdealtime,
			String edealtime, int pageNo, int pageSize) {
		Page<ImpComplaints> page = new Page<>(pageNo, pageSize);
		try {
            int totalcount = impComplaintsMapper.searchCount(status, username, businesstype, sdealtime, edealtime);
//            pageNo = Page.setPageNo(pageNo, totalcount, pageSize);
            List<ImpComplaints> list = impComplaintsMapper.searchList(status, username, businesstype, sdealtime, 
            		edealtime, pageNo, pageSize);
            if(null != list && list.size()>0){
            	for (ImpComplaints impComplaints : list) {
            		//获取各类型标题
            		getImpComplaintsTitle(impComplaints);
            		//各类型进步被投诉次数
            		Integer compcount = impComplaintsMapper.selectCountByImpid(status, impComplaints.getImpid(), 
            				impComplaints.getBusinessid(), businesstype);
            		impComplaints.setCompcount(compcount);
            		//获取选择的投诉类型
            		getContenttype(impComplaints);
            		//实例化用户信息
            		selectUserNickname(impComplaints);
				}
            }
            page.setTotalCount(totalcount);
            page.setList(list);
        } catch (Exception e) {
            logger.error("searchList status = {}, username = {}, businesstype = {}", 
            		status, username, businesstype, e);
        }
        return page;
	}
	
	private void selectUserNickname(ImpComplaints impComplaints){
		String nickname = initNicknameByFriendid(impComplaints.getUserid());
		impComplaints.setNickname(nickname);
		String cnickname = initNicknameByFriendid(impComplaints.getComuserid());
		impComplaints.setCnickname(cnickname);
	}
	
	/**
	* @Title: getContenttype 
	* @Description: 获取选择的投诉类型
	* @param @param impComplaints    设定文件 
	* @return void    返回类型
	 */
	private void getContenttype(ImpComplaints impComplaints){
		String contenttype = "";
		String contenttypes[] = impComplaints.getContenttype().split(",");
		if(contenttypes.length>1){
			for (int i = 0; i < contenttypes.length; i++) {
				if("1".equals(contenttypes[i])){
					contenttype += "垃圾营销信息,";
				}else if("2".equals(contenttypes[i])){
					contenttype += "虚假/恶意中伤信息,";
				}else if("3".equals(contenttypes[i])){
					contenttype += "敏感信息,";
				}else if("4".equals(contenttypes[i])){
					contenttype += "淫秽色情信息,";
				}else if("5".equals(contenttypes[i])){
					contenttype += "冒名顶替其他用户,";
				}else if("6".equals(contenttypes[i])){
					contenttype += "涉嫌抄袭,";
				}else{
					contenttype += "内容与榜主题不符,";
				}
			}
			contenttype = contenttype.substring(0, contenttype.length()-1);
		}else{
			if("1".equals(contenttypes[0])){
				contenttype = "垃圾营销信息";
			}else if("2".equals(contenttypes[0])){
				contenttype = "虚假/恶意中伤信息";
			}else if("3".equals(contenttypes[0])){
				contenttype = "敏感信息";
			}else if("4".equals(contenttypes[0])){
				contenttype = "淫秽色情信息";
			}else if("5".equals(contenttypes[0])){
				contenttype = "冒名顶替其他用户";
			}else if("6".equals(contenttypes[0])){
				contenttype += "涉嫌抄袭";
			}else{
				contenttype = "内容与榜主题不符";
			}
		}
		impComplaints.setContenttype(contenttype);
	}
	
	/**
	* @Title: getImpComplaintsTitle 
	* @Description: 获取各类型标题
	* @param @param impComplaints    设定文件 
	* @return void    返回类型
	 */
	private void getImpComplaintsTitle(ImpComplaints impComplaints){
		//businesstype 类型    0 零散进步(无标题)   1 目标进步(无标题)    2 榜中  3圈子中进步 4 教室     
		if("1".equals(impComplaints.getBusinesstype())){
			//1  目标进步(无标题) 
			impComplaints.setBusinessname("目标");
		}else if("2".equals(impComplaints.getBusinesstype())){
			//2 榜中   获取榜标题
			Rank rank = rankMapper.selectByPrimaryKey(impComplaints.getBusinessid());
			if(null != rank){
				impComplaints.setBusinesstitle(rank.getRanktitle());
			}
			impComplaints.setBusinessname("龙榜");
		}else if("3".equals(impComplaints.getBusinesstype())){
			//3圈子中   获取圈子标题
			Circle circle = circleMapper.selectByPrimaryKey(impComplaints.getBusinessid());
			if(null != circle){
				impComplaints.setBusinesstitle(circle.getCircletitle());
			}
			impComplaints.setBusinessname("圈子");
		}else if("4".equals(impComplaints.getBusinesstype())){
			//4 教室中    获取教室标题
			Classroom classroom = classroomMapper.selectByPrimaryKey(impComplaints.getBusinessid());
			if(null != classroom){
				impComplaints.setBusinesstitle(classroom.getClasstitle());
			}
			impComplaints.setBusinessname("教室");
		}else{
			impComplaints.setBusinessname("其他");
		}
	}

	@Override
	public BaseResp<Object> updateImpComplaintsByStatus(long id, String status, Date dealtime, String dealuser,
			String checkoption) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			ImpComplaints impComplaints = impComplaintsMapper.selectByPrimaryKey(id);
			boolean temp = updateStatus(status, dealtime, dealuser, checkoption, impComplaints.getImpid(), 
					impComplaints.getBusinessid(), impComplaints.getBusinesstype());
			if (temp) {
				//status 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理  4: 已忽略
				if("1".equals(status)){
					//businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
					if("2".equals(impComplaints.getBusinesstype())){
						//删除
						improveService.removeImprove(impComplaints.getComuserid().toString(), impComplaints.getImpid().toString(), 
								impComplaints.getBusinesstype().toString(), impComplaints.getBusinessid().toString());
					}
				}
				if("2".equals(status)){
					//businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
					if("2".equals(impComplaints.getBusinesstype())){
						//下榜
						improveService.removeImproveFromBusiness(impComplaints.getImpid().toString(), 
								impComplaints.getBusinessid().toString(), impComplaints.getBusinesstype().toString());
					}
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateImpComplaintsByStatus status = {}, dealuser = {}, "
					+ "checkoption = {}, id = {}", 
					status, dealuser, checkoption, id, e);
		}
		return reseResp;
	}
	
	private boolean updateStatus(String status, Date dealtime, String dealuser,
			String checkoption, long impid, long businessid, String businesstype){
		int temp = impComplaintsMapper.updateImpComplaintsByStatus(status, dealtime, dealuser, 
				checkoption, impid, businessid, businesstype);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<ImpComplaints> selectDetail(long id) {
		BaseResp<ImpComplaints> reseResp = new BaseResp<>();
		try {
			ImpComplaints impComplaints = impComplaintsMapper.selectByPrimaryKey(id);
			if(null != impComplaints){
				Improve improve = improveService.selectImproveByImpid(impComplaints.getImpid(), 
						impComplaints.getComuserid().toString(),  
						impComplaints.getBusinesstype(), impComplaints.getBusinessid().toString());
				impComplaints.setImprove(improve);
				//获取各类型标题
				getImpComplaintsTitle(impComplaints);
				//各类型进步被投诉次数
        		Integer compcount = impComplaintsMapper.selectCountByImpid(impComplaints.getStatus(), impComplaints.getImpid(), 
        				impComplaints.getBusinessid(), impComplaints.getBusinesstype());
        		impComplaints.setCompcount(compcount);
        		//获取选择的投诉类型
        		getContenttype(impComplaints);
        		//实例化用户信息
        		selectUserNickname(impComplaints);
			}
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			reseResp.setData(impComplaints);
		} catch (Exception e) {
			logger.error("selectDetail id = {}", id, e);
		}
		return reseResp;
	}
	
	
//	private Improve selectImprove(Long impid,
//            String businesstype,String businessid, String isdel,String ispublic){
//		Improve improve = null;
//		try {
//		    switch (businesstype){
//		        case Constant.IMPROVE_SINGLE_TYPE:
//		            improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE,isdel,ispublic);
//		            break;
//		        case Constant.IMPROVE_RANK_TYPE:
//		            improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_RANK,isdel,ispublic);
//		            break;
//		        case Constant.IMPROVE_CLASSROOM_TYPE:
//		            improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_CLASSROOM,isdel,ispublic);
//		            break;
//		        case Constant.IMPROVE_CIRCLE_TYPE:
//		            improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_CIRCLE,isdel,ispublic);
//		            break;
//		        case Constant.IMPROVE_GOAL_TYPE:
//		            improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_GOAL,isdel,ispublic);
//		            break;
//		        default:
//		            improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE,isdel,ispublic);
//		            break;
//		    }
//		
//		} catch (Exception e) {
//		    logger.error("select improve by id:{} businesstype:{} businessid:{}",
//		            impid,businesstype,businessid,e);
//		}
//		return improve;
//	}
	
	


	
	//------------------------公用方法，初始化用户信息------------------------------------------
	/**
     * 初始化用户信息 ------Friendid
     */
    private String initMsgUserInfoByFriendid(long friendid){
		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(friendid));
		if(null != appUserMongoEntity){
			return appUserMongoEntity.getUsername();
		}
        return "";
    }

    /**
     * 初始化用户信息 ------Friendid
     */
    private String initNicknameByFriendid(long friendid){
		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(friendid));
		if(null != appUserMongoEntity){
			return appUserMongoEntity.getNickname();
		}
        return "";
    }

}
