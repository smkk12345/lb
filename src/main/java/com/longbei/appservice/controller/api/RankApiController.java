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
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.ImproveService;
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
import java.util.Date;
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
    @Autowired
    private ImproveService improveService;


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
            Page<Rank> page = rankService.selectRankList(rank,Integer.parseInt(pageno),Integer.parseInt(pagesize),false);
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
    public BaseResp<Object> selectRankDetail(String rankid){
        logger.info("selectRankDetail rankid={}",rankid);
        BaseResp<Object> baseResp = new BaseResp();
        if (com.longbei.appservice.common.utils.StringUtils.isBlank(rankid)){
            return baseResp;
        }
        try {
            baseResp = rankService.selectRankDetailByRankid(null,rankid,true,false);
        } catch (NumberFormatException e) {
            logger.error("select rank info rankid={} is error:",rankid,e);
        }
        return baseResp;
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
            issuccess = rankService.updateRankImageSymbol(rankImage);
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
     * 编辑榜单状态 公告等信息 线上
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updatesymbol")
    public BaseResp<Object> updateRankSymbol(@RequestBody Rank rank){

        boolean issuccess = false;
        try {
            issuccess = rankService.updateRankSymbol(rank);
            if(issuccess){
                BaseResp baseResp = BaseResp.ok(Constant.RTNINFO_SYS_52);
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

    /**
     * 获取榜单成员列表
     * @param rankMembers
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "members/{pageNo}/{pageSize}")
    public BaseResp<Page<RankMembers>> getRankMembers(@RequestBody RankMembers rankMembers,
                                                @PathVariable("pageNo") String pageNo,
                                                @PathVariable("pageSize") String pageSize){
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers || null == rankMembers.getRankid()){
            return baseResp;
        }
        if (StringUtils.isEmpty(pageNo)){
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = rankService.selectRankMemberList(rankMembers,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
        } catch (NumberFormatException e) {
            logger.error("select rankmembers rankid = {} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }




    /**
     * 获取成员列表 预览
     * @param rankMembers
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkmemberspreview/{pageNo}/{pageSize}")
    public BaseResp<Page<RankMembers>> rankMemberCheckResultPreview(@RequestBody RankMembers rankMembers,
                                                                    @PathVariable("pageNo") String pageNo,
                                                                    @PathVariable("pageSize") String pageSize){
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers || null == rankMembers.getRankid()){
            return baseResp;
        }
        if (StringUtils.isEmpty(pageNo)){
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = rankService.rankMemberCheckResultPreview(rankMembers,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
        } catch (NumberFormatException e) {
            logger.error("select rankmembers rankid = {} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }


    /**
     * 获取榜单待审核成员列表
     * @param rankMembers
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "waitcheckmembers/{pageNo}/{pageSize}")
    public BaseResp<Page<RankMembers>> getRankWaitCheckMembers(@RequestBody RankMembers rankMembers,
                                                               @PathVariable("pageNo") String pageNo,
                                                               @PathVariable("pageSize") String pageSize){
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers || null == rankMembers.getRankid()){
            return baseResp;
        }
        if (StringUtils.isEmpty(pageNo)){
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = rankService.selectRankMemberWaitCheckList
                    (rankMembers,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
        } catch (NumberFormatException e) {
            logger.error("select wait check rankmembers rankid = {} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }

    /**
     * 获取用户在榜单中所发的进步列表
     * @param rankid  榜单id
     * @param userid 用户id
     * @param pageno
     * @param pagesize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "memberimproves/{pageNo}/{pageSize}")
    public BaseResp<Page<Improve>> getRankImprovePageByUserid(String rankid,String userid,
                                                              @PathVariable("pageNo") String pageno,
                                                              @PathVariable("pageSize") String pagesize){
        BaseResp<Page<Improve>> baseResp = new BaseResp<>();

        if (com.longbei.appservice.common.utils.StringUtils.hasBlankParams(rankid,userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isEmpty(pageno)){
            pageno = "1";
        }
        Page<Improve> page = new Page<>(Integer.parseInt(pageno),Integer.parseInt(pagesize));
        if (StringUtils.isEmpty(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            RankMembers rankMembers = new RankMembers();
            rankMembers.setRankid(Long.parseLong(rankid));
            rankMembers.setUserid(Long.parseLong(userid));
            BaseResp<RankMembers> rk = rankService.selectRankMemberInfo(rankid,userid);
            int totalcount = 0;
            if (null != rk.getData()){
                totalcount = rk.getData().getIcount();
            }
            BaseResp<List<Improve>> listBaseResp = improveService.selectBusinessImproveList(userid,rankid,
                    Constant.IMPROVE_RANK_TYPE,Integer.parseInt(pagesize)*(Integer.parseInt(pageno)-1),
                    Integer.parseInt(pagesize));
            page.setTotalCount(totalcount);
            page.setList(listBaseResp.getData());
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (NumberFormatException e) {
            logger.error("select rank improve list by user userid={} is error:",userid,e);
        }
        return baseResp;
    }

    /**
     * 获取榜中成员详细信息
     * @param rankid
     * @param userid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "memberinfo/{rankid}/{userid}")
    public BaseResp<RankMembers> getRankMemberInfo(@PathVariable("rankid") String rankid,
                                                   @PathVariable("userid") String userid){
        BaseResp<RankMembers> baseResp = new BaseResp<>();

        if (com.longbei.appservice.common.utils.StringUtils.hasBlankParams(rankid,userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = rankService.selectRankMemberInfo(rankid,userid);
        } catch (Exception e) {
            logger.error("select rankmemberinfo rankid={} userid={} is error:",rankid,userid,e);
        }
        return baseResp;
    }

    /**
     * 下榜，下榜再不能参加次榜
     * @param rankMembers
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "removemember")
    public BaseResp<Object> removeRankMember(@RequestBody RankMembers rankMembers){
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == rankMembers
                || rankMembers.getRankid()==null
                || rankMembers.getUserid() == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = rankService.removeRankMember(rankMembers);
        } catch (Exception e) {
            logger.error("remove rankmember rankid={} userid={} is error:",rankMembers.getRankid(),
                    rankMembers.getUserid(),e);
        }
        return baseResp;
    }


    /**
     * 设置，取消达人
     * @param rankMembers
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "fashionman")
    public BaseResp<Object> setFashionman(@RequestBody RankMembers rankMembers){
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == rankMembers
                || rankMembers.getRankid()==null
                || rankMembers.getUserid() == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = rankService.setIsfishionman(rankMembers);
        } catch (Exception e) {
            logger.error("set rank fashionman is error:",e);
        }
        return baseResp;
    }

    /**
     * 成员审核结果
     * @param rankMembers
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkmember")
    public BaseResp<Object> updateRankMemberCheckStatus(@RequestBody RankMembers rankMembers){
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == rankMembers
                || rankMembers.getRankid()==null
                || rankMembers.getUserid() == null
                || rankMembers.getCheckstatus() == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            rankMembers.setCheckdate(new Date());
            baseResp = rankService.updateRankMemberCheckStatus(rankMembers);
        } catch (Exception e) {
            logger.error("check rankmember status rankid={} userid={} checkstatus={}" +
                    " is error:",rankMembers.getRankid(),rankMembers.getUserid(),
                    rankMembers.getCheckstatus(),e);
        }
        return baseResp;
    }

    /**
     * 提交榜单审核结果 予发布
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "subcheckresultpre")
    public BaseResp<Object> subRankMemberCheckResultPre(String rankid){
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(rankid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = rankService.submitRankMemberCheckResultPreview(rankid);
        } catch (Exception e) {
            logger.error("submit rank member check result rankid={} is error:",rankid,e);
        }
        return baseResp;
    }

    /**
     * 提交榜单审核结果
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "subcheckresult")
    public BaseResp<Object> subRankMemberCheckResult(@RequestBody Rank rank){
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == rank || StringUtils.isEmpty(rank.getRankid())){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = rankService.submitRankMemberCheckResult(rank,true);
        } catch (Exception e) {
            logger.error("submit rank member check result rankid={} is error:",rank.getRankid(),e);
        }
        return baseResp;
    }

    /**
     * 获取榜单详情
     * @param rankId
     * @return
     */
    @RequestMapping(value="rankDetail")
    public BaseResp<Object> rankDetail(String rankId){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(com.longbei.appservice.common.utils.StringUtils.isEmpty(rankId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = this.rankService.selectRankDetailByRankid(null,rankId,true,false);
        return baseResp;
    }


}
