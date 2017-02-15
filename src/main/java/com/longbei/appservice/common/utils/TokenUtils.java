package com.longbei.appservice.common.utils;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicLong;

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.security.MD5;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TokenUtils {
	public static Hashtable has;
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final String MOBILE_ID_FORMAT = "%h";
	private static final AtomicLong seq = new AtomicLong(1010);
	
	public static String cryptToken(String token) {
		if (token.length() < 32) {
			return token;
		}
//		token = "0cc175b9c0f1b6a831c399e269772661";
		// token.charAt(8);
		if (token.length() <= 9) {
			// DebugLog.e("获取token签名", "token的hash签名，长度不够返回 “” ");
			return "";
		}
		String mToken = token.charAt(2) + "" + token.charAt(5) + "" + token.charAt(8);
		//mToken = mToken.toLowerCase();
		int mInt = Integer.parseInt(mToken, 16);
		int index = mInt % 8;
		String mtoken = "";
		// DebugLog.e("int [] ====",
		// getHashTable(index).toString()+"token:"+token);
		for (int i = 0; i < getHashTable(index).length; i++) {
			mtoken += token.charAt(getHashTable(index)[i]);
		}
		return mtoken;
	}

	public static int[] getHashTable(int a) {
		if (has == null) {
			has = new Hashtable();
			has.put(0, new int[] { 0, 5, 9, 15, 22, 28 });
			has.put(1, new int[] { 2, 8, 19, 25, 30, 31 });
			has.put(2, new int[] { 20, 25, 31, 3, 4, 8 });
			has.put(3, new int[] { 25, 31, 0, 9, 13, 17 });
			has.put(4, new int[] { 29, 2, 11, 17, 21, 26 });
			has.put(5, new int[] { 10, 15, 18, 29, 2, 3 });
			has.put(6, new int[] { 5, 10, 15, 17, 18, 22 });
			has.put(7, new int[] { 8, 20, 22, 27, 19, 21 });
		}
		return (int[]) has.get(a);
	}

	// String data = path + time + uid + paramString +
	public static String getSigin(String path, String time, String userid, String paramString, String token) {
		StringBuffer resultSb = new StringBuffer();
		resultSb.append(path).append(time);
		// paramString = StringEscapeUtils.escapeJson(paramString);
		resultSb.append(userid).append(paramString);

		String cryStr = cryptToken(token);
		resultSb.append(cryStr);
		String str = resultSb.toString().replaceAll("\n", "");
		MD5 md5 = new MD5();
		String random = md5.getMD5ofStr(str);
		return random;
	}

//	public static void main(String[] agrs) {
//		String token = cryptToken("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJsb25nYmVpIiwiaWF0IjoxNDg2NTU2MTg1LCJzdWIiOiJhcHBzZXJ2aWNlIiwiaXNzIjoiYXBpIiwiZXhwIjoxNDg3NTk1NDE0fQ.A8mfKFiMC8An2CV8J5NKbCDoRp9mDo2o8_v31XEcC58");
//	}
	
	public static String createToken(String mobile) {
		StringBuffer sb = new StringBuffer(32);
		long now = System.currentTimeMillis();
		String nowStr = DATE_TIME_FORMAT.format(now);  //14
		sb.append(nowStr.substring(12, nowStr.length()));//秒
		MD5 md5 = new MD5();
		mobile = md5.getMD5ofStr(mobile);
		mobile = md5.getMD5ofStr(mobile);
		sb.append(mobile);
		sb.append(String.format("%05d", seq.getAndIncrement() % 100000));
		return sb.toString();
	}


	/**
	 * 生成token
	 * @param id 无实际意义可自行定义
	 * @param issuer  该token的签发着
	 * @param subject  该token所面向的用户
	 * @param ttlMillis  什么时候过期
	 * @return
	 * @author luye
	 */
	private static String createToken(String id, String issuer, String subject, long ttlMillis) {

		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Constant.TOKEN_SIGN_USER);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		//Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId(id)
				.setIssuedAt(now)
				.setSubject(subject)
				.setIssuer(issuer)
				.signWith(signatureAlgorithm, signingKey);

		//if it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		//Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	/**
	 * @Title: createAppToken
	 * @Description: 龙杯app token
	 * @param @param subject
	 * @param @param ttlMillis
	 * @param @return
	 * @auther smkk
	 * @currentdate:2017年1月16日
	 */
	public static String createAppToken(long subject) {
		return createToken("longbei","app",subject+"",30*24*60*60);
	}

	/**
	 * @Title: createApiToken
	 * @Description:获取API之间调用的api
	 * @auther smkk
	 * @currentdate:2017年1月16日
	 */
	public static String createServiceToken(String serviceName,long exp){
		return createToken("longbei","api",serviceName,exp);
	}
	
}
