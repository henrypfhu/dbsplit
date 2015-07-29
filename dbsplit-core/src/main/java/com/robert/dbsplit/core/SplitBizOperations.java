package com.robert.dbsplit.core;

public interface SplitBizOperations extends SplitJdbcOperations {
	public <T, K> void insert(K splitKey, T object);

	public <T, K> void update(K splitKey, T object);

	public <T, K> void delete(K splitKey, long id);
}
