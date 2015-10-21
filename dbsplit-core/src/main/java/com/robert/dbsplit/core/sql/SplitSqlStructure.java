package com.robert.dbsplit.core.sql;

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
}
