package com.longbei.appservice.common.constant;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * 全局常量类
 */
public class Constant {

	public static final String UPDATE_RULE = "updaterule";

	public static final double DISTANCE_CONVERT_2D = 111.12;

	public static final Integer AUTO_CONFIRM_RECEIPT = 10*24*60;//自动确认收货时间 10天

	public static final int FLOWER_PRICE = 10; //鲜花和龙币兑换价格
	public static final int DIAMOND_PRICE = 100; //钻石和龙币兑换价格
	public static final int RMB_COIN = 10;//人民币 兑换 龙币 1元兑换10 龙币


	public static final String TOKEN_SIGN_USER = "appservice&token";

	public static final String []OK_SERVICE = new String[]{
			"adminservice"
	};


	public static final String RANK_SOURCE_TYPE_0 = "0"; //运营
	public static final String RANK_SOURCE_TYPE_1 = "1"; //app
	public static final String RANK_SOURCE_TYPE_2 = "2"; //商户


	/**
	 * OSS属性  
	 */
	public static final String OSS_IMG = "https://my-first-oss-bucket-test.oss-cn-beijing.aliyuncs.com/";
	public static final String OSS_IMGSTYLE_300_300 = "?x-oss-process=image/resize,m_fill,w_300,h_300,limit_0/auto-orient,0/quality,q_85/watermark,text_6b6Z5p2v,type_d3F5LXplbmhlaQ,size_12,t_100,color_e7bc5d,g_se,y_10,x_10";//缩略图默认大小  _2o.jpg
	public static final String OSS_CDN = "https://my-first-oss-bucket-test.oss-cn-beijing.aliyuncs.com/";//图片路径 ＋／imgName。png
//	public static final String OSS_MEDIA = "http://longbei3-mp4-out.oss-cn-shanghai.aliyuncs.com/";
	public static final String WORKFLOW1 = "longbei3-mp3";
	public static final String WORKFLOW2 = "longbei3-mp4";

	public static final String DEFAULT_START_NO = "0";   //默认分页开始条数
	public static final String DEFAULT_PAGE_SIZE = "15"; //默认分页时每页显示条数


	public static final String IOS_DEV_BUYFLOWER="21007";//IOS买花  走正式环境的验证返回21007表示现在使用沙盒环境

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
	public static final int STATUS_SYS_14 = -14;
	public static final int STATUS_SYS_15 = -15;
	public static final int STATUS_SYS_16 = -16;
	public static final int STATUS_SYS_17 = -17;
	public static final int STATUS_SYS_18 = -18;
	public static final int STATUS_SYS_19 = -19;
	public static final int STATUS_SYS_110 = -110;
	public static final int STATUS_SYS_111 = -111;
	
	public static final int STATUS_SYS_20 = -20;
	public static final int STATUS_SYS_21 = -21;
	public static final int STATUS_SYS_22 = -22;
	public static final int STATUS_SYS_23 = -23;
	public static final int STATUS_SYS_24 = -24;
	public static final int STATUS_SYS_25 = -25;
	
	public static final int STATUS_SYS_26 = -26;
	public static final int STATUS_SYS_27 = -27;
	public static final int STATUS_SYS_28 = -28;
	public static final int STATUS_SYS_29 = -29;
	public static final int STATUS_SYS_30 = -30;
	public static final int STATUS_SYS_31 = -31;
	public static final int STATUS_SYS_32 = -32;
	public static final int STATUS_SYS_33 = -33;
	public static final int STATUS_SYS_34 = -34;
	public static final int STATUS_SYS_35 = -35;
	public static final int STATUS_SYS_36 = -36;
	public static final int STATUS_SYS_37 = -37;
	public static final int STATUS_SYS_38 = -38;
	public static final int STATUS_SYS_39 = -39;

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
	public static final int STATUS_SYS_401 = -401;


	public static final int STATUS_SYS_50 = -50;
	public static final int STATUS_SYS_51 = -51;
	public static final int STATUS_SYS_52 = -52;
	public static final int STATUS_SYS_53 = -53;
	public static final int STATUS_SYS_54 = -54;
	public static final int STATUS_SYS_55 = -55;
	public static final int STATUS_SYS_56 = -56;
	public static final int STATUS_SYS_57 = -57;
	public static final int STATUS_SYS_58 = -58;
	public static final int STATUS_SYS_59 = -59;

