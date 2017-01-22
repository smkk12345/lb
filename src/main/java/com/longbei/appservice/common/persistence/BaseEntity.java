package com.longbei.appservice.common.persistence;

import java.io.Serializable;

/**
 * @author smkk
 */
public abstract class BaseEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	public String createtime;
	public String updatetime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
}
