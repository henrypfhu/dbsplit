package com.robert.dbsplit.core.sql.parser;

import org.springframework.util.StringUtils;

public class SplitSqlStructure {
	public enum SqlType {
		SELECT, INSERT, UPDATE, DELETE
	};

	private SqlType sqlType;

	private String dbName;
	private String tableName;

	private String previousPart;
	private String sebsequentPart;

	public SqlType getSqlType() {
		return sqlType;
	}

	public void setSqlType(SqlType sqlType) {
		this.sqlType = sqlType;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPreviousPart() {
		return previousPart;
	}

	public void setPreviousPart(String previousPart) {
		this.previousPart = previousPart;
	}

	public String getSebsequentPart() {
		return sebsequentPart;
	}

	public void setSebsequentPart(String sebsequentPart) {
		this.sebsequentPart = sebsequentPart;
	}

	public String getSplitSql(int dbNo, int tableNo) {
		if (sqlType == null || StringUtils.isEmpty(dbName)
				|| StringUtils.isEmpty(tableName)
				|| StringUtils.isEmpty(previousPart)
				|| StringUtils.isEmpty(sebsequentPart))
			throw new IllegalStateException(
					"The split SQL should be constructed after the SQL is parsed completely.");

		StringBuffer sb = new StringBuffer();
		sb.append(previousPart).append(" ");
		sb.append(dbName).append("_").append(dbNo);
		sb.append(".");
		sb.append(tableName).append("_").append(tableNo).append(" ");
		sb.append(sebsequentPart);

		return sb.toString();
	}
}
