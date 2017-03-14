package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankImage;
import com.longbei.appservice.service.RankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 榜单操作
 *
 * @author luye
 * @create 2017-01-21 上午11:44
 **/
@Controller
@RequestMapping(value = "rank")
public class RankController {

    private static Logger logger = LoggerFactory.getLogger(RankController.class);

    @Autowired
    private RankService rankService;



//    /**
//     * url: http://ip:port/app_service/rank/insert
//     * method:  POST
//     * 添加榜单
//     * @param ranktitle 榜单标题
//     * @param rankdetail 榜单简介
//     * @param ranklimite  榜单限制人数
//     * @param rankscope 榜单范围
//     * @param rankphotos 榜单图片
//     * @param rankrate  榜单中奖率
//     * @param starttime 开始时间
//     * @param endtime  结束时间
//     * @param areaname 地域名字
//     * @param createuserid 创建人id
//     * @param ranktype 榜单类型
//     * @param ispublic 是否公开
//     * @param rankcateid 榜单类型
//     * @param likescore 赞的分数
//     * @param flowerscore 花的分数
//     * @param diamondscore 砖石的分数
//     * @param codeword  入榜口令
//     * @param ptype  十全十美类型
//     * @param sourcetype  来源类型
//     * @param companyname  公司名字
//     * @param companyphotos 公司图片
//     * @param companybrief 公司简介
//     * @return
//     * @author:luye
//     * @date: 2017/2/4
//     */
//    @ResponseBody
//    @RequestMapping(value = "insert",method = RequestMethod.POST)
//    public BaseResp<Object> insertRank(RankImage rankImage){
//        boolean issuccess = false;
//        try {
//            issuccess = rankService.insertRank(rankImage);
//            if(issuccess){
//                return BaseResp.ok(Constant.RTNINFO_SYS_50);
//            }
//        } catch (Exception e) {
//            logger.error("insert rank is error:{}",e);
//        }
//        return BaseResp.fail(Constant.RTNINFO_SYS_51);
//    }


}
