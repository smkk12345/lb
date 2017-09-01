package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.Classroom;

public interface ClassroomMapper {
    int deleteByPrimaryKey(@Param("classroomid") long classroomid);

    int insert(Classroom record);

    int insertSelective(Classroom record);

    Classroom selectByPrimaryKey(@Param("classroomid") long classroomid);

    int updateByPrimaryKeySelective(Classroom record);

    int updateByPrimaryKey(Classroom record);
    
    /**
	 * @author yinxc
	 * 获取教室信息
	 * param pageNo   pageSize
	 * 2017年3月3日
	 */
//    List<Classroom> selectClassroomList(@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 获取教室信息
	 * param isup 是否所有人可见。0 所有人可见。1，部分可见
	 * param isrecommend 是否推荐。0 - 没有推荐 1 - 推荐
	 * param pageNo   pageSize
	 * 2017年3月3日
	 */
    List<Classroom> selectClassroomListByIspublic(@Param("isup") String isup,
												  @Param("ptype") String ptype,
												  @Param("isrecommend") String isrecommend,
												  @Param("startNum") int startNum, @Param("endNum") int endNum);
    /**
	 * @author yinxc
	 * 获取我加入的教室信息
	 * param userid 
	 * param startNum   endNum
	 * 2017年7月21日
	 */
    List<Classroom> selectInsertByUserid(@Param("userid") long userid, 
    									@Param("startNum") int startNum, 
    									@Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 获取教室信息
	 * param ptype:十全十美类型    可为null
	 * param userid
	 * param pageNo   pageSize
	 * 2017年2月27日
	 */
    List<Classroom> selectListByUserid(@Param("userid") long userid, @Param("ptype") String ptype, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 根据ptype获取教室信息(通用方法)
	 * param ptype:十全十美类型
	 * param keyword:关键字搜索    可为null
	 * param pageNo   pageSize
	 * 2017年2月27日
	 */
    List<Classroom> selectListByPtype(@Param("ptype") String ptype, @Param("keyword") String keyword, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /*
     * 获取count
     */
    Integer selectCountByPtype(@Param("ptype") String ptype, @Param("keyword") String keyword);
    
    /**
	 * @author yinxc
	 * 修改教室公告---classnotice
	 * 2017年3月2日
	 */
    int updateClassnoticeByClassroomid(@Param("classroomid") long classroomid, 
    		@Param("classnotice") String classnotice, @Param("updatetime") String updatetime); 
    
    /**
	 * @author yinxc
	 * 修改教室参与人数---classinvoloed
	 * param userid
	 * param classroomid 
	 * param num 加入教室为1   剔除教室为-1
	 * 2017年3月2日
	 */
    int updateClassinvoloedByClassroomid(@Param("classroomid") long classroomid, @Param("num") Integer num); 
    
    /**
	 * @author yinxc
	 * 修改教室课程数量---allcourses
	 * param userid
	 * param classroomid 
	 * param num 加入教室为1   剔除教室为-1
	 * 2017年3月2日
	 */
    int updateAllcoursesByClassroomid(@Param("classroomid") long classroomid, @Param("num") Integer num);
    
    
    /**
	 * @author yinxc
	 * 修改教室总收益---earnings
	 * param classroomid 
	 * param charge 教室课程价格
	 * 2017年7月24日
	 */
    Integer updateEarningsByClassroomid(@Param("classroomid") long classroomid, @Param("charge") Integer charge);
    
    /**
	 * @author yinxc
	 * 修改教室是否免费---isfree -----暂时不用(03-03不让修改)
	 * 2017年3月2日
	 * param isfree 是否免费。0 免费 1 收费
	 * param charge 课程价格
	 * param freecoursenum 免费课程数量
	 */
    int updateIsfreeByClassroomid(@Param("classroomid") long classroomid, @Param("isfree") String isfree, 
    		@Param("charge") String charge, @Param("freecoursenum") String freecoursenum); 
    
    /**
     * @Description: 关闭教室
     * @param @param classroomid 教室id
     * @param @param closeremark 关闭原因
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
    Integer updateIsdel(@Param("classroomid") long classroomid, 
    		@Param("closeremark") String closeremark, @Param("closedate") String closedate);
    
    /**
     * @Description: 发布教室
     * @param @param classroomid 教室id
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
    Integer updateIsup(@Param("classroomid") long classroomid);
    
    /*
     * 获取count
     */
    Integer selectCount(@Param("isup") String isup,  @Param("isdel") String isdel);
    
    /**
	 * @author yinxc
	 * 获取教室信息
	 * param ispublic 是否所有人可见。0 所有人可见。1，部分可见
	 * param pageNo   pageSize
	 */
    List<Classroom> selectClassroomList(@Param("isup") String isup,  @Param("isdel") String isdel,
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
     * @Description: 检查教室标题是否重复
     * @param @param classtitle  教室标题
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @return 0:不存在重复   1：存在重复需要更改
     * @auther yinxc
     * @currentdate:2017年7月11日
 	*/
    List<Classroom> checkClasstitle(@Param("classtitle") String classtitle);
    
    /*
     * 获取count
     */
    Integer selectSearchCount(Classroom classroom);
    
    
    /*
     * 获取用户发教室count
     */
    Integer selectCountByUserid(@Param("userid") long userid);
    
    /**
	 * @author yinxc
	 * 获取教室信息
	 * param startNum   endNum
	 */
    List<Classroom> selectSearchList(@Param("classroom") Classroom classroom,
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
     * @Description: 修改教室推荐状态
     * @param @param classroomid 教室id
     * @param @param isrecommend 是否推荐。0 - 没有推荐 1 - 推荐   默认0
     * @auther yinxc
     * @currentdate:2017年7月13日
 	*/
    Integer updateIsrecommend(@Param("classroomid") long classroomid, @Param("isrecommend") String isrecommend);
    
    /**
     * @Description: 修改教室今日推荐状态
     * @param @param classroomid 教室id
     * @param @param ishomerecommend 是否在首页推荐 0 - 没有 1 - 推荐
     * @auther yinxc
     * @currentdate:2017年7月13日
 	*/
    Integer updateIshomerecommend(@Param("classroomid") long classroomid, @Param("ishomerecommend") String ishomerecommend);
    
    /**
     * @Description: 修改教室推荐权重
     * @param @param classroomid 教室id
     * @param @param weight 权重 值越大排序越靠前
     * @auther yinxc
     * @currentdate:2017年7月13日
 	*/
    Integer updateRoomRecommendSort(@Param("classroomid") long classroomid, @Param("weight") String weight);

	/**
	 * 更新教室直播状态
	 * @param classroomid
	 * @param status
	 * @return
	 */
	Integer updateLiveStatus(@Param("classroomid") long classroomid,
							 @Param("status") String status);
	
	
	
	/**
	 * @author yinxc
	 * 获取直播中的教室列表
	 * param status  直播状态  未开始 0，直播中 1，，直播结束未开启回放 2，直播结束开启回放 3
	 */
    List<Classroom> selectStatusList(@Param("status") String status);
    
}