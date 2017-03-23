package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.RankAcceptAward;
import com.longbei.appservice.entity.RankMembers;
import com.longbei.appservice.service.RankAcceptAwardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 榜单领奖管理
 *
 * @author luye
 * @create 2017-03-21 下午5:47
 **/
@RestController
@RequestMapping(value = "/api/rankacceptaward")
public class RankAcceptAwardApiController {

    private static Logger logger = LoggerFactory.getLogger(RankAcceptAwardApiController.class);

    @Autowired
    private RankAcceptAwardService rankAcceptAwardService;

    @RequestMapping(value = "list/{pageno}/{pagesize}")
    public BaseResp<Page<RankAcceptAward>> selectRankAcceptAwardList(@RequestBody RankAcceptAward rankAcceptAward,
                                                                     @PathVariable("pageno") String pageno,
                                                                     @PathVariable("pagesize") String pagesize){
        BaseResp<Page<RankAcceptAward>> baseResp = new BaseResp<>();
        if (null == rankAcceptAward){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(pageno)){
            pageno = "1";
        }
        if (StringUtils.isBlank(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = rankAcceptAwardService.selectRankAccepteAwardList(rankAcceptAward,Integer.parseInt(pageno),Integer.parseInt(pagesize));
        } catch (NumberFormatException e) {
            logger.error("select rankacceptaward list is error:",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "detail/{rankid}/{userid}")
    public BaseResp<RankAcceptAward> selectRankAcceptAwardDetail(@PathVariable("rankid") String rankid,
                                                                 @PathVariable("userid") String userid){
        BaseResp<RankAcceptAward> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(rankid,userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        try {
            baseResp = rankAcceptAwardService.selectRankAcceptAwardDetail(rankid,userid);
        } catch (Exception e) {
            logger.error("select rankacceptaward detail  rankid={} userid={} is error:",
                    rankid,userid,e);
        }

        return baseResp;

    }

    @RequestMapping(value = "update")
    public BaseResp<Object> updateRankAccepteAward(@RequestBody RankAcceptAward rankAcceptAward){
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == rankAcceptAward ){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        try {
            baseResp = rankAcceptAwardService.updateRankAcceptAward(rankAcceptAward);
        } catch (Exception e) {
            logger.error("update rankacceptaward detail  rankid={} userid={} is error:"
                    ,e);
        }
        return baseResp;
    }





}
