package com.longbei.appservice.controller.api;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * Created by wangyongzhi 17/4/19.
 */
@ControllerAdvice(basePackages = "com.longbei.appservice.controller.api")
public class JSONPBaseController extends AbstractJsonpResponseBodyAdvice {

    public JSONPBaseController() {
        super("callback","jsonp");
    }
}
