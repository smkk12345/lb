package com.longbei.appservice.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;

/**
 * Created by longbei on 2016/8/12.
 */
public class LoggerUtil {
    /**
     * 获取异常信息
     * @param t  异常
     * @return
     */
    public static String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }

    public static void info(Logger logger,String format,Object...objects){
//    		if(logger.isInfoEnabled()){
//    			if(objects != null){
    				logger.info(format,objects);
//    			}else{
//    				logger.info(format);
//    			}
//    		}
    }
    
    public static void error(Logger logger,String format,Object...objects){
//		if(logger.isErrorEnabled()){
			logger.error(format,objects);
//		}
	}
    
    public static void debug(Logger logger,String format,Object...objects){
//		if(logger.isDebugEnabled()){
			logger.debug(format,objects);
//		}
	}
    
    public static void warn(Logger logger,String format,Object...objects){
//		if(logger.isWarnEnabled()){
			logger.warn(format,objects);
//		}
	}
    
}
