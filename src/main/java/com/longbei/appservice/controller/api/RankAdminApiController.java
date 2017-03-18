package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.RankCard;
import com.longbei.appservice.service.RankCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 榜主名片
 *
 * @author luye
 * @create 2017-03-02 上午10:00
 **/
@RestController
@RequestMapping(value = "api/rkcard")
public class RankAdminApiController {


    private Logger logger = LoggerFactory.getLogger(RankAdminApiController.class);

    @Autowired
    private RankCardService rankCardService;

    /**
     * 添加榜主名片
     * @param rankCard
     * @return
     */
    @RequestMapping(value = "add")
    public BaseResp insertRankAdmin(@RequestBody RankCard rankCard){
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankCardService.insertRankAdmin(rankCard);
        } catch (Exception e) {
            logger.error("add rank card is error:",e);
        }
        return baseResp;
    }

    /**
     * 更新榜主名单
     * @param rankCard
     * @return
     */
    @RequestMapping(value = "update")
    public BaseResp updateRankAdmin(@RequestBody  RankCard rankCard){
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankCardService.updateRankAdmin(rankCard);
        } catch (Exception e) {
            logger.error("update rank card is error:",e);
        }
        return baseResp;
    }

    /**
     * 删除榜主名片
     * @param rankadminid
     * @return
     */
    @RequestMapping(value = "del/{rankadminid}")
    public BaseResp deleteRankAdmin(@PathVariable("rankadminid") String rankadminid){
        BaseResp baseResp = new BaseResp();
        if (StringUtils.isBlank(rankadminid)){
            return baseResp;
        }
        try {
            baseResp = rankCardService.deleteRankAdmin(rankadminid);
        } catch (Exception e) {
            logger.error("delete rank card is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取榜主名片详情
     * @param rankadminid
     * @return
     */
    @RequestMapping(value = "get/{rankadminid}")
    public BaseResp<RankCard> selectRankAdmin(@PathVariable("rankadminid") String rankadminid){
        BaseResp<RankCard> baseResp = new BaseResp();
        if (StringUtils.isBlank(rankadminid)){
            return baseResp;
        }
        try {
            baseResp = rankCardService.selectRankAdmin(rankadminid);
        } catch (Exception e) {
            logger.error("select rank card id={} is error:",rankadminid,e);
        }
        return baseResp;
    }

    /**
     * 获取榜主名片列表（分页）
     * @param rankCard
     * @param pageno
     * @param pagesize
     * @return
     */
    @RequestMapping(value = "list/{pageno}/{pagesize}")
    public BaseResp<Page<RankCard>> selectRankAminListWithPage(@RequestBody RankCard rankCard, @PathVariable("pageno") String pageno,
                                                     @PathVariable("pagesize") String pagesize){
        BaseResp<Page<RankCard>> baseResp = new BaseResp();
        if (StringUtils.hasBlankParams(pageno,pagesize)){
            return baseResp;
        }
        try {
            baseResp = rankCardService.selectRankAminListWithPage(rankCard,Integer.parseInt(pageno),
                    Integer.parseInt(pagesize));
        } catch (Exception e) {
            logger.error("select rank card list with page  is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取榜主名片列表
     * @param rankCard
     * @return
     */
    @RequestMapping(value = "list")
    public BaseResp<List<RankCard>> selectRankAdminList(RankCard rankCard){
        BaseResp<List<RankCard>> baseResp = new BaseResp();
        try {
            baseResp = rankCardService.selectRankAdminList(rankCard);
        } catch (Exception e) {
            logger.error("select rank card list is error:",e);
        }
        return baseResp;
    }





}