	public static final int STATUS_SYS_60 = -60;
	public static final int STATUS_SYS_61 = -61;
	public static final int STATUS_SYS_62 = -62;
	public static final int STATUS_SYS_63 = -63;
	public static final int STATUS_SYS_64 = -64;
	public static final int STATUS_SYS_65 = -65;
	public static final int STATUS_SYS_66 = -66;
	public static final int STATUS_SYS_67 = -67;
	public static final int STATUS_SYS_68 = -68;
	public static final int STATUS_SYS_69 = -69;
	public static final int STATUS_SYS_70 = -70;
	public static final int STATUS_SYS_610 = -610;
	public static final int STATUS_SYS_611 = -611;
	public static final int STATUS_SYS_612 = -612;
	public static final int STATUS_SYS_613 = -613;
	public static final int STATUS_SYS_614 = -614;
	public static final int STATUS_SYS_615 = -615;
	public static final int STATUS_SYS_616 = -616;
	public static final int STATUS_SYS_617 = -617;
	public static final int STATUS_SYS_618 = -618;
	public static final int STATUS_SYS_619 = -619;
	public static final int STATUS_SYS_620 = -620;
	//80-89 兴趣圈  wangyongzhi
	public static final int STATUS_SYS_80 = -80;
	public static final int STATUS_SYS_81 = -81;
	public static final int STATUS_SYS_82 = -82;
	public static final int STATUS_SYS_83 = -83;
	public static final int STATUS_SYS_84 = -84;
	public static final int STATUS_SYS_85 = -85;
	public static final int STATUS_SYS_86 = -86;
	public static final int STATUS_SYS_87 = -87;
	public static final int STATUS_SYS_88 = -88;

	//90-99 好友相关 wangyongzhi
	public static final int STATUS_SYS_90 = -90;
	public static final int STATUS_SYS_91 = -91;
	public static final int STATUS_SYS_92 = -92;
	public static final int STATUS_SYS_93 = -93;
	public static final int STATUS_SYS_94 = -94;
	public static final int STATUS_SYS_95 = -95;
	public static final int STATUS_SYS_96 = -96;
	public static final int STATUS_SYS_97 = -97;
	public static final int STATUS_SYS_98 = -98;
	public static final int STATUS_SYS_99 = -99;
	public static final int STATUS_SYS_910 = -910;
	public static final int STATUS_SYS_911 = -911;

	//100-120 商品有关
	public static final int STATUS_SYS_100 = -100;
	public static final int STATUS_SYS_101 = -101;

	public static final int STATUS_SYS_1000 = -1000;
	public static final int STATUS_SYS_1001 = -1001;
	public static final int STATUS_SYS_1002 = -1002;
	
	
	
	/**
	 * 服务端返回描述
	 */
	//1-19  lxb
	public static final String RTNINFO_SYS_00 = "操作成功!";
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
	public static final String RTNINFO_SYS_14 = "加榜个数已达到上限";
	public static final String RTNINFO_SYS_15 = "邀请人不存在";
	public static final String RTNINFO_SYS_16 = "昵称已存在";
	public static final String RTNINFO_SYS_17 = "内容中含有敏感词";
	public static final String RTNINFO_SYS_18 = "发布的公开榜单已达上限";
	public static final String RTNINFO_SYS_19 = "发布的私有榜单已达上限";
	public static final String RTNINFO_SYS_110 = "根据你当前的用户等级,您创建的公开榜单人数上限为";
	public static final String RTNINFO_SYS_111 = "根据你当前的用户等级,您创建的私有榜单人数上限为";

	//20-39  yxc
	public static final String RTNINFO_SYS_20 = "暂无收货地址，请添加";
	public static final String RTNINFO_SYS_21 = "暂无评论信息";
	public static final String RTNINFO_SYS_22 = "该评论已点赞";
	public static final String RTNINFO_SYS_23 = "龙币数量不足";
	public static final String RTNINFO_SYS_24 = "进步币数量不足";
	public static final String RTNINFO_SYS_25 = "最低进步币要求数量不足";
	
