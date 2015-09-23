package com.robert.dbsplit.single;

public interface SimpleJdbcOperations {
	public <T> void insert(T bean);

	public <T> void update(T bean);

	public void delete(long id);

	public <T> T get(long id);
}
