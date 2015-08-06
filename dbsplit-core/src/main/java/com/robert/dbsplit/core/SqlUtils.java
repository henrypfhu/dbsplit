package com.robert.dbsplit.core;

import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.Token;

public abstract class SqlUtils {
	public static String[] getDbTableNames(String sql) {
		Lexer lexer = new Lexer(sql);

		String dbName = null;
		String tableName = null;
		boolean inProcess = false;

		for (;;) {
			lexer.nextToken();
			Token tok = lexer.token();
			if ("FROM".equals(tok.name))
				inProcess = true;
			else if ("WHERE".equals(tok.name))
				inProcess = false;
			if (inProcess) {
				if (dbName == null && (tok == Token.IDENTIFIER))
					dbName = lexer.stringVal();
				else if (dbName != null && (tok == Token.IDENTIFIER))
					tableName = lexer.stringVal();
			}
			if (tok == Token.EOF) {
				break;
			}
		}


		return new String[]{dbName, tableName};
	}

	public static String splitSelectSql(String sql, int dbNo, int tableNo) {
		Lexer lexer = new Lexer(sql);

		String dbName = null;
		String tableName = null;
		boolean inProcess = false;

		for (;;) {
			lexer.nextToken();
			Token tok = lexer.token();
			if ("FROM".equals(tok.name))
				inProcess = true;
			else if ("WHERE".equals(tok.name))
				inProcess = false;
			if (inProcess) {
				if (dbName == null && (tok == Token.IDENTIFIER))
					dbName = lexer.stringVal();
				else if (dbName != null && (tok == Token.IDENTIFIER))
					tableName = lexer.stringVal();
			}
			if (tok == Token.EOF) {
				break;
			}
		}

		sql = sql.replace(dbName, dbName + "_" + dbNo);
		sql = sql.replace(tableName, tableName + "_" + tableNo);

		return sql;
	}

	public static String splitUpdateSql(String sql, int dbNo, int tableNo) {
		Lexer lexer = new Lexer(sql);

		String dbName = null;
		String tableName = null;
		boolean inProcess = false;

		for (;;) {
			lexer.nextToken();
			Token tok = lexer.token();
			if ("UPDATE".equals(tok.name))
				inProcess = true;
			else if ("SET".equals(tok.name))
				inProcess = false;
			if (inProcess) {
				if (dbName == null && (tok == Token.IDENTIFIER))
					dbName = lexer.stringVal();
				else if (dbName != null && (tok == Token.IDENTIFIER))
					tableName = lexer.stringVal();
			}
			if (tok == Token.EOF) {
				break;
			}
		}

		sql = sql.replace(dbName, dbName + "_" + dbNo);
		sql = sql.replace(tableName, tableName + "_" + tableNo);

		return sql;
	}
}
