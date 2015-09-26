package com.robert.dbsplit.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.robert.dbsplit.util.OrmUtil;
import com.robert.dbsplit.util.SqlUtil;
import com.robert.dbsplit.util.SqlUtil.SqlRunningBean;

public class SimpleSplitJdbcTemplate extends SplitJdbcTemplate implements
		SimpleSplitJdbcOperations {
	private static final Logger log = LoggerFactory
			.getLogger(SimpleSplitJdbcTemplate.class);

	private enum UpdateOper {
		INSERT, UPDATE, DELETE
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
		JdbcTemplate jt = sn.getMasterTemplate();

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
		JdbcTemplate jt = sn.getMasterTemplate();

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
