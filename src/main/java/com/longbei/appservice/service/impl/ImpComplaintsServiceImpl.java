package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import com.longbei.appservice.common.constant.Constant_table;
import com.longbei.appservice.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.CircleMapper;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.ImpComplaintsMapper;
import com.longbei.appservice.dao.UserGoalMapper;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.dao.ImproveMapper;
import com.longbei.appservice.dao.RankMembersMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.service.ImpComplaintsService;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.UserMsgService;

@Service("impComplaintsService")
public class ImpComplaintsServiceImpl implements ImpComplaintsService {

	@Autowired
	private ImpComplaintsMapper impComplaintsMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private UserGoalMapper userGoalMapper;
	@Autowired
	private RankMapper rankMapper;
	@Autowired
	private CircleMapper circleMapper;
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private ImproveService improveService;
	@Autowired
	private UserMsgService userMsgService;
	@Autowired
	private ImproveMapper improveMapper;
	@Autowired
	private RankMembersMapper rankMembersMapper;
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
			if("2".equals(record.getBusinesstype()))
			{
				rankMembersMapper.updateRankMenberComplaincount(record.getBusinessid(),record.getComuserid());
			}
			updateImpComplaincount(record);
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

	/**
	 * @Title: updateImpComplaincount
	 * @Description: 更新进步的被投诉次数
	 * @return boolean 返回类型
	 * @auther IngaWu
	 * @currentdate:2017年6月8日
	 */
	private void updateImpComplaincount(ImpComplaints impComplaints){
		String impid = impComplaints.getImpid()+"";
		//businesstype 类型   0 零散进步  1 目标进步  2 榜中进步  3圈子中进步 4 教室进步
		if("0".equals(impComplaints.getBusinesstype())){
			improveMapper.updateImpComplaincount(impid,Constant_table.IMPROVE);
		}else if("1".equals(impComplaints.getBusinesstype())){
			improveMapper.updateImpComplaincount(impid, Constant_table.IMPROVE_GOAL);
		}else if("2".equals(impComplaints.getBusinesstype())){
			improveMapper.updateImpComplaincount(impid,Constant_table.IMPROVE_RANK);
		}else if("3".equals(impComplaints.getBusinesstype())){
			improveMapper.updateImpComplaincount(impid,Constant_table.IMPROVE_CIRCLE);
		}else if("4".equals(impComplaints.getBusinesstype())){
			improveMapper.updateImpComplaincount(impid,Constant_table.IMPROVE_CLASSROOM);
		}
	}

