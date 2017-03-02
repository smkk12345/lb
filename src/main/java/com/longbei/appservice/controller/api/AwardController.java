package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.entity.Award;
import com.longbei.appservice.entity.AwardClassify;
import com.longbei.appservice.service.AwardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 奖品接口
 *
 * @author luye
 * @create 2017-02-27 下午5:27
 **/
@RestController
@RequestMapping(value = "api/award")
public class AwardController {

    private static Logger logger = LoggerFactory.getLogger(AwardController.class);

    @Autowired
    private AwardService awardService;


    /**
     * 添加奖品
     * @param award
     * @return
     */
    @RequestMapping(value = "add")
    public BaseResp addAward(@RequestBody Award award){
        BaseResp<Page<Award>> baseResp = new BaseResp<>();
        try {
            boolean flag = awardService.insertAward(award);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("add award  is error:{}",e);
        }
        return baseResp;
    }

    /**
     * 编辑奖品
     * @param award
     * @return
     */
    @RequestMapping(value = "update")
    public BaseResp updateAward(@RequestBody Award award){
        BaseResp<Page<Award>> baseResp = new BaseResp<>();
        try {
            boolean flag = awardService.updateAward(award);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("update award  is error:{}",e);
        }
        return baseResp;
    }


    /**
     * 删除奖品
     * @param awardid
     * @return
     */
    @RequestMapping(value = "del/{awardid}")
    public BaseResp deleteAward(@PathVariable("awardid") String awardid){
        BaseResp baseResp = new BaseResp();
        if (StringUtils.isEmpty(awardid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            boolean flag = awardService.deleteAward(Integer.parseInt(awardid));
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("delete award  is error:{}",e);
        }
        return baseResp;

    }

    /**
     * 奖品详情
     * @param awardid
     * @return
     */
    @RequestMapping(value = "get/{awardid}")
    public BaseResp<Award> getAward(@PathVariable("awardid") String awardid){
        BaseResp baseResp = new BaseResp();
        if (StringUtils.isEmpty(awardid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            Award award = awardService.selectAward(awardid);
            baseResp.setData(award);
        } catch (NumberFormatException e) {
            logger.error("delete award  is error:{}",e);
        }
        return baseResp;
    }


    /**
     * 奖品列表
     * @param award
     * @param pageno
     * @param pagesize
     * @return
     */
    @RequestMapping(value = "list/{pageno}/{pagesize}")
    public BaseResp<Page<Award>> getAwardListWithPage(@RequestBody Award award,
                                                @PathVariable("pageno") String pageno,
                                                @PathVariable("pagesize") String pagesize){
        BaseResp<Page<Award>> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(pageno)){
            pageno = "1";
        }
        if (StringUtils.isEmpty(pagesize)){
            pageno = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            Page<Award> page = awardService.selectAwardListWithPage(award,Integer.parseInt(pageno),Integer.parseInt(pagesize));
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (NumberFormatException e) {
            logger.error("get award list with page is error:{}",e);
        }
        return baseResp;
    }

    /**
     * 获取奖品列表
     * @param award
     * @return
     */
    @RequestMapping(value = "list")
    public BaseResp<List<Award>> getAwardList(@RequestBody Award award){
        BaseResp<List<Award>> baseResp = new BaseResp<>();

        try {
            List<Award> awards = awardService.selectAwardList(award);
            baseResp = BaseResp.ok();
            baseResp.setData(awards);
        } catch (Exception e) {
            logger.error("get award list is error:{}",e);
        }
        return baseResp;
    }


    @RequestMapping(value = "classify/add")
    public BaseResp addAwardClassify(@RequestBody AwardClassify awardClassify){
        BaseResp<Page<Award>> baseResp = new BaseResp<>();
        try {
            boolean flag = awardService.insertAwardClassify(awardClassify);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("add awardclassify  is error:{}",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "classify/update")
    public BaseResp updateAwardClassify(@RequestBody AwardClassify awardClassify){
        BaseResp<Page<Award>> baseResp = new BaseResp<>();
        try {
            boolean flag = awardService.updateAwardClassify(awardClassify);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("update awardclassify  is error:{}",e);
        }
        return baseResp;
    }


    @RequestMapping(value = "classify/del/{classifyid}")
    public BaseResp deleteAwardClassify(@PathVariable("classifyid") String classifyid){
        BaseResp<Page<Award>> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(classifyid)){
            return baseResp;
        }
        try {
            boolean flag = awardService.deleteAward(Integer.parseInt(classifyid));
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("delete awardclassify  is error:{}",e);
        }
        return baseResp;
    }


    @RequestMapping(value = "classify/list")
    public BaseResp<List<AwardClassify>> getAwardClassifyList(){
        BaseResp<List<AwardClassify>> baseResp = new BaseResp<>();
        try {
            List<AwardClassify> awardClassifies = awardService.selectAwardClassifyList(null);
            baseResp = BaseResp.ok();
            baseResp.setData(awardClassifies);
        } catch (Exception e) {
            logger.error("get awardclassify list is error:{}",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "classify/get/{classid}")
    public BaseResp<AwardClassify> getAwardClassify(@PathVariable("classid") String classid){
        BaseResp<AwardClassify> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(classid)){
            return baseResp;
        }
        try {
            AwardClassify awardClassify = awardService.selectAwardClassify(Integer.parseInt(classid));
            baseResp = BaseResp.ok();
            baseResp.setData(awardClassify);
        } catch (Exception e) {
            logger.error("get awardclassify is error:{}",e);
        }
        return baseResp;
    }



}
