package com.longbei.appservice.common.persistence.dialect;

public interface Dialect {
	public boolean supportsLimit();
	public String getLimitString(String sql, int offset, int limit);
}