	public static final String RTNINFO_SYS_26 = "暂无好友信息";
	public static final String RTNINFO_SYS_27 = "暂无搜索信息";
	public static final String RTNINFO_SYS_28 = "暂无消息记录";
	public static final String RTNINFO_SYS_29 = "暂无签到记录";
	public static final String RTNINFO_SYS_30 = "用户已签到";
	public static final String RTNINFO_SYS_31 = "用户签到成功";
	public static final String RTNINFO_SYS_32 = "暂无成员信息";
	public static final String RTNINFO_SYS_33 = "暂无教室信息";
	public static final String RTNINFO_SYS_34 = "暂无课程信息";
	public static final String RTNINFO_SYS_35 = "暂无教室提问疑答信息";
	public static final String RTNINFO_SYS_36 = "教室参与人数已达到限额";
	public static final String RTNINFO_SYS_37 = "用户已加入教室";
	public static final String RTNINFO_SYS_38 = "工作信息最多可添加5条";
	public static final String RTNINFO_SYS_39 = "教育信息最多可添加5条";

	//40-60 luy
	public static final String RTNINFO_SYS_40 = "请填写进步内容";
	public static final String RTNINFO_SYS_41 = "发布进步成功";
	public static final String RTNINFO_SYS_42 = "发布进步失败";
	public static final String RTNINFO_SYS_43 = "获取进步列表失败";
	public static final String RTNINFO_SYS_44 = "获取进步列表成功";
	public static final String RTNINFO_SYS_45 = "你没有为该进步点过赞";
	public static final String RTNINFO_SYS_46 = "删除进步成功";
	public static final String RTNINFO_SYS_47 = "删除进步失败";
	public static final String RTNINFO_SYS_48 = "送花失败，龙币扣除";
	public static final String RTNINFO_SYS_49 = "送钻失败，龙币扣除";
	public static final String RTNINFO_SYS_401 = "进步不存在，或进步已经被删除";

	public static final String RTNINFO_SYS_54 = "还有内容没有审核";
	public static final String RTNINFO_SYS_55 = "该进步不存在";
	public static final String RTNINFO_SYS_56 = "送钻失败，龙币扣除";
	public static final String RTNINFO_SYS_57 = "送钻失败，龙币扣除";

	public static final String RTNINFO_SYS_58 = "目标不存在";
	public static final String RTNINFO_SYS_59 = "所选目标不是你的目标";


	public static final String RTNINFO_SYS_60 = "榜单没有审核，或审核不通过";
	public static final String RTNINFO_SYS_61 = "参榜口令不正确,请重新输入";
	public static final String RTNINFO_SYS_62 = "抱歉,由于当前已超过了最后的参榜时间,因此您无法参榜!";
	public static final String RTNINFO_SYS_63 = "抱歉,由于龙榜已结束或未设置为开放等原因,您暂时无法进行参榜!";
	public static final String RTNINFO_SYS_64 = "已经点过赞";
	public static final String RTNINFO_SYS_65 = "您已在该榜单中领过奖了";
	public static final String RTNINFO_SYS_66 = "非常抱歉,您未在该榜单中获奖!";
	public static final String RTNINFO_SYS_67 = "该榜单还未结束!";
	public static final String RTNINFO_SYS_68 = "该榜单还未开始";
	public static final String RTNINFO_SYS_69 = "非常抱歉,该榜单已结束";
	public static final String RTNINFO_SYS_70 = "榜单已经结束，无法退出";
	public static final String RTNINFO_SYS_610 = "抱歉,参榜人数已达榜最大限制人数!";
	public static final String RTNINFO_SYS_611 = "抱歉,您暂时无法领奖,详情请联系客服";
	public static final String RTNINFO_SYS_612 = "榜主不可退榜";
	public static final String RTNINFO_SYS_613 = "榜不存在";
	public static final String RTNINFO_SYS_614 = "当前系统正忙或您已经关注了该榜单!";
	public static final String RTNINFO_SYS_615 = "抱歉,您没有权限修改该榜单信息!";
	public static final String RTNINFO_SYS_616 = "该榜单不存在";
	public static final String RTNINFO_SYS_617 = "榜单中所发进步达到最大数量";
	public static final String RTNINFO_SYS_618 = "通过人数以达到上限";
	public static final String RTNINFO_SYS_619 = "榜已经结束，无法删除进步";
	public static final String RTNINFO_SYS_620 = "该内容不存在";
	//100-120 商品有关
	public static final String RTNINFO_SYS_100 = "商品已下架";
	public static final String RTNINFO_SYS_101 = "结算总价不一致";
	
