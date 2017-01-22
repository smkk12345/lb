package com.longbei.appservice.common.persistence.dialect;

public class MySQLDialect implements Dialect {
	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
	}

	public boolean supportsLimit() {
		return true;
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
		StringBuilder stringBuilder = new StringBuilder(sql);
		stringBuilder.append(" limit ");
		if (offset > 0) {
			stringBuilder.append(offsetPlaceholder).append(",").append(limitPlaceholder);
		} else {
			stringBuilder.append(limitPlaceholder);
		}
		return stringBuilder.toString();
	}
}
