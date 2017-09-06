package com.longbei.appservice.service.api.staticresourceservice;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.MediaResourceDetail;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ppt
 *
 * @author luye
 * @create 2017-09-06 上午10:41
 **/
@FeignClient("StaticResourceService")
@RequestMapping("staticresourceservice")
public interface PPTServiceApi {


    /**
     * 将ppt转换成图片
     * @param pptUrl
     * @param filename
     * @param mediaResourceId
     * @return
     */
    @RequestMapping(value = "/ppt/toimage")
    BaseResp<List<MediaResourceDetail>> PPTToImage(@RequestParam("pptUrl") String pptUrl,
                                                   @RequestParam("filename") String filename,
                                                   @RequestParam("mediaResourceId") Integer mediaResourceId);

}