	public static final String RTNINFO_SYS_1000 = "未获取授权信息";
	public static final String RTNINFO_SYS_1001 = "token错误";
	public static final String RTNINFO_SYS_1002 = "token失效";



	public static final String RTNINFO_SYS_50 = "添加榜单成功";
	public static final String RTNINFO_SYS_51 = "添加榜单失败";
	public static final String RTNINFO_SYS_52 = "修改榜单成功";
	public static final String RTNINFO_SYS_53 = "修改榜单失败";

	//80-89 兴趣圈  wangyongzhi
	public static final String RTNINFO_SYS_80 = "兴趣圈名称重复";
	public static final String RTNINFO_SYS_81 = "抱歉,您无权更新该兴趣圈的信息";
	public static final String RTNINFO_SYS_82 = "您已经在该兴趣圈中了";
	public static final String RTNINFO_SYS_83 = "您已经申请了加入该兴趣圈,圈主正在验证中,请稍后~~";
	public static final String RTNINFO_SYS_84 = "申请加圈子成功,正在等待圈主验证";
	public static final String RTNINFO_SYS_85 = "该用户还未加入该圈子";
	public static final String RTNINFO_SYS_86 = "抱歉,未查询到该兴趣圈相关信息";
	public static final String RTNINFO_SYS_87 = "该用户已经在圈子中了";
	public static final String RTNINFO_SYS_88 = "抱歉,您目前无权限创建圈子或您创建的圈子已达上限";

	/** 90-99  好友  群组 相关 by wangyongzhi **/
	public static final String RTNINFO_SYS_90 = "你们已经是好友关系了,快去聊天吧!";
	public static final String RTNINFO_SYS_91 = "抱歉,七天内不能重复添加同一个好友!";
	public static final String RTNINFO_SYS_92 = "抱歉,被邀请人入群后人数将超过群人数上限!";
	public static final String RTNINFO_SYS_93 = "您已提交申请,正在等待审核";
	public static final String RTNINFO_SYS_94 = "抱歉,您当前没有权限操作该群组相关信息";
	public static final String RTNINFO_SYS_95 = "群主不可退出群组!";
	public static final String RTNINFO_SYS_96 = "加入该榜单,需要用户实名认证,您还未实名认证通过";
	public static final String RTNINFO_SYS_97 = "由于您的等级未满足参榜的等级要求,请先提高自己的等级吧!";
	public static final String RTNINFO_SYS_98 = "您已在龙榜中,无需再次提交申请";
	public static final String RTNINFO_SYS_99 = "由于您被榜主剔除了该龙榜,因此无法再次申请加入!";
	public static final String RTNINFO_SYS_910 = "您当前还未加入该榜单,或已退出该榜单";
	public static final String RTNINFO_SYS_911 = "您的昵称备注超过了长度限制";

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
	/**
	 * 不需要用户token的接口
	 */
	public static final String NOT_NEED_SECURITY_FILTER_URL_ARR = "/user/login,/user/sms," +
			"/user/registerbasic,/user/thirdlogin,/user/thirdregister," +
			"/user/checkSms,/init,/user/findPassword,/syssetting/upgrade";

