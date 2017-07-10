package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Award;
import com.longbei.appservice.entity.RankAwardRelease;
import com.longbei.appservice.entity.RankMembers;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RankMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankMembers record);

    int insertSelective(RankMembers record);

    RankMembers selectByPrimaryKey(Integer id);

    int selectpasscount(@Param("rankid") Long rankid);

    int updateByPrimaryKeySelective(RankMembers record);

    int selectCount(RankMembers rankMembers);

    int selectRankIcount(@Param("rankid") String rankid);

    String getRankImproveCount(@Param("rankid") String rankid);



    List<RankMembers> selectList(@Param("rankmember") RankMembers rankMembers,
                                 @Param("order") String order,
                                 @Param("startno") Integer startno,
                                 @Param("pagesize") Integer pagesize);

    List<RankMembers> selectWaitCheckList(@Param("rankmember") RankMembers rankMembers,
                                          @Param("startno") Integer startno,
                                          @Param("pagesize") Integer pagesize,
                                          @Param("totalcount") Integer totalcount);

    Integer selectCountByStatusAndCheckstatus(@Param("rankid") Long rankid, @Param("status") Integer status, @Param("checkstatus") String checkstatus);

    /**
     * @Title: selectWaitCheckListCount
     * @Description: 获取榜单待审核成员数量
     */
    Integer selectWaitCheckListCount(@Param("rankmember") RankMembers rankMembers, @Param("totalcount") Integer totalcount);

    int updateByPrimaryKey(RankMembers record);

    RankMembers selectByRankIdAndUserId(@Param("rankid") long rankid, @Param("userid") long userid);

    /**
     * 更改rankMe
     * @param updateMap
     * @return
     */
    int updateRank(Map<String, Object> updateMap);

    /**
     * 更新榜单成员各种状态
     * @param rankMembers
     * @return
     */
    int updateRankMemberState(RankMembers rankMembers);

    /**
     * @param rankid
     * @param userid
     * @param icount  0删除进步  1 增加进步
     * @return
     */
    int updateRankImproveCount(@Param("rankid")long rankid,@Param("userid")long userid,@Param("icount") int icount);

    /**
     * @Title: updateRankMenberComplaincount
     * @Description: 更新榜成员的被投诉次数
     * @auther IngaWu
     * @currentdate:2017年6月8日
     */
    int updateRankMenberComplaincount(@Param("rankid")long rankid,@Param("userid")long userid);

    /**
     * 查询用户排名的集合
     * @param rankId 榜单id
     * @param sortType 排序字段
     * @param startNum
     * @param pageSize
     * @return
     */
    List<RankMembers> selectUserSort(@Param("rankId")Long rankId,@Param("sortType") Integer sortType,@Param("searchIsfinishFiled") Integer searchIsfinishFiled,@Param("startNum") Integer startNum,@Param("pageSize") Integer pageSize);

    /**
     * 审核用户的进步条数
     * @param updateMap
     * @return
     */
    int instanceRankMember(Map<String, Object> updateMap);

    /**
     * 根据条件 查询rankMembers
     * @param parameterMap
     * @return
     */
    List<RankMembers> selectRankMembers(Map<String, Object> parameterMap);

    /**
     * 查看已结束的榜单的获奖情况 带分组,按照中奖等级分组
     * @param rankId
     * @return
     */
    List<RankAwardRelease> selectAwardMemberList(Long rankId);

    /**
     * 查询榜单的所有中奖用户 不分组,按照中奖等级排序
     * @param rankid
     * @return
     */
    List<RankAwardRelease> rankMemberAwardList(Long rankid);


    List<RankMembers> selectRankAcceptAwardList(@Param("rankmember") RankMembers rankMembers,
                                                @Param("startno") Integer startno,@Param("pagesize") Integer pagesize);

    /**
     * 获取可以挤走的榜成员
     * @param map
     * @return
     */
    int getSureRemoveRankMemberCount(Map<String, Object> map);

    /**
     * 移除超时未发进步的成员,只删除一个
     * @param map
     * @return
     */
    int removeOverTimeRankMember(Map<String, Object> map);

    /**
     * 查询可以挤走的榜单用户id
     * @param map
     * @return
     */
    Long removeOverTimeRankMemberUserId(Map<String,Object> map);

    /**
     * 查询中奖的用户
     * @return
     */
    List<RankMembers> selectWinningRankAward();

    /**
     * 获取榜单的中奖用户
     * @param rankid 榜单id
     * @param startNum
     * @param pageSize
     * @return
     */
    List<RankMembers> getWinningRankAwardUser(@Param("rankid") Long rankid,@Param("startNum") Integer startNum,@Param("pageSize") Integer pageSize);

    /**
     * 更新赞 花到快照中去
     * @param rankid
     * @return
     */
    int updateSortSource(@Param("rankid") Long rankid);

    /**
     * 获取用户参与的榜单 数量
     * @param map
     * @return
     */
    int getJoinRankCount(Map<String, Object> map);

    /**
     * 根据榜单id获取获奖人员列表
     * @param rankid
     * @return
     */
    List<RankMembers> selectWinningRankAwardByRank(@Param("rankid") Long rankid);

    /**
     * 关闭榜单
     * @param rankid
     * @return
     */
    int deleteByRankId(@Param("rankid") Long rankid);


}