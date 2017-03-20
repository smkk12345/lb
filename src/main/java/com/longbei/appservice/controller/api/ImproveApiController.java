package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.service.ImproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by smkk on 17/2/21.
 */
@Controller
@RequestMapping(value = "/api/improve")
public class ImproveApiController {

    @Autowired
    private ImproveService improveService;

    private static Logger logger = LoggerFactory.getLogger(ImproveApiController.class);

    @ResponseBody
    @RequestMapping(value = "updatemedia")
    public BaseResp<Object> updatemedia(String objid, String pickey, String filekey,String workflow){
        try {
            return improveService.updateMedia(objid,pickey,filekey,workflow);
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

}
