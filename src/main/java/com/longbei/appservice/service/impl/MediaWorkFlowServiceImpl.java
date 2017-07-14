package com.longbei.appservice.service.impl;

import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.mts.model.v20140618.QueryMediaListRequest;
import com.aliyuncs.profile.DefaultProfile;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.HttpUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.MediaWorkFlowService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyongzhi 17/7/14.
 */
@Service("mediaWorkFlowService")
public class MediaWorkFlowServiceImpl implements MediaWorkFlowService {
    private static String imageInfo="?x-oss-process=image/info";
    protected static DefaultAcsClient defaultAcsClient;

    @Autowired
    private ImproveService improveService;
    @Autowired
    private SpringJedisDao springJedisDao;

    /**
     * 处理转码结果通知
     * @param bodyJson
     */
    @Override
    public void handleMediaTranscodingNotice(JSONObject bodyJson, HttpServletResponse response) {
        JSONObject message = JSONObject.fromObject(bodyJson.getString("Message"));
        if(!message.get("Type").equals("Report") || !message.get("State").equals("Success")){
            return ;
        }
        String run_id = message.getString("RunId");
        Map<String,String> messageMap = new HashMap<String,String>();
        JSONObject mediaWorkflowExecution = message.getJSONObject("MediaWorkflowExecution");
        String workFlow = mediaWorkflowExecution.getString("Name");
        if(workFlow.contains("mp3")){
            messageMap = getObjectMapForMp3(message);
        }else{
            messageMap = getObjectMap(message);
        }

        if(!updateRemoteStatus(messageMap)){
            long num = this.springJedisDao.increment("media_notice_error_"+run_id,1);
            if(num > 5){
                response.setStatus(200);
                return;
            }
            response.setStatus(500);
        }else{
            response.setStatus(200);
        }
    }

    private boolean updateRemoteStatus(Map<String, String> map) {
        boolean result = false;
        String key = map.get("key");
        String url = map.get("url");
        String workflow = map.get("workflow");
        String picurl = map.get("pickey");
        String picAttribute = null;
        if(StringUtils.isNotBlank(picurl)){
            picAttribute = getPicInfo(picurl);
        }
        String duration = map.get("duration");
        BaseResp baseResp = improveService.updateMedia(key,picurl,url,workflow,duration,picAttribute);
        if(baseResp.getCode() == Constant.STATUS_SYS_00){
            result = true;
        }
        return  result;

    }

    //ActivityList
    public static Map<String,String> getObjectMap(JSONObject json){
        if(json.get("Type").equals("Report")&&json.get("State").equals("Success")){
            Map<String,String> map = new HashMap<>();

            JSONObject mediaWorkflowExecution = json.getJSONObject("MediaWorkflowExecution");
            String name = (String)mediaWorkflowExecution.get("Name");
            //通过工作流名称 判断走mp3 还是 mp4
            map.put("workflow",name);

            JSONObject jsonObject2 = mediaWorkflowExecution.getJSONObject("Input").getJSONObject("InputFile");
            String objId = jsonObject2.getString("Object");
            String[] objArr = objId.split("/");
            objId = objArr[objArr.length-1];
            map.put("key",objId);
            JSONArray activityList = mediaWorkflowExecution.getJSONArray("ActivityList");
            //获取截图和文件
            for (int i = 0; i < activityList.size(); i++) {
                JSONObject j = JSONObject.fromObject(activityList.get(i));
                if("Snapshot".equals(j.get("Type"))){
                    map.put("pickey",getPicKey(j,objId));
                    continue;
                }
                if("Transcode".equals(j.get("Type"))){
                    map.put("url",getFileKey(j,objId));
                    continue;
                }
            }
            return map;
        }
        return null;
    }

    private static String getFileKey(JSONObject js, String objid){
        String runid = (String)js.get("RunId");
        String result = "filekey/"+runid+"/"+ AppserviceConfig.alimedia_mp4_query_mp4+"/"+objid;
        return result;
    }

