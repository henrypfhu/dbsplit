package com.robert.dbsplit.core;

public class VerticalHashSplitStrategy implements SplitStrategy {
	private int portNum;
	private int dbNum;
	private int tableNum;

	public VerticalHashSplitStrategy() {

	}

	public VerticalHashSplitStrategy(int portNum, int dbNum, int tableNum) {
		this.portNum = portNum;
		this.dbNum = dbNum;
		this.tableNum = tableNum;
	}

	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}

	public int getTableNum() {
		return tableNum;
	}

	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}

	public int getDbNum() {
		return dbNum;
	}

	public void setDbNum(int dbNum) {
		this.dbNum = dbNum;
	}

	public int getNodeNo(Object splitKey) {
		int hashCode = calcHashCode(splitKey);
		return hashCode % portNum;
	}

	public int getDbNo(Object splitKey) {
		int hashCode = calcHashCode(splitKey);
		return hashCode / portNum % dbNum;
	}

	public int getTableNo(Object splitKey) {
		int hashCode = calcHashCode(splitKey);
		return hashCode / portNum / dbNum % tableNum;
	}

	private int calcHashCode(Object splitKey) {
		int hashCode = splitKey.hashCode();
		return hashCode;
	}
}
