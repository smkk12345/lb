/**   
* @Title: RankController.java 
* @Package com.longbei.appservice.controller.api 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月20日 上午10:58:54 
* @version V1.0   
*/
package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankCheckDetail;
import com.longbei.appservice.entity.RankImage;
import com.longbei.appservice.service.RankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author smkk
 *
 */
@Controller
@RequestMapping(value = "/api/rank")
public class RankApiController {

    private static Logger logger = LoggerFactory.getLogger(RankApiController.class);

    @Autowired
    private  RankService rankService;


    /**
     * 获取榜单列表
     * @param pageno  页码
     * @param pagesize  条数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectlist")
    public BaseResp<Page<Rank>> selectRankList(@RequestBody Rank rank,String pageno,String pagesize){
        Page.initPageNoAndPageSize(pageno,pagesize);
        BaseResp<Page<Rank>> baseResp = new BaseResp<>();
        try {
            Page<Rank> page = rankService.selectRankList(rank,Integer.parseInt(pageno),Integer.parseInt(pagesize));
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (NumberFormatException e) {
            logger.error("select rank list for adminservice is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取草稿榜单
     * @param pageno
     * @param pagesize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectimagelist")
    public BaseResp selectRankImageList(@RequestBody RankImage rankImage, String pageno, String pagesize, HttpServletRequest request){
        Page.initPageNoAndPageSize(pageno,pagesize);
        BaseResp<Page<RankImage>> baseResp = new BaseResp<>();
        try {
            Page<RankImage> rankImages = rankService.selectRankImageList
                    (rankImage,Integer.parseInt(pageno),Integer.parseInt(pagesize));
            baseResp = BaseResp.ok();
            baseResp.setData(rankImages);
        } catch (NumberFormatException e) {
            logger.error("select rank iamge list for adminservice is error:",e);
        }
        return baseResp;
    }





    /**
     * 获取榜单详情
     * @param rankid 榜单id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectdetail")
    public BaseResp<Rank> selectRankDetail(String rankid){
        return null;
    }


    /**
     *
     * @param rankid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectimagedetail")
    public BaseResp<RankImage> selectRankImageDetail(String rankid){
        logger.info("selectRankImageDetail rankid={}",rankid);
        BaseResp<RankImage>  baseResp = rankService.selectRankImage(rankid);
        return baseResp;
    }



    /**
     * 添加榜单

     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insert")
    public BaseResp<Object> insertRank(@RequestBody RankImage rankImage){

        boolean issuccess = false;

        try {
            issuccess = rankService.insertRank(rankImage);
            if(issuccess){
                return BaseResp.ok(Constant.RTNINFO_SYS_50);
            }
        } catch (Exception e) {
            logger.error("insert rank is error:{}",e);
        }
        return BaseResp.fail(Constant.RTNINFO_SYS_51);
    }

    /**
     * 编辑榜单
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateimage")
    public BaseResp<Object> updateRankImage(@RequestBody RankImage rankImage){

        boolean issuccess = false;
        try {
            issuccess = rankService.updateRankImage(rankImage);
            if(issuccess){
                return BaseResp.ok(Constant.RTNINFO_SYS_52);
            }
        } catch (Exception e) {
            logger.error("update rank is error:{}",e);
        }
        return BaseResp.fail(Constant.RTNINFO_SYS_53);
    }



    /**
     * 编辑榜单状态
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateimagesymbol")
    public BaseResp<Object> updateRankImageSymbol(@RequestBody RankImage rankImage){

        boolean issuccess = false;
        try {
            issuccess = rankService.updateRankSymbol(rankImage);
            if(issuccess){
                BaseResp baseResp = BaseResp.ok(Constant.RTNINFO_SYS_52);
                if (null != rankImage.getAutotime()){
                    baseResp.setData(rankImage.getAutotime());
                }
                return baseResp;
            }
        } catch (Exception e) {
            logger.error("update rank is error:{}",e);
        }
        return BaseResp.fail(Constant.RTNINFO_SYS_53);
    }

    /**
     * 发布榜单
     * @param rankid 榜单id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "publish/{rankid}")
    public BaseResp<Object> publishRank(@PathVariable("rankid") String rankid){
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.publishRankImage(rankid);
        } catch (Exception e) {
            logger.error("publish rank rankid={} is error:{}",rankid,e);
        }
        return baseResp;
    }

    /**
     * 审核榜单
     * @param rankCheckDetail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "check")
    public BaseResp<Object> checkRank(@RequestBody RankCheckDetail rankCheckDetail){
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.checkRankImage(rankCheckDetail);
        } catch (Exception e) {
            logger.error("check rank rankid={} is error:{}",rankCheckDetail.getRankid(),e);
        }
        return baseResp;
    }

    /**
     * 删除榜单
     * @param rankid 榜单id
     * @param createuserid 创建人id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "remove")
    public BaseResp<Object> removeRank(String rankid,String createuserid){
        return null;
    }

    /**
     * 删除草稿榜单
     * @param rankimageid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "removeimage/{rankimageid}")
    public BaseResp<Object> removeRankImage(@PathVariable("rankimageid") String rankimageid){
        boolean flag = rankService.deleteRankImage(rankimageid);
        if(flag){
            BaseResp baseResp = BaseResp.ok();
            baseResp.setData(rankimageid);
            return baseResp;
        }
        return BaseResp.fail();
    }

    /**
     * 修改榜单 发布情况
     * @param rankid  榜单id
     * @param createuserid 创建人id
     * @param ispublic  是否发布
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "ispublic")
    public BaseResp<Object> isPublicRank(String rankid,String createuserid,String ispublic){
        return null;
    }

}
