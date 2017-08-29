package com.longbei.appservice.entity;

import java.io.Serializable;

/**
 * 抽奖专用实体
 * @author smkk
 */
public class Gift implements Serializable {
    private int index;
    private int gitfCount;
    private String giftName;
    private double probability;

    public Gift(int index, int gitfCount, String giftName, double probability) {
        this.index = index;
        this.gitfCount = gitfCount;
        this.giftName = giftName;
        this.probability = probability;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

   
    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "Gift [index=" + index + ", gitfCount=" + gitfCount + ", giftName=" + giftName + ", probability=" + probability + "]";
    }

	public int getGitfCount() {
		return gitfCount;
	}

	public void setGitfCount(int gitfCount) {
		this.gitfCount = gitfCount;
	}

}