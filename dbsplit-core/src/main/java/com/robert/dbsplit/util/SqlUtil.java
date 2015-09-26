package com.robert.dbsplit.util;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.robert.dbsplit.util.reflect.FieldHandler;
import com.robert.dbsplit.util.reflect.FieldVisitor;

public abstract class SqlUtil {
	public static class SqlRunningBean {
		private String sql;
		private Object[] params;

		public SqlRunningBean(String sql, Object[] params) {
			this.sql = sql;
			this.params = params;
		}

		public String getSql() {
			return sql;
		}

		public void setSql(String sql) {
			this.sql = sql;
		}

		public Object[] getParams() {
			return params;
		}

		public void setParams(Object[] params) {
			this.params = params;
		}
	}

	public static <T> SqlRunningBean generateInsertSql(T bean,
			String databasePrefix, String tablePrefix, int dbIndex,
			int tableIndex) {
		final StringBuilder sb = new StringBuilder();
		sb.append("insert into ");

		if (!StringUtils.isEmpty(databasePrefix))
			sb.append(databasePrefix);

		if (dbIndex != -1)
			sb.append("_").append(dbIndex).append(".");

		if (StringUtils.isEmpty(tablePrefix))
			tablePrefix = OrmUtil.javaClassName2DbTableName(bean.getClass()
					.getSimpleName());

		sb.append(tablePrefix);

		if (tableIndex != -1)
			sb.append("_").append(tableIndex).append(" ");

		sb.append("(");

		final List<Object> params = new LinkedList<Object>();

		new FieldVisitor<T>(bean).visit(new FieldHandler() {
			public void handle(int index, Field field, Object value) {
				if (index != 0)
					sb.append(",");

				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName()));

				if (value instanceof Enum)
					value = ((Enum<?>) value).ordinal();

				params.add(value);
			}
		});

		sb.append(") values (");
		sb.append(OrmUtil.generateParamPlaceholders(params.size()));
		sb.append(")");

		return new SqlRunningBean(sb.toString(), params.toArray());
	}

	public static <T> SqlRunningBean generateInsertSql(T bean) {
		return generateInsertSql(bean, null, null, -1, -1);
	}

	public static <T> SqlRunningBean generateInsertSql(T bean,
			String databasePrefix) {
		return generateInsertSql(bean, databasePrefix, null, -1, -1);
	}

	public static <T> SqlRunningBean generateInsertSql(T bean,
			String databasePrefix, String tablePrefix) {
		return generateInsertSql(bean, databasePrefix, tablePrefix, -1, -1);
	}

}
