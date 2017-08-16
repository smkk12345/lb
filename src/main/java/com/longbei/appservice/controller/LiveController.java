package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.AES;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.LiveGift;
import com.longbei.appservice.entity.MediaResource;
import com.longbei.appservice.service.MediaResourceService;
import com.longbei.appservice.service.impl.LiveGiftServiceImpl;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 直播互动接口
 * Created by lixb on 2017/8/11.
 */
@RestController
@RequestMapping(value = "/live")
public class LiveController {

    private static Logger logger = LoggerFactory.getLogger(LiveController.class);

    @Autowired
    private LiveGiftServiceImpl liveGiftService;
    @Autowired
    private MediaResourceService mediaResourceService;

    /**
     *
     * @param startnum
     * @param endnum
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "giftList")
    public Map<String,String> giftList(String startnum, String endnum) {
        logger.info("giftList startNum={},endNum={}",startnum,endnum);
        BaseResp<List<LiveGift>> baseResp = new BaseResp<>();
        Map map = new HashMap();
        int startn = 0;
        if(!StringUtils.isBlank(startnum)){
            startn = Integer.parseInt(startnum);
        }
        int endn = 15;
        if(!StringUtils.isBlank(endnum)){
            endn = Integer.parseInt(endnum);
        }
        try {
            baseResp = liveGiftService.selectList(startn,endn);
        } catch (Exception e) {
            logger.error("giftList startNum={},endNum={}",startnum,endnum,e);
        }
        map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
        return map;
    }



    @SuppressWarnings("unchecked")
    @RequestMapping(value = "giveGift")
    public Map<String,String> giveGift(String giftid,
                                     String fromuid,
                                     String num,
                                     String touid,
                                     String classroomid) {
        logger.info("giveGift giftId={},fromUid={},num={},toUId={}" +
                "",giftid,fromuid,num,touid);
        Map map = new HashMap();
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(giftid,fromuid,num,touid,classroomid)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
            return map;
        }
        try {
            baseResp = liveGiftService.giveGift(
                    Long.parseLong(giftid),
                    Long.parseLong(fromuid),
                    Integer.parseInt(num),
                    Long.parseLong(touid),
                    Long.parseLong(classroomid),
                    Constant.IMPROVE_CLASSROOM_TYPE);
        } catch (Exception e) {
            logger.error("giveGift giftId={},fromUid={},num={},toUId={}",giftid,fromuid,num,touid,e);
        }
        map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
        return map;
    }
    

    /**
     * 获取资源列表
//     * @param filetype 文件类型 0.视频 1.音频 2.其他文件 3.PPT 4.图片 5.doc 6.ppt 7.excel 默认是只查询PPT
     * @param userid 用户id
//     * @param title 资源的标题
     * @return
     */
    @RequestMapping(value="getMediaResource")
    public Map<String,String> getMediaResource(Long userid,Integer pageno,Integer pagesize){
        logger.info("get mediaResource userid:{} ",userid);
        Map<String,String> map = new HashMap<>();
        BaseResp<Page<MediaResource>> baseResp = new BaseResp<Page<MediaResource>>();
        if(userid == null){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
            return map;
        }
        MediaResource mediaResource = new MediaResource();
        mediaResource.setUserid(userid);
//        if(filetype != null){
//            mediaResource.setFiletype("");
//        }else{
            mediaResource.setFiletype(3);
//        }
//        if(StringUtils.isNotEmpty(title)){
//            mediaResource.setTitle(title);
//        }
        if(pageno == null || pageno < 1){
            pageno = 1;
        }
        if(pagesize == null || pagesize < 0){
            pagesize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        baseResp = this.mediaResourceService.findMediaResourceList(mediaResource,null,pageno,pagesize);
        System.out.print(JSONObject.fromObject(baseResp).toString());
        map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
        return map;
    }

    /**
     * 获取资源详情
     * @param mediaid
     * @param userid
     * @return
     */
    @RequestMapping(value="mediaDetail")
    public Map<String,String> getMediaResourceDetailList(Integer mediaid,Long userid){
        logger.info("get mediaResource detail mediaresourceid:{} userid:{}",mediaid,userid);
        BaseResp<List<String>> baseResp = new BaseResp<>();
        Map<String,String> map = new HashMap<>();
        if(mediaid == null || userid == null){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
            return map;
        }
        baseResp = this.mediaResourceService.findMediaResourceDetailList(mediaid,userid);
        System.out.print(JSONObject.fromObject(baseResp).toString());
        map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
        return map;
    }

    /**
     * 上传图片到阿里云
     * @param mediaid
     * @param userid
     * @return
     */
    @RequestMapping(value="uploadImage")
    public Map<String,String> uploadImage(Integer mediaid,Long userid){
        logger.info("get mediaResource detail mediaresourceid:{} userid:{}",mediaid,userid);
        BaseResp<List<String>> baseResp = new BaseResp<>();
        Map<String,String> map = new HashMap<>();
        if(mediaid == null || userid == null){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
            return map;
        }
        baseResp = this.mediaResourceService.findMediaResourceDetailList(mediaid,userid);
        map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
        return map;
    }



}
