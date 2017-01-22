package com.longbei.appservice.common.constant;

/**
 * 消息相关，模版
 * @author smkk

 */
public class Constant_Msg {
	//消息类别 －－ 
	//消息类型，0：邀请证明、1：中奖、2：献花、3：榜结束、4：点赞、5：关注、6：评论、7：收藏、8： 上榜，下榜  9：等级升级 10 :PC端删除榜及关闭榜 11：其他 12:主动证明
	public static final String MESSAGE_IMPROVE = "0";
	public static final String MESSAGE_PRIZE = "1";
	public static final String MESSAGE_FLOWER = "2";
	public static final String MESSAGE_END = "3";
	public static final String MESSAGE_LIKE = "4";
	public static final String MESSAGE_FOLLOW ="5";
	public static final String MESSAGE_COMMENT = "6";
	public static final String MESSAGE_FAVORITE = "7";
	public static final String MESSAGE_OTHER = "8";
	public static final String MESSAGE_LEVEL = "9";
	public static final String MESSAGE_DELETE_RANK = "10";
	
	
	public static String msgTitle_prove_request="证明人验证消息";
	public static String msgTemplate_prove_request="用户?希望您为进步?证明";
	
	public static String msgTitle_prove="证明人验证成功";
	public static String msgTemplate_prove="用户?已经为您的进步?证明";
	
	public static String msgTitle_award="进步中奖消息";
	public static String msgTemplate_award="您的进步?中奖啦";
	
	public static String msgTitle_flower="好友为进步献花消息";
	public static String msgTemplate_flower="用户?给您的进步?献花?朵";
	
	public static String msgTitle_addRank="上榜消息";
	public static String msgTemplate_addRank="您的进步?上榜成功，参榜的进步需符合榜规则，否则将影响您获奖！";
	
	public static String msgTitle_quitRank="下榜消息";
	public static String msgTemplate_quitRank="您的进步?已成功下榜";
	
	public static String msgTitle_levelUp="用户等级升级消息";
	public static String msgTemplate_levelUp="您等级已升级为?级";
	
	public static String msgTitle_levelDown="用户等级降级消息";
	public static String msgTemplate_levelDown="您等级已降级为?级";
	
	public static String getMsgContent(String tem,String[] param){
		String resultStr = "";
		String[] temArr = tem.split("\\?");
		if(temArr.length!=param.length+1){
//			throw new Exception("illegal param");
		}
		for (int i = 0; i < temArr.length-1; i++) {
			resultStr = resultStr + temArr[i]+param[i];
		}
		resultStr = resultStr + temArr[temArr.length-1];
		return resultStr;
	}
	
}
