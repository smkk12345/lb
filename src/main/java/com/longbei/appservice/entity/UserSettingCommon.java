package com.longbei.appservice.entity;

import java.util.Date;

public class UserSettingCommon {
    private Integer id;

    private Long userid;

    private String ukey;//键名称     显示首页工具栏:is_page_tool 新消息提醒    (新粉丝：is_new_fans 
    						//点赞:is_like  献花:is_flower  钻石:is_diamond  评论设置:is_comment(我同意接收到这些人的评论通知))
    						//隐私设置  (允许通过昵称搜到我:is_nick_search   允许通过此手机号搜到我:is_phone_search  
    						//允许熟人看我的微进步:is_acquaintance_look)

    private String uvalue;//值   0:关闭  1：开启   评论设置:0:关闭  1：与我相关（好友、Like、熟人） 2：所有人

    private String displayname;//显示名称

    private Date updatetime;

    private Date createtime;

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }

    public String getUvalue() {
        return uvalue;
    }

    public void setUvalue(String uvalue) {
        this.uvalue = uvalue;
    }

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