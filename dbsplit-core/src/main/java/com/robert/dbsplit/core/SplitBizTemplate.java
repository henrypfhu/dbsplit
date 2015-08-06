package com.robert.dbsplit.core;

import java.util.List;

public class SplitBizTemplate extends SplitJdbcTemplate implements
		SplitBizOperations {
	public SplitBizTemplate() {

	}

	public SplitBizTemplate(List<String> ipPorts, String user, String password,
			String... tables) {
		super(ipPorts, user, password, tables);
	}

	public SplitBizTemplate(SplitTablesHolder splitTablesHolder) {
		super(splitTablesHolder);
	}

	public <T, K> void insert(K splitKey, T object) {
		// TODO Auto-generated method stub
	}

	public <T, K> void update(K splitKey, T object) {
		// TODO Auto-generated method stub
	}

	public <T, K> void delete(K splitKey, long id) {
		// TODO Auto-generated method stub
	}
}
