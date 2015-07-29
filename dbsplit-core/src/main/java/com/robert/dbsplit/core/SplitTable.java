package com.robert.dbsplit.core;

public class SplitTable {
	private String tableName;
	private String dbName;
	private int dbNum;
	private int tableNum;

	public SplitTable(String tableName, String dbName, int dbNum, int tableNum) {
		this.tableName = tableName;
		this.dbName = dbName;
		this.dbNum = dbNum;
		this.tableNum = tableNum;
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

	public int getDbNum() {
		return dbNum;
	}

	public void setDbNum(int dbNum) {
		this.dbNum = dbNum;
	}

	public int getTableNum() {
		return tableNum;
	}

	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}
}
