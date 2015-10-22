package com.robert.dbsplit.core;

public class HorizontalHashSplitStrategy implements SplitStrategy {
	private int portNum;
	private int dbNum;
	private int tableNum;

	public HorizontalHashSplitStrategy() {

	}

	public HorizontalHashSplitStrategy(int portNum, int dbNum, int tableNum) {
		this.portNum = portNum;
		this.dbNum = dbNum;
		this.tableNum = tableNum;
	}

	public int getNodeNo(Object splitKey) {
		return getDbNo(splitKey) / dbNum;
	}

	public int getDbNo(Object splitKey) {
		return getTableNo(splitKey) / tableNum;
	}

	public int getTableNo(Object splitKey) {
		int hashCode = calcHashCode(splitKey);
		return hashCode % (portNum * dbNum * tableNum);
	}

	private int calcHashCode(Object splitKey) {
		int hashCode = splitKey.hashCode();
		if (hashCode < 0)
			hashCode = -hashCode;

		return hashCode;
	}
}
