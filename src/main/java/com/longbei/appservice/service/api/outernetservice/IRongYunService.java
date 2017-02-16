package com.longbei.appservice.service.api.outernetservice;

import com.longbei.appservice.common.BaseResp;
import feign.Param;
import feign.RequestLine;

/**
 * Created by smkk on 17/2/15.
 */
public interface IRongYunService {

    @RequestLine("GET /rongyun/getRYToken?userid={userid}&username={username}&portraitUri={portraitUri}")
    BaseResp<Object> getRYToken(@Param("userid") String userid,
                                 @Param("username") String username,
                                 @Param("portraitUri") String portraitUri);
}
