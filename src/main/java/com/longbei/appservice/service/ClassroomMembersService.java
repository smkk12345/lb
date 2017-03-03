package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.ClassroomMembers;

public interface ClassroomMembersService {
	
	/**
    * @Description: 添加教室成员
    * @param @param classroomid 教室id 
    * @param @param userid
    * 成员在加入教室之前，如果该教室收费，需先交费后才可加入
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	BaseResp<Object> insertClassroomMembers(ClassroomMembers record);
	
	/**
	 * @author yinxc
	 * 获取教室成员列表---(剔除   退出教室的)
	 * 2017年2月28日
	 * param classroomid 教室id
	 */
	BaseResp<Object> selectListByClassroomid(long classroomid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 获取我加入的教室成员列表---(剔除   退出教室的)
	 * 2017年2月28日
	 * param classroomid 教室id
	 */
	List<ClassroomMembers> selectInsertByUserid(long userid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 获取教室成员信息
	 * 2017年2月28日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室     为null查全部
	 */
	ClassroomMembers selectListByClassroomidAndUserid(long classroomid, long userid, String itype);
	
	/**
	 * @author yinxc
	 * 教室剔除成员
	 * 2017年2月28日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室
	 * 
	 * 教室剔除成员，如果该成员已经交费，老师强制剔除成员时，需要把成员所交费用返回
	 */
	BaseResp<Object> updateItypeByClassroomidAndUserid(long classroomid, long userid, String itype);

}
