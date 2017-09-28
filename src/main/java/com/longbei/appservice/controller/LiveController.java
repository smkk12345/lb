package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.service.OSSService;
import com.longbei.appservice.common.utils.AES;
import com.longbei.appservice.common.utils.DecodesUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.OssConfig;
import com.longbei.appservice.entity.LiveGift;
import com.longbei.appservice.entity.LiveInfo;
import com.longbei.appservice.entity.MediaResource;
import com.longbei.appservice.service.ClassroomCoursesService;
import com.longbei.appservice.service.ClassroomService;
import com.longbei.appservice.service.LiveGiftService;
import com.longbei.appservice.service.LiveInfoMongoService;
import com.longbei.appservice.service.MediaResourceService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 直播互动接口
 * Created by lixb on 2017/8/11.
 */
@RestController
@RequestMapping(value = "/live")
public class LiveController {

    private static Logger logger = LoggerFactory.getLogger(LiveController.class);

    @Autowired
    private LiveGiftService liveGiftService;
    @Autowired
    private MediaResourceService mediaResourceService;
    @Autowired
    private OSSService ossService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private ClassroomCoursesService classroomCoursesService;
    @Autowired
    private LiveInfoMongoService liveInfoMongoService;

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


    /**
     *
     * @param giftid
     * @param fromuid
     * @param num
     * @param touid
     * @param classroomid  直播专用 liveid
     * @return
     */
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
                    Long.parseLong(classroomid)
                    );
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
        map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
        return map;
    }

//    /**
//     * 上传图片到阿里云
//     * @param mediaid
//     * @param userid
//     * @return
//     */
//    @RequestMapping(value="uploadImage")
//    public Map<String,String> uploadImage(Integer mediaid,Long userid){
//        logger.info("get mediaResource detail mediaresourceid:{} userid:{}",mediaid,userid);
//        BaseResp<List<String>> baseResp = new BaseResp<>();
//        Map<String,String> map = new HashMap<>();
//        if(mediaid == null || userid == null){
//            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
//            map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
//            return map;
//        }
//        baseResp = this.mediaResourceService.findMediaResourceDetailList(mediaid,userid);
//        map.put("result",AES.encrypt(AES.A_KEY, JSONObject.fromObject(baseResp).toString()));
//        return map;
//    }

    /**
     * url: http://47.93.37.85:8080/app_service/live/uploadImage
//     * @param MultipartFile[] imgFiles 参数是一个文件数组
     * @return
     * {
        "expandData": {},
        "code": 0,
        "rtnInfo": "操作成功!",
        "data": [
        "http://longbei-dev-media-out.oss-cn-beijing.aliyuncs.com/9f3b985e-1f46-425c-8572-17fc5e4cdbb9",
        "http://longbei-dev-media-out.oss-cn-beijing.aliyuncs.com/14813b69-f5cf-409a-b170-d788472948d7",
        "http://longbei-dev-media-out.oss-cn-beijing.aliyuncs.com/8a262ab3-4259-4871-8d10-bddae98d9d9f"
        ],
        "displayStatus": 0
        }
     */
    @RequestMapping({ "/uploadImage" })
    @ResponseBody
    public Map<String,String> uploadImage(@RequestParam("imgFiles") MultipartFile[] files) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            List<String> list = new ArrayList<>();
            for (int i = 0; i <files.length ; i++) {
                MultipartFile file = files[i];
                String key = UUID.randomUUID().toString();
                //String bucketName, String key, MultipartFile file
                String result = ossService.putObject(OssConfig.bucketName,key,file.getInputStream());
                list.add(OssConfig.url+key);
            }
            baseResp.initCodeAndDesp();
            baseResp.setData(list);
        } catch (Exception e) {
            logger.error("uploadImage eeror and msg=",e);
            return null;
        }
        Map<String,String> map = new HashMap<>();
        String result = JSONObject.fromObject(baseResp).toString();
        map.put("result",AES.encrypt(AES.A_KEY, result));
        return map;
    }

    /**
     * url: http://47.93.37.85:8080/app_service/live/closeOnLineRoom
     * @param roomid  教室id
     * @param userid  教师id
     * @param duration 直播时长 格式化好的，小时，分钟，秒
     * @return
     */
    @RequestMapping(value="closeOnLineRoom")
    public BaseResp closeOnLine(String roomid,String courseid,String userid,String duration){
        logger.info("closeOnLineRoom roomid:{} courseid:{} userid:{} duration:{}",
                roomid,courseid,userid,duration);
        BaseResp baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(roomid,courseid,userid,duration)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        roomid = DecodesUtils.getFromBase64(roomid);
        courseid = DecodesUtils.getFromBase64(courseid);
        userid = DecodesUtils.getFromBase64(userid);
        duration = DecodesUtils.getFromBase64(duration);
        logger.info("closeOnLineRoom after getFromBase64 roomid:{} courseid={} userid:{} duration:{}",
                roomid,courseid,userid,duration);
        //处理教室直播逻辑
        return baseResp.initCodeAndDesp();
    }


    /**
     * url: http://47.93.37.85:8080/app_service/live/updateLiveMedia
     * @param uid 主播id
     * @param filekey 视频全路径
     * @param pickey 图片全路径
     * @param duration 时长 单位是 秒
     * 参数都做base64加密
     * @return
     * {
        "expandData": {},
        "code": 0,
        "rtnInfo": "操作成功!",
        "data": null,
        "displayStatus": 0
        }
     */
    @RequestMapping(value="updateLiveMedia")
    public BaseResp updateLiveMedia(String uid,String filekey,
                                    String pickey,String duration){
        logger.info("updateLiveMedia uid:{} filekey:{} pickey:{},duration={}",
                uid,filekey,pickey,duration);
        BaseResp baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(uid,filekey,duration)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        uid = DecodesUtils.getFromBase64(uid);
        filekey = DecodesUtils.getFromBase64(filekey);
        pickey = DecodesUtils.getFromBase64(pickey);
        duration = DecodesUtils.getFromBase64(duration);
        logger.info("updateLiveMedia after base64 uid:{} filekey:{} pickey:{} duration={}",
                uid,filekey,pickey,duration);
        //处理教室直播逻辑
        try{
            logger.info("updateOnlineStatusupdate satrt ------------------");
            LiveInfo liveInfo = liveInfoMongoService.selectLiveInfoByLiveid(Long.parseLong(uid));
            if(null == liveInfo){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
            }
//            classroomService.updateOnlineStatus(liveInfo.getClassroomid() + "",liveInfo.getCourseid() + "",liveInfo.getUserid() + "","2");
            Double durations = Double.parseDouble(duration)*1000;
            Integer dur = durations.intValue();
            logger.info("courseid={},roomid={},filekey={},durations={}",
                    liveInfo.getCourseid().intValue(),liveInfo.getClassroomid(),filekey,durations);
            classroomCoursesService.updateMedia(liveInfo.getCourseid().intValue(),
                    liveInfo.getClassroomid(),filekey,dur.toString());
            liveInfoMongoService.deleteLiveInfo(liveInfo.getClassroomid(), liveInfo.getCourseid());
        }catch (Exception e){
            logger.error("updateLiveMedia" ,e);
        }
        return baseResp.initCodeAndDesp();
    }

}
