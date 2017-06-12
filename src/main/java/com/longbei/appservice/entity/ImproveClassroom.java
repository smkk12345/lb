package com.longbei.appservice.entity;

import java.util.ArrayList;
import java.util.List;

public class ImproveClassroom extends Improve{

	//评论
	private List<Comment> list = new ArrayList<Comment>();
	
	

	public List<Comment> getList() {
		return list;
	}

	public void setList(List<Comment> list) {
		this.list = list;
	}
	
}