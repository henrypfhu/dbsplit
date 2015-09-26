package com.robert.dbsplit.core;

import java.util.List;

public class SimpleSplitJdbcTemplate extends SplitJdbcTemplate implements
		SimpleSplitJdbcOperations {
	public SimpleSplitJdbcTemplate() {

	}

	public SimpleSplitJdbcTemplate(List<String> ipPorts, String user, String password,
			String... tables) {
		super(ipPorts, user, password, tables);
	}

	public SimpleSplitJdbcTemplate(SplitTablesHolder splitTablesHolder) {
		super(splitTablesHolder);
	}
	public <K, T> void insert(K splitKey, T bean) {
		
	}

	public <K, T> void update(K splitKey, T bean) {
		
	}

	public <K, T> void delete(K splitKey, long id, Class<T> clazz) {
		
	}

	public <K, T> T get(K splitKey, long id, final Class<T> clazz) {
		return null;
	}

	public <K, T> T get(K splitKey, String key, String value, final Class<T> clazz) {
		return null;
	}

}
