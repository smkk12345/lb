package com.longbei.appservice.entity;

/**
 * Created by lixb on 2017/3/18.
 */
public class SysPerfectTag {

    private Integer id;

    private String tag;//十全十美  分类名字

    private String ptype;//图片

    public Integer getId() {
        return id;
    }

    public String getPtype() {
        return ptype;
    }

    public String getTag() {
        return tag;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
