package com.robert.dbsplit.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
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

public interface SplitJdbcOperations {
	public <T, K> T execute(K splitKey, ConnectionCallback<T> action)
			throws DataAccessException;

	public <T, K> T execute(K splitKey, StatementCallback<T> action)
			throws DataAccessException;

	public <K> void execute(K splitKey, String sql) throws DataAccessException;

	public <T, K> T query(K splitKey, String sql, ResultSetExtractor<T> rse)
			throws DataAccessException;

	public <K> void query(K splitKey, String sql, RowCallbackHandler rch)
			throws DataAccessException;

	public <T, K> List<T> query(K splitKey, String sql, RowMapper<T> rowMapper)
			throws DataAccessException;

	public <T, K> T queryForObject(K splitKey, String sql,
			RowMapper<T> rowMapper) throws DataAccessException;

	public <T, K> T queryForObject(K splitKey, String sql, Class<T> requiredType)
			throws DataAccessException;

	public <K> Map<String, Object> queryForMap(K splitKey, String sql)
			throws DataAccessException;

	public <T, K> List<T> queryForList(K splitKey, String sql,
			Class<T> elementType) throws DataAccessException;

	public <K> List<Map<String, Object>> queryForList(K splitKey, String sql)
			throws DataAccessException;

	public <K> SqlRowSet queryForRowSet(K splitKey, String sql)
			throws DataAccessException;

	public <K> int update(K splitKey, String sql) throws DataAccessException;

	public <K> int[] batchUpdate(K splitKey, String... sql)
			throws DataAccessException;

	public <T, K> T execute(K splitKey, PreparedStatementCreator psc,
			PreparedStatementCallback<T> action) throws DataAccessException;

	public <T, K> T execute(K splitKey, String sql,
			PreparedStatementCallback<T> action) throws DataAccessException;

	public <T, K> T query(K splitKey, PreparedStatementCreator psc,
			ResultSetExtractor<T> rse) throws DataAccessException;

	public <T, K> T query(K splitKey, String sql, PreparedStatementSetter pss,
			ResultSetExtractor<T> rse) throws DataAccessException;

	public <T, K> T query(K splitKey, String sql, Object[] args,
			int[] argTypes, ResultSetExtractor<T> rse)
			throws DataAccessException;

	public <T, K> T query(K splitKey, String sql, Object[] args,
			ResultSetExtractor<T> rse) throws DataAccessException;

	public <T, K> T query(K splitKey, String sql, ResultSetExtractor<T> rse,
			Object... args) throws DataAccessException;

	public <K> void query(K splitKey, PreparedStatementCreator psc,
			RowCallbackHandler rch) throws DataAccessException;

	public <K> void query(K splitKey, String sql, PreparedStatementSetter pss,
			RowCallbackHandler rch) throws DataAccessException;

	public <K> void query(K splitKey, String sql, Object[] args,
			int[] argTypes, RowCallbackHandler rch) throws DataAccessException;

	public <K> void query(K splitKey, String sql, Object[] args,
			RowCallbackHandler rch) throws DataAccessException;

	public <K> void query(K splitKey, String sql, RowCallbackHandler rch,
			Object... args) throws DataAccessException;

	public <T, K> List<T> query(K splitKey, PreparedStatementCreator psc,
			RowMapper<T> rowMapper) throws DataAccessException;

	public <T, K> List<T> query(K splitKey, String sql,
			PreparedStatementSetter pss, RowMapper<T> rowMapper)
			throws DataAccessException;

	public <T, K> List<T> query(K splitKey, String sql, Object[] args,
			int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException;

	public <T, K> List<T> query(K splitKey, String sql, Object[] args,
			RowMapper<T> rowMapper) throws DataAccessException;

	public <T, K> List<T> query(K splitKey, String sql, RowMapper<T> rowMapper,
			Object... args) throws DataAccessException;

	public <T, K> T queryForObject(K splitKey, String sql, Object[] args,
			int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException;

	public <T, K> T queryForObject(K splitKey, String sql, Object[] args,
			RowMapper<T> rowMapper) throws DataAccessException;

	public <T, K> T queryForObject(K splitKey, String sql,
			RowMapper<T> rowMapper, Object... args) throws DataAccessException;

	public <T, K> T queryForObject(K splitKey, String sql, Object[] args,
			int[] argTypes, Class<T> requiredType) throws DataAccessException;

	public <T, K> T queryForObject(K splitKey, String sql, Object[] args,
			Class<T> requiredType) throws DataAccessException;

	public <T, K> T queryForObject(K splitKey, String sql,
			Class<T> requiredType, Object... args) throws DataAccessException;

	public <K> Map<String, Object> queryForMap(K splitKey, String sql,
			Object[] args, int[] argTypes) throws DataAccessException;

	public <K> Map<String, Object> queryForMap(K splitKey, String sql,
			Object... args) throws DataAccessException;

	public <T, K> List<T> queryForList(K splitKey, String sql, Object[] args,
			int[] argTypes, Class<T> elementType) throws DataAccessException;

	public <T, K> List<T> queryForList(K splitKey, String sql, Object[] args,
			Class<T> elementType) throws DataAccessException;

	public <T, K> List<T> queryForList(K splitKey, String sql,
			Class<T> elementType, Object... args) throws DataAccessException;

	public <K> List<Map<String, Object>> queryForList(K splitKey, String sql,
			Object[] args, int[] argTypes) throws DataAccessException;

	public <K> List<Map<String, Object>> queryForList(K splitKey, String sql,
			Object... args) throws DataAccessException;

	public <K> SqlRowSet queryForRowSet(K splitKey, String sql, Object[] args,
			int[] argTypes) throws DataAccessException;

	public <K> SqlRowSet queryForRowSet(K splitKey, String sql, Object... args)
			throws DataAccessException;

	public <K> int update(K splitKey, PreparedStatementCreator psc)
			throws DataAccessException;

	public <K> int update(K splitKey, PreparedStatementCreator psc,
			KeyHolder generatedKeyHolder) throws DataAccessException;

	public <K> int update(K splitKey, String sql, PreparedStatementSetter pss)
			throws DataAccessException;

	public <K> int update(K splitKey, String sql, Object[] args, int[] argTypes)
			throws DataAccessException;

	public <K> int update(K splitKey, String sql, Object... args)
			throws DataAccessException;

	public <K> int[] batchUpdate(K splitKey, String sql,
			BatchPreparedStatementSetter pss) throws DataAccessException;

	public <K> int[] batchUpdate(K splitKey, String sql,
			List<Object[]> batchArgs) throws DataAccessException;

	public <K> int[] batchUpdate(K splitKey, String sql,
			List<Object[]> batchArgs, int[] argTypes)
			throws DataAccessException;

	public <T, K> int[][] batchUpdate(K splitKey, String sql,
			Collection<T> batchArgs, int batchSize,
			ParameterizedPreparedStatementSetter<T> pss)
			throws DataAccessException;

	public <T, K> T execute(K splitKey, CallableStatementCreator csc,
			CallableStatementCallback<T> action) throws DataAccessException;

	public <T, K> T execute(K splitKey, String callString,
			CallableStatementCallback<T> action) throws DataAccessException;

	public <K> Map<String, Object> call(K splitKey,
			CallableStatementCreator csc, List<SqlParameter> declaredParameters)
			throws DataAccessException;
}
