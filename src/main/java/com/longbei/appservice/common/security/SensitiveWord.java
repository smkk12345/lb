package com.longbei.appservice.common.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 敏感词过滤工具类
 * @author smkk
 */
public class SensitiveWord {
	
	/**
	 * 敏感词库
	 */
	public static Map sensitiveWordMap = new ConcurrentHashMap<>();
	
	/**
	 * 只过滤最小敏感词
	 */
	public static int minMatchTYpe = 1;

	/**
	 * 过滤所有敏感词
	 */
	public static int maxMatchType = 2;

	public static Set<String> readTxtFile(String filePath) {
		Set<String> s = new HashSet<>();
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					s.add(lineTxt);
//					System.out.println(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return s;
	}
	     	
	@SuppressWarnings("unchecked")
	public static void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		// 初始化HashMap对象并控制容器的大小
		sensitiveWordMap = new HashMap(keyWordSet.size());
		// 敏感词
		String key = null;
		// 用来按照相应的格式保存敏感词库数据
		Map nowMap = null;
		// 用来辅助构建敏感词库
		Map<String, String> newWorMap = null;
		// 使用一个迭代器来循环敏感词集合
		Iterator<String> iterator = keyWordSet.iterator();
		while (iterator.hasNext()) {
			key = iterator.next();
			// 等于敏感词库，HashMap对象在内存中占用的是同一个地址，所以此nowMap对象的变化，sensitiveWordMap对象也会跟着改变
			nowMap = sensitiveWordMap;
			for (int i = 0; i < key.length(); i++) {
				// 截取敏感词当中的字，在敏感词库中字为HashMap对象的Key键值
				char keyChar = key.charAt(i);
				// 判断这个字是否存在于敏感词库中
				Object wordMap = nowMap.get(keyChar);
				if (wordMap != null) {
					nowMap = (Map) wordMap;
				} else {
					newWorMap = new HashMap<String, String>();
					newWorMap.put("isEnd", "0");
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				// 如果该字是当前敏感词的最后一个字，则标识为结尾字
				if (i == key.length() - 1) {
					nowMap.put("isEnd", "1");
				}
			}
		}
	}
	
	
	/**
	 * 敏感词库敏感词数量
	 * @return
	 */
	public static int getWordSize() {
		if (SensitiveWord.sensitiveWordMap == null) {
			return 0;
		}
		return SensitiveWord.sensitiveWordMap.size();
	}

	/**
	 * 是否包含敏感词
	 * @param txt
	 * @param matchType
	 * @return
	 */
	public static boolean isContaintSensitiveWord(String txt, int matchType, Map sensitiveWordMap) {
		boolean flag = false;
		for (int i = 0; i < txt.length(); i++) {
			int matchFlag = checkSensitiveWord(txt, i, matchType);
			if (matchFlag > 0) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取敏感词内容
	 * @param txt
	 * @param matchType
	 * @return 敏感词内容
	 */
	public static Set<String> getSensitiveWord(String txt, int matchType) {
		Set<String> sensitiveWordList = new HashSet<String>();

		for (int i = 0; i < txt.length(); i++) {
			int length = checkSensitiveWord(txt, i, matchType);
			if (length > 0) {
				// 将检测出的敏感词保存到集合中
				sensitiveWordList.add(txt.substring(i, i + length));
				i = i + length - 1;
			}
		}

		return sensitiveWordList;
	}

	/**
	 * 替换敏感词
	 * @param txt
	 * @param matchType
	 * @param replaceChar
	 * @return
	 */
	public static String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
		if(sensitiveWordMap.isEmpty()){
			return txt;
		}
		String resultTxt = txt;
		Set<String> set = getSensitiveWord(txt, matchType);
		Iterator<String> iterator = set.iterator();
		String word = null;
		String replaceString = null;
		while (iterator.hasNext()) {
			word = iterator.next();
			replaceString = getReplaceChars(replaceChar, word.length());
			resultTxt = resultTxt.replaceAll(word, replaceString);
		}

		return resultTxt;
	}
	
//	@SuppressWarnings("rawtypes")
//	public static String replaceWithSensitivemap(String name,HttpServletRequest request,JedisDao jedisDao,RulesService rulesService) {
//		byte[] bt = jedisDao.getBytes("sys_sensitive");
//		if(null != bt && bt.length>0){
//			ObjectsTranscoder<Serializable> transcoder = new ObjectsTranscoder<Serializable>();
//			sensitiveWordMap = (HashMap) transcoder.deserialize(bt);
//			name = SensitiveWord.replaceSensitiveWord(name,2,"*");
//		}else{
//			Set<String> list = rulesService.getSensitiveWord();
//       		SensitiveWord.addSensitiveWordToHashMap(list,request);
//			name = SensitiveWord.replaceSensitiveWord(name,2,"*");
//		}
//		return name;
//	}
	
	/**
	 * 替换敏感词内容
	 * @param replaceChar
	 * @param length
	 * @return
	 */
	private static String getReplaceChars(String replaceChar, int length) {
		String resultReplace = replaceChar;
		for (int i = 1; i < length; i++) {
			resultReplace += replaceChar;
		}
		return resultReplace;
	}

	/**
	 * 检查敏感词数量
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return
	 */
	public static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
		boolean flag = false;
		// 记录敏感词数量
		int matchFlag = 0;
		char word = 0;
		Map nowMap = SensitiveWord.sensitiveWordMap;
		for (int i = beginIndex; i < txt.length(); i++) {
			word = txt.charAt(i);
			// 判断该字是否存在于敏感词库中
//			Object o = nowMap.get(word);
			nowMap = (Map) nowMap.get(word);
			if (nowMap != null) {
				matchFlag++;
				// 判断是否是敏感词的结尾字，如果是结尾字则判断是否继续检测
				if ("1".equals(nowMap.get("isEnd"))) {
					flag = true;
					// 判断过滤类型，如果是小过滤则跳出循环，否则继续循环
					if (SensitiveWord.minMatchTYpe == matchType) {
						break;
					}
				}
			} else {
				break;
			}
		}
		if (!flag) {
			matchFlag = 0;
		}
		return matchFlag;
	}
	
	
	
	
	
}