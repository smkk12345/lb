package com.longbei.appservice.entity;

import java.util.Date;

public class UserSettingCommon {
    private Integer id;

    private Long userid;

    private String key;//键名称     显示首页工具栏:is_page_tool 新消息提醒    (新粉丝：is_new_fans  是否接收私信提醒:is_privat_eletter  
    						//点赞:is_like  献花:is_flower  钻石:is_diamond  评论设置:is_comment)
    						//隐私设置  (允许通过昵称搜到我:is_nick_search   允许通过此手机号搜到我:is_phone_search  
    						//允许熟人看我的微进步:is_acquaintance_look)

    private String value;//值   0:关闭  1：开启   评论设置:0:关闭  1：与我相关（好友、Like、熟人） 2：所有人

    private String displayname;//显示名称

    private Date updatetime;

    private Date createtime;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return userid 
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 
     * @param userid 
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 键名称
     * @return key 键名称
     */
    public String getKey() {
        return key;
    }

    /**
     * 键名称
     * @param key 键名称
     */
    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    /**
     * 值
     * @return value 值
     */
    public String getValue() {
        return value;
    }

    /**
     * 值
     * @param value 值
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    /**
     * 显示名称
     * @return displayname 显示名称
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * 显示名称
     * @param displayname 显示名称
     */
    public void setDisplayname(String displayname) {
        this.displayname = displayname == null ? null : displayname.trim();
    }

    /**
     * 
     * @return updatetime 
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 
     * @param updatetime 
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 
     * @return createtime 
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 
     * @param createtime 
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}