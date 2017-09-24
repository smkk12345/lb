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
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.RankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author smkk
 *
 */
@RestController
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
        logger.info("rank={},pageno={},pagesize={}", JSON.toJSONString(rank),pageno,pagesize);
        Page.initPageNoAndPageSize(pageno,pagesize);
        BaseResp<Page<Rank>> baseResp = new BaseResp<>();
        try {
            Page<Rank> page = rankService.selectRankList(rank,Integer.parseInt(pageno),Integer.parseInt(pagesize),false);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select rank list for adminservice is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取榜单列表（带人数、评论数排序）
     * @param pageno  页码
     * @param pagesize  条数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectListWithPageOrderByInvolved")
    public BaseResp<Page<Rank>> selectListWithPageOrderByInvolved(@RequestBody Rank rank,String pageno,String pagesize,String orderByInvolved){
        logger.info("rank={},pageno={},pagesize={},orderByInvolved={}", JSON.toJSONString(rank),pageno,pagesize,orderByInvolved);
        Page.initPageNoAndPageSize(pageno,pagesize);
        BaseResp<Page<Rank>> baseResp = new BaseResp<>();
        try {
            Page<Rank> page = rankService.selectRankList(rank,Integer.parseInt(pageno),Integer.parseInt(pagesize),orderByInvolved);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("selectListWithPageOrderByInvolved for adminservice is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取榜单列表数量
     * @title selectRankListNum
     * @author IngaWu
     * @currentdate:2017年6月24日
     */
    @ResponseBody
    @RequestMapping(value = {"selectRankListNum"})
    public BaseResp<Integer> selectRankListNum(@RequestBody Rank rank){
        logger.info("selectRankListNum for adminservice and rank ={}", JSON.toJSONString(rank));
        BaseResp<Integer> baseResp = new BaseResp<Integer>();
        try {
            baseResp = rankService.selectRankListNum(rank);
        } catch (Exception e) {
            logger.error("selectRankListNum for adminservice and rank ={}", JSON.toJSONString(rank), e);
        }
        return  baseResp;
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
        logger.info("rankImage={},pageno={},pagesize={}", JSON.toJSONString(rankImage),pageno,pagesize);
        Page.initPageNoAndPageSize(pageno,pagesize);
        BaseResp<Page<RankImage>> baseResp = new BaseResp<>();
        try {
            Page<RankImage> rankImages = rankService.selectRankImageList
                    (rankImage,Integer.parseInt(pageno),Integer.parseInt(pagesize));
            baseResp = BaseResp.ok();
            baseResp.setData(rankImages);
        } catch (Exception e) {
            logger.error("select rank iamge list for adminservice is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取草稿榜单数量
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"selectRankImageListNum"})
    public BaseResp<Object> selectRankImageListNum(@RequestBody RankImage rankImage){
        logger.info("selectRankImageListNum for adminservice and rankImage ={}", JSON.toJSONString(rankImage));
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            baseResp = rankService.selectRankImageListNum(rankImage);
        } catch (Exception e) {
            logger.error("selectRankImageListNum for adminservice and rankImage ={}", JSON.toJSONString(rankImage), e);
        }
        return  baseResp;
    }

    /**
     * 获取榜单详情
     * @param rankid 榜单id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectdetail")
    public BaseResp<Rank> selectRankDetail(String rankid){
        logger.info("selectRankDetail rankid={}",rankid);
        BaseResp<Rank> baseResp = new BaseResp();
        if (com.longbei.appservice.common.utils.StringUtils.isBlank(rankid)){
            return baseResp;
        }
        try {
            baseResp = rankService.selectRankDetailByRankid(null,rankid,true,true);
        } catch (Exception e) {
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
    public BaseResp insertRank(@RequestBody RankImage rankImage){
        logger.info("insert rankImage={}",JSON.toJSONString(rankImage));
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.insertRank(rankImage);
        } catch (Exception e) {
            logger.error("insert rank is error:{}",e);
        }
        return baseResp;
    }

    /**
     * 编辑榜单
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateimage")
    public BaseResp<Object> updateRankImage(@RequestBody RankImage rankImage){
        logger.info("update rankImage={}",JSON.toJSONString(rankImage));
        boolean issuccess = false;
        try {
            issuccess = rankService.updateRankImage(rankImage);
            if(issuccess){
                return BaseResp.ok(Constant.RTNINFO_SYS_52);
            }
        } catch (Exception e) {
            logger.error("update rankImage is error:{}",e);
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
        logger.info("update rankImage={}",JSON.toJSONString(rankImage));
        boolean issuccess = false;
        try {
            issuccess = rankService.updateRankImageSymbol(rankImage);
            if(issuccess){
                BaseResp baseResp = BaseResp.ok(Constant.RTNINFO_SYS_52);
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
        logger.info("rank={}",JSON.toJSONString(rank));
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
     * 发布公告
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insertnotice")
    public BaseResp<Object> insertNotice(@RequestBody Rank rank,String noticetype){
        logger.info("rank={},noticetype={}",JSON.toJSONString(rank),noticetype);
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            baseResp = rankService.insertNotice(rank,noticetype);
        } catch (Exception e) {
            logger.error("update rank is error:{}",e);
        }
        return baseResp;
    }

    /**
     * 发布榜单
     * @param rankid 榜单id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "publish/{rankid}")
    public BaseResp<Object> publishRank(@PathVariable("rankid") String rankid){
        logger.info("rankid={}",rankid);
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.publishRankImage(rankid);
        } catch (Exception e) {
            logger.error("publish rank rankid={} is error:{}",rankid,e);
        }
        return baseResp;
    }


    @ResponseBody
    @RequestMapping(value = "publishpcprivate")
    public BaseResp<Object> publishPCPrivateRank(String rankid){
        logger.info("rankid={}",rankid);
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.publishPCPrivateRank(rankid);
        } catch (Exception e) {
            logger.error("publish pc private rank id={} is error:{}",rankid,e);
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
        logger.info("rankCheckDetail={}",JSON.toJSONString(rankCheckDetail));
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.checkRankImage(rankCheckDetail);
        } catch (Exception e) {
            logger.error("check rank rankid={} is error:{}",rankCheckDetail.getRankid(),e);
        }
        return baseResp;
    }

    @ResponseBody
    @RequestMapping(value = "submit")
    public BaseResp submitCheckRank(String rankid){
        logger.info("rankid={}",rankid);
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.submitCheckRank(rankid);
        } catch (Exception e) {
            logger.error("submit check rank id={} is error:{}",rankid,e);
        }
        return baseResp;
    }

    /**
     * 判断榜主是否有权限
     * @param rankid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "privilege")
    public BaseResp hasPrivilege(String rankid){
        logger.info("rankid={}",rankid);
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.hasPrivilege(rankid);
        } catch (Exception e) {
            logger.error("has privilege of rank id={} is error:{}",rankid,e);
        }
        return baseResp;
    }

    /**
     * 查询pc榜单参榜最大人数-权限
     * @param userid
     * @param ranktype
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "maxjoinnum")
    public BaseResp maxRankJoinNum(String userid,String ranktype){
        logger.info("userid={},ranktype={}",userid,ranktype);
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.maxRankJoinNum(userid,ranktype);
        } catch (Exception e) {
            logger.error("maxRankJoinNum userid={} ranktype={} is error:[}",userid,ranktype,e);
        }
        return baseResp;
    }

    /**
     * 撤回审核中的榜单
     */
    @ResponseBody
    @RequestMapping(value = "back")
    public BaseResp setBackCheckRank(String rankid){
        logger.info("rankid={}",rankid);
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = rankService.setBackCheckRank(rankid);
        } catch (Exception e) {
            logger.error("set back rank rankid={} is error:{}",rankid,e);
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
        logger.info("rankid={},createuserid={}",rankid,createuserid);
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
        logger.info("rankimageid={}",rankimageid);
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
        logger.info("rankid={},createuserid={},ispublic={}",rankid,createuserid,ispublic);
        return null;
    }

    /**
     * 获取榜单成员列表
     * @param rankMembers
     * @param startNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "members/{startNum}/{pageSize}")
    public BaseResp<Page<RankMembers>> getRankMembers(@RequestBody RankMembers rankMembers,
                                                @PathVariable("startNum") Integer startNum,
                                                @PathVariable("pageSize") Integer pageSize){
        logger.info("rankMembers={},startNum={},pageSize={}",JSON.toJSONString(rankMembers),startNum,pageSize);
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers || null == rankMembers.getRankid()){
            return baseResp;
        }
        if (StringUtils.isEmpty(startNum)){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if (StringUtils.isEmpty(pageSize)){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        try {
            baseResp = rankService.selectRankMemberList(rankMembers,startNum,pageSize);
        } catch (Exception e) {
            logger.error("select rankmembers rankid = {} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }

    @ResponseBody
    @RequestMapping(value = "membersall/{pageNo}/{pageSize}")
    public BaseResp<Page<RankMembers>> getAllRankMembers(@RequestBody UserInfo userInfo,
                                                         @PathVariable("pageNo") String pageNo,
                                                         @PathVariable("pageSize") String pageSize){
        logger.info("userInfo={},pageNo={},pageSize={}",JSON.toJSONString(userInfo),pageNo,pageSize);
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(pageNo)){
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = rankService.selectRankAllMemberList(userInfo,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error("select all rank member list for pc is error:",e);
        }
        return baseResp;

    }

    /**
     * 查询榜单达人列表
     * @param rankId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectRankFashionManList/{rankId}/{pageNo}/{pageSize}")
    public BaseResp<Page<RankMembers>> selectRankFashionManList(@PathVariable("rankId") String rankId,
                                                         @PathVariable("pageNo") String pageNo,
                                                         @PathVariable("pageSize") String pageSize){
        logger.info("rankId={},pageNo={},pageSize={}",rankId,pageNo,pageSize);
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(pageNo)){
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = rankService.selectRankFashionManList(rankId,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error("selectRankFashionManList for pc is error:",e);
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
        logger.info("rankMembers={},pageNo={},pageSize={}",JSON.toJSONString(rankMembers),pageNo,pageSize);
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
        } catch (Exception e) {
            logger.error("select rankmembers rankid = {} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }


    /**
     * 获取榜单待审核成员列表
     * @param rankMembers
     * @param startNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "waitcheckmembers/{startNum}/{pageSize}")
    public BaseResp<Page<RankMembers>> getRankWaitCheckMembers(@RequestBody RankMembers rankMembers,
                                                               @PathVariable("startNum") Integer startNum,
                                                               @PathVariable("pageSize") Integer pageSize){
        logger.info("rankMembers={},startNum={},pageSize={}",JSON.toJSONString(rankMembers),startNum,pageSize);
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers || null == rankMembers.getRankid()){
            return baseResp;
        }
        if (StringUtils.isEmpty(startNum)){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if (StringUtils.isEmpty(pageSize)){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        try {
            baseResp = rankService.selectRankMemberWaitCheckList
                    (rankMembers,startNum,pageSize);
        } catch (Exception e) {
            logger.error("select wait check rankmembers rankid = {} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }

    /**
     * 获取用户在榜单中所发的进步列表
     * @param rankid  榜单id
     * @param userid 用户id
     * @param startNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "memberimproves/{startNum}/{pageSize}")
    public BaseResp<Page<Improve>> getRankImprovePageByUserid(String rankid,String userid,String iscomplain,
                                                              @PathVariable("startNum") Integer startNum,
                                                              @PathVariable("pageSize") Integer pageSize){
        logger.info("rankid={},userid={},iscomplain={},startNum={},pageSize={}",rankid,userid,iscomplain,startNum,pageSize);
        BaseResp<Page<Improve>> baseResp = new BaseResp<>();

        if (com.longbei.appservice.common.utils.StringUtils.hasBlankParams(rankid,userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isEmpty(startNum)){
            startNum = 0;
        }
        Page<Improve> page = new Page<>(startNum/pageSize+1,pageSize);
        if (StringUtils.isEmpty(pageSize)){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        try {
            RankMembers rankMembers = new RankMembers();
            rankMembers.setRankid(Long.parseLong(rankid));
            rankMembers.setUserid(Long.parseLong(userid));
            BaseResp<List<Improve>> listBaseResp = improveService.selectBusinessImproveList(null,userid,rankid,iscomplain,
                    Constant.IMPROVE_RANK_TYPE,startNum,pageSize,true);
            Integer totalcount = Integer.parseInt(listBaseResp.getExpandData().get("totalcount")+"");
            page.setTotalCount(totalcount);
            page.setList(listBaseResp.getData());
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
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
        logger.info("rankid={},userid={}", rankid,userid);
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

    @RequestMapping(value = "updatestatus/{status}/{userid}/{rankid}/{improveid}")
    public BaseResp<Object> updateStatus(@PathVariable("status")String status,
                                         @PathVariable("userid")String userid,
                                         @PathVariable("rankid")String rankid,
                                         @PathVariable("improveid")String improveid){
        logger.info(" status={},userid={},rankid={},improveid={} :",status,userid,rankid,improveid);
        BaseResp<Object> baseResp = new BaseResp<>();

        if (com.longbei.appservice.common.utils.StringUtils.hasBlankParams(status,userid,rankid,improveid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = rankService.updateStatus(status,userid,rankid,improveid);
        } catch (Exception e) {
            logger.error("remove rank improve improveid={} userid={} is error:",improveid,userid,e);
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
        logger.info("rankMembers={}",JSON.toJSONString(rankMembers));
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == rankMembers
                || rankMembers.getRankid()==null
                || rankMembers.getUserid() == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = rankService.removeRankMember(rankMembers,"0");
        } catch (Exception e) {
            logger.error("remove rankmember rankid={} userid={} is error:",rankMembers.getRankid(),
                    rankMembers.getUserid(),e);
        }
        return baseResp;
    }


    @ResponseBody
    @RequestMapping(value = "close/{rankid}")
    BaseResp<Object> closeRank(@PathVariable("rankid")String rankid){
        logger.info("rankid={}", rankid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(null==rankid){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = rankService.closeRank(rankid);
            //删除榜单
            if(ResultUtil.isSuccess(baseResp)){
                Rank rank =  new Rank();
                rank.setRankid(Long.parseLong(rankid));
                rank.setIsdel("1");
                rankService.updateRankSymbol(rank);
                RankImage rankImage =  new RankImage();
                rankImage.setRankid(Long.parseLong(rankid));
                rankImage.setIsdel("1");
                rankService.updateRankImageSymbol(rankImage);
            }
        }catch (Exception e){
            logger.error("close rank_ rankid={} is error:",rankid);
        }
        return baseResp;
    }
    /**
     * 设置，取消达人,附带 达人排序
     * @param rankMembers
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "fashionman")
    public BaseResp<Object> setFashionman(@RequestBody RankMembers rankMembers){
        logger.info("rankMembers={}",JSON.toJSONString(rankMembers));
        BaseResp<Object> baseResp = new BaseResp<>();
        logger.info(String.valueOf(rankMembers.getRankid()));
        logger.info(String.valueOf(rankMembers.getUserid()));
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
        logger.info("rankMembers={}",JSON.toJSONString(rankMembers));
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == rankMembers
                || rankMembers.getRankid()==null
                || rankMembers.getUserid() == null
                || rankMembers.getCheckstatus() == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            rankMembers.setCheckdate(new Date());
            rankMembers.setIcount(0);
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
        logger.info("rankid={}", rankid);
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
        logger.info("rank={}",JSON.toJSONString(rank));
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == rank || StringUtils.isEmpty(rank.getRankid())){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = rankService.submitRankMemberCheckResult(rank,true,false);
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
    public BaseResp<Rank> rankDetail(String rankId){
        logger.info("rankId={}", rankId);
        BaseResp<Rank> baseResp = new BaseResp<Rank>();
        if(com.longbei.appservice.common.utils.StringUtils.isEmpty(rankId)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }

        baseResp = this.rankService.selectRankDetailByRankid(null,rankId,true,true);
        return baseResp;
    }

    /**
     * 通知关注榜单的用户 榜单已开始
     * @param currentTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value="noticeFollowRankUser")
    public BaseResp<Object> noticeFollowRankUser(Long currentTime){
        logger.info("currentTime={}", currentTime);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(currentTime == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        Date currentDate = new Date(currentTime);
//        baseResp = this.rankService.noticeFollowRankUser(currentDate);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    /**
     * 将已经开始的榜单置为已开始
     * @param currentTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value="handleStartRank")
    public BaseResp<Object> handleStartRank(Long currentTime){
        logger.info("currentTime={}", currentTime);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        logger.info("handleStartRank currentTime={}",currentTime);
        if(currentTime == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        Date currentDate = new Date(currentTime);
        baseResp = this.rankService.handleStartRank(currentDate);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    /**
     * 榜单自动确认收货
     * @param currentTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value="rankAwardConfirmReceipt")
    public BaseResp<Object> rankAwardConfirmReceipt(Long currentTime){
        logger.info("currentTime={}", currentTime);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(currentTime == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        Date currentDate = new Date(currentTime);
        baseResp = this.rankService.rankAwardConfirmReceipt(currentDate);
        return baseResp;
    }

    /**
     * 结束榜单
     * @param currentTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value="endRank")
    public BaseResp<Object> endRank(Long currentTime){
        logger.info("currentTime={}", currentTime);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(currentTime == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        Date currentDateTime= new Date(currentTime);
        baseResp = this.rankService.endRank(currentDateTime);
        return baseResp;
    }

    /***
     * 自动发布榜单
     * @param currentTime
     * @return
     */
    @RequestMapping(value="autoPublishRank")
    @ResponseBody
    public BaseResp<Object> autoPublishRank(Long currentTime){
        logger.info("currentTime={}", currentTime);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(currentTime == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        Date currentDateTime= new Date(currentTime);
        baseResp = this.rankService.publishRank(currentDateTime);
        return baseResp;
    }

    /**
     * 获取整个榜单的排名
     * @url http://ip:port/app_service/rank/rankMemberSort
     * @param rankId 榜单id
     * @param sortType 排序的方式 0:综合排序 1:赞 2:花
     * @param startNum
     * @param endNum
     * @return
     */
    @RequestMapping(value="rankMemberSort")
    public BaseResp<Object> rankMemberSort(Long rankId,Integer sortType,Integer startNum,Integer endNum){
        logger.info("rankId={},sortType={},startNum={},endNum={}", rankId,sortType,startNum,endNum);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(rankId == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(sortType == null){//默认综合排序
            sortType = 0;
        }
        if(startNum == null || startNum < 1){
            startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNum != null && endNum > startNum){
            pageSize = endNum - startNum;
        }
        baseResp = this.rankService.rankMemberSort(null,rankId,sortType,startNum,pageSize);
        return baseResp;
    }

    /**
     * 获取榜单审核记录
     * @param rankid
     * @return
     */
    @RequestMapping("checkdetaillist")
    public BaseResp<List<RankCheckDetail>> selectRankCheckDetailList(String rankid){
        logger.info("rankid={}", rankid);
        BaseResp<List<RankCheckDetail>> baseResp = new BaseResp<>();
        try {
            baseResp = rankService.selectRankCheckDetailList(rankid);
        } catch (Exception e) {
            logger.error("select rank rankid={} checkdetail list is error:",rankid,e);
        }
        return baseResp;
    }

}
