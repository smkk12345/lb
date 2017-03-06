package com.longbei.appservice.dao;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.Improve;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ImproveMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Improve record);

    int insertSelective(@Param("improve") Improve improve,@Param("tablename") String tablename);

    Improve selectByPrimaryKey(@Param("impid")Long impid,
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
                                         @Param("orderby")String orderby,
                                         @Param("startno")int startno,
                                         @Param("pagesize")int pagesize);
    
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
}