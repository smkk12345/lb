package com.longbei.appservice.common.constant;

/**
 * 全局常量类
 */
public class Constant {



	public static final int FLOWER_PRICE = 10; //鲜花和龙币兑换价格
	public static final int DIAMOND_PRICE = 100; //钻石和龙币兑换价格


	public static final String TOKEN_SIGN_USER = "appservice&token";

	public static final String []OK_SERVICE = new String[]{
			"adminservice"
	};


	/**
	 * OSS属性  
	 */
	public static final String OSS_IMG = "https://my-first-oss-bucket-test.oss-cn-beijing.aliyuncs.com/";
	public static final String OSS_IMGSTYLE_300_300 = "?x-oss-process=image/resize,m_fill,w_300,h_300,limit_0/auto-orient,0/quality,q_85/watermark,text_6b6Z5p2v,type_d3F5LXplbmhlaQ,size_12,t_100,color_e7bc5d,g_se,y_10,x_10";//缩略图默认大小  _2o.jpg
	public static final String OSS_CDN = "https://my-first-oss-bucket-test.oss-cn-beijing.aliyuncs.com/";//图片路径 ＋／imgName。png
	public static final String OSS_MEDIA = "http://longbei3-mp4-out.oss-cn-shanghai.aliyuncs.com/";
	public static final String WORKFLOW1 = "longbei3-mp3";
	public static final String WORKFLOW2 = "longbei3-mp4";

	public static final String DEFAULT_START_NO = "0";   //默认分页开始条数
	public static final String DEFAULT_PAGE_SIZE = "15"; //默认分页时每页显示条数




	/**
	 * 服务端返回代码  code  
	 */
	public static final int STATUS_SYS_00 = 0;
	public static final int STATUS_SYS_01 = -1;
	public static final int STATUS_SYS_02 = -2;
	public static final int STATUS_SYS_03 = -3;
	public static final int STATUS_SYS_04 = -4;
	public static final int STATUS_SYS_05 = -5;
	public static final int STATUS_SYS_06 = -6;
	public static final int STATUS_SYS_07 = -7;
	public static final int STATUS_SYS_08 = -8;
	public static final int STATUS_SYS_09 = -9;
	public static final int STATUS_SYS_10 = -10;
	public static final int STATUS_SYS_11 = -11;
	public static final int STATUS_SYS_12 = -12;
	public static final int STATUS_SYS_13 = -13;
	
	public static final int STATUS_SYS_15 = -15;
	public static final int STATUS_SYS_16 = -16;
	
	public static final int STATUS_SYS_20 = -20;
	public static final int STATUS_SYS_21 = -21;
	public static final int STATUS_SYS_22 = -22;
	
	public static final int STATUS_SYS_26 = -26;
	public static final int STATUS_SYS_27 = -27;
	public static final int STATUS_SYS_28 = -28;
	public static final int STATUS_SYS_29 = -29;
	public static final int STATUS_SYS_30 = -30;
	public static final int STATUS_SYS_31 = -31;

	public static final int STATUS_SYS_40 = -40;
	public static final int STATUS_SYS_41 = -41;
	public static final int STATUS_SYS_42 = -42;
	public static final int STATUS_SYS_43 = -43;
	public static final int STATUS_SYS_44 = -44;
	public static final int STATUS_SYS_45 = -45;
	public static final int STATUS_SYS_46 = -46;
	public static final int STATUS_SYS_47 = -47;
	public static final int STATUS_SYS_48 = -48;
	public static final int STATUS_SYS_49 = -49;

	public static final int STATUS_SYS_60 = -60;
	public static final int STATUS_SYS_61 = -61;
	public static final int STATUS_SYS_62 = -62;
	public static final int STATUS_SYS_63 = -63;
	public static final int STATUS_SYS_64 = -64;


	public static final int STATUS_SYS_1000 = -1000;
	public static final int STATUS_SYS_1001 = -1001;
	public static final int STATUS_SYS_1002 = -1002;
	
	
	
