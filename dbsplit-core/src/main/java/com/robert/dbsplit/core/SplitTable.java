package com.robert.dbsplit.core;

import java.util.List;

public class SplitTable {
	private String dbNamePrifix;
	private String tableNamePrifix;

	private int dbNum;
	private int tableNum;

	private List<SplitNode> splitNode;
	private HashSplitStrategy hashSplitStrategy;

	public String getDbNamePrifix() {
		return dbNamePrifix;
	}

	public void setDbNamePrifix(String dbNamePrifix) {
		this.dbNamePrifix = dbNamePrifix;
	}

	public String getTableNamePrifix() {
		return tableNamePrifix;
	}

	public void setTableNamePrifix(String tableNamePrifix) {
		this.tableNamePrifix = tableNamePrifix;
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

	public List<SplitNode> getSplitNode() {
		return splitNode;
	}

	public void setSplitNode(List<SplitNode> splitNode) {
		this.splitNode = splitNode;
	}

	public HashSplitStrategy getHashSplitStrategy() {
		return hashSplitStrategy;
	}

	public void setHashSplitStrategy(HashSplitStrategy hashSplitStrategy) {
		this.hashSplitStrategy = hashSplitStrategy;
	}
}
