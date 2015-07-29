package com.robert.dbsplit.core;

public interface SplitStrategy {
	public <K> int getNodeNo(K splitKey);

	public <K> int getDbNo(K splitKey);

	public <K> int getTableNo(K splitKey);
}
