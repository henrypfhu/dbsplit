package com.robert.dbsplit.core;

public class SplitBizTemplate extends SplitJdbcTemplate implements
		SplitBizOperations {
	public SplitBizTemplate() {
		
	}
	
	public SplitBizTemplate(SplitTablesHolder splitTablesHolder,
			boolean readWriteSeparate) {
		super(splitTablesHolder, readWriteSeparate);
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
