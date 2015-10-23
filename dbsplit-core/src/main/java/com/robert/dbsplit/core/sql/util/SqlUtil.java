package com.robert.dbsplit.core.sql.util;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.robert.dbsplit.util.reflect.FieldHandler;
import com.robert.dbsplit.util.reflect.FieldVisitor;
import com.robert.dbsplit.util.reflect.ReflectionUtil;

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
			String databasePrefix, String tablePrefix, int databseIndex,
			int tableIndex) {
		final StringBuilder sb = new StringBuilder();
		sb.append("insert into ");

		if (StringUtils.isEmpty(tablePrefix))
			tablePrefix = OrmUtil.javaClassName2DbTableName(bean.getClass()
					.getSimpleName());

		sb.append(getQualifiedTableName(databasePrefix, tablePrefix,
				databseIndex, tableIndex));

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

	public static <T> SqlRunningBean generateUpdateSql(T bean,
			String databasePrefix, String tablePrefix, int databaseIndex,
			int tableIndex) {
		final StringBuilder sb = new StringBuilder();
		sb.append(" update ");

		if (StringUtils.isEmpty(tablePrefix))
			tablePrefix = OrmUtil.javaClassName2DbTableName(bean.getClass()
					.getSimpleName());

		sb.append(getQualifiedTableName(databasePrefix, tablePrefix,
				databaseIndex, tableIndex));

		sb.append(" set ");

		final List<Object> params = new LinkedList<Object>();

		new FieldVisitor<T>(bean).visit(new FieldHandler() {
			public void handle(int index, Field field, Object value) {
				if (index != 0)
					sb.append(", ");

				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName()))
						.append("=? ");

				if (value instanceof Enum)
					value = ((Enum<?>) value).ordinal();

				params.add(value);
			}
		});

		sb.append(" where ID = ?");

		params.add(ReflectionUtil.getFieldValue(bean, "id"));

		return new SqlRunningBean(sb.toString(), params.toArray());
	}

	public static <T> SqlRunningBean generateUpdateSql(T bean) {
		return generateUpdateSql(bean, null, null, -1, -1);
	}

	public static <T> SqlRunningBean generateUpdateSql(T bean,
			String databasePrefix) {
		return generateUpdateSql(bean, databasePrefix, null, -1, -1);
	}

	public static <T> SqlRunningBean generateUpdateSql(T bean,
			String databasePrefix, String tablePrefix) {
		return generateUpdateSql(bean, databasePrefix, tablePrefix, -1, -1);
	}

	public static <T> SqlRunningBean generateDeleteSql(long id, Class<T> clazz,
			String databasePrefix, String tablePrefix, int databaseIndex,
			int tableIndex) {
		final StringBuilder sb = new StringBuilder();
		sb.append("delete from ");

		if (StringUtils.isEmpty(tablePrefix))
			tablePrefix = OrmUtil.javaClassName2DbTableName(clazz
					.getSimpleName());

		sb.append(getQualifiedTableName(databasePrefix, tablePrefix,
				databaseIndex, tableIndex));

		sb.append(" where ID = ?");

		List<Object> params = new LinkedList<Object>();
		params.add(id);

		return new SqlRunningBean(sb.toString(), params.toArray());
	}

	public static <T> SqlRunningBean generateDeleteSql(long id, Class<T> clazz) {
		return generateDeleteSql(id, clazz, null, null, -1, -1);
	}

	public static <T> SqlRunningBean generateDeleteSql(long id, Class<T> clazz,
			String databasePrefix) {
		return generateDeleteSql(id, clazz, databasePrefix, null, -1, -1);
	}

	public static <T> SqlRunningBean generateDeleteSql(long id, Class<T> clazz,
			String databasePrefix, String tablePrefix) {
		return generateDeleteSql(id, clazz, databasePrefix, tablePrefix, -1, -1);
	}

	public static <T> SqlRunningBean generateSelectSql(String name,
			Object value, Class<T> clazz, String databasePrefix,
			String tablePrefix, int databaseIndex, int tableIndex) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");

		if (StringUtils.isEmpty(tablePrefix))
			tablePrefix = OrmUtil.javaClassName2DbTableName(clazz
					.getSimpleName());

		sb.append(getQualifiedTableName(databasePrefix, tablePrefix,
				databaseIndex, tableIndex));

		sb.append(" where ");
		sb.append(name).append("=?");

		List<Object> params = new LinkedList<Object>();
		params.add(value);

		return new SqlRunningBean(sb.toString(), params.toArray());
	}

	public static <T> SqlRunningBean generateSelectSql(String name,
			Object value, Class<T> clazz) {
		return generateSelectSql(name, value, clazz, null, null, -1, -1);
	}

	public static <T> SqlRunningBean generateSelectSql(String name,
			Object value, Class<T> clazz, String databasePrefix) {
		return generateSelectSql(name, value, clazz, databasePrefix, null, -1,
				-1);
	}

	public static <T> SqlRunningBean generateSelectSql(String name,
			Object value, Class<T> clazz, String databasePrefix,
			String tablePrefix) {
		return generateSelectSql(name, value, clazz, databasePrefix,
				tablePrefix, -1, -1);
	}

	public static <T> SqlRunningBean generateSearchSql(T bean, String name,
			Object valueFrom, Object valueTo, String databasePrefix,
			String tablePrefix, int databaseIndex, int tableIndex) {
		final StringBuilder sb = new StringBuilder();
		sb.append("select * from ");

		if (StringUtils.isEmpty(tablePrefix))
			tablePrefix = OrmUtil.javaClassName2DbTableName(bean.getClass()
					.getSimpleName());
		sb.append(getQualifiedTableName(databasePrefix, tablePrefix,
				databaseIndex, tableIndex));

		sb.append(" where ");

		final List<Object> params = new LinkedList<Object>();

		new FieldVisitor<T>(bean).visit(new FieldHandler() {
			public void handle(int index, Field field, Object value) {
				if (index != 0)
					sb.append(" and ");

				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName()))
						.append("=? ");

				if (value instanceof Enum)
					value = ((Enum<?>) value).ordinal();

				params.add(value);
			}
		});

		if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(valueFrom)
				&& !StringUtils.isEmpty(valueTo)) {
			sb.append(" and ").append(name).append(">=? and ");
			sb.append(name).append("<=? ");
			params.add(valueFrom);
			params.add(valueTo);
		} else if (!StringUtils.isEmpty(name)
				&& !StringUtils.isEmpty(valueFrom)
				&& StringUtils.isEmpty(valueTo)) {
			sb.append(" and ").append(name).append("=? ");
			params.add(valueFrom);
		}

		return new SqlRunningBean(sb.toString(), params.toArray());
	}

	public static <T> SqlRunningBean generateSearchSql(T bean,
			String databasePrefix, String tablePrefix, int databaseIndex,
			int tableIndex) {
		return generateSearchSql(bean, null, null, null, databasePrefix,
				tablePrefix, databaseIndex, tableIndex);
	}

	public static <T> SqlRunningBean generateSearchSql(T bean, String name,
			Object value, String databasePrefix, String tablePrefix,
			int databaseIndex, int tableIndex) {
		return generateSearchSql(bean, name, value, null, databasePrefix,
				tablePrefix, databaseIndex, tableIndex);
	}

	public static <T> SqlRunningBean generateSearchSql(T bean) {
		return generateSearchSql(bean, null, null, null, null, null, -1, -1);
	}

	private static String getQualifiedTableName(String databasePrefix,
			String tablePrefix, int dbIndex, int tableIndex) {
		StringBuffer sb = new StringBuffer();

		if (!StringUtils.isEmpty(databasePrefix))
			sb.append(databasePrefix);

		if (dbIndex != -1)
			sb.append("_").append(dbIndex).append(".");

		if (!StringUtils.isEmpty(tablePrefix))
			sb.append(tablePrefix);

		if (tableIndex != -1)
			sb.append("_").append(tableIndex).append(" ");

		return sb.toString();
	}
}