	public static final String VISITOR_UID = "-1";
	/**
	 * 游客模式 uid -1 可进入接口
	 *
	 * 首页轮播图
	 * 获奖公示
	 * 首页榜单列表
	 * 首页关注达人
	 *
	 * 进步推荐列表
	 * 微进步广场
	 *
	 * 龙榜列表
	 * 龙榜详情
	 */
	public static final String VISITOR_URL = "/home/homepics,/rank/selectWinningRankAward," +
			"/rank/selectRankListForApp," +
			"/user/selectFashionManUser,/improve/recommendlist,/improve/line/list," +
			"/rank/selectRankList," +
			"/rank/rankDetail,/rank/rankMemberSort," +
			"/rank/ownRankSort,/rank/selectFashionMan,/improve/rank/list,/rank/selectRankArea,"+
			"/rank/rankAwardList,/rank/onlyRankAward,/rank/getWinningRankAwardUser";

	/**
	 * 进步相关配置
	 */

	public static final String  IMPROVE_SINGLE_TYPE = "0";    //独立进步
	public static final String  IMPROVE_GOAL_TYPE = "1";     //目标
	public static final String  IMPROVE_RANK_TYPE = "2";      //榜
	public static final String  IMPROVE_CIRCLE_TYPE = "3";    //圈子
	public static final String  IMPROVE_CLASSROOM_TYPE = "4"; //教室
	public static final String  IMPROVE_CLASSROOM_REPLY_TYPE = "5"; //教室批复作业

	/**
	 * 进步列表过滤
	 */
	public static final String IMPROVE_LIST_ALL = "0";  //全部
	public static final String IMPROVE_LIST_FANS = "1";  //关注
	public static final String IMPROVE_LIST_FRIEND = "2";  //好友
	public static final String IMPROVE_LIST_ACQUAINTANCE = "3";  //熟人

	public static final String IMPROVE_ISPUBLIC_0 = "0";//私密
	public static final String IMPROVE_ISPUBLIC_1 = "1";//好友可见
	public static final String IMPROVE_ISPUBLIC_2 = "2";//公开

	/**
	 * 进步明细类型（赞，花，钻）
	 */
	public static final String IMPROVE_ALL_DETAIL_LIKE = "0";  //赞的明细
	public static final String IMPROVE_ALL_DETAIL_FLOWER = "1";  //花的明细
	public static final String IMPROVE_ALL_DETAIL_DIAMOND = "2";  //钻明细

	public static final String MONGO_IMPROVE_LFD_OPT_LIKE = "0";   //点赞
	public static final String MONGO_IMPROVE_LFD_OPT_FLOWER = "1";  //送花
	public static final String MONGO_IMPROVE_LFD_OPT_DIAMOND = "2";  //送钻


	public static final String IMPROVE_LIKE_ADD = "0"; //点赞
	public static final String IMPROVE_LIKE_CANCEL = "1"; //取消赞
	public static final String IMPROVE_FLOWER = "2"; //送花

	public static final Integer DEFAULT_IMPROVE_ALL_DETAIL_LIST_NUM = 10;


	public static final String RANK_TYEP_APP = "2";
	public static final String RANK_TYEP_B = "1";
	public static final String RANK_TYEP_C = "0";

	public enum SYS_COMMON_KEYS{
		publishbg,//首页背景图
		regprotocol,//协议
		shareip,	//分享ip
		shareport,	//分享端口
		flowertomoney,//花转换龙币比例
		moneytocoin,//龙币兑换进步币比例
		flowertocoin,//花兑换进步币比例
		yuantomoney //人民币兑换龙币比例
	}


	/**
	 * 进步时间线类型
	 */
	public static final String SQUARE_USER_ID = "10000";  //系统用户id
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
	 * 进步币添加来源   
	 * 0:签到   1:发进步  2:分享  3：邀请好友  4：榜获奖  5：收到钻石礼物 
     *				    6：收到鲜花礼物  7:兑换商品  8：公益抽奖获得进步币  
     * 					9：公益抽奖消耗进步币  10.消耗进步币(例如超级用户扣除进步币)
     * 					11:取消订单返还龙币     12:兑换鲜花
	 */
	public static final String USER_IMP_COIN_CHECKIN="0";
	public static final String USER_IMP_COIN_ADDIMPROVE="1";
	public static final String USER_IMP_COIN_SHARE = "2";
	public static final String USER_IMP_COIN_INVITE="3";
	public static final String USER_IMP_COIN_RANKAWARD = "4";
	public static final String USER_IMP_COIN_DIAMONDED = "5";
	public static final String USER_IMP_COIN_FLOWERD = "6";


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


