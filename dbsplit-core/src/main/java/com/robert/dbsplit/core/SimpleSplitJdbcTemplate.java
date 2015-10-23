package com.robert.dbsplit.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.robert.dbsplit.core.sql.util.OrmUtil;
import com.robert.dbsplit.core.sql.util.SqlUtil;
import com.robert.dbsplit.core.sql.util.SqlUtil.SqlRunningBean;

public class SimpleSplitJdbcTemplate extends SplitJdbcTemplate implements
		SimpleSplitJdbcOperations {

	private enum UpdateOper {
		INSERT, UPDATE, DELETE
	};

	private enum SearchOper {
		NORMAL, RANGE, FIELD
	};

	public SimpleSplitJdbcTemplate() {

	}

	public SimpleSplitJdbcTemplate(List<String> ipPorts, String user,
			String password, String... tables) {
		super(ipPorts, user, password, tables);
	}

	public SimpleSplitJdbcTemplate(SplitTablesHolder splitTablesHolder) {
		super(splitTablesHolder);
	}

	public <K, T> void insert(K splitKey, T bean) {
		doUpdate(splitKey, bean.getClass(), UpdateOper.INSERT, bean, -1);
	}

	public <K, T> void update(K splitKey, T bean) {
		doUpdate(splitKey, bean.getClass(), UpdateOper.UPDATE, bean, -1);
	}

	public <K, T> void delete(K splitKey, long id, Class<T> clazz) {
		doUpdate(splitKey, clazz, UpdateOper.DELETE, null, id);
	}

	public <K, T> T get(K splitKey, long id, final Class<T> clazz) {
		return doSelect(splitKey, clazz, "id", new Long(id));
	}

	public <K, T> T get(K splitKey, String name, String value,
			final Class<T> clazz) {
		return doSelect(splitKey, clazz, name, value);
	}

	public <K, T> List<T> search(K splitKey, T bean) {
		return doSearch(splitKey, bean, null, null, null, SearchOper.NORMAL);
	}

	public <K, T> List<T> search(K splitKey, T bean, String name,
			Object valueFrom, Object valueTo) {
		return doSearch(splitKey, bean, name, valueFrom, valueTo,
				SearchOper.RANGE);
	}

	public <K, T> List<T> search(K splitKey, T bean, String name, Object value) {
		return doSearch(splitKey, bean, name, value, null, SearchOper.FIELD);
	}

	protected <K, T> List<T> doSearch(K splitKey, final T bean, String name,
			Object valueFrom, Object valueTo, SearchOper searchOper) {
		log.debug(
				"SimpleSplitJdbcTemplate.doSearch, the split key: {}, the bean: {}, the name: {}, the valueFrom: {}, the valueTo: {}.",
				splitKey, bean, name, valueFrom, valueTo);

		SplitTable splitTable = splitTablesHolder.searchSplitTable(OrmUtil
				.javaClassName2DbTableName(bean.getClass().getSimpleName()));

		SplitStrategy splitStrategy = splitTable.getSplitStrategy();
		List<SplitNode> splitNdoes = splitTable.getSplitNodes();

		String dbPrefix = splitTable.getDbNamePrefix();
		String tablePrefix = splitTable.getTableNamePrefix();

		int nodeNo = splitStrategy.getNodeNo(splitKey);
		int dbNo = splitStrategy.getDbNo(splitKey);
		int tableNo = splitStrategy.getTableNo(splitKey);

		log.info(
				"SimpleSplitJdbcTemplate.doSearch, splitKey={} dbPrefix={} tablePrefix={} nodeNo={} dbNo={} tableNo={}.",
				splitKey, dbPrefix, tablePrefix, nodeNo, dbNo, tableNo);

		SplitNode sn = splitNdoes.get(nodeNo);
		JdbcTemplate jt = getReadJdbcTemplate(sn);

		SqlRunningBean srb = null;

		switch (searchOper) {
		case NORMAL:
			srb = SqlUtil.generateSearchSql(bean, dbPrefix, tablePrefix, dbNo,
					tableNo);
			break;
		case RANGE:
			srb = SqlUtil.generateSearchSql(bean, name, valueFrom, valueTo,
					dbPrefix, tablePrefix, dbNo, tableNo);
			break;
		case FIELD:
			srb = SqlUtil.generateSearchSql(bean, name, valueFrom, dbPrefix,
					tablePrefix, dbNo, tableNo);
			break;
		}

		log.debug(
				"SimpleSplitJdbcTemplate.doSearch, the split SQL: {}, the split params: {}.",
				srb.getSql(), srb.getParams());
		List<T> beans = jt.query(srb.getSql(), srb.getParams(),
				new RowMapper<T>() {
					public T mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return (T) OrmUtil.convertRow2Bean(rs, bean.getClass());
					}
				});

		log.info("SimpleSplitJdbcTemplate.doSearch, search result: {}.", beans);
		return beans;

	}

	protected <K, T> T doSelect(K splitKey, final Class<T> clazz, String name,
			Object value) {
		log.debug(
				"SimpleSplitJdbcTemplate.doSelect, the split key: {}, the clazz: {}, where {} = {}.",
				splitKey, clazz, name, value);

		SplitTable splitTable = splitTablesHolder.searchSplitTable(OrmUtil
				.javaClassName2DbTableName(clazz.getSimpleName()));

		SplitStrategy splitStrategy = splitTable.getSplitStrategy();
		List<SplitNode> splitNdoes = splitTable.getSplitNodes();

		String dbPrefix = splitTable.getDbNamePrefix();
		String tablePrefix = splitTable.getTableNamePrefix();

		int nodeNo = splitStrategy.getNodeNo(splitKey);
		int dbNo = splitStrategy.getDbNo(splitKey);
		int tableNo = splitStrategy.getTableNo(splitKey);

		log.info(
				"SimpleSplitJdbcTemplate.doSelect, splitKey={} dbPrefix={} tablePrefix={} nodeNo={} dbNo={} tableNo={}.",
				splitKey, dbPrefix, tablePrefix, nodeNo, dbNo, tableNo);

		SplitNode sn = splitNdoes.get(nodeNo);
		JdbcTemplate jt = getReadJdbcTemplate(sn);

		SqlRunningBean srb = SqlUtil.generateSelectSql(name, value, clazz,
				dbPrefix, tablePrefix, dbNo, tableNo);

		log.debug(
				"SimpleSplitJdbcTemplate.doSelect, the split SQL: {}, the split params: {}.",
				srb.getSql(), srb.getParams());
		T bean = jt.queryForObject(srb.getSql(), srb.getParams(),
				new RowMapper<T>() {
					public T mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return OrmUtil.convertRow2Bean(rs, clazz);
					}
				});

		log.info("SimpleSplitJdbcTemplate.doSelect, select result: {}.", bean);
		return bean;
	}

	protected <K, T> void doUpdate(K splitKey, final Class<?> clazz,
			UpdateOper updateOper, T bean, long id) {
		log.debug(
				"SimpleSplitJdbcTemplate.doUpdate, the split key: {}, the clazz: {}, the updateOper: {}, the split bean: {}, the ID: {}.",
				splitKey, clazz, updateOper, bean, id);

		SplitTable splitTable = splitTablesHolder.searchSplitTable(OrmUtil
				.javaClassName2DbTableName(clazz.getSimpleName()));

		SplitStrategy splitStrategy = splitTable.getSplitStrategy();
		List<SplitNode> splitNdoes = splitTable.getSplitNodes();

		String dbPrefix = splitTable.getDbNamePrefix();
		String tablePrefix = splitTable.getTableNamePrefix();

		int nodeNo = splitStrategy.getNodeNo(splitKey);
		int dbNo = splitStrategy.getDbNo(splitKey);
		int tableNo = splitStrategy.getTableNo(splitKey);

		log.info(
				"SimpleSplitJdbcTemplate.doUpdate, splitKey={} dbPrefix={} tablePrefix={} nodeNo={} dbNo={} tableNo={}.",
				splitKey, dbPrefix, tablePrefix, nodeNo, dbNo, tableNo);

		SplitNode sn = splitNdoes.get(nodeNo);
		JdbcTemplate jt = getWriteJdbcTemplate(sn);

		SqlRunningBean srb = null;
		switch (updateOper) {
		case INSERT:
			srb = SqlUtil.generateInsertSql(bean, dbPrefix, tablePrefix, dbNo,
					tableNo);
			break;
		case UPDATE:
			srb = SqlUtil.generateUpdateSql(bean, dbPrefix, tablePrefix, dbNo,
					tableNo);
			break;
		case DELETE:
			srb = SqlUtil.generateDeleteSql(id, clazz, dbPrefix, tablePrefix,
					dbNo, tableNo);
			break;
		}

		log.debug(
				"SimpleSplitJdbcTemplate.doUpdate, the split SQL: {}, the split params: {}.",
				srb.getSql(), srb.getParams());
		long updateCount = jt.update(srb.getSql(), srb.getParams());
		log.info("SimpleSplitJdbcTemplate.doUpdate, update record num: {}.",
				updateCount);
	}
}
