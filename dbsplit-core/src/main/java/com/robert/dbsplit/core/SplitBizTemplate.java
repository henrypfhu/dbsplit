package com.robert.dbsplit.core;

import java.util.List;

public class SplitBizTemplate extends SplitJdbcTemplate implements
		SplitBizOperations {
	public SplitBizTemplate(SplitStrategy dbSplitStrategy,
			List<SplitNode> dbNodes, List<SplitTable> dbCells,
			boolean readWriteSeparate) {
		super(dbSplitStrategy, dbNodes, dbCells, readWriteSeparate);
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
