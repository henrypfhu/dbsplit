package com.robert.dbsplit.single;

public interface SimpleJdbcOperations {
	public <T> void insert(T bean);

	public <T> void update(T bean);

	public <T> void delete(long id, Class<T> clazz);

	public <T> T get(long id, final Class<T> clazz);

	public <T> T get(String name, String value, final Class<T> clazz);
}
