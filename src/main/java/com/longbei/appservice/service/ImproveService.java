package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.*;

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
     * @param businesstype  微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     * @param ptype 十全十美id
     * @param ispublic 可见程度  0 私密 1 好友可见 2 全部可见
     * @param itype 类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
     * @return
     */
    BaseResp<Object> insertImprove(String userid, String brief, String pickey, String filekey,
                           String businesstype, String businessid, String ptype,
                           String ispublic, String itype);
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
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectRankImproveList(String userid,String rankid,String sift,int pageNo,int pageSize);

    /**
     *  榜中按时间排序
     * @param userid
     * @param rankid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectRankImproveListByDate(String userid,String rankid,String sift,int pageNo,int pageSize);

    /**
     *  圈子中成员动态
      * @param userid
     * @param circleid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectCircleImproveList(String userid,String circleid,String sift,int pageNo,int pageSize);

    /**
     *  圈子中按时间排序
     * @param userid
     * @param circleid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectCircleImproveListByDate(String userid,String circleid,String sift,int pageNo,int pageSize);

    /**
     *  教室中成员动态
     * @param userid
     * @param classroomid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectClassroomImproveList(String userid,String classroomid,String sift,int pageNo,int pageSize);

    /**
     *  教室中按时间排序
     * @param userid
     * @param classroomid
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectClassroomImproveListByDate(String userid,String classroomid,String sift,int pageNo,int pageSize);


    /**
     *  获取目标中进步列表
     * @param userid
     * @param goalid
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Improve> selectGoalImproveList(String userid,String goalid,int pageNo,int pageSize);
    
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




}