	public static final String RANK_ISAUTO_YES = "0";  //自动发布
	public static final String RANK_ISAUTO_NO = "1";    //手动发布
	public static final String RANK_ISAUTO_TIME = "2";    //定时发布


	public static final String RANK_ISUP_YES = "1"; //已发布
	public static final String RANK_ISUP_NO = "0"; //未发布

	
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
//	public static final String  MSG_QUITRANK_MODEL = "您在龙榜'n'中,被榜主剔除了该龙榜!";      //下榜
//	public static final String  MSG_QUITRANK_IMP_MODEL = "您在龙榜'n'中的进步已删除!";      //下榜
//	public static final String  MSG_QUITRANK_QUIT_MODEL = "您在龙榜'n'中的进步已下榜!";      //下榜
	public static final String  MSG_IMP_DEL_MODEL = "您此条微进步(n)因严重违反龙杯相关规则和倡导精神,已被删除"; //删除进步
	public static final String  MSG_RANKIMP_QUIT_MODEL = "您的参榜进步与榜规则或倡导精神不符，已被请下《n》龙榜"; //下榜进步
	public static final String  MSG_RANK_CLOSS_MODEL = "您参加的龙榜因严重违反龙杯相关规则和倡导精神,已被关闭"; //榜关闭
	
	
	
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
     * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问   
						14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
     * 消息展示模版
	 */
	public static final String  MSG_FLOWER_MODEL = "给这条微进步献了n朵花"; 		//进步送花消息模板
	public static final String  MSG_LIKE_MODEL = "赞了这条微进步"; 				//进步点赞消息模板
	public static final String  MSG_DIAMOND_MODEL = "给这条微进步赞赏了n颗砖石"; 	//进步送钻石消息模板
	
	public static final String  MSG_CLASSROOM_REPLY_MODEL = "批复了你在教室'n'上传的作业"; 	//老师回复提问消息模板
	public static final String  MSG_USER_LEVEL_MODEL = "你的龙级升级到n级"; 	//等级提升消息模板
	public static final String  MSG_USER_PL_LEVEL_MODEL = "十全十美能级---m升级到n级"; 	//十全十美类型等级提升消息模板
	

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
	
	/*
	 * 用户龙币   进步币   缓存到redis
	 */
	public static final String RP_USER_IMP_COIN_VALUE = "user_imp_coin_";  //进步币
	public static final String RP_USER_MONEY_VALUE = "user_money_";  //龙币
	

	/**
	 * 目标中每日进步 缓存到截止两天  榜单中进步  缓存一天
	 */
	public static final String RP_IMPROVE_NDAY = "improve_day_";

	/**
	 * 圈子中的当天的入圈人数
	 */
	public static final String RP_CIRCLE_INSERT_CIRCLEMEMBER="circle_insert_date_";


	public static final String REDIS_IMPROVE_LIKE = "improve_like_";  //redis中like相关
	public static final String REDIS_IMPROVE_FLOWER = "improve_flower_"; //redis中flower
	public static final String REDIS_IMPROVE_LFD = "improve_lfd_"; //进步的 赞，花，钻石

	public static final String REDIS_RANK_SORT = "rank_sort_";//榜单 排名
	public static final String REDIS_RANK_END = "rank_end_";//榜单结束 标识


    /**
     * 用户特权  操作
     * Privilege
     */
    public static final String USER_PRIVILEGE_ADD_CIRCLE = "0";//
    public static final String USER_PRIVILEGE_ADD_RANK = "1";//
    public static final String USER_PRIVILEGE_ADD_CLASSROOM = "2";//

