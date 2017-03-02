package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.RankCheckDetail;
import com.longbei.appservice.entity.RankImage;

import java.util.Date;
import java.util.List;

/**
 * 榜单操作接口
 *
 * @author luye
 * @create 2017-01-20 下午3:25
 **/
public interface RankService {

    /**
     * 添加榜单接口
     * @param ranktitle 榜单标题
     * @param rankdetail 榜单简介
     * @param ranklimite  榜单限制人数
     * @param rankscope 榜单范围
     * @param rankphotos 榜单图片
     * @param rankrate  榜单中奖率
     * @param starttime 开始时间
     * @param endtime  结束时间
     * @param areaname 地域名字
     * @param createuserid 创建人id
     * @param ranktype 榜单类型
     * @param ispublic 是否公开
     * @param rankcateid 榜单类型
     * @param likescore 赞的分数
     * @param flowerscore 花的分数
     * @param diamondscore 钻石的分数
     * @param codeword  入榜口令
     * @param ptype  十全十美类型
     * @param sourcetype  来源类型
     * @param companyname  公司名字
     * @param companyphotos 公司图片
     * @param companybrief 公司简介
     * @return
     */
    boolean insertRank(String rankdetail, String ranktitle,
                       int ranklimite, String rankscope,
                       String rankphotos, double rankrate,
                       Date starttime, Date endtime, String areaname,
                       String createuserid, String ranktype, String ispublic,
                       String rankcateid, int likescore,
                       int flowerscore, int diamondscore,
                       String codeword, String ptype, String sourcetype,
                       String companyname, String companyphotos,
                       String companybrief);

    /**
     * 编辑榜单
     * @return
     */
    boolean updateRankImage(RankImage rankImage);


    BaseResp<RankImage> selectRankImage(String rankimageid);


    BaseResp publishRankImage(String rankimageid);

    Page<RankImage> selectRankImageList(RankImage rankImage,int startno, int pagesize);


    boolean deleteRankImage(String rankimageid);


    /**
     * @Title: updateSponsornumAndSponsormoney
     * @Description: 更新赞助的统计人数和统计龙币数量
     * @param @param userid 赞助人
     * @param @param bid榜单
     * @auther IngaWu
     * @currentdate:2017年2月27日
     */
    boolean updateSponsornumAndSponsormoney(long rankid);

    BaseResp checkRankImage(RankCheckDetail rankCheckDetail);

    BaseResp<Object> selectRankByRankid(long rankid);

}
