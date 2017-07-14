package com.longbei.appservice.service;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by wangyongzhi 17/7/14.
 */
public interface MediaWorkFlowService {

    /**
     * 处理转码结果通知
     * @param bodyJson
     */
    void handleMediaTranscodingNotice(JSONObject bodyJson, HttpServletResponse response);
}
