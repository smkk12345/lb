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
     * 获取草稿榜单
     * @param pageno
     * @param pagesize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectimagelist")
    public BaseResp selectRankImageList(@RequestBody RankImage rankImage, String pageno, String pagesize, HttpServletRequest request){

        Page<RankImage> rankImages = rankService.selectRankImageList
                (Integer.parseInt(pageno),Integer.parseInt(pagesize));
        BaseResp<Page<RankImage>> ranks = BaseResp.ok();
        ranks.setData(rankImages);
        return ranks;
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
            issuccess = rankService.insertRank(rankImage.getRankdetail(),rankImage.getRanktitle(),
                    rankImage.getRanklimite(),
                    rankImage.getRankscope(),rankImage.getRankphotos(),rankImage.getRankrate(),
                    rankImage.getStarttime(),
                    rankImage.getEndtime(),
                    rankImage.getAreaname(),String.valueOf(rankImage.getCreateuserid()),rankImage.getRanktype(),
                    rankImage.getIspublic(),String.valueOf(rankImage.getRankcateid()),
                    rankImage.getLikescore(),rankImage.getFlowerscore(),
                    rankImage.getDiamondscore(),rankImage.getCodeword(),rankImage.getPtype(),
                    rankImage.getSourcetype(),rankImage.getCompanyname(),rankImage.getCompanyphotos(),rankImage.getCompanybrief());
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