    private static String getPicKey(JSONObject js, String objid){
        String runid = (String)js.get("RunId");
        return "pickey/" + runid + ".jpg";
    }

    private static String getPicInfo(String picKey){
        String url = AppserviceConfig.oss_media+ picKey +imageInfo;
        String sRequest = HttpUtils.getRequest(url,null);
        if(StringUtils.isBlank(sRequest)){
            return null;
        }
        JSONObject jsonObject = JSONObject.fromObject(sRequest);
        if(null != jsonObject){
            JSONArray jsonArray = new JSONArray();
            JSONObject reJs = new JSONObject();
            JSONObject jHeight = JSONObject.fromObject(jsonObject.get("ImageHeight"));
            JSONObject jWidth = JSONObject.fromObject(jsonObject.get("ImageWidth"));
            reJs.put("height",jHeight.get("value"));
            reJs.put("width",jWidth.get("value"));
            jsonArray.add(reJs);
            return jsonArray.toString();
        }
        return null;
    }

    /**
     * mp3 视频转码
     * @param json
     * @return
     */
    private static Map<String,String> getObjectMapForMp3(JSONObject json){
        Map<String,String> map = new HashMap<>();

        JSONObject mediaWorkflowExecution = json.getJSONObject("MediaWorkflowExecution");
        String name = (String)mediaWorkflowExecution.get("Name");
        String mediaId = (String)mediaWorkflowExecution.get("MediaId");
        map.put("workflow",name);

        JSONObject jsonObject2 = mediaWorkflowExecution.getJSONObject("Input").getJSONObject("InputFile");
        String objId = jsonObject2.getString("Object");
        String[] objArr = objId.split("/");
        objId = objArr[objArr.length-1];
        map.put("key",objId);
        JSONArray activityList = mediaWorkflowExecution.getJSONArray("ActivityList");
        //获取文件
        for (int i = 0; i < activityList.size(); i++) {
            JSONObject j = JSONObject.fromObject(activityList.get(i));
            if(j.get("State").equals("Success")){
                map.put("url",getFileForMp3(j,objId));
                map.put("duration",getTimeStr(mediaId));
//                JSONObject jsonObject = JSONObject.fromObject(map);
                break;
            }
        }
        return map;
    }

    private static String getFileForMp3(JSONObject js,String objid){
        String runid = (String)js.get("RunId");
        String result = "mp3key/"+runid+"/"+AppserviceConfig.alimedia_mp3_query_mp3+"/"+objid;
        return  result;
    }

    private static String getTimeStr(String mediaids) {
        try{
            QueryMediaListRequest qu = new QueryMediaListRequest();
            qu.setMediaIds(mediaids);
            qu.setAcceptFormat(FormatType.JSON);
            AcsResponse ac = getDefaultAcsClient().getAcsResponse(qu);
            JSONObject js = JSONObject.fromObject(ac);
            JSONArray jsArray = js.getJSONArray("mediaList");
            if(null != jsArray && jsArray.size()>0){
                JSONObject j = JSONObject.fromObject(jsArray.get(0));
                return (String)j.get("duration");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static DefaultAcsClient getDefaultAcsClient(){
        try{
            if(null == defaultAcsClient){
                DefaultProfile.addEndpoint("cn-"+AppserviceConfig.alimedia_area,
                        "cn-"+AppserviceConfig.alimedia_area,
                        "Mts",
                        "mts.cn-"+AppserviceConfig.alimedia_area+".aliyuncs.com");
                DefaultProfile profile = DefaultProfile.getProfile(
                        "cn-"+AppserviceConfig.alimedia_area,
                        AppserviceConfig.alimedia_accessKeyId,
                        AppserviceConfig.alimedia_accessKeySecret);
                defaultAcsClient = new DefaultAcsClient(profile);
            }
        }catch (Exception e){
        }
        return defaultAcsClient;
    }
}