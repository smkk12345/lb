package com.longbei.appservice.common.constant;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class PayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088221612231350";
	// 商户的私钥
	public static String private_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANqXGaFDYrpeVdCeSgEX1mgdQgIKG4n71vCLTqvBfVJSrBH5mBTGbMsha+5OIRhW2CFZXOV9600fx4bmwED9H9/fJqhVqyOB0MLlc63aa7u0HmSohjb8u6bEtYsqYAud0YRmdu2Rn+MJsEvWQ4nzFxbDfBT3W6PpvPCtF9DxPtmLAgMBAAECgYArDLUcgHPyNl/6PSJgArzUNmbolesEitey+gydYPb4fpWq2JguB0In40YpYbO84mWgc8QK4niz8L1QASP96JwdEsWTEtOxnE0aOwDAbUf3bVnnISth/yQ0zPX7aBckPsksY13qfHubUZ57w4a0qstLbbva8w9srGLL59XI0RjqSQJBAPdZ4rRg5PtzSa2nICugKSLoT+qHXajTemy3Z2IFbVnRv9jViZebenhoKhJnZf79bm+VquVMP3BCN3Y3JESx3VUCQQDiO8RphVdquNG8ZBkYwvNP6RhPlpPHVoMTSpfj/fI5qDxroOrccU1Zuuehf257A2EaQZkywmGugbgcZ7ZvTttfAkB+vQO4QN2TKaNa4wNM1ye8xyQLm4iV9KYKBn9mbjl1iE+wntl9YSEDiPvlqi1M1jY73ohOLR9kOcmJPP/MYwZxAkBsz3RDkGegI+50nuxXNYTTPXh85x6CtMBsaecuODjltF4DIi10A2bBrpyz11erANAWUdC+UjGrPibwOo3LcXEfAkAsVNgI2fB64IJ+4AMZjTGMr0ui+KZD2GlIL4kz+owGtwJ/bXGQKkfbMfPS4b5dupf6a33TTDqJQyQmwDN7a15h";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDalxmhQ2K6XlXQnkoBF9ZoHUICChuJ+9bwi06rwX1SUqwR+ZgUxmzLIWvuTiEYVtghWVzlfetNH8eG5sBA/R/f3yaoVasjgdDC5XOt2mu7tB5kqIY2/LumxLWLKmALndGEZnbtkZ/jCbBL1kOJ8xcWw3wU91uj6bzwrRfQ8T7ZiwIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	public static String sellerid = "yl@longbei.com";
	
	
	/**
	 * 微信支付
	 */
	public static String PartnerId = "1331657801";
	public static String PartnerKey = "longbei2016kdjls65klm6djkmx27qal";
	public static String AppID = "wx06ebbab950565d75";
	public static String AppSecret = "21fb68fc5ccb50e9daca83ed71d555f1";
	

	
	
	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";

}
