package com.longbei.appservice.service.impl;

import com.longbei.appservice.service.BaseService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * Created by wangyongzhi 17/3/7.
 */
public class BaseServiceImpl implements BaseService {
    /**
     * 打印报错日志,并回滚事务
     * @param e
     */
    void printExceptionAndRollBackTransaction(Exception e) {
        e.printStackTrace();
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    /**
     * 只是打印错误日志
     * @param e
     */
    void printException(Exception e){
        e.printStackTrace();
    }
}