	/****** JPUSH 消息的tag start ******/
	//10开头的代表好友相关的通知
	public static final String JPUSH_TAG_COUNT_1001 = "1001";//申请加好友通知
	public static final String JPUSH_TAG_COUNT_1002 = "1002";//用户回复了添加好友的消息
	public static final String JPUSH_TAG_COUNT_1003 = "1003";//加好友申请被拒绝
	public static final String JPUSH_TAG_COUNT_1004 = "1004";//加好友申请被同意
	public static final String JPUSH_TAG_COUNT_1005 = "1005";//删除好友
	//11开头的代表群组相关的通知  前端推送
	public static final String JPUSH_TAG_COUNT_1101 = "1101";//群消息
	//12开头 代表实名认证
	public static final String JPUSH_TAG_COUNT_1201 = "1201";//实名认证通过
	public static final String JPUSH_TAG_COUNT_1202 = "1202";//实名认证未通过
	//13开头 代表被运营选中
//	public static final String JPUSH_TAG_COUNT_1301 = "1301";//实名认证未通过
	public static final String JPUSH_TAG_COUNT_1302 = "1302";//被选为达人
//	public static final String JPUSH_TAG_COUNT_1303 = "1303";//创建的龙榜／教室／圈子被选为推荐
	//14开头 好友对话  前端推送
//	public static final String JPUSH_TAG_COUNT_1401 = "1401";
	//15 龙榜消息
//	public static final String JPUSH_TAG_COUNT_1501 = "1501";//榜主@好友上榜
	public static final String JPUSH_TAG_COUNT_1502 = "1502";//龙榜获奖
//	public static final String JPUSH_TAG_COUNT_1503 = "1503";//创建的榜单通过审核
	/**---------------自定义消息--设置title为空即可------------------**/
	//100开始 10001

	/****** JPUSH 消息的tag end ******/


	/** 操作类型 **/
	public enum OperationType{
		like,//点赞
		flower,//送花
		cancleLike,//取消点赞

	}

	/**
	 * 操作分类
	 */
	public static String MQACTION_IMPROVE = "improve";
	public static String MQACTION_USERRELATION = "userrelation";

	/**
	 * 具体类型
	 */
	public static String MQDOMAIN_IMP_ADD = "improve_add";
	public static String MQDOMAIN_IMP_UPDATE = "improve_update";
	public static String MQDOMAIN_IMP_DEL = "improve_del";

	public static String MQDOMAIN_USER_UPDATE = "user_update";
	public static String MQDOMAIN_USER_ADDFRIEND = "user_addfriend";
	public static String MQDOMAIN_USER_REMOVEFRIEND = "user_removefriend";
	public static String MQDOMAIN_USER_ADDFUN = "user_addfun";
	public static String MQDOMAIN_USER_REMOVEFUN = "user_removefun";
	/**
	 * 排序类型
	 */
	public enum SortType{
		comprehensive,//综合
		likes,//赞
		flower,//花
	}
	/**
	 * money
	 *   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用进步币
	 * 					3：设榜单    4：赞助榜单    5：赞助教室
	 */
	public static final String USER_MONEY_BUY = "0";
	public static final String USER_MONEY_GIFT = "1";
//	public static final String ;

	/**
	 * 	用户统计数据处理----运营后台使用
	 *        0 发进步 1 删除进步
	 *        2 点赞 3 取消赞
	 *        4 送花
	 *        5 被关注
	 *        6 取消关注
	 *        7 签到
	 */
	public enum UserSumType{
		addedImprove,//发进步
		removedImprove,//删除
		addedLike,//被点赞
		removedLike,//被取消赞
		addedFlower,//被送花
		addedFans,//被关注
		removedFans,//被移除关注
		checkIn, //签到
		giveFlower//送花
	}

	/**
	 * 特权类型
	 */
	public enum PrivilegeType{
		joinranknum,//加入榜单限制
		createRank, //创建榜单时  私有榜单数量参与人数  公共榜单数量 参与人数
		publishRank,//发布榜单时
		createCircle
	}

	/**
	 * 进步币支付
	 */
	public static final String 	PAY_TYPE_01 = "0";
	/**
	 * 支付宝支付
	 */
	public static final String 	PAY_TYPE_02 = "1";
	/**
	 * 微信支付
	 */
	public static final String 	PAY_TYPE_03 = "2";

	/**
	 * 内购测试帐号支付
	 */
	public static final String 	PAY_TYPE_04 = "3";

	/**
	 * 内购生产环境支付
	 */
	public static final String 	PAY_TYPE_05 = "4";

}