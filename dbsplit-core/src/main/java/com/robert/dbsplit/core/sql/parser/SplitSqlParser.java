package com.robert.dbsplit.core.sql.parser;

public interface SplitSqlParser {
	public static final SplitSqlParser INST = new SplitSqlParserDefImpl();

	public SplitSqlStructure parseSplitSql(String sql);
}
