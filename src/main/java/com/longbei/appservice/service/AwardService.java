package com.longbei.appservice.service;

import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Award;
import com.longbei.appservice.entity.AwardClassify;

import java.util.List;

/**
 * 奖品相关操作
 *
 * @author luye
 * @create 2017-02-27 下午4:05
 **/
public interface AwardService {

    /**
     * 添加奖品
     * @param award
     * @return
     */
    boolean insertAward(Award award);

    /**
     * 编辑奖品
     * @param award
     * @return
     */
    boolean updateAward(Award award);

    /**
     * 删除奖品
     * @param id
     * @return
     */
    boolean deleteAward(Long id);

    /**
     * 获取奖品详情
     * @param awardid
     * @return
     */
    Award selectAward(String awardid);

    /**
     * 获取奖品列表
     * @param award
     * @return
     */
    List<Award> selectAwardList(Award award);

    /**
     * 获取奖品信息列表（分页）
     * @param award
     * @param pageno
     * @param pagesize
     * @return
     */
    Page<Award> selectAwardListWithPage(Award award,int pageno,int pagesize);

    /**
     * 获取奖品类列表
     * @return
     */
    List<AwardClassify> selectAwardClassifyList();

    /**
     * 获取奖品类列表，带分页
     * @param pageno
     * @param pagesize
     * @return
     */
    Page<AwardClassify> selectAwardClassifyListWithPage(int pageno,int pagesize);

    /**
     * 添加奖品类
     * @param awardClassify
     * @return
     */
    boolean insertAwardClassify(AwardClassify awardClassify);

    /**
     * 编辑奖品类
     * @param awardClassify
     * @return
     */
    boolean updateAwardClassify(AwardClassify awardClassify);

    /**
     * 奖品类中是否包含奖品
     * @param classifyid
     * @return
     */
    boolean hasAwards(String classifyid);

    /**
     * 删除奖品类
     * @param id
     * @return
     */
    boolean deleteAwardClassify(Integer id);

    /**
     * 查询奖品类
     * @param id
     * @return
     */
    AwardClassify selectAwardClassify(Integer id);

}
