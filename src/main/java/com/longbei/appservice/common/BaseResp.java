package com.longbei.appservice.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.longbei.appservice.common.constant.Constant;

public class BaseResp<T extends Object> implements Serializable {

    public static int SUCCESS = 0;
    public static int FAIlURE = -1;
    private int code;
    private T data;
    private String rtnInfo;
    private Map<String, Object> expandData = new HashMap<>();

    public BaseResp() {
    		this.code = 	Constant.STATUS_SYS_01;
    		this.rtnInfo = Constant.RTNINFO_SYS_01;
    };
    
    //成功直接返回 BaseResp.ok()
    public static BaseResp ok() {
        return new BaseResp(BaseResp.SUCCESS, "操作成功!");
    }
    //成功直接返回 BaseResp.ok(msg)
    public static BaseResp ok(String msg) {
        return new BaseResp(BaseResp.SUCCESS, msg);
    }

    //失败直接返回 BaseResp.fail()
    public static BaseResp fail() {
        return new BaseResp(BaseResp.FAIlURE, "服务器异常!");
    }

    //失败直接返回 BaseResp.fail(msg)
    public static BaseResp fail(String msg) {
        return new BaseResp(BaseResp.FAIlURE, msg);
    }
    
    public BaseResp(int code, String rtnInfo) {
        this.code = code;
        this.rtnInfo = rtnInfo;
    }
    
    public BaseResp(int code,String rtnInfo,T data){
    		this.code = code;
    		this.rtnInfo = rtnInfo;
    		this.data = data;
    }

    public BaseResp(T data) {
        this.code = SUCCESS;
        this.data = data;
    }
    @JsonInclude(Include.ALWAYS)
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    
    public BaseResp initCodeAndDesp(int code,String rtnInfo){
    		this.code = code;
    		this.rtnInfo = rtnInfo;
    		return this;
    }

	public Map<String, Object> getExpandData() {
		return expandData;
	}

	public void setExpandData(Map<String, Object> expandData) {
		this.expandData = expandData;
	}

	public String getRtnInfo() {
		return rtnInfo;
	}

	public void setRtnInfo(String rtnInfo) {
		this.rtnInfo = rtnInfo;
	}

}
