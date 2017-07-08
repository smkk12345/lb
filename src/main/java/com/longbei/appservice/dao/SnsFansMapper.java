package com.longbei.appservice.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.longbei.appservice.entity.SnsFans;
import org.apache.ibatis.annotations.Param;

public interface SnsFansMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsFans record);

    int insertSelective(SnsFans record);

    SnsFans selectByPrimaryKey(Integer id);
    
    SnsFans selectByUidAndLikeid(@Param("userid") long userid, @Param("likeuserid") long likeuserid);

    int updateByPrimaryKeySelective(SnsFans record);

    int updateByPrimaryKey(SnsFans record);
    
    int deleteByUidAndLid(@Param("userid") long userid, @Param("likeuserid") long likeuserid);
    
    List<SnsFans> selectFansByUserid(@Param("userid") long userid,
									 @Param("startNum") Integer startNum, @Param("endNum") Integer endNum);

	/**
	 * @author yinxc
	 * 获取用户粉丝;关注列表
	 * @param @param userid
	 * @param ftype 0:查询关注列表   1：粉丝列表
	 * 2017年4月13日
	 */
	List<SnsFans> selectFansList(@Param("userid") long userid, @Param("ftype") int ftype,
								 @Param("startNum") Integer startNum, @Param("endNum") Integer endNum);

	/**
	 * @author yinxc
	 * 获取用户粉丝;关注数量Count
	 * @param @param userid
	 * @param ftype 0:查询关注列表   1：粉丝列表
	 * 2017年4月13日
	 */
	int selectFansFtypeCount(@Param("userid") long userid, @Param("ftype") int ftype);
    
    /**
	 * @author yinxc
	 * 获取用户粉丝数量Count
	 * 2017年3月9日
	 */
    int selectCountFans(@Param("userid") long userid);
    
    /**
	 * @author yinxc
	 * 获取当前用户粉丝idList
	 * 2017年2月9日
	 * return_type
	 * SnsFansMapper
	 */
    Set<String> selectListidByUid(@Param("userid") long userid);
    
    /**
	 * @author yinxc
	 * 获取当前用户粉丝idList(分页)
	 * 2017年2月9日
	 * return_type
	 * SnsFansMapper
	 */
    List<String> selectListidByUidNum(@Param("userid") long userid, @Param("startNum") int startNum, @Param("endNum") int endNum);

	/**
	 * 查询关注了likeuserId的用户列表
	 * @param likeuserId 被关注人的用户id
	 * @param startNum
	 * @param pageSize
     * @return
     */
	List<SnsFans> selectFansByLikeUserid(@Param("likeuserId")Long likeuserId,@Param("startNum") Integer startNum,@Param("pageSize") Integer pageSize);

}