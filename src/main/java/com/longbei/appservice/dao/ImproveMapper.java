package com.longbei.appservice.dao;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Improve;
import org.apache.ibatis.annotations.Param;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ImproveMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Improve record);

    int insertSelective(@Param("improve") Improve improve,@Param("tablename") String tablename);

    Improve selectByPrimaryKey(@Param("impid")Long impid,
                               @Param("businessid") String businessid,
                               @Param("tablename")String tablename,
                               @Param("isdel")String isdel,
                               @Param("ispublic")String ispublic);

    /**
     * 获取进步列表
     *
     * @return
     */
    List<Improve> selectListByBusinessid(@Param("businessid")String businessid,
                                         @Param("tablename")String tablename,
                                         @Param("ismainimp")String ismainimp,
                                         @Param("userid") String userid,
                                         @Param("orderby")String orderby,
                                         @Param("startno")int startno,
                                         @Param("pagesize")int pagesize);

    /**
     * 获取榜单进步列表
     * @param businessid 榜单id
     * @param orderby  排序类型 0 - 成员动态 1 - 热度 2 - 时间
     * @param flowerscore 鲜花分数
     * @param likescore 赞分数
     * @param startno 开始条数
     * @param pagesize 每页条数
     * @return
     */
    List<Improve> selectListByRank(
                                    @Param("businessid")String businessid,
                                    @Param("orderby")String orderby,
                                    @Param("flowerscore") int flowerscore,
                                    @Param("likescore") int likescore,
                                    @Param("startno")int startno,
                                    @Param("pagesize")int pagesize,
                                    @Param("lastdate") Date lastdate
                            );


    List<Improve> selectGoalImproveList(@Param("startdate")Date startdate,
                                 @Param("ismain") String ismain,
                                 @Param("brief") String breif,
                                 @Param("users") List<AppUserMongoEntity> users,
                                 @Param("order") String order,
                                 @Param("startno") Integer startno,
                                 @Param("pagesize") Integer pagesize);

    int selectGoalImproveCount(@Param("startdate")Date startdate,
                                 @Param("ismain") String ismain,
                                 @Param("brief") String breif,
                                 @Param("users") List<AppUserMongoEntity> users);


    List<Improve> selectImproveList(@Param("startdate")Date startdate,
                                 @Param("ismain") String ismain,
                                 @Param("brief") String breif,
                                 @Param("users") List<AppUserMongoEntity> users,
                                 @Param("order") String order,
                                 @Param("startno") Integer startno,
                                 @Param("pagesize") Integer pagesize);


    int selectImproveCount(@Param("startdate")Date startdate,
                                        @Param("ismain") String ismain,
                                        @Param("brief") String breif,
                                        @Param("users") List<AppUserMongoEntity> users);




    List<Improve> selectRankImproveList(@Param("startdate")Date startdate,
                                 @Param("ismain") String ismain,
                                 @Param("brief") String breif,
                                 @Param("users") List<AppUserMongoEntity> users,
                                 @Param("order") String order,
                                 @Param("startno") Integer startno,
                                 @Param("pagesize") Integer pagesize);



    int selectRankImproveCount(@Param("startdate")Date startdate,
                                        @Param("ismain") String ismain,
                                        @Param("brief") String breif,
                                        @Param("users") List<AppUserMongoEntity> users);






    /**
	 * @author yinxc
	 * 获取目标进步最新微进步
	 * 2017年2月9日
	 * return_type
	 * ImproveMapper
	 */
    Improve selectImproveGoalByGoalid(@Param("goalid") String goalid);

    int updateByPrimaryKeySelective(Improve record);

    int updateByPrimaryKey(Improve record);

    /**
     * 更新赞的数量
     * @param impid
     * @param opttype 0 -- 点赞  1 -- 取消赞
     * @param tablename
     * @return
     */
    int updateLikes(@Param("impid") String impid,
                    @Param("opttype") String opttype,
                    @Param("businessid") String businessid,
                    @Param("tablename") String tablename);

    int updateFlower(@Param("impid") String impid,
                     @Param("flowernum") int flowernum,
                     @Param("businessid") String businessid,
                     @Param("tablename") String tablename);

    int updateDiamond(@Param("impid") String impid,
                      @Param("diamondnum") int diamondnum,
                      @Param("businessid") String businessid,
                      @Param("tablename") String tablename);

    int updateMedia(@Param("key") String key,
                    @Param("pickey") String pickey,
                    @Param("filekey") String filekey,
                    @Param("businessid") String businessid,
                    @Param("tablename") String table);

    /**
     * 假删
     * @param userid 用户id
     * @param improveid  进步id
     * @return
     */
    int remove(@Param("userid") String userid,@Param("improveid") String improveid);





    /**
     * 查询兴趣圈中某人的进步
     * @param map
     * @return
     */
    List<Improve> findCircleMemberImprove(Map<String, Object> map);

    List<Improve> selectGoalMainImproveList(@Param("userid") long userid,@Param("startNum") int startNum,@Param("endNum") int endNum);

    int delGoalToImprove(@Param("goalid") long goalid,@Param("userid") long userid,@Param("businesstype")String businesstype);

    /**
     * 更新主进步为非主进步
     * @param businessid
     * @param userid
     * @return
     */
    int updateGolaMainImprove(@Param("businessid")long businessid,@Param("userid")long userid);

    int updateRankMainImprove(@Param("businessid")long businessid,@Param("userid")long userid);
    //improve.getBusinessid(),improve.getUserid(),tableName,"rankid"

    /**
     * 选举主进步
     * @param businessid
     * @param userid
     * @param tablename
     * @param field
     * @return
     */
    int chooseMainImprove(@Param("businessid")long businessid,
                          @Param("userid")long userid,
                          @Param("tablename")String tablename,
                          @Param("field")String field);

    /**
     * 进步删除之后修改脏数据
     * @param businessid
     * @param userid
     * @param flower
     * @param like
     * @param tablename
     * @param field
     * @return
     */
    int afterDelSubImp(@Param("businessid")long businessid,
                       @Param("userid")long userid,
                       @Param("flower")int flower,
                       @Param("like")int like,
                       @Param("tablename")String tablename,
                       @Param("field")String field);

    /**
     * 更新总的数据
     * @param businessid
     * @param userid
     * @param count
     * @param otype  0 点赞  1 鲜花 2 送钻
     * @param tablename
     * @param field
     * @return
     */
    int updateSourceLike(@Param("businessid")long businessid,
                         @Param("userid")long userid,
                         @Param("icount")int count,
                         @Param("operatetype")String otype,
                         @Param("tablename")String tablename,
                         @Param("field")String field);

    int updateImproveRecommend(@Param("tablename") String tablename,
                               @Param("impids") List<Long> impids,
                               @Param("isrecommend")String isrecommend);


    int removeImproveFromBusiness(@Param("tablename") String tablename,
                                  @Param("businessid") String businessid,
                                  @Param("impid") String impid);

}