package com.longbei.appservice.common.utils;
import java.util.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.entity.Gift;

/**
 * 不同概率抽奖工具包
 *
 * @author smkk
 */
public class LotteryUtil {
    /**
     * 抽奖
     * @param orignalRates
     * 原始的概率列表，保证顺序和实际物品对应
     * @return
     * 物品的索引
     */
    public static int lottery(List<Double> orignalRates) {
        if (orignalRates == null || orignalRates.isEmpty()) {
            return -1;
        }

        int size = orignalRates.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : orignalRates) {
            sumRate += rate;
        }

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0d;
        for (double rate : orignalRates) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }

        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return sortOrignalRates.indexOf(nextDouble);
    }
    
    /**
     * 获取获奖礼品
     * @param gifts
     * @return
     * @author smkk
     * @currentdate:2017年2月24日
     */
    public static Gift getRandomGift(List<Gift> gifts){
    		List<Double> orignalRates = new ArrayList<Double>(gifts.size());
        for (Gift gift : gifts) {
            double probability = gift.getProbability();
            if (probability < 0) {
                probability = 0;
            }
            orignalRates.add(probability);
        }
        int orignalIndex = lottery(orignalRates);
    		return gifts.get(orignalIndex);
    }
    
    static List<Gift> gifts1 = new ArrayList<Gift>(){{
    	add(new Gift(1,1,"iphone",10));
    	add(new Gift(2,2,"魅族",20));
    	add(new Gift(3,3,"红米",30));
    	add(new Gift(4,4,"手环",100));
    }};
    
    static List<Gift> gifts2 = new ArrayList<Gift>(){{
    	add(new Gift(1,1,"iphone",0.15));
    	add(new Gift(2,2,"魅族",0.25));
    	add(new Gift(3,3,"红米",0.55));
    	add(new Gift(4,4,"手环",0.40));
    }};
    
    
    public static void main1(String[] args){
    	Map<Integer,Integer> map = new HashMap<Integer, Integer>();
    	for (int i = 0; i < 100000; i++) {
    		Gift gift = getRandomGift(gifts1);
    		if(map.containsKey(gift.getIndex())){
    			map.put(gift.getIndex(), 1+map.get(gift.getIndex()));
    		}else{
    			map.put(gift.getIndex(), 1);
    		}
		}
    	
    	Iterator it = map.keySet().iterator();
    	while (it.hasNext()) {
			Integer i = (Integer) it.next();
			Gift g = getGiftByIndex(i);
			System.out.println(g.getGiftName()+":"+map.get(i));
//			Integete value = 
//			
		}
    	
    	
//    	System.out.println(JSONObject.fromObject(map).toString());
    	
    }
    
    private static Gift getGiftByIndex(int nidex){
    	for (int i = 0; i < gifts1.size(); i++) {
    		Gift g = gifts1.get(i);
			if(g.getIndex() == nidex){
				return g;
			}
		}
    	return null;
    }

    public static int getRdomNumber(int leftNumber){
        return new Random().nextInt(leftNumber);
    }

    public static int getRdomNumber1(int leftNumber,int totalNumber){
        Random r = new Random(totalNumber);
        int randomNum = r.nextInt();
        while (randomNum > leftNumber){
            randomNum = r.nextInt();
        }
        return randomNum;
    }
}