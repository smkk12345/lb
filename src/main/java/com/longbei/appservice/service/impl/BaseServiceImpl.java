package com.longbei.appservice.service.impl;

import com.longbei.appservice.service.BaseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * Created by wangyongzhi 17/3/7.
 */
public class BaseServiceImpl implements BaseService {
	private static Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);
    /**
     * 打印报错日志,并回滚事务
     * @param e
     */
    void printExceptionAndRollBackTransaction(Exception e) {
    	//TODO 这种方式打印日志是无法追踪到的
    	logger.error("iprintExceptionAndRollBackTransaction ", e);
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    /**
     * 只是打印错误日志
     * @param e
     */
    void printException(Exception e){
    	//TODO 这种方式打印日志是无法追踪到的
    	logger.error("printException ", e);
    }
}
