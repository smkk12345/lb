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



    boolean insertAward(Award award);


    boolean updateAward(Award award);


    boolean deleteAward(Integer id);

    /**
     * 获取奖品信息列表（分页）
     * @param award
     * @param pageno
     * @param pagesize
     * @return
     */
    Page<Award> selectAwardListWithPage(Award award,int pageno,int pagesize);


    List<Award> selectAwardList(Award award);


    /**
     * 获取奖品详情
     * @param awardid
     * @return
     */
    Award selectAward(String awardid);



    boolean insertAwardClassify(AwardClassify awardClassify);


    boolean updateAwardClassify(AwardClassify awardClassify);


    boolean deleteAwardClassify(Integer id);


    List<AwardClassify> selectAwardClassifyList(AwardClassify awardClassify);


    AwardClassify selectAwardClassify(Integer id);

}
