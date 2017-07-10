package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.ClassroomMembers;

public interface ClassroomMembersService {
	
	/**
    * @Description: 添加教室成员
    * 先判断教室参与人数是否已满
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
	BaseResp<List<ClassroomMembers>> selectListByClassroomid(long classroomid, int startNum, int endNum);
	
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
	 * 成员退出教室
	 * 2017年2月28日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室
	 * 
	 * 教室剔除成员，如果该成员已经交费，老师强制剔除成员时，需要把成员所交费用返回
	 */
	BaseResp<Object> updateItypeByClassroomidAndUserid(long classroomid, long userid, String itype);
	
	/**
	 * @author yinxc
	 * 教室老师剔除成员---推送消息
	 * 2017年7月7日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室
	 */
	BaseResp<Object> quitClassroom(long classroomid, long userid, String itype);
	
	
	
	
	
	
	//--------------------------admin调用方法------------------------------------
	
	/**
	 * @author yinxc
	 * 获取教室成员列表---(剔除   退出教室的)
	 * 2017年7月7日
	 * param classroomMembers 教室
	 */
	BaseResp<Page<ClassroomMembers>> selectPcMembersList(ClassroomMembers classroomMembers, int startNum, int endNum);
	
	/**
     * 教室中单条进步下教室或删除
     * @param status 0：未处理 1： 删除微进步    2： 下教室微进步  3： 通过其他方式已处理  4: 已忽略
     * @param userid
     * @param classroomid
     * @param improveid
     */
    BaseResp<Object> updateStatus(String status,String userid,String classroomid,String improveid);
	

}
