package com.robert.dbsplit.core.sql;

public interface SplitSqlParser {
	public static final SplitSqlParser INST = new SplitSqlParserDefImpl();

	public SplitSqlStructure parseSplitSql(String sql);
}
