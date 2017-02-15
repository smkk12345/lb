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
import com.longbei.appservice.entity.RankImage;
import com.longbei.appservice.service.RankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @param ranktitle  榜单标题
     * @param rankscope  榜单范围
     * @param starttimestart  开始时间（起始）
     * @param starttimeend  开始时间（结束）
     * @param endtimestart  结束时间 （起始）
     * @param endtimeend  结束时间（结束）
     * @param rankcateid  榜单类型
     * @param ispublic  是否公开
     * @param ptype   十全十美类型
     * @param sourcetype  来源类型
     * @param companyname  公司名字
     * @param pageno  页码
     * @param pagesize  条数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectlist")
    public BaseResp<Page<Rank>> selectRankList(String ranktitle, String rankscope,
                                              String starttimestart, String starttimeend,
                                              String endtimestart, String endtimeend,
                                              String rankcateid, String ispublic,
                                              String ptype, String sourcetype, String companyname,
                                              String pageno, String pagesize
                                         ){
        return null;
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
     * 添加榜单

     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insert")
    public BaseResp<Object> insertRank(String rankImage){

        boolean issuccess = false;
        if (StringUtils.isEmpty(rankImage)){
            BaseResp.fail(Constant.RTNINFO_SYS_07);
        }
        try {
            RankImage rankImage1 = JSON.parseObject(rankImage,RankImage.class);
            issuccess = rankService.insertRank(rankImage1.getRankdetail(),rankImage1.getRanktitle(),
                    rankImage1.getRanklimite(),
                    rankImage1.getRankscope(),rankImage1.getRankphotos(),rankImage1.getRankrate(),
                    rankImage1.getStarttime(),
                    rankImage1.getEndtime(),
                    rankImage1.getAreaname(),String.valueOf(rankImage1.getCreateuserid()),rankImage1.getRanktype(),
                    rankImage1.getIspublic(),String.valueOf(rankImage1.getRankcateid()),
                    rankImage1.getLikescore(),rankImage1.getFlowerscore(),
                    rankImage1.getDiamondscore(),rankImage1.getCodeword(),rankImage1.getPtype(),
                    rankImage1.getSourcetype(),rankImage1.getCompanyname(),rankImage1.getCompanyphotos(),rankImage1.getCompanybrief());
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
     * @param rankid  榜单id
     * @param rankdetail  榜单详情
     * @param ranktitle  榜单标题
     * @param ranklimite 榜单限制人数
     * @param rankscope  榜单范围
     * @param rankphotos 榜单图片
     * @param rankrate  榜单中奖率
     * @param starttime  开始时间
     * @param endtime   结束时间
     * @param areaname  区域名字
     * @param createuserid 创建人id
     * @param ranktype  榜单类型
     * @param ispublic  是否公开
     * @param rankcateid 榜单类型
     * @param likescore 赞分数
     * @param flowerscore 花分数
     * @param diamondscore 钻石分数
     * @param codeword 入榜口令
     * @param ptype 十全十美类型
     * @param sourcetype 来源类型
     * @param companyname  公司名字
     * @param companyphotos 公司图片
     * @param companybrief 公司简介
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "update")
    public BaseResp<Object> updateRank(String rankid,String rankdetail,String ranktitle,
                                       String ranklimite, String rankscope,
                                       String rankphotos, String rankrate,
                                       String starttime,String endtime,String areaname,
                                       String createuserid,String ranktype,String ispublic,
                                       String rankcateid,String likescore,
                                       String flowerscore,String diamondscore,
                                       String codeword,String ptype,String sourcetype,
                                       String companyname,String companyphotos,
                                       String companybrief){

        boolean issuccess = false;
        try {
            issuccess = rankService.updateRank(Long.parseLong(rankid),rankdetail,ranktitle,
                    Integer.parseInt(ranklimite),rankscope,rankphotos,Double.parseDouble(rankrate),
                    DateUtils.formatDate(starttime,"yyyy-MM-dd HH:mm:ss"),
                    DateUtils.formatDate(endtime,"yyyy-MM-dd HH:mm:ss"),areaname,
                    createuserid,ranktype,ispublic,rankcateid,Integer.parseInt(likescore),
                    Integer.parseInt(flowerscore),Integer.parseInt(diamondscore),codeword,ptype,
                    sourcetype,companyname,companyphotos,companybrief);
            if(issuccess){
                return BaseResp.ok(Constant.RTNINFO_SYS_52);
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
    @RequestMapping(value = "publish")
    public BaseResp<Object> publishRank(String rankid){
        return null;
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