	/**
	 * 服务端返回描述
	 */
	//1-19  lxb
	public static final String RTNINFO_SYS_00 = "系统正常";
	public static final String RTNINFO_SYS_01 = "系统异常";
	public static final String RTNINFO_SYS_02 = "您已注册，请您直接登录";
	public static final String RTNINFO_SYS_03 = "密码错误";
	public static final String RTNINFO_SYS_04 = "用户未注册";
	public static final String RTNINFO_SYS_05 = "验证码失效";
	public static final String RTNINFO_SYS_06 = "验证码错误";
	public static final String RTNINFO_SYS_07 = "参数错误";
	public static final String RTNINFO_SYS_08 = "帐号已经过期，请重新登录";
	public static final String RTNINFO_SYS_09 = "手机时间异常，请调整到标准北京时间";
	public static final String RTNINFO_SYS_10 = "系统检测到您切换设备登录，请验证";
	public static final String RTNINFO_SYS_11 = "您已绑定其他帐号";
	public static final String RTNINFO_SYS_12 = "密码和验证码均错误";
	public static final String RTNINFO_SYS_13 = "不能给自己点赞";
	
	public static final String RTNINFO_SYS_15 = "邀请人不存在";
	public static final String RTNINFO_SYS_16 = "昵称已存在";
	
	//20-39  yxc
	public static final String RTNINFO_SYS_20 = "暂无收货地址，请添加";
	public static final String RTNINFO_SYS_21 = "暂无评论信息";
	public static final String RTNINFO_SYS_22 = "该评论已点赞";
	
	public static final String RTNINFO_SYS_26 = "暂无好友信息";
	public static final String RTNINFO_SYS_27 = "暂无搜索信息";
	public static final String RTNINFO_SYS_28 = "暂无消息记录";
	public static final String RTNINFO_SYS_29 = "暂无签到记录";
	public static final String RTNINFO_SYS_30 = "用户已签到";
	public static final String RTNINFO_SYS_31 = "用户签到成功";

	//40-60 luy
	public static final String RTNINFO_SYS_40 = "请填写进步内容";
	public static final String RTNINFO_SYS_41 = "发布进步成功";
	public static final String RTNINFO_SYS_42 = "发布进步失败";
	public static final String RTNINFO_SYS_43 = "获取进步列表失败";
	public static final String RTNINFO_SYS_44 = "获取进步列表成功";
	public static final String RTNINFO_SYS_45 = "你没有为该进步点过赞";
	public static final String RTNINFO_SYS_48 = "送花失败，龙币扣除";
	public static final String RTNINFO_SYS_49 = "送钻失败，龙币扣除";


	public static final String RTNINFO_SYS_60 = "榜单没有审核，或审核不通过";
	public static final String RTNINFO_SYS_61 = "送钻失败，龙币扣除";
	public static final String RTNINFO_SYS_62 = "送钻失败，龙币扣除";
	public static final String RTNINFO_SYS_63 = "送钻失败，龙币扣除";
	
	public static final String RTNINFO_SYS_1000 = "未获取授权信息";
	public static final String RTNINFO_SYS_1001 = "token错误";
	public static final String RTNINFO_SYS_1002 = "token失效";



	public static final String RTNINFO_SYS_50 = "添加榜单成功";
	public static final String RTNINFO_SYS_51 = "添加榜单失败";
	public static final String RTNINFO_SYS_52 = "修改榜单成功";
	public static final String RTNINFO_SYS_53 = "修改榜单失败";






	//新浪提供的短连接
	public static final String WB_SHORTURL="http://api.t.sina.com.cn/short_url/shorten.json?source=3271760578&url_long=";
	
	/**
	 * api调用服务器名称
	 */
	public static final String SERVER_USER_SERVICE = "userservice";
	public static final String SERVER_PRODUCT_SERVICE = "productservice";
	public static final String SERVER_APP_SERVICE = "appservice";
	
	/**
	 * 移动端调用api的token失效
	 */
	public static final int APP_TOKEN_EXPIRE = 30*24*60*60;
	
	public static final String NOT_NEED_SECURITY_FILTER_URL_ARR = "/user/login,/user/sms,/user/registerbasic,/user/thirdlogin,/user/thirdregister,/user/checkSms,/init";

	/**
	 * 进步相关配置
	 */

	public static final String  IMPROVE_SINGLE_TYPE = "0";    //独立进步
	public static final String  IMPROVE_GOAL_TYPE = "1";     //目标
	public static final String  IMPROVE_RANK_TYPE = "2";      //榜
	public static final String  IMPROVE_CIRCLE_TYPE = "3";    //圈子
	public static final String  IMPROVE_CLASSROOM_TYPE = "4"; //教室

	/**
	 * 进步列表过滤
	 */
	public static final String IMPROVE_LIST_ALL = "0";  //全部
	public static final String IMPROVE_LIST_FANS = "1";  //关注
	public static final String IMPROVE_LIST_FRIEND = "2";  //好友
	public static final String IMPROVE_LIST_ACQUAINTANCE = "3";  //熟人

