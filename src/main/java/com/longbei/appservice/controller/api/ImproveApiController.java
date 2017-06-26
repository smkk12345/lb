package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.TimeLineDetail;
import com.longbei.appservice.service.ImproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by smkk on 17/2/21.
 */
@RestController
@RequestMapping(value = "/api/improve")
public class ImproveApiController {

    @Autowired
    private ImproveService improveService;

    private static Logger logger = LoggerFactory.getLogger(ImproveApiController.class);

    @ResponseBody
    @RequestMapping(value = "updatemedia")
    public BaseResp<Object> updatemedia(String objid, String pickey, String filekey,
                                        String workflow,String duration,String picAttribute){
        logger.info("objid={},pickey={},filekey={},workflow={},duration={},picAttribute={}",
                objid,pickey,filekey,workflow,duration,picAttribute);
        try {
            return improveService.updateMedia(objid,pickey,filekey,workflow,duration,picAttribute);
        }catch (Exception e){
            logger.error("improveService.updateMedia error and msg={}",e);
        }
        return new BaseResp<>();
    }

    /**
     * 获取目标中的进步
     *
     * @param userid
     *            用户id
     * @param goalid
     *            目标id
     * @param startNo
     *            分页开始条数
     * @param pageSize
     *            分页每页显示条数
     * @return
     * @author:luye
     * move by lixb on 2017/3/20
     * @date 2017/2/4
     *
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping(value = "goal/list", method = RequestMethod.POST)
    public BaseResp selectGoalImproveList(String userid, String goalid, String startNo, String pageSize) {
        logger.info("userid={},goalid={},startNo={},pageSize={}",userid,goalid,startNo,pageSize);
        if (StringUtils.hasBlankParams(userid, goalid)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(startNo)) {
            startNo = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        List<Improve> improves = null;
        try {
            improves = improveService.selectGoalImproveList(userid, goalid,null, Integer.parseInt(startNo),
                    Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error("select goal improve list is error:{}", e);
        }
        if (null == improves) {
            return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
        }
        BaseResp<List<Improve>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
        baseres.setData(improves);
        return baseres;
    }

    /**
     * improve/select
     * @param userid
     * @param impid
     * @param businesstype
     * @param businessid
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping(value = "select")
    public BaseResp select(String userid, String impid, String businesstype,String businessid) {
        logger.info("userid={},impid={},businesstype={},businessid={}",userid,impid,businesstype,businessid);
        if (StringUtils.hasBlankParams(userid, impid)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        logger.info("inprove select userid={},impid={}",userid,impid);
        try {
            return improveService.select(userid,impid,businesstype,businessid);
        }catch (Exception e){
            logger.error("get improve detail  is error userid={},impid={} ",userid,impid,e);
        }
        return null;
    }

    /**
     * 获取推荐进步列表（pc）
     * @param improve
     * @param pageno
     * @param pagesize
     * @return
     */
    @RequestMapping(value = "recommendlist")
    public BaseResp<Page<TimeLineDetail>> selectRecommendImproveList(@RequestBody Improve improve,
                                                                     String pageno, String pagesize){
        logger.info("improve={},pageno={},pagesize={}", JSON.toJSONString(improve),pageno,pagesize);
        BaseResp<Page<TimeLineDetail>> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(pageno)){
            pageno = "1";
        }
        if (StringUtils.isBlank(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = improveService.selectRecommendImproveList(improve==null?null:improve.getBrief(),
                    improve==null?null:improve.getAppUserMongoEntity()==null?null:improve.getAppUserMongoEntity().getNickname(),
                    improve==null?null:improve.getCreatetime(),Integer.parseInt(pageno),Integer.parseInt(pagesize));
        } catch (Exception e) {
            logger.error("select improve recommend list for pc is error:",e);
        }

        return baseResp;
    }

    /**
     * 获取不同类型的进步列表（pc）
     * @param pageno
     * @param pagesize
     * @param order
     * @return
     */
    @RequestMapping(value = "list")
    public BaseResp<Page<Improve>> selectImproveList(@RequestBody Improve improve, String pageno,
                                                     String pagesize, String order){
        logger.info("improve={},pageno={},pagesize={},order={}", JSON.toJSONString(improve),pageno,pagesize,order);
        BaseResp<Page<Improve>> baseResp = new BaseResp<>();
        if (null == improve || StringUtils.isBlank(improve.getBusinesstype())){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            System.out.println(improve.getBusinesstype());
            baseResp = improveService.selectImproveList(improve.getBusinesstype(),improve.getBrief(),
                    improve.getAppUserMongoEntity()==null?null:improve.getAppUserMongoEntity().getNickname(),
                    improve.getCreatetime(),
                    Integer.parseInt(pageno),Integer.parseInt(pagesize),order);
        } catch (Exception e) {
            logger.error("select improve list for pc is error:",e);
        }
        return baseResp;

    }

    /**
     * 更新进步推荐状态
     * @param businesstype
     * @param impids
     * @param isrecommend
     * @return
     */
    @RequestMapping(value = "updaterecommend")
    public BaseResp<Object> updateImproveRecommentStatus(String businesstype,@RequestBody List<Long> impids,String isrecommend){
        logger.info("businesstype={},impids={},isrecommend={}",businesstype, JSON.toJSONString(impids),isrecommend);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(businesstype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = improveService.updateImproveRecommentStatus(businesstype,impids,isrecommend);
        } catch (Exception e) {
            logger.error("update improve recommend status for pc is error:",e);
        }
        return baseResp;
    }

    /**
     * 更新进步推荐排序
     * @param impid
     * @param businesstype
     * @param sort
     * @return
     */
    @RequestMapping(value = "updaterecommendsort")
    public BaseResp updateImproveRecommendSort(Long impid, String businesstype, Integer sort) {
        logger.info("impid={},businesstype={},sort={}",impid, businesstype,sort);
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = improveService.updateImproveRecommendSort(impid,businesstype,sort);
        } catch (Exception e) {
            logger.error("update improve recommend sort is error:",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "recommendopt")
    public BaseResp recommendImproveOpt(){
        BaseResp baseResp = new BaseResp();
        logger.info("recommendopt start");
        try {
            baseResp = improveService.recommendImproveOpt();
        } catch (Exception e) {
            logger.error("option recommend improve is error:",e);
        }
        return baseResp;
    }



}


