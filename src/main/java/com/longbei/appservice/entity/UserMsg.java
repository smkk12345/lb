package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UserMsg {
	private Integer id;

	private Long userid;

	private Long friendid;

	private String msgtype;// 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等

	private Long snsid;// 业务id

	private String remark;// 可能会有一些说明

	private String isdel;// 消息假删 0 未删 1 假删

	private String isread;// 0 未读 1 已读

	private String gtype; // 0 零散 1 目标中 2 榜中 3圈子中 4 教室中

	private String mtype; // 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3
							// 送花 4 送钻石 等等)

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
	@JsonInclude(Include.ALWAYS)
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
	 * 
	 * @return friendid
	 */
	@JsonInclude(Include.ALWAYS)
	public Long getFriendid() {
		return friendid;
	}

	/**
	 * 
	 * @param friendid
	 */
	public void setFriendid(Long friendid) {
		this.friendid = friendid;
	}

	/**
	 * 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
	 * 
	 * @return msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
	 */
	@JsonInclude(Include.ALWAYS)
	public String getMsgtype() {
		return msgtype;
	}

	/**
	 * 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
	 * 
	 * @param msgtype
	 *            0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
	 */
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype == null ? null : msgtype.trim();
	}

	/**
	 * 业务id
	 * 
	 * @return snsid 业务id
	 */
	@JsonInclude(Include.ALWAYS)
	public Long getSnsid() {
		return snsid;
	}

	/**
	 * 业务id
	 * 
	 * @param snsid
	 *            业务id
	 */
	public void setSnsid(Long snsid) {
		this.snsid = snsid;
	}

	/**
	 * 可能会有一些说明
	 * 
	 * @return remark 可能会有一些说明
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 可能会有一些说明
	 * 
	 * @param remark
	 *            可能会有一些说明
	 */
	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	/**
	 * 消息假删 0 未删 1 假删
	 * 
	 * @return isdel 消息假删 0 未删 1 假删
	 */
	@JsonInclude(Include.ALWAYS)
	public String getIsdel() {
		return isdel;
	}

	/**
	 * 消息假删 0 未删 1 假删
	 * 
	 * @param isdel
	 *            消息假删 0 未删 1 假删
	 */
	public void setIsdel(String isdel) {
		this.isdel = isdel == null ? null : isdel.trim();
	}

	/**
	 * 0 未读 1 已读
	 * 
	 * @return isread 0 未读 1 已读
	 */
	@JsonInclude(Include.ALWAYS)
	public String getIsread() {
		return isread;
	}

	/**
	 * 0 未读 1 已读
	 * 
	 * @param isread
	 *            0 未读 1 已读
	 */
	public void setIsread(String isread) {
		this.isread = isread == null ? null : isread.trim();
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

	@JsonInclude(Include.ALWAYS)
	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	@JsonInclude(Include.ALWAYS)
	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	
}