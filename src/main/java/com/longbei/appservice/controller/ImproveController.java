package com.longbei.appservice.controller;


import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.ImproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 进步操作
 *
 * @author luye
 * @create 2017-01-19 上午11:11
 **/
@Controller
@RequestMapping(value = "improve")
public class ImproveController {

    private static Logger logger = LoggerFactory.getLogger(ImproveController.class);

    @Autowired
    private ImproveService improveService;


    /**
     * 发布进步
     * @param userid 用户id
      * @param brief 说明
     * @param pickey 图片的key
     * @param filekey 文件key  视频文件  音频文件 普通文件
     * @param businesstype  微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     * @param ptype 十全十美id
     * @param ispublic 可见程度  0 私密 1 好友可见 2 全部可见
     * @param itype 类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insert",method = RequestMethod.GET)
    public BaseResp<Object> insertImprove(String userid, String brief, String pickey, String filekey,
                                          String businesstype,String businessid, String ptype,
                                          String ispublic, String itype){
        logger.debug("insertImprove brief:{}," +
                "pickey:{},filekey:{},businesstype:{},ptype:{}," +
                "ispublic:{},itype:{}",brief,pickey,filekey,businesstype,ptype,
                ispublic,itype);
        if(StringUtils.hasBlankParams(userid, businesstype, ptype, ispublic, itype)){
            return new BaseResp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(StringUtils.isBlank(brief)
                && StringUtils.isBlank(pickey)
                && StringUtils.isBlank(filekey)){
            return new BaseResp(Constant.STATUS_SYS_40,Constant.RTNINFO_SYS_40);
        }
        boolean flag = false;
        try {
            flag = improveService.insertImprove(userid,brief,pickey,
                    filekey,businesstype,businessid,ptype,ispublic,itype);
            if (flag) {
                logger.debug("insert improve success");
                return BaseResp.ok(Constant.RTNINFO_SYS_41);
            }
        } catch (Exception e) {
            logger.error("insert improve is error:{}",e);
        }
        logger.info("insert improve fail");
        return new BaseResp(Constant.STATUS_SYS_42,Constant.RTNINFO_SYS_42);
    }

    /**
     * 删除进步
     * @param userid 用户id
     * @param improveid 进步id
     * @param businesstype  进步所属业务类型 如 榜，教室等
     * @param businessid  所属业务类型id 如 榜id 教室id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "remove",method = RequestMethod.GET)
    public BaseResp<Object> removeImprove(String userid,String improveid,
                                          String businesstype,String businessid){
        logger.debug("remove improve userid:{} improveid:{} businesstype:{} businessid:{}"
                ,userid,improveid,businesstype,businessid);
        if(StringUtils.hasBlankParams(userid, improveid, businesstype, businessid)){
            return new BaseResp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        boolean flag = false;
        try {
            flag = improveService.removeImprove(userid,improveid,businesstype,businessid);
            if (flag) {
                logger.debug("remove improve success");
                return BaseResp.ok(Constant.RTNINFO_SYS_41);
            }
        } catch (Exception e) {
            logger.error("remove improve is error:{}",e);
        }
        logger.info("remove improve fail");
        return new BaseResp(Constant.STATUS_SYS_42,Constant.RTNINFO_SYS_42);
    }

}