	/**
	 * 进步明细类型（赞，花，钻）
	 */
	public static final String IMPROVE_ALL_DETAIL_LIKE = "0";  //赞的明细
	public static final String IMPROVE_ALL_DETAIL_FLOWER = "1";  //花的明细
	public static final String IMPROVE_ALL_DETAIL_DIAMOND = "2";  //钻明细
	public static final String IMPROVE_LIKE_ADD = "0"; //点赞
	public static final String IMPROVE_LIKE_CANCEL = "1"; //取消赞

	public static final Integer DEFAULT_IMPROVE_ALL_DETAIL_LIST_NUM = 10;


	/**
	 * 进步时间线类型
	 */
	public static final String SQUARE_USER_ID = "0";  //系统用户id
	/**
	 * 广场
	 */
	public static final String TIMELINE_IMPROVE_SQUARE = "0";
	/**
	 * 我的
	 */
	public static final String TIMELINE_IMPROVE_SELF = "1";
	/**
	 * 好友，关注，熟人
	 */
	public static final String TIMELINE_IMPROVE_ALL = "2";
	/**
	 * 好友
	 */
	public static final String TIMELINE_IMPROVE_FRIEND = "3";
	/**
	 * 关注
	 */
	public static final String TIMELINE_IMPROVE_ATTR = "4";
	/**
	 * 熟人
	 */
	public static final String TIMELINE_IMPROVE_ACQ = "5";
	
	
	/**
	 * 进步币添加来源   0:签到   1:发进步
	 */
	public static final String USER_IMP_COIN_CHECK = "0";  //签到


	/**
	 * 榜单相关配置
	 */


	/**
	 * 榜单状态
	 */
	public static final String RANKIMAGE_STATUS_0 = "0";  //草稿
	public static final String RANKIMAGE_STATUS_1 = "1";  //审核中
	public static final String RANKIMAGE_STATUS_2 = "2";  //审核不通过 可以修改
	public static final String RANKIMAGE_STATUS_3 = "3";  //神格不通过 不可以修改
	public static final String RANKIMAGE_STATUS_4 = "4";  //审核通过

	
	/**
	 * 评论相关配置    itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
	 */
	public static final String  COMMENT_SINGLE_TYPE = "0";    //进步(零散)评论
	public static final String  COMMENT_GOAL_TYPE = "1";      //目标进步评论
	public static final String  COMMENT_RANK_TYPE = "2";      //榜评论
	public static final String  COMMENT_CIRCLE_TYPE = "3";    //圈子评论
	public static final String  COMMENT_CLASSROOM_TYPE = "4"; //教室微进步评论
	
	/**
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * 
     * mtype  0 系统消息    1 对话消息    2:@我消息
	 */
	public static final String  MSG_SYSTEM_TYPE = "0";     //系统消息
	public static final String  MSG_DIALOGUE_TYPE = "1";   //对话消息
	public static final String  MSG_OTHER_TYPE = "2";      //@我消息
	
	/**
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * 
     * msgtype
	 */
	public static final String  MSG_CHAT_TYPE = "0";     //聊天
	public static final String  MSG_COMMENT_TYPE = "1";  //评论
	public static final String  MSG_LIKE_TYPE = "2";     //点赞
	public static final String  MSG_FLOWER_TYPE = "3";   //送花
	public static final String  MSG_DIAMOND_TYPE = "4";  //送钻石
	public static final String  MSG_FANS_TYPE = "5";     //粉丝
	