	@Override
	public Page<ImpComplaints> selectListByStatus(String status, int pageNo, int pageSize) {
		Page<ImpComplaints> page = new Page<>(pageNo, pageSize);
		try {
            int totalcount = impComplaintsMapper.selectCountByStatus(status);
//            pageNo = Page.setPageNo(pageNo, totalcount, pageSize);
			List<ImpComplaints> list = impComplaintsMapper.selectListByStatus(status, pageSize*(pageNo-1), pageSize);
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
            		edealtime, (pageNo-1)*pageSize, pageSize);
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
					contenttype += "微进步内容与龙榜无关,";
				}else if("2".equals(contenttypes[i])){
					contenttype += "垃圾营销信息,";
				}else if("3".equals(contenttypes[i])){
					contenttype += "虚假/恶意中伤信息,";
				}else if("4".equals(contenttypes[i])){
					contenttype += "敏感信息,";
				}else if("5".equals(contenttypes[i])){
					contenttype += "淫秽色情信息,";
				}else if("6".equals(contenttypes[i])){
					contenttype += "冒名顶替其他用户,";
				}else if("7".equals(contenttypes[i])){
					contenttype += "涉嫌抄袭,";
				}else if("8".equals(contenttypes[i])){
					contenttype += "泄露Ta人隐私,";
				}else{
					contenttype += "其他,";
				}
			}
			contenttype = contenttype.substring(0, contenttype.length()-1);
		}else{
			if("1".equals(contenttypes[0])){
				contenttype += "微进步内容与龙榜无关";
			}else if("2".equals(contenttypes[0])){
				contenttype += "垃圾营销信息";
			}else if("3".equals(contenttypes[0])){
				contenttype += "虚假/恶意中伤信息";
			}else if("4".equals(contenttypes[0])){
				contenttype += "敏感信息";
			}else if("5".equals(contenttypes[0])){
				contenttype += "淫秽色情信息";
			}else if("6".equals(contenttypes[0])){
				contenttype += "冒名顶替其他用户";
			}else if("7".equals(contenttypes[0])){
				contenttype += "涉嫌抄袭";
			}else if("8".equals(contenttypes[0])){
				contenttype += "泄露Ta人隐私";
			}else{
				contenttype += "其他";
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
		//businesstype 类型    0 零散进步(无标题)   1 目标进步   2 榜中  3圈子中进步 4 教室
		if("1".equals(impComplaints.getBusinesstype())){
			//1  目标进步 获取目标标题
			UserGoal userGoal = userGoalMapper.selectByGoalId(impComplaints.getBusinessid());
			if(null != userGoal){
				impComplaints.setBusinesstitle(userGoal.getGoaltag());
			}
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
					impComplaints.getBusinessid(), impComplaints.getBusinesstype(),id);
			if (temp) {
				//status 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理  4: 已忽略
				if("1".equals(status)){
					//businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
					if("0".equals(impComplaints.getBusinesstype()) || "1".equals(impComplaints.getBusinesstype())
							|| "2".equals(impComplaints.getBusinesstype())
							|| "3".equals(impComplaints.getBusinesstype())
							|| "4".equals(impComplaints.getBusinesstype())){
						//删除
						improveService.removeImprove(impComplaints.getComuserid().toString(), impComplaints.getImpid().toString(), 
								impComplaints.getBusinesstype().toString(), impComplaints.getBusinessid().toString());
						Improve improve = improveService.selectImproveByImpidMuc(impComplaints.getImpid(), 
								impComplaints.getComuserid().toString(), 
								impComplaints.getBusinesstype().toString(), impComplaints.getBusinessid().toString());
						String remark = Constant.MSG_IMP_DEL_MODEL;
						if(null != improve){
							if(!StringUtils.isBlank(improve.getBrief())){
								if(improve.getBrief().length()>=20){
									//抓取内容20个字
									String brief = improve.getBrief().substring(0, 20);
									remark = remark.replace("n", "(" + brief + ")");
								}else{
									remark = remark.replace("n", "(" + improve.getBrief() + ")");
								}
							}else{
								remark = remark.replace("n", "");
							}
						}
//						remark = Constant.MSG_IMP_DEL_MODEL.replace("n", flowernum + "");
//						String remark = "您的进步已被删除,原因：" + checkoption;
		            	//mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
						//						22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
						//						25:订单发货N天后自动确认收货    26：实名认证审核结果
						//						27:工作认证审核结果      28：学历认证审核结果
						//						29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
						//						32：创建的龙榜/教室/圈子被选中推荐  
						//						40：订单已取消 41 榜中进步下榜   
						// 						42.榜单公告更新   43:后台反馈回复消息    45:榜中删除成员进步)
		            	//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
						//			10：榜中  11 圈子中  12 教室中  13:教室批复作业   14:反馈 15 关注
						if("1".equals(impComplaints.getBusinesstype())){
							if(!StringUtils.isBlank(impComplaints.getBusinessid().toString())){
								userMsgService.insertMsg(Constant.SQUARE_USER_ID, impComplaints.getComuserid().toString(), 
				            			impComplaints.getImpid().toString(), impComplaints.getBusinesstype().toString(), 
				            			impComplaints.getBusinessid().toString(), remark, "0", "45", "榜中删除成员进步", 0, "", "");
							}else{
								if(!StringUtils.isBlank(improve.getGoalid().toString())){
									userMsgService.insertMsg(Constant.SQUARE_USER_ID, impComplaints.getComuserid().toString(), 
					            			impComplaints.getImpid().toString(), impComplaints.getBusinesstype().toString(), 
					            			improve.getGoalid().toString(), remark, "0", "45", "榜中删除成员进步", 0, "", "");
								}
								
							}
						}else{
							userMsgService.insertMsg(Constant.SQUARE_USER_ID, impComplaints.getComuserid().toString(), 
			            			impComplaints.getImpid().toString(), impComplaints.getBusinesstype().toString(), 
			            			impComplaints.getBusinessid().toString(), remark, "0", "45", "榜中删除成员进步", 0, "", "");
						}
					}
				}
				if("2".equals(status)){
					//businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
					if("2".equals(impComplaints.getBusinesstype())){
						//下榜
						improveService.removeImproveFromBusiness(impComplaints.getImpid().toString(), 
								impComplaints.getBusinessid().toString(), impComplaints.getBusinesstype().toString());
						Rank rank = rankMapper.selectRankByRankid(impComplaints.getBusinessid());
						String remark = Constant.MSG_RANKIMP_QUIT_MODEL;
						if(null != rank){
							remark = remark.replace("n", rank.getRanktitle());
						}else{
							remark = remark.replace("n", "");
						}
//						String remark = "您的进步已被下榜,原因：" + checkoption;
		            	//mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
						//						22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
						//						25:订单发货N天后自动确认收货    26：实名认证审核结果
						//						27:工作认证审核结果      28：学历认证审核结果
						//						29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
						//						32：创建的龙榜/教室/圈子被选中推荐  
						//						40：订单已取消 41 榜中进步下榜   
						// 						42.榜单公告更新   43:后台反馈回复消息    45:榜中删除成员进步)
		            	//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
						//			10：榜中  11 圈子中  12 教室中  13:教室批复作业   14:反馈 15 关注
		            	userMsgService.insertMsg(Constant.SQUARE_USER_ID, impComplaints.getComuserid().toString(), 
		            			impComplaints.getImpid().toString(), impComplaints.getBusinesstype().toString(), 
		            			impComplaints.getBusinessid().toString(), remark, "0", "41", "榜中进步下榜", 0, "", "");
						int finish = Integer.parseInt(rank.getIsfinish());
						if (finish >= 2){//假删
							int res = improveMapper.removeImproveFromBusiness("improve_rank",impComplaints.getBusinessid().toString(),impComplaints.getImpid().toString());
							if(res > 0){//更新新榜中进步条数
							rankMembersMapper.updateRankImproveCount(impComplaints.getBusinessid(),impComplaints.getComuserid(),-1);
							String remark1 = Constant.MSG_RANKIMP_REMOVE_MODEL;
							userMsgService.insertMsg(Constant.SQUARE_USER_ID, impComplaints.getComuserid().toString(),
										impComplaints.getImpid().toString(), impComplaints.getBusinesstype().toString(),
										impComplaints.getBusinessid().toString(), remark, "0", "45", "榜中删除成员进步", 0, "", "");
							}
						}
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
			String checkoption, long impid, long businessid, String businesstype,long  id){
		int temp = impComplaintsMapper.updateImpComplaintsByStatus(status, dealtime, dealuser, 
				checkoption, impid, businessid, businesstype, id);
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
