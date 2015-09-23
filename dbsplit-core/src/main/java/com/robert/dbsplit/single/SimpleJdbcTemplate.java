package com.robert.dbsplit.single;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.robert.dbsplit.util.FieldHandler;
import com.robert.dbsplit.util.FieldVisitor;
import com.robert.dbsplit.util.OrmUtil;
import com.robert.dbsplit.util.ReflectionUtil;

public class SimpleJdbcTemplate extends JdbcTemplate implements
		SimpleJdbcOperations {

	private static final Logger log = LoggerFactory
			.getLogger(SimpleJdbcTemplate.class);

	public <T> void insert(T bean) {
		final StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		sb.append(
				OrmUtil.javaClassName2DbTableName(bean.getClass()
						.getSimpleName())).append(" ");
		sb.append("(");

		final List<Object> params = new LinkedList<Object>();

		new FieldVisitor(bean).visit(new FieldHandler() {
			public void handle(int index, Field field, Object value) {
				if (index != 0)
					sb.append(",");

				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName()));
				
				if (value instanceof Enum ) 
					value = ((Enum<?>)value).ordinal();

				params.add(value);
			}
		});

		sb.append(") values (");
		sb.append(OrmUtil.generateParamPlaceholders(params.size()));
		sb.append(")");

		log.debug("The bean class: {} ---> the SQL: {}", bean.getClass()
				.getName(), sb.toString());

		log.debug("The bean: {} ---> the params: {}", bean, params);

		this.update(sb.toString(), params.toArray());
	}

	public <T> void update(T bean) {
		final StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(
				OrmUtil.javaClassName2DbTableName(bean.getClass()
						.getSimpleName())).append(" ");
		sb.append("set ");

		final List<Object> params = new LinkedList<Object>();

		new FieldVisitor(bean).visit(new FieldHandler() {
			public void handle(int index, Field field, Object value) {
				if (index != 0)
					sb.append(", ");

				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName()))
						.append("=? ");
				
				if (value instanceof Enum ) 
					value = ((Enum<?>)value).ordinal();

				params.add(value);
			}
		});

		sb.append("where ID = ?");

		params.add(ReflectionUtil.getFieldValue(bean, "id"));

		log.debug("The bean class: {} ---> the SQL: {}", bean.getClass()
				.getName(), sb.toString());

		log.debug("The bean: {} ---> the params: {}", bean, params);

		this.update(sb.toString(), params.toArray());
	}

	public <T> void delete(long id, Class<T> clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ");
		sb.append(OrmUtil.javaClassName2DbTableName(clazz.getSimpleName()))
				.append(" ");
		sb.append("where ID = ?");

		this.update(sb.toString(), id);
	}

	public <T> T get(long id, final Class<T> clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(OrmUtil.javaClassName2DbTableName(clazz.getSimpleName()))
				.append(" ");
		sb.append("where ID = ?");

		T bean = this.queryForObject(sb.toString(), new Object[] { id },
				new RowMapper<T>() {
					public T mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return OrmUtil.convertRow2Bean(rs, clazz);
					}
				});
		return bean;
	}

	public <T> T get(String key, String value, final Class<T> clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(OrmUtil.javaClassName2DbTableName(clazz.getSimpleName()))
				.append(" ");
		sb.append("where ");
		sb.append(key).append("=?");

		T bean = this.queryForObject(sb.toString(), new Object[] { value },
				new RowMapper<T>() {
					public T mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return OrmUtil.convertRow2Bean(rs, clazz);
					}
				});
		return bean;
	}

}
