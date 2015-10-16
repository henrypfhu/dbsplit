package com.robert.dbsplit.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.robert.dbsplit.excep.NotSupportedException;
import com.robert.dbsplit.util.SqlUtil;

public class SplitJdbcTemplate implements SplitJdbcOperations {
	protected SplitTablesHolder splitTablesHolder;

	protected boolean readWriteSeparate = false;

	public SplitJdbcTemplate() {

	}

	public SplitJdbcTemplate(SplitTablesHolder splitTablesHolder) {
		this.splitTablesHolder = splitTablesHolder;
	}

	public SplitJdbcTemplate(List<String> ipPorts, String user,
			String password, String... tables) {
		this.addTable(ipPorts, user, password, tables);
	}

	public void addTable(List<String> ipPorts, String user, String password,
			String... tables) {
		// TODO parse datasources and tables
	}

	public <T, K> T execute(K splitKey, ConnectionCallback<T> action)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T execute(K splitKey, StatementCallback<T> action)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> void execute(K splitKey, String sql) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T query(K splitKey, String sql, ResultSetExtractor<T> rse)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> void query(K splitKey, String sql, RowCallbackHandler rch)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> query(K splitKey, String sql, RowMapper<T> rowMapper)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T queryForObject(K splitKey, String sql,
			RowMapper<T> rowMapper) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T queryForObject(K splitKey, String sql, Class<T> requiredType)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> Map<String, Object> queryForMap(K splitKey, String sql)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> queryForList(K splitKey, String sql,
			Class<T> elementType) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> List<Map<String, Object>> queryForList(K splitKey, String sql)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> SqlRowSet queryForRowSet(K splitKey, String sql)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> int update(K splitKey, String sql) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> int[] batchUpdate(K splitKey, String... sql)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T execute(K splitKey, PreparedStatementCreator psc,
			PreparedStatementCallback<T> action) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T execute(K splitKey, String sql,
			PreparedStatementCallback<T> action) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T query(K splitKey, PreparedStatementCreator psc,
			ResultSetExtractor<T> rse) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T query(K splitKey, String sql, PreparedStatementSetter pss,
			ResultSetExtractor<T> rse) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T query(K splitKey, String sql, Object[] args,
			int[] argTypes, ResultSetExtractor<T> rse)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T query(K splitKey, String sql, Object[] args,
			ResultSetExtractor<T> rse) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T query(K splitKey, String sql, ResultSetExtractor<T> rse,
			Object... args) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> void query(K splitKey, PreparedStatementCreator psc,
			RowCallbackHandler rch) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> void query(K splitKey, String sql, PreparedStatementSetter pss,
			RowCallbackHandler rch) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> void query(K splitKey, String sql, Object[] args,
			int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> void query(K splitKey, String sql, Object[] args,
			RowCallbackHandler rch) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> void query(K splitKey, String sql, RowCallbackHandler rch,
			Object... args) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> query(K splitKey, PreparedStatementCreator psc,
			RowMapper<T> rowMapper) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> query(K splitKey, String sql,
			PreparedStatementSetter pss, RowMapper<T> rowMapper)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> query(K splitKey, String sql, Object[] args,
			int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> query(K splitKey, String sql, Object[] args,
			RowMapper<T> rowMapper) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> query(K splitKey, String sql, RowMapper<T> rowMapper,
			Object... args) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T queryForObject(K splitKey, String sql, Object[] args,
			int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T queryForObject(K splitKey, String sql, Object[] args,
			RowMapper<T> rowMapper) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T queryForObject(K splitKey, String sql,
			RowMapper<T> rowMapper, Object... args) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T queryForObject(K splitKey, String sql, Object[] args,
			int[] argTypes, Class<T> requiredType) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T queryForObject(K splitKey, String sql, Object[] args,
			Class<T> requiredType) throws DataAccessException {
		String[] dbTableNames = SqlUtil.getDbTableNamesSelect(sql);
		String dbName = dbTableNames[0];
		String tableName = dbTableNames[1];

		SplitTable splitTable = splitTablesHolder.searchSplitTable(dbName,
				tableName);

		SplitStrategy splitStrategy = splitTable.getSplitStrategy();
		List<SplitNode> splitNdoes = splitTable.getSplitNodes();

		int dbNo = splitStrategy.getDbNo(splitKey);
		int tableNo = splitStrategy.getTableNo(splitKey);
		sql = SqlUtil.splitSelectSql(sql, dbNo, tableNo);

		int nodeNo = splitStrategy.getNodeNo(splitKey);
		SplitNode sn = splitNdoes.get(nodeNo);

		JdbcTemplate jt = null;

		if (splitTable.isReadWriteSeparate())
			jt = sn.getRoundRobinSlaveTempate();
		else
			jt = sn.getMasterTemplate();

		return jt.queryForObject(sql, args, requiredType);
	}

	public <T, K> T queryForObject(K splitKey, String sql,
			Class<T> requiredType, Object... args) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> Map<String, Object> queryForMap(K splitKey, String sql,
			Object[] args, int[] argTypes) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> Map<String, Object> queryForMap(K splitKey, String sql,
			Object... args) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> queryForList(K splitKey, String sql, Object[] args,
			int[] argTypes, Class<T> elementType) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> queryForList(K splitKey, String sql, Object[] args,
			Class<T> elementType) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> List<T> queryForList(K splitKey, String sql,
			Class<T> elementType, Object... args) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> List<Map<String, Object>> queryForList(K splitKey, String sql,
			Object[] args, int[] argTypes) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> List<Map<String, Object>> queryForList(K splitKey, String sql,
			Object... args) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> SqlRowSet queryForRowSet(K splitKey, String sql, Object[] args,
			int[] argTypes) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> SqlRowSet queryForRowSet(K splitKey, String sql, Object... args)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> int update(K splitKey, PreparedStatementCreator psc)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> int update(K splitKey, PreparedStatementCreator psc,
			KeyHolder generatedKeyHolder) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> int update(K splitKey, String sql, PreparedStatementSetter pss)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> int update(K splitKey, String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> int update(K splitKey, String sql, Object... args)
			throws DataAccessException {
		String[] dbTableNames = SqlUtil.getDbTableNamesUpdate(sql);
		String dbName = dbTableNames[0];
		String tableName = dbTableNames[1];

		SplitTable splitTable = splitTablesHolder.searchSplitTable(dbName,
				tableName);

		SplitStrategy splitStrategy = splitTable.getSplitStrategy();
		List<SplitNode> splitNdoes = splitTable.getSplitNodes();

		int dbNo = splitStrategy.getDbNo(splitKey);
		int tableNo = splitStrategy.getTableNo(splitKey);
		sql = SqlUtil.splitUpdateSql(sql, dbNo, tableNo);

		int nodeNo = splitStrategy.getNodeNo(splitKey);
		SplitNode sn = splitNdoes.get(nodeNo);

		JdbcTemplate jt = sn.getMasterTemplate();

		return jt.update(sql, args);

	}

	public <K> int[] batchUpdate(K splitKey, String sql,
			BatchPreparedStatementSetter pss) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> int[] batchUpdate(K splitKey, String sql,
			List<Object[]> batchArgs) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> int[] batchUpdate(K splitKey, String sql,
			List<Object[]> batchArgs, int[] argTypes)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> int[][] batchUpdate(K splitKey, String sql,
			Collection<T> batchArgs, int batchSize,
			ParameterizedPreparedStatementSetter<T> pss)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T execute(K splitKey, CallableStatementCreator csc,
			CallableStatementCallback<T> action) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <T, K> T execute(K splitKey, String callString,
			CallableStatementCallback<T> action) throws DataAccessException {
		throw new NotSupportedException();
	}

	public <K> Map<String, Object> call(K splitKey,
			CallableStatementCreator csc, List<SqlParameter> declaredParameters)
			throws DataAccessException {
		throw new NotSupportedException();
	}

	public SplitTablesHolder getSplitTablesHolder() {
		return splitTablesHolder;
	}

	public void setSplitTablesHolder(SplitTablesHolder splitTablesHolder) {
		this.splitTablesHolder = splitTablesHolder;
	}

	public boolean isReadWriteSeparate() {
		return readWriteSeparate;
	}

	public void setReadWriteSeparate(boolean readWriteSeparate) {
		this.readWriteSeparate = readWriteSeparate;
	}

	protected JdbcTemplate getWriteJdbcTemplate(SplitNode sn) {
		return getJdbcTemplate(sn, false);
	}

	protected JdbcTemplate getReadJdbcTemplate(SplitNode sn) {
		return getJdbcTemplate(sn, true);
	}

	protected JdbcTemplate getJdbcTemplate(SplitNode sn, boolean read) {
		if (!read)
			return sn.getMasterTemplate();

		if (readWriteSeparate)
			return sn.getRoundRobinSlaveTempate();

		return sn.getMasterTemplate();
	}
}
