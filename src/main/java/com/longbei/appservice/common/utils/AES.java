package com.longbei.appservice.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.core.util.Base64;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private static final String AESTYPE = "AES/ECB/PKCS5Padding";

	public static final String A_KEY = "AKxNB89D3Fcgenkc";
	
	/**
	 * 对字符串进行Base64编码加密
	 * @param keyStr
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String keyStr, String plainText) {
		byte[] encrypt = null;
		try {
			Key key = generateKey(keyStr);
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encrypt = cipher.doFinal(plainText.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			String result = new String(Base64.encode(encrypt),"utf-8");
			return result;
		}catch (Exception e){

		}
//		String result = new String(Base64.encode(encrypt),"utf-8");
		return null;
	}
	
	/**
	 * 对字符串进行Base64解密
	 * @param keyStr
	 * @param encryptData
	 * @return
	 */
	public static String decrypt(String keyStr, String encryptData) {
		byte[] decrypt = null;
		try {
			Key key = generateKey(keyStr);
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decrypt = cipher.doFinal(Base64.decode(encryptData));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(decrypt).trim();
	}

	private static Key generateKey(String key) throws Exception {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			return keySpec;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * 加密
	 * @param map
	 * @return
	 */
	protected static String encryptParamsMap(HashMap<String, String> map) {
		if (map != null) {
			// 避免Gson使用时将一些字符自动转换为Unicode转义字符,setPrettyPrinting将json字符串格式化
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			String json = gson.toJson(map);
			return AES.encrypt(A_KEY, json);
		} else {
			return null;
		}
	}

	/**
	 * 解密
	 * @param jsonBody
	 * @return
	 */
	protected static String decryptJsonBody(String jsonBody) {
		Gson gson = new Gson();
		HashMap rs = gson.fromJson(jsonBody, HashMap.class);
		//LogUtils.d("this.isEncrypted =" + this.isEncrypted + ", rs:\r\n" + jsonObj);
		String result = rs.get("result").toString();
		if (!StringUtils.isEmpty(result)) {
			return AES.decrypt(A_KEY, result);
		}
		return jsonBody;
	}

	public static void main1(String[] args){
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("startnum","5");
		map.put("endnum","2");
		String encryStr = encryptParamsMap(map);
		System.out.println("encry:"+encryStr);
		String deStr = AES.decrypt(A_KEY, encryStr);
		System.out.println("decry:"+deStr);
	}


}