	public static final String  MSG_INVITE_TYPE = "10";     //邀请
	public static final String  MSG_APPLY_TYPE = "11";      //申请加入特定圈子
	public static final String  MSG_GIVE_TYPE = "12";       //老师批复作业
	public static final String  MSG_REVERT_TYPE = "13";     //老师回复提问 
	public static final String  MSG_PUBLISH_TYPE = "14";    //发布新公告
	public static final String  MSG_AWARD_TYPE = "15";      //获奖
	public static final String  MSG_DELETE_TYPE = "16";     //剔除
	public static final String  MSG_CHECK_TYPE = "17";      //加入请求审批结果
	
	
	/**
	 * mtype   2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * 
     * 消息展示模版
	 */
	public static final String  MSG_INVITE_MODEL = "邀请您加入m'n'";       	  			//邀请    m:榜，圈子，教室     n:名称
	public static final String  MSG_APPLY_MODEL = "申请加入您创建的m'n'";         			//申请加入特定榜，圈子，教室     m:榜，圈子，教室     n:名称   
	public static final String  MSG_GIVE_MODEL = "批复了您在教室'n'上传的作业";  	  			//老师批复作业
	public static final String  MSG_REVERT_MODEL = "回答了您在教室'n'中的提问";    			//老师回复提问 
	public static final String  MSG_PUBLISH_MODEL = "在m'n'中@了您";  		 			//发布新公告          m:榜，圈子，教室     n:名称   
	public static final String  MSG_AWARD_MODEL = "恭喜您在龙榜'n'中获得了m,快去领奖吧!";  		//获奖         m:奖品     n:榜名称 
	public static final String  MSG_DELETE_MODEL = "由于您在加入'n'mg";  					//剔除         m:榜，圈子，教室     n:名称    g:投诉理由
	public static final String  MSG_CHECK_MODEL = "您加入m'n'的申请已被g,快去发第一条进步吧";  	//加入请求审批结果      m:榜，圈子，教室     n:名称   g:结果:通过或拒绝     
	
	
	/*
	 * 龙杯公司   推送消息
	 * 
     * 模版
	 */
	public static final String  MSG_LONGBEI_NICKNAME = "龙杯公司";
	public static final String  MSG_LONGBEI_DIFAULT_AVATAR = "";   //龙杯公司默认URL
	
	
	/*
	 * mtype 0 系统消息(通知消息.进步消息等) 
     * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石  5:粉丝  等等)
     * 消息展示模版
	 */
	public static final String  MSG_FLOWER_MODEL = "给这条微进步献了n朵花"; 		//进步送花消息模板
	public static final String  MSG_LIKE_MODEL = "赞了这条微进步"; 				//进步点赞消息模板
	public static final String  MSG_DIAMOND_MODEL = "给这条微进步赞赏了n颗砖石"; 	//进步送钻石消息模板

	/**
	 * activemq
	 */
	public static final String  QUEUE_ADD_NAME = "IMPROVECIRCLE_ADD";
	public static final String  QUEUE_UPDATE_NAME = "IMPROVECIRCLE_UPDATE";
	public static final String  TOPIC_NAME = "TOPIC_OPT";


	public static final String TOKEN_SIGN_COMMON = "commonservice&token";


	/**
	 * redis中缓存的过期时间
	 */
	public static final long EXPIRE_USER_RANDOMCODE = 2*60;//用户发送验证码过期时间
	public static final long EXPIRE_USER_TOKEN = 0;//用户登录token过期时间
	public static final long EXPIRE_USER_MAILCODE = 0;//邮箱验证码过期时间
	/**
	 * redis 缓存时间 单位秒
	 */
	public static final long CACHE_24X60X60 = 24*3600;
	public static final long CACHE_24X60X60X2 = 24*3600*2;

	/**
	 * 每日可操作参数    签到   发进步  点赞 ...
	 */
	public static final String PERDAY_CHECK_IN = "checkIn";//签到
	public static final String PERDAY_ADD_IMPROVE = "addImprove";//发进步
	public static final String PERDAY_ADD_LIKE = "addLike";//点赞

	/**
	 * 缓存一日数据
	 */
	public static final String PERDAY_POINT = "point";
	public static final String DAILY_SHARE_LIMIT = "DAILY_SHARE_LIMIT";
	//-------------各种缓存前缀  improve  rank user 等------------------//
	/**
	 * redis中各种请求前缀   redis_prefix
	 */
	public static final String RP_USER_PERDAY = "user_perday_";
	
	/**
	 * 签到连续天数     缓存到截止两天
	 */
	public static final String RP_USER_CHECK = "user_check_";
	public static final String RP_USER_CHECK_DATE = "user_check_date_";
	public static final String RP_USER_CHECK_VALUE = "user_check_value_";
	

	/**
	 * 目标中每日进步 缓存到截止两天  榜单中进步  缓存一天
	 */
	public static final String RP_IMPROVE_NDAY = "improve_day_";



	public static final String REDIS_IMPROVE_LFD = "improve_lfd_"; //进步的 赞，花，钻石
	public static final String MONGO_IMPROVE_LFD_OPT_LIKE = "0";   //点赞
	public static final String MONGO_IMPROVE_LFD_OPT_FLOWER = "1";  //送花
	public static final String MONGO_IMPROVE_LFD_OPT_DIAMOND = "2";  //送钻

}