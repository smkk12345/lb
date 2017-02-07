package com.longbei.appservice.common.constant;

/**
 * 全局常量类
 */
public class Constant {
	/**
	 * OSS属性  
	 */
//	public static final String OSS_IMG = "http://my-first-oss-bucket-test.img-cn-beijing.aliyuncs.com/";//缩略图  OSS_IMG+OSS_IMGSTYLE
	public static final String OSS_IMG = "https://my-first-oss-bucket-test.oss-cn-beijing.aliyuncs.com/";
	public static final String OSS_BUCKET = "my-first-oss-bucket-test";//bucket参数
	public static final String OSS_IMGSTYLE = "@498w_498h_1o";//缩略图默认大小
//	public static final String OSS_IMGSTYLE_300_300 = "@300w_300h_1o";//缩略图默认大小  _2o.jpg
	public static final String OSS_IMGSTYLE_300_300 = "?x-oss-process=image/resize,m_fill,w_300,h_300,limit_0/auto-orient,0/quality,q_85/watermark,text_6b6Z5p2v,type_d3F5LXplbmhlaQ,size_12,t_100,color_e7bc5d,g_se,y_10,x_10";//缩略图默认大小  _2o.jpg
	public static final String OSS_CDN = "https://my-first-oss-bucket-test.oss-cn-beijing.aliyuncs.com/";//图片路径 ＋／imgName。png
//	public static final String OSS_CDN = "https://pic1.longbeidata.com/";//图片路径 ＋／imgName。png
	public static final String OSS_CDN_HTTP = "http://pic1.longbeidata.com/";



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
	
	public static final int STATUS_SYS_15 = -15;
	public static final int STATUS_SYS_16 = -16;
	
	public static final int STATUS_SYS_20 = -20;
	public static final int STATUS_SYS_21 = -21;
	public static final int STATUS_SYS_22 = -22;
	
	public static final int STATUS_SYS_26 = -26;
	public static final int STATUS_SYS_27 = -27;


	public static final int STATUS_SYS_40 = -40;
	public static final int STATUS_SYS_41 = -41;
	public static final int STATUS_SYS_42 = -42;
	public static final int STATUS_SYS_43 = -43;
	public static final int STATUS_SYS_44 = -44;
	public static final int STATUS_SYS_45 = -45;
	public static final int STATUS_SYS_46 = -46;
	public static final int STATUS_SYS_47 = -47;
	public static final int STATUS_SYS_48 = -48;


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
	
	public static final String RTNINFO_SYS_15 = "邀请人不存在";
	public static final String RTNINFO_SYS_16 = "昵称已存在";
	
	//20-39  yxc
	public static final String RTNINFO_SYS_20 = "暂无收货地址，请添加";
	public static final String RTNINFO_SYS_21 = "暂无评论信息";
	public static final String RTNINFO_SYS_22 = "该评论已点赞";
	
	public static final String RTNINFO_SYS_26 = "暂无好友信息";
	public static final String RTNINFO_SYS_27 = "暂无搜索信息";

	//40-60 luy
	public static final String RTNINFO_SYS_40 = "请填写进步内容";
	public static final String RTNINFO_SYS_41 = "发布进步成功";
	public static final String RTNINFO_SYS_42 = "发布进步失败";
	public static final String RTNINFO_SYS_43 = "获取进步列表失败";
	public static final String RTNINFO_SYS_44 = "获取进步列表成功";
	
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
	public static final int APP_TOKEN_EXPIRE = 30*24*60*60*1000;
	
	public static final String NOT_NEED_SECURITY_FILTER_URL_ARR = "/user/login,/user/sms,/user/registerbasic,/user/thirdlogin,/user/thirdregister";

	/**
	 * 进步相关配置
	 */

	public static final String  IMPROVE_SINGLE_TYPE = "0";    //独立进步
	public static final String  IMPROVE_GOAL_TYPE = "1";     //目标
	public static final String  IMPROVE_RANK_TYPE = "2";      //榜
	public static final String  IMPROVE_CIRCLE_TYPE = "3";    //圈子
	public static final String  IMPROVE_CLASSROOM_TYPE = "4"; //教室
	
	/**
	 * 评论相关配置    itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
	 */
	public static final String  COMMENT_SINGLE_TYPE = "0";    //进步(零散)评论
	public static final String  COMMENT_GOAL_TYPE = "1";      //目标进步评论
	public static final String  COMMENT_RANK_TYPE = "2";      //榜评论
	public static final String  COMMENT_CIRCLE_TYPE = "3";    //圈子评论
	public static final String  COMMENT_CLASSROOM_TYPE = "4"; //教室微进步评论

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
	public static final long EXPIRE_USER_RANDOMCODE = 2*60*1000;//用户发送验证码过期时间
	public static final long EXPIRE_USER_TOKEN = 0;//用户登录token过期时间
	public static final long EXPIRE_USER_MAILCODE = 0;//邮箱验证码过期时间




}