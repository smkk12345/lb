package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.UserCard;

public interface ClassroomService {
	
	/**
     * 获取教室列表 推荐的 针对app
     * @param startNo
     * @param pageSize
     * @return
     * @author yinxc
     */
    BaseResp<List<Classroom>> selectClassroomListForApp(long userid,Integer startNo,Integer pageSize);

	Classroom selectByClassroomid(long classroomid);
	
	BaseResp<Object> updateByClassroomid(Classroom record);
	
	/**
	 * @author yinxc
	 * 获取教室详情信息---教室有关数据(拆分)
	 * 2017年3月6日
	 * @param classroomid 教室业务id
	 * @param userid 当前访问者id
	 */
	BaseResp<Object> selectRoomDetail(Long classroomid, Long userid);
	
	/**
	 * @author yinxc
	 * 获取教室详情信息---教室有关数据(拆分)---教室顶部数据
	 * 2017年6月14日
	 * @param classroomid 教室业务id
	 * @param userid 当前访问者id
	 */
	BaseResp<Object> selectRoomHeadDetail(Long classroomid, Long userid);
	
	/**
	 * @author yinxc
	 * 获取老师h5信息
	 * 2017年7月20日
	 * @param classroomid 教室业务id
	 */
	BaseResp<Object> selectUsercard(long classroomid);
	
	/**
	 * @author yinxc
	 * 获取教室详情---教室介绍信息
     * @param @param classroomid 教室业务id
	 * 2017年7月14日
	 */
	BaseResp<Object> croomDetail(long classroomid);
	
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
	BaseResp<Object> selectClassroomListByIspublic(long userid, String ispublic, String ptype, int startNum, int endNum);
	
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
	 * param userid
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	BaseResp<List<Classroom>> selectInsertByUserid(long userid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 获取已加入的教室信息List
	 * param userid
	 * param pageNo   pageSize
	 * 2017年7月19日
	 */
	BaseResp<List<Classroom>> selectFansByUserid(long userid, int startNum, int endNum);
	
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	//--------------------------admin调用方法------------------------------------
	/**
	 * @author yinxc
	 * 获取教室信息
	 * @param isup   0 - 未发布 。1 --已发布
	 * @param isdel  0 未删除。1 删除
	 * @param startNum   endNum
	 * 2017年6月17日
	 */
	BaseResp<Page<Classroom>> selectPcClassroomList(String isup, String isdel, int startNum, int endNum);
	
	/**
    * @Description: 检查教室标题是否重复
    * @param @param classtitle  教室标题
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @return 0:不存在重复   1：存在重复需要更改
    * @auther yinxc
    * @currentdate:2017年7月11日
	*/
	BaseResp<Object> checkClasstitle(String classtitle);
	
	/**
    * @Description: 获取教室名片列表
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年6月17日
	*/
	BaseResp<List<UserCard>> selectPcUserCardList(int startNum, int endNum);
	
	/**
    * @Description: 添加教室
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年7月4日
	*/
	BaseResp<Object> insertClassroom(Classroom record);
	
	/**
    * @Description: 获取教室列表
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年7月4日
	*/
	BaseResp<Page<Classroom>> selectPcSearchClassroomList(Classroom classroom, int startNum, int endNum);
	
	/**
     * @Description: 关闭教室
     * @param @param classroomid 教室id
     * @param @param closeremark 关闭原因
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
	BaseResp<Object> closeRoom(long classroomid, String closeremark);
	
	/**
     * @Description: 删除教室
     * @param @param classroomid 教室id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
	BaseResp<Object> delRoom(long classroomid);
	
	/**
     * @Description: 发布教室
     * @param @param classroomid 教室id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
	BaseResp<Object> uproom(long classroomid);
	
	/**
     * 更新教室推荐状态
     * @param classroomid
     * @param isrecommend
     * @return
     */
	BaseResp<Object> updateRoomRecommend(long classroomid, String isrecommend);
	
	/**
     * 更新教室推荐权重
     * @param classroomid
     * @param weight
     * @return
     */
	BaseResp<Object> updateRoomRecommendSort(long classroomid, String weight);
	
	/**
	 * @author yinxc
	 * 修改教室公告---classnotice
	 * 2017年3月2日
	 * param classnotice 公告
	 * param userid 老师id
	 * param classroomid 教室业务id
	 * param ismsg 是否@全体成员   0：否   1：是
	 */
	BaseResp<Object> updateClassnoticeByPCClassroomid(long classroomid, long userid, String classnotice, String ismsg);
	
	
	//----------------------share调用----------------------------
	
	/**
	 * @author yinxc
	 * 获取教室详情信息
	 * 2017年3月6日
	 * @param classroomid 教室业务id
	 * @param userid 当前访问者id
	 */
	BaseResp<Object> selectRoomDetailAll(Long classroomid, Long userid);

	/**
	 * 当前用户是否为老师
	 * @param userid
	 * @param classroom
	 * @return
	 */
	int isTeacher(String userid,Classroom classroom);
	
}
