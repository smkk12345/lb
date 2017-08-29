package com.longbei.appservice.entity;

import java.io.Serializable;

/**
 * Created by smkk on 17/2/17.
 */
public class PerfectTen implements Serializable {
    private String id;
    private String distitle;
    private Integer sort;

    public PerfectTen(String id,String distitle,Integer sort){
        super();
        this.id = id;
        this.distitle = distitle;
        this.sort = sort;
    }

    public PerfectTen(){

    };

    public void setDistitle(String distitle) {
        this.distitle = distitle;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }

    public String getDistitle() {
        return distitle;
    }

    public String getId() {
        return id;
    }

}
