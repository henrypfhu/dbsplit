package com.robert.dbsplit.core;

import java.util.List;

public interface SimpleSplitJdbcOperations extends SplitJdbcOperations {

	public <K, T> void insert(K splitKey, T bean);

	public <K, T> void update(K splitKey, T bean);

	public <K, T> void delete(K splitKey, long id, Class<T> clazz);

	public <K, T> T get(K splitKey, long id, final Class<T> clazz);

	public <K, T> T get(K splitKey, String key, String value,
			final Class<T> clazz);

	public <K, T> List<T> search(K splitKey, T bean);

	public <K, T> List<T> search(K splitKey, T bean, String name,
			Object valueFrom, Object valueTo);

	public <K, T> List<T> search(K splitKey, T bean, String name, Object value);
}
