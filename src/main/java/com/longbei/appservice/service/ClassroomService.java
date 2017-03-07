package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.Classroom;

public interface ClassroomService {

	BaseResp<Object> insertClassroom(Classroom record);
	
	Classroom selectByClassroomid(long classroomid);
	
	BaseResp<Object> updateByClassroomid(Classroom record);
	
	/**
	 * @author yinxc
	 * 获取教室详情信息---教室有关数据(拆分)
	 * 2017年3月6日
	 * @param classroomid 教室业务id
	 * @param userid 当前访问者id
	 */
	BaseResp<Object> selectRoomDetail(long classroomid, long userid);
	
	/**
	 * @author yinxc
	 * 获取教室详情信息---教室课程有关数据(拆分)
	 * 2017年3月7日
	 * @param classroomid 教室业务id
	 */
	BaseResp<Object> selectCoursesDetail(long classroomid);
	
	/**
	 * @author yinxc
	 * 教室学员作业列表
	 * 2017年3月6日
	 * @param userId 当前登录用户id
	 * @param classroomid 教室业务id
	 * @param type 0.学员动态   1.按热度排列    2.按时间倒序排列 
	 * @param 
	 */
//	BaseResp<Object> selectImprove(long classroomid, long userid, String type, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 获取教室信息
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	BaseResp<Object> selectClassroomListByIspublic(long userid, String ispublic, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 获取教室信息
	 * param ptype:十全十美类型    可为null
	 * param userid
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	BaseResp<Object> selectListByUserid(long userid, String ptype, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 获取我加入的教室信息List
	 * param ptype:十全十美类型    可为null
	 * param userid
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	BaseResp<Object> selectInsertByUserid(long userid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 根据ptype获取教室信息(通用方法)
	 * param ptype:十全十美类型
	 * param keyword:关键字搜索    可为null
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	BaseResp<Object> selectListByPtype(String ptype, String keyword, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 修改教室公告---classnotice
	 * 2017年3月2日
	 * param classnotice 公告
	 * param userid 老师id
	 * param classroomid 教室业务id
	 * param ismsg 是否@全体成员   0：否   1：是
	 */
	BaseResp<Object> updateClassnoticeByClassroomid(long classroomid, long userid, String classnotice, String ismsg);
	
	/**
	 * @author yinxc
	 * 修改教室参与人数---classinvoloed
	 * param userid
	 * param classroomid 
	 * param num 加入教室为1   剔除教室为-1
	 * 2017年3月3日
	 */
	BaseResp<Object> updateClassinvoloedByClassroomid(long classroomid, long userid, Integer num);
	
}
