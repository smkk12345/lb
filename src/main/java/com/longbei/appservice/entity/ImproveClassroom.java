package com.longbei.appservice.entity;

import java.util.ArrayList;
import java.util.List;

public class ImproveClassroom extends Improve{

	//评论
	private List<CommentLower> lowerlist = new ArrayList<CommentLower>();
	
	

	public List<CommentLower> getLowerlist() {
		return lowerlist;
	}

	public void setLowerlist(List<CommentLower> lowerlist) {
		this.lowerlist = lowerlist;
	}
	
}