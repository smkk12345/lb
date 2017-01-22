package com.longbei.appservice.common.utils;

public class SqlHelperUtils {
	public static StringBuffer getSelectSqlSb(String field,String tableName){
		return new StringBuffer("SELECT ").append(field).append(" FROM ").append(tableName).append(" WHERE 1=1 ");
	}
}
