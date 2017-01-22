package com.longbei.appservice.entity;

import java.util.Date;

public class SnsChatDetail {
    private Integer id;

    private Long fuserid;//发起人用户id

    private Long tuserid;//接受人用户id（可能食群id）

    private String chattype;//聊天类型 0—普通。1—群

    private String chatcontent;//聊天内容

    private Date createtime;//创建时间

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
     * 发起人用户id
     * @return fuserid 发起人用户id
     */
    public Long getFuserid() {
        return fuserid;
    }

    /**
     * 发起人用户id
     * @param fuserid 发起人用户id
     */
    public void setFuserid(Long fuserid) {
        this.fuserid = fuserid;
    }

    /**
     * 接受人用户id（可能食群id）
     * @return tuserid 接受人用户id（可能食群id）
     */
    public Long getTuserid() {
        return tuserid;
    }

    /**
     * 接受人用户id（可能食群id）
     * @param tuserid 接受人用户id（可能食群id）
     */
    public void setTuserid(Long tuserid) {
        this.tuserid = tuserid;
    }

    /**
     * 聊天类型 0—普通。1—群
     * @return chattype 聊天类型 0—普通。1—群
     */
    public String getChattype() {
        return chattype;
    }

    /**
     * 聊天类型 0—普通。1—群
     * @param chattype 聊天类型 0—普通。1—群
     */
    public void setChattype(String chattype) {
        this.chattype = chattype == null ? null : chattype.trim();
    }

    /**
     * 聊天内容
     * @return chatcontent 聊天内容
     */
    public String getChatcontent() {
        return chatcontent;
    }

    /**
     * 聊天内容
     * @param chatcontent 聊天内容
     */
    public void setChatcontent(String chatcontent) {
        this.chatcontent = chatcontent == null ? null : chatcontent.trim();
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}