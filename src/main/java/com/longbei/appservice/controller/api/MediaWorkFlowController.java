package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.MediaWorkFlowService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.*;

/**
 * Created by wangyongzhi 17/7/14.
 */
@Controller
@RequestMapping(value="api/media")
public class MediaWorkFlowController {
    private static PublicKey public_key=null;

    @Autowired
    private MediaWorkFlowService mediaWorkFlowService;

    private static Logger logger = LoggerFactory.getLogger(MediaWorkFlowController.class);

    @RequestMapping(value="transcodingNotice")
    public void transcodingNotice(HttpServletRequest request, @RequestBody String requestBody, HttpServletResponse response){
        logger.info("transcodingNotice call and requestBoby={}",requestBody);
        if(StringUtils.isEmpty(requestBody)){
            response.setStatus(500);
            return ;
        }
        if(!authenticate(request)){//权限验证 校验是否是阿里云的请求
            response.setStatus(403);
            return;
        }

        JSONObject bodyJson = JSONObject.fromObject(requestBody);
        System.out.println(bodyJson);
        this.mediaWorkFlowService.handleMediaTranscodingNotice(bodyJson,response);
    }

    public static Boolean authenticate(HttpServletRequest request){
        String method = request.getMethod().toUpperCase(Locale.ENGLISH);
        if (!"GET".equals(method) && !method.equals("HEAD") && !method.equals("POST")) {
            return false;
        }
        String uri = request.getRequestURI();

        Map<String,String> headerMap = new HashMap<String,String>();
        Enumeration<String> enum1 = request.getHeaderNames();
        while(enum1.hasMoreElements()){
            String header = enum1.nextElement();
            headerMap.put(header,request.getHeader(header));
        }

        String sign_cert_url = headerMap.get("x-mns-signing-cert-url");
        String cert_url = new String(Base64.decode(sign_cert_url.getBytes()));
        try {
            if(public_key == null){
                URL url = new URL(cert_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                DataInputStream in = new DataInputStream(conn.getInputStream());
                CertificateFactory cf = CertificateFactory.getInstance("X.509");

                Certificate c = cf.generateCertificate(in);
                public_key = c.getPublicKey();
            }

            String str2sign = MediaWorkFlowController.getSignStr(method, uri, headerMap);

            java.security.Signature signetcheck = java.security.Signature.getInstance("SHA1withRSA");
            signetcheck.initVerify(public_key);
            signetcheck.update(str2sign.getBytes());

            String signature = headerMap.get("authorization");
            byte[] decodedSign = org.apache.commons.codec.binary.Base64.decodeBase64(signature);
            return signetcheck.verify(decodedSign);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * build string for sign
     * @param method, http method
     * @param uri, http uri
     * @param headers, http headers
     * @return String fro sign
     */
    public static String getSignStr(String method, String uri, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        sb.append("\n");
        sb.append(safeGetHeader(headers, "content-md5"));
        sb.append("\n");
        sb.append(safeGetHeader(headers, "content-type"));
        sb.append("\n");
        sb.append(safeGetHeader(headers, "date"));
        sb.append("\n");

        List<String> tmp = new ArrayList<String>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey().startsWith("x-mns-"))
                tmp.add(entry.getKey() + ":" + entry.getValue());
        }
        Collections.sort(tmp);

        for (String kv : tmp) {
            sb.append(kv);
            sb.append("\n");
        }

        sb.append(uri);
        return sb.toString();
    }

    private static String safeGetHeader(Map<String, String> headers, String name) {
        if (headers.containsKey(name))
            return headers.get(name);
        else
            return "";
    }
}
