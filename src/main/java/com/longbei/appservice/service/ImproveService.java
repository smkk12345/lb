package com.longbei.appservice.service;

import java.awt.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 进步业务操作接口
 *
 * @author luye
 * @create 2017-01-19 上午11:22
 **/
public interface ImproveService {



    /**
     * 添加进步
     * @param brief 说明
     * @param pickey 图片的key
     * @param filekey 文件key  视频文件  音频文件 普通文件
     * @param businesstype  微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室    5：教室批复作业
     * @param ptype 十全十美id
     * @param ispublic 可见程度  0 私密 1 好友可见 2 全部可见
     * @param itype 类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
     * @param pimpid : 批复父进步 id businesstype为5时传
     * @return
     */
    BaseResp<Object> insertImprove(String userid, String brief, String pickey, String filekey,
                           String businesstype, String businessid, String ptype,
                           String ispublic, String itype, String pimpid,String picattribute);
    /**
     * 添加独立进步
     * @param improve
     * @return
     */
    boolean insertImproveSingle(Improve improve);

    /**
     * 添加兴趣圈中进步
     * @param improveCircle
     * @return
     */
    boolean insertImproveForCircle(Improve improveCircle);

    /**
     * 添加教室中进步
     * @param improveClassroom
     * @return
     */
    boolean insertImproveForClassroom(Improve improveClassroom);

    /**
     * 添加榜单中进步
     * @param improveRank
     * @return
     */
    boolean insertImproveForRank(Improve improveRank);

    /**
     * 添加目标中进步
     * @param improveGoal
     * @return
     */
    boolean insertImproveForGoal(Improve improveGoal);


    /**
     * 查询进步详情
     * @param impid
     * @return
     */
    Improve selectImproveByImpid(Long impid,String userid,
                          String businesstype,String businessid);


    Improve selectImproveByImpidMuc(Long impid,String userid,
                                 String businesstype,String businessid);


    /**
     *  榜中成员动态
     * @param userid
     * @param rankid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param orderby 排序字段 0 - 成员动态 1 - 热度 2 - 时间
     * @param pageNo
     * @param pageSize
     * @return
     */
    BaseResp<List<Improve>> selectRankImproveList(String userid,String rankid,String sift,
                                        String orderby,int pageNo,int pageSize,String lastdate);

    /**
     *  榜中按时间排序
     * @param userid
     * @param rankid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param orderby 排序字段 0 - 成员动态 1 - 热度 2 - 时间
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectRankImproveListByDate(String userid,String rankid,String sift,String orderby,int pageNo,int pageSize);

    /**
     *  圈子中成员动态
      * @param userid
     * @param circleid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param orderby 排序字段 0.按照时间倒叙排列 1.按照热度排序
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectCircleImproveList(String userid,String circleid,String sift,String orderby,int pageNo,int pageSize);

    /**
     *  圈子中按时间排序
     * @param userid
     * @param circleid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param orderby 排序字段 0.按照时间倒叙排列 1.按照热度排序
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectCircleImproveListByDate(String userid,String circleid,String sift,String orderby ,int pageNo,int pageSize);

    /**
     *  教室中按时间排序
     * @param userid
     * @param classroomid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param orderby 排序字段 0.按照时间倒叙排列 1.按照热度排序
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectClassroomImproveList(String userid,String classroomid,String sift,String orderby,int pageNo,int pageSize);

    /**
     *  教室中成员动态
     * @param userid
     * @param classroomid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param orderby 排序字段 0.按照时间倒叙排列 1.按照热度排序
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectClassroomImproveListByDate(String userid,String classroomid,String sift,String orderby,int pageNo,int pageSize);


    /**
     *  获取目标中进步列表
     * @param userid
     * @param goalid
     * @param orderby 排序字段 0.按照时间倒叙排列 1.按照热度排序
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectGoalImproveList(String userid,String goalid,String orderby,int pageNo,int pageSize);
    
    /**
     *  获取目标中进步Count
     * @param userid
     * @param goalid
     * @return
     */
    int selectCountGoalImprove(String userid,String goalid);
    
    /**
     * 删除进步入口
     * @param userid  用户id
     * @param improveid  进步id
     * @param businesstype  所属业务类型
     * @param businessid  所属业务类型id
     * @return
     */
    BaseResp<Object> removeImprove(String userid,String improveid,
                          String businesstype,String businessid);


    /**
     * 删除单独进步
     * @param userid  用户id
     * @param improveid  进步id
     * @return
     */
    BaseResp<Object> removeSingleImprove(String userid,String improveid);

    /**
     * 删除榜中进步
     * @param userid  用户id
     * @param rankid  榜id
     * @param improveid  进步id
     * @return
     */
    BaseResp<Object> removeRankImprove(String userid,String rankid,String improveid);

    /**
     * 删除已结束榜的进步
     * @param userid  用户id
     * @param rankid  榜id
     * @param improveid  进步id
     * @return
     */
    BaseResp<Object> removeFinishedRankImprove(String userid,String rankid,String improveid);

    /**
     * 删除圈子中进步
     * @param userid  用户id
     * @param circleid  圈子id
     * @param improveid   进步id
     * @return
     */
    BaseResp<Object> removeCircleImprove(String userid,String circleid,String improveid);

    /**
     * 删除教室中的进步
     * @param userid  用户id
     * @param classroomid 教室id
     * @param improveid 进步id
     * @return
     */
    BaseResp<Object> removeClassroomImprove(String userid,String classroomid,String improveid);

    /**
     * 删除目标中的进步
     * @param userid  用户id
     * @param goalid  目标id
     * @param improveid  进步id
     * @return
     */
    BaseResp<Object> removeGoalImprove(String userid,String goalid,String improveid);


    /**
     * 获取我的进步列表
     * @param userid  用户id
     * @param ctype  0--广场 1--我的 2--好友，关注，熟人 3-好友 4-关注 5-熟人
     * @param lastdate  最后一条时间
     * @param pagesize  每页显示条数
     * @return
     */
    List<Improve> selectImproveListByUser(String userid,String ptype,
                                          String ctype, Date lastdate,
                                          int pagesize,Integer ispublic);


    /**
     * 获取指定用户所发的全部进步
     * @param userid  用户id
     * @param targetuserid  所要查看的用户id
     * @param lastdate 最后一条的时间
     * @param pagesize 获取数据条数
     * @return
     * @author luye
     */
    BaseResp<List<Improve>> selectOtherImproveList(String userid,String targetuserid,Date lastdate,int pagesize);
    
    /**
     * 获取我的进步列表(根据lastdate时间获取当天的进步列表)
     * @param userid  用户id
     * @param ctype  0--广场 1--我的 2--好友，关注，熟人 3-好友 4-关注 5-熟人
     * @param lastdate  最后一条时间
     * @param pagesize  每页显示条数
     * @return
     */
    List<Improve> selectImproveListByUserDate(String userid,String ptype,String ctype, Date searchDate, Date lastdate,int pagesize);
    
    /**
     * 点赞
     * 点赞每天限制  --- 每天内次只能点一次
     * 进步必须是公开的
     * 不能给自己点赞
     * 取消或者点赞
     * 点赞-----进步赞个数  总赞
     * 点赞对积分的影响
     * 点完赞之后数据返回
     * @param userid
     * @param impid
     * @return BaseResp
     */
    BaseResp<Object> addlike(String userid,String impid,String businesstype,String businessid);


    /**
     * 取消赞
     * @param userid
     * @param impid
     * @param businesstype
     * @param businessid
     * @return
     */
    BaseResp<Object> cancelLike(String userid,String impid,String businesstype,String businessid);

    /**
     * 收藏微进步
     * @param userid impid businesstype
     */
    BaseResp<Object> collectImp(String userid,String impid,String businesstype,String businessid);

    /**
     * 取消微进步收藏
     * @param userid
     * @param impid
     * @param buinesstype 
     * @return
     */
    BaseResp<Object> removeCollect(String userid,String impid,String buinesstype);

    /**
     * 获取用户收藏列表
     * @param userid
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<Object> selectCollect(String userid,int startNum,int pageSize);



    BaseResp<Object> addFlower(String userid,String friendid,String impid,int flowernum,String businesstype,String businessid);


    BaseResp<Object> addDiamond(String userid,String friendid,String impid,int diamondnum,String businesstype,String businessid);

    BaseResp<Object> updateMedia(String key,String pickey,String filekey,String workflow,String duration,String picAttribute);


    BaseResp<List<ImpAllDetail>> selectImproveLFDListByUserid(String userid, String impid, String listtype,int pagesize, Date lastdate);
    
    BaseResp<List<ImpAllDetail>> selectImproveLFDList(String impid, String listtype,int pagesize, Date lastdate);

    /**
     * 获取进步详情
     * @param userid
     * @param impid
     * @param businesstype
     * @param businessid
     * @return
     */
    BaseResp select(String userid, String impid, String businesstype,String businessid);

    /**
     * 查询某个用户在business中的进步
     * @param curuserid 当前登录用户
     * @param userid 查看的用户id
     * @param businessid 业务id 圈子id
     * @param businesstype 类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     * @param selectCount 是否查询总数 如果查询总数 则会在expandData中显示该用户在business中发表的进步总数
     * @return
     */
    BaseResp<List<Improve>> selectListInBusiness(String curuserid,String userid,String businessid,
                                    String businesstype,Integer startno,Integer pagesize,boolean selectCount);

    /**
     * 进步信息初始化
     * @param improve
     */
    public void initImproveInfo(Improve improve,Long userid);

    public int getPerDayImproveCount(long userid,String businesstype);

