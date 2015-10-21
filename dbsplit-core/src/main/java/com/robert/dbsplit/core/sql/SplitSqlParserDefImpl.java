package com.robert.dbsplit.core.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.Token;
import com.robert.dbsplit.core.sql.SplitSqlStructure.SqlType;
import com.robert.dbsplit.excep.NotSupportedException;

public class SplitSqlParserDefImpl implements SplitSqlParser {
	private static final Logger log = LoggerFactory
			.getLogger(SplitSqlParserDefImpl.class);

	public SplitSqlParserDefImpl() {
		log.info("Default SplitSqlParserDefImpl is used.");
	}

	public SplitSqlStructure parseSplitSql(String sql) {
		SplitSqlStructure splitSqlStructure = new SplitSqlStructure();

		String dbName = null;
		String tableName = null;
		boolean inProcess = false;
		
		StringBuffer sbPreviousPart = new StringBuffer();
		StringBuffer sbSebsequentPart = new StringBuffer();

		// Need to opertimize for better performance

		Lexer lexer = new Lexer(sql);
		do {
			lexer.nextToken();
			Token tok = lexer.token();
			if (tok == Token.EOF) {
				break;
			}

			if (!inProcess)
				sbPreviousPart.append(lexer.stringVal()).append(" ");
			else
				sbSebsequentPart.append(lexer.stringVal()).append(" ");
			
			switch (tok.name) {
			case "SELECT":
				splitSqlStructure.setSqlType(SqlType.SELECT);
				break;

			case "INSERT":
				splitSqlStructure.setSqlType(SqlType.INSERT);
				break;

			case "UPDATE":
				inProcess = true;
				splitSqlStructure.setSqlType(SqlType.UPDATE);
				break;

			case "DELETE":
				splitSqlStructure.setSqlType(SqlType.DELETE);
				break;

			case "INTO":
				if (SqlType.INSERT.equals(splitSqlStructure.getSqlType()))
					inProcess = true;
				break;

			case "FROM":
				if (SqlType.SELECT.equals(splitSqlStructure.getSqlType())
						|| SqlType.DELETE
								.equals(splitSqlStructure.getSqlType()))
					inProcess = true;
				break;
			}

			if (inProcess) {
				if (dbName == null && (tok == Token.IDENTIFIER))
					dbName = lexer.stringVal();
				else if (dbName != null && (tok == Token.IDENTIFIER)) {
					tableName = lexer.stringVal();
					break;
				}
			}
		} while (true);

		if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName))
			throw new NotSupportedException("The split sql is not supported: "
					+ sql);

		splitSqlStructure.setDbName(dbName);
		splitSqlStructure.setTableName(tableName);
		
		splitSqlStructure.setPreviousPart(sbPreviousPart.toString());
		splitSqlStructure.setSebsequentPart(sbSebsequentPart.toString());

		return splitSqlStructure;
	}
}
