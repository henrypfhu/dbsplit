package com.robert.dbsplit.single;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.robert.dbsplit.util.OrmUtil;
import com.robert.dbsplit.util.SqlUtil;
import com.robert.dbsplit.util.SqlUtil.SqlRunningBean;

public class SimpleJdbcTemplate extends JdbcTemplate implements
		SimpleJdbcOperations {

	private static final Logger log = LoggerFactory
			.getLogger(SimpleJdbcTemplate.class);

	public <T> void insert(T bean) {
		SqlRunningBean srb = SqlUtil.generateInsertSql(bean);

		log.debug("Insert, the bean: {} ---> the SQL: {} ---> the params: {}",
				bean, srb.getSql(), srb.getParams());

		this.update(srb.getSql(), srb.getParams());
	}

	public <T> void update(T bean) {
		SqlRunningBean srb = SqlUtil.generateUpdateSql(bean);

		log.debug("Update, the bean: {} ---> the SQL: {} ---> the params: {}",
				bean, srb.getSql(), srb.getParams());

		this.update(srb.getSql(), srb.getParams());
	}

	public <T> void delete(long id, Class<T> clazz) {
		SqlRunningBean srb = SqlUtil.generateDeleteSql(id, clazz);

		log.debug("Delete, the bean: {} ---> the SQL: {} ---> the params: {}",
				id, srb.getSql(), srb.getParams());

		this.update(srb.getSql(), srb.getParams());
	}

	public <T> T get(long id, final Class<T> clazz) {
		SqlRunningBean srb = SqlUtil.generateSelectSql("id", id, clazz);

		T bean = this.queryForObject(srb.getSql(), srb.getParams(),
				new RowMapper<T>() {
					public T mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return OrmUtil.convertRow2Bean(rs, clazz);
					}
				});
		return bean;
	}

	public <T> T get(String name, String value, final Class<T> clazz) {
		SqlRunningBean srb = SqlUtil.generateSelectSql(name, value, clazz);

		T bean = this.queryForObject(srb.getSql(), srb.getParams(),
				new RowMapper<T>() {
					public T mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return OrmUtil.convertRow2Bean(rs, clazz);
					}
				});
		return bean;
	}

}
