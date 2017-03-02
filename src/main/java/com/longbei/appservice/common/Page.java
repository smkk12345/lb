package com.longbei.appservice.common;

import com.longbei.appservice.common.constant.Constant;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luye on 2017/1/18.
 */
public class Page<T> implements Serializable{

    private static final long serialVersionUID = 5760097915453738435L;
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 每页显示个数
     */
    private int pageSize;
    /**
     * 当前页数
     */
    private int currentPage;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 结果列表
     */
    private List<T> list;

    public Page(){
        this.currentPage = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }
    public Page(int currentPage,int pageSize){
        this.currentPage=currentPage<=0?1:currentPage;
        this.pageSize=pageSize<=0?1:pageSize;
    }
    public Page(int currentPage,int pageSize,int totalCount,List<T> list){
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.list = list;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static int getDefaultPageSize() {
        return DEFAULT_PAGE_SIZE;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public static int setPageNo(int pageno,int totalCount,int pageSize){
        int totalPageNum = (totalCount  +  pageSize  - 1) / pageSize;
        if (pageno >= totalPageNum) {
            pageno = totalPageNum;
        }
        if (pageno <= 0) {
            pageno = 1;
        }
        return pageno;
    }

    public static void initPageNoAndPageSize(String pageno,String pagesize){
        if (StringUtils.isEmpty(pageno)){
            pageno = "1";
        }
        if (StringUtils.isEmpty(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
    }


}
