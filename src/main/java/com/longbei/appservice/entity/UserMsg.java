package com.longbei.appservice.entity;

import java.util.Date;

//import org.springframework.format.annotation.DateTimeFormat;

//import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UserMsg {
	private Integer id;

	private Long userid;

	private Long friendid;
	
	private String title; //消息标题

	private String mtype; 	//mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
												//22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
												//25:订单发货N天后自动确认收货    26：实名认证审核结果
												//27:工作认证审核结果      28：学历认证审核结果
												//29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
												//32：创建的龙榜/教室/圈子被选中推荐  34.加入的榜获奖通知---中奖消息
												//40：订单已取消 41 榜中进步下榜   
												// 42.榜单公告更新   43:后台反馈回复消息    45:榜中删除成员进步 53：被授予龙杯名人认证 54: 教室成员退出教室 55：转让群主权限 56：被授予龙杯Star认证
												// 54: 教室成员退出教室)
							//1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3  送花 4 送钻石  5:粉丝  等等)
							//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问
							//					14:发布新公告   15:获奖   16:剔除  42.榜单公告更新   17:加入请求审批结果 
							//					34.加入的榜获奖通知---获奖消息  44: 榜中成员下榜   49:发布榜单审核未通过 50:个人定制榜邀请
							// 					51.入榜审核通过 52.入榜审核不通过)

	private String msgtype; //0 聊天 1 评论 2 点赞 3  送花 4 送钻石  5:粉丝 等等
							//10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问
							//14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果 33.榜主/群主接收申请消息
							// 18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
							//22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
							//25:订单发货N天后自动确认收货    26：实名认证审核结果
							//27:工作认证审核结果      28：学历认证审核结果
							//29：被PC选为热门话题    30：被选为达人   31：微进步被推荐 47: 龙榜达人
							//32：创建的龙榜/教室/圈子被选中推荐  33.榜主/群主接收申请消息 34.加入的榜获奖通知 35.群主收到的加群申请
	  						//40：订单已取消 41 榜中进步下榜 42.榜单公告更新      43:后台反馈回复消息  44: 榜中成员下榜
							//  45:榜中删除成员进步 46 榜关闭 48:入群被拒绝     49:发布榜单审核未通过   50:个人定制榜邀请
							// 51.入榜审核通过 52.入榜审核不通过 53：被授予龙杯名人认证 54: 教室成员退出教室 55：转让群主权限 56：被授予龙杯Star认证
	private Long snsid;// 进步id

	private String remark;// 可能会有一些说明

	private String isdel;// 消息假删 0 未删 1 假删

	private String isread;// 0 未读 1 已读

	private String gtype; //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
									//10：榜中  11 圈子中  12 教室中  13:教室批复作业   14:反馈 15 关注
	
	private Long gtypeid;  //榜id  教室id 圈子id

	private Integer num;  //送花  送钻石   个数
	
	private Date createtime;

	private Date updatetime;
	
	private String commentid;  //主评论id
	
	private String commentlowerid;  //子评论id

	private String href; //连接
	
	
	//----------------扩展字段------------------------------------------
	
	private AppUserMongoEntity appUserMongoEntityUserid; //消息用户信息----Userid
	
	private AppUserMongoEntity appUserMongoEntityFriendid; //消息用户信息----Friendid
	
	private String impItype;//类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
	
	private String impBrief;//进步说明
	
	private String impPicFilekey;//图片的key----文件key  视频文件  音频文件 普通文件
	
//	private String impPickey;//图片的key
	
//	private String impkey;//文件key  视频文件  音频文件 普通文件


	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
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
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

	public AppUserMongoEntity getAppUserMongoEntityUserid() {
		return appUserMongoEntityUserid;
	}

	public void setAppUserMongoEntityUserid(AppUserMongoEntity appUserMongoEntityUserid) {
		this.appUserMongoEntityUserid = appUserMongoEntityUserid;
	}

	public AppUserMongoEntity getAppUserMongoEntityFriendid() {
		return appUserMongoEntityFriendid;
	}

	public void setAppUserMongoEntityFriendid(AppUserMongoEntity appUserMongoEntityFriendid) {
		this.appUserMongoEntityFriendid = appUserMongoEntityFriendid;
	}

	@JsonInclude(Include.ALWAYS)
	public String getImpItype() {
		return impItype;
	}

	public void setImpItype(String impItype) {
		this.impItype = impItype;
	}

	public String getImpBrief() {
		return impBrief;
	}

	public void setImpBrief(String impBrief) {
		this.impBrief = impBrief;
	}

	public String getImpPicFilekey() {
		return impPicFilekey;
	}

	public void setImpPicFilekey(String impPicFilekey) {
		this.impPicFilekey = impPicFilekey;
	}

	@JsonInclude(Include.ALWAYS)
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@JsonInclude(Include.ALWAYS)
	public Long getGtypeid() {
		return gtypeid;
	}

	public void setGtypeid(Long gtypeid) {
		this.gtypeid = gtypeid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonInclude(Include.ALWAYS)
	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

	@JsonInclude(Include.ALWAYS)
	public String getCommentlowerid() {
		return commentlowerid;
	}

	public void setCommentlowerid(String commentlowerid) {
		this.commentlowerid = commentlowerid;
	}

	//	public String getImpPickey() {
//		return impPickey;
//	}
//
//	public void setImpPickey(String impPickey) {
//		this.impPickey = impPickey;
//	}
//
//	public String getImpFilekey() {
//		return impFilekey;
//	}
//
//	public void setImpFilekey(String impFilekey) {
//		this.impFilekey = impFilekey;
//	}

}