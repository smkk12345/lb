package com.longbei.appservice.service;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.*;
import com.mysql.fabric.xmlrpc.base.Data;
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
                           String ispublic, String itype, String pimpid);
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


    /**
     *  榜中成员动态
     * @param userid
     * @param rankid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param orderby 排序字段 0.按照时间倒叙排列 1.按照热度排序
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectRankImproveList(String userid,String rankid,String sift,String orderby,int pageNo,int pageSize);

    /**
     *  榜中按时间排序
     * @param userid
     * @param rankid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param orderby 排序字段 0.按照时间倒叙排列 1.按照热度排序
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
     * 删除进步入口
     * @param userid  用户id
     * @param improveid  进步id
     * @param businesstype  所属业务类型
     * @param businessid  所属业务类型id
     * @return
     */
    boolean removeImprove(String userid,String improveid,
                          String businesstype,String businessid);


    /**
     * 删除单独进步
     * @param userid  用户id
     * @param improveid  进步id
     * @return
     */
    boolean removeSingleImprove(String userid,String improveid);

    /**
     * 删除榜中进步
     * @param userid  用户id
     * @param rankid  榜id
     * @param improveid  进步id
     * @return
     */
    boolean removeRankImprove(String userid,String rankid,String improveid);

    /**
     * 删除圈子中进步
     * @param userid  用户id
     * @param circleid  圈子id
     * @param improveid   进步id
     * @return
     */
    boolean removeCircleImprove(String userid,String circleid,String improveid);

    /**
     * 删除教室中的进步
     * @param userid  用户id
     * @param classroomid 教室id
     * @param improveid 进步id
     * @return
     */
    boolean removeClassroomImprove(String userid,String classroomid,String improveid);

    /**
     * 删除目标中的进步
     * @param userid  用户id
     * @param goalid  目标id
     * @param improveid  进步id
     * @return
     */
    boolean removeGoalImprove(String userid,String goalid,String improveid);


    /**
     * 获取我的进步列表
     * @param userid  用户id
     * @param ctype  0--广场 1--我的 2--好友，关注，熟人 3-好友 4-关注 5-熟人
     * @param lastdate  最后一条时间
     * @param pagesize  每页显示条数
     * @return
     */
    List<Improve> selectImproveListByUser(String userid,String ptype,String ctype, Date lastdate,int pagesize);
    
    /**
     * 获取我的进步列表(根据lastdate时间获取当天的进步列表)
     * @param userid  用户id
     * @param ctype  0--广场 1--我的 2--好友，关注，熟人 3-好友 4-关注 5-熟人
     * @param lastdate  最后一条时间
     * @param pagesize  每页显示条数
     * @return
     */
    List<Improve> selectImproveListByUserDate(String userid,String ptype,String ctype, Date lastdate,int pagesize);
    
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
     * @param endNum
     * @return
     */
    BaseResp<Object> selectCollect(String userid,int startNum,int endNum);



    BaseResp<Object> addFlower(String userid,String friendid,String impid,int flowernum,String businesstype,String businessid);


    BaseResp<Object> addDiamond(String userid,String friendid,String impid,int diamondnum,String businesstype,String businessid);

    BaseResp<Object> updateMedia(String key,String pickey,String filekey,String workflow);


    BaseResp<List<ImpAllDetail>> selectImproveLFDList(String impid, String listtype,int pagesize, Date lastdate);

    /**
     * 获取进步详情
     * @param userid
     * @param impid
     * @param businesstype
     * @param businessid
     * @return
     */
    public BaseResp select(String userid, String impid, String businesstype,String businessid);

    /**
     * 查询某个用户在兴趣圈中的进步
     * @param circleId 兴趣圈id
     * @param userId 用户id
     * @param startNo 开始下标
     * @param pageSize 每页条数
     * @return
     */
    List<Improve> findCircleMemberImprove(Long circleId, Long userId,Long currentUserId, Integer startNo, Integer pageSize);

    /**
     * 进步信息初始化
     * @param improve
     */
    public void initImproveInfo(Improve improve,long userid);

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
     * @return
     */
    BaseResp<List<Improve>> selectBusinessImproveList(String userid,String businessid,
                                                String businesstype,Integer startno,Integer pagesize);


    BaseResp<Object> selectGoalMainImproveList(long userid,int startNum,int endNum);

    /**
     * 删除目标 修改目标中的进步状态
     * @param goalId
     * @param userId
     * @return
     */
    BaseResp<Object> delGoal(long goalId,long userId);

}
