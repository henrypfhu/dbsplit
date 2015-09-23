package com.robert.dbsplit.single;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.robert.dbsplit.util.FieldHandler;
import com.robert.dbsplit.util.FieldVisitor;
import com.robert.dbsplit.util.OrmUtil;

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

				params.add(value);
			}
		});

		sb.append(") values (");
		sb.append(OrmUtil.generateParamPlaceholders(params.size()));
		sb.append(")");

		log.debug("The bean class: {} ---> the SQL: {}", bean.getClass()
				.getName(), sb.toString());

		log.debug("The bean: {} ---> Params: {}", bean, params);

		this.update(sb.toString(), params.toArray());
	}

	public <T> void update(T bean) {

	}

	public void delete(long id) {
		// TODO Auto-generated method stub

	}

	public <T> T get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