    /**
     * 获取超级话题下的进步列表
     * @param topicid
     * @param orderby 排序字段 0.按照时间倒叙排列 1.按照热度排序
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectSuperTopicImproveList(long userid,String topicid,String orderby,int pageNo,int pageSize);

    /**
     * 领取默认奖品
     * @param rankid
     * @param userid
     * @return
     */
    @Transactional
    BaseResp<Object> acceptBasicAward(long rankid, long userid);

    /**
     * 领取实物奖品
     * @param rankid
     * @param userid
     * @return
     */
    BaseResp<Object> acceptAward(long rankid, long userid, Integer addressId);


    /**
     * 查询榜中，圈子，教室，目标中的用户所发的进步的列表
     * @param userid  用户id
     * @param businessid  榜，圈子，教室，目标 id
     * @param businesstype 业务类型 榜，圈子，教室，目标
     * @param startno 分页起始条数
     * @param pagesize 分页每页条数
     * @param selectCount 是否查询总数 只有在startno == 0 && selectCount == true时 才会查询总数
     * @return
     */
    BaseResp<List<Improve>> selectBusinessImproveList(String userid,String businessid,String iscomplain,
                                                String businesstype,Integer startno,Integer pagesize,boolean selectCount);


    BaseResp<Object> selectGoalMainImproveList(long userid,int startNum,int pageSize);

    /**
     * 删除目标 修改目标中的进步状态
     * @param goalId
     * @param userId
     * @return
     */
    BaseResp<Object> delGoal(long goalId,long userId);

    /**
     * 获取推荐进步列表（pc）
     * @param brief
     * @param usernickname
     * @param starttime
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<TimeLineDetail>> selectRecommendImproveList(String brief, String usernickname,
                                                              Date starttime, Integer pageno, Integer pagesize);

    /**
     * 获取进步推荐列表
     * @param startno
     * @param pagesize
     * @return
     */
    BaseResp<List<Improve>> selectRecommendImproveList(String userid,Integer startno,Integer pagesize);

    /**
     * 获取不同类型的进步列表（pc）
     * @param businesstype
     * @param brief
     * @param usernickname
     * @param starttime
     * @param pageno
     * @param pagesize
     * @param order
     * @return
     */
    BaseResp<Page<Improve>> selectImproveList(String businesstype,String brief, String usernickname,
                                              Date starttime,Integer pageno,
                                              Integer pagesize,String order);

    /**
     * 更新进步推荐状态
     * @param businesstype
     * @param impids
     * @param isrecommend
     * @return
     */
    BaseResp<Object> updateImproveRecommentStatus(String businesstype, List<Long> impids,String isrecommend);


    /**
     * 更新进步推荐排序
     * @param impid
     * @param businesstype
     * @param sort
     * @return
     */
    BaseResp updateImproveRecommendSort(Long impid,String businesstype,Integer sort);

    /**
     * 查询个人榜单中的进步列表
     * @param userid
     * @param businessid
     * @param businesstype
     * @param startno
     * @param pagesize
     * @return
     */
    BaseResp<List<Improve>> selectListInRank(String curuserid,String userid,String businessid,
                                                      String businesstype,Integer startno,Integer pagesize);

    /**
     * 将榜中单个微进步下榜
     * @param impid  进步id
     * @param businessid  榜 ，教室 ，圈子 ID
     * @param businesstype  "0" -- 独立进步 "1" -- 目标 "2" -- 榜 "3" -- 圈子 "4" -- 教室
     * @return
     */
    BaseResp<Object> removeImproveFromBusiness(String impid,String businessid,String businesstype);

    /**
     * smkk
     * @param impid
     * @param businesstype
     * @param businessid
     * @return
     */
    int updateMemberSumInfo(String impid,String businesstype,String businessid,String type,int count);

    /**
     * 点赞  送花同步到menber表中
     * @param improve
     * @param count
     * @param otype
     */
    void afterImproveInfoChange(Improve improve,int count,String otype);

    /**
     * 获取进步的第一张图片
     * @param improve
     * @return
     */
    String getFirstPhotos(Improve improve);

    /**
     * 更新点赞送花数到快找
     * @param rankId
     * @return
     */
    int updateSortSource(Long rankId);
    
    /**
     * 查询用户对进步献花的总数
     * @param userid
     * @param improveid 
     * @param number
     */
    BaseResp<Object> canGiveFlower(long userid, String improveid, String businesstype, String businessid, String number);

    /**
     * 获取用户的关注用户id列表
     * @param userid
     * @return
     */
    String getFansIds(Long userid);

    /**
     * 获取用户的好友用户id列表
     * @param userid
     * @return
     */
    String getFriendIds(Long userid);


    /**
     * 获取热门推荐进步
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<List<Improve>> selectRecommendImprove(String userid,Integer startNum,Integer pageSize);



    BaseResp recommendImproveOpt();

}
