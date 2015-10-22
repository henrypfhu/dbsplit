package com.robert.dbsplit.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.robert.dbsplit.core.TestTable.Gender;
import com.robert.vesta.service.intf.IdService;

@ContextConfiguration(locations = "/spring/dbsplit-test.xml")
public class SimpleSplitJdbcTemplateTest extends
		AbstractTestNGSpringContextTests {
	@Test(groups = { "simpleSplitJdbcTemplate" })
	public void testSimpleSplitJdbcTemplate() {
		SimpleSplitJdbcTemplate simpleSplitJdbcTemplate = (SimpleSplitJdbcTemplate) applicationContext
				.getBean("simpleSplitJdbcTemplate");
		IdService idService = (IdService) applicationContext
				.getBean("idService");

		// Make sure the id generated is not align multiple of 1000
		Random random = new Random(new Date().getTime());
		for (int i = 0; i < random.nextInt(16); i++)
			idService.genId();

		long id = idService.genId();
		System.out.println("id:" + id);

		TestTable testTable = new TestTable();
		testTable.setId(id);
		testTable.setName("Alice-" + id);
		testTable.setGender(Gender.MALE);
		testTable.setLstUpdTime(new Date());
		testTable.setLstUpdUser("SYSTEM");

		simpleSplitJdbcTemplate.insert(id, testTable);

		TestTable q = new TestTable();

		TestTable testTable1 = simpleSplitJdbcTemplate.get(id, id,
				TestTable.class);

		AssertJUnit.assertEquals(testTable.getId(), testTable1.getId());
		AssertJUnit.assertEquals(testTable.getName(), testTable1.getName());
		AssertJUnit.assertEquals(testTable.getGender(), testTable1.getGender());
		AssertJUnit.assertEquals(testTable.getLstUpdUser(),
				testTable1.getLstUpdUser());
		// mysql store second as least time unit but java stores miliseconds, so
		// round up the millisends from java time
		AssertJUnit.assertEquals(
				(testTable.getLstUpdTime().getTime() + 500) / 1000 * 1000,
				testTable1.getLstUpdTime().getTime());

		System.out.println("testTable1:" + testTable1);
	}

	@Test(groups = { "splitJdbcTemplate" })
	public void testSplitJdbcTemplate() {
		SimpleSplitJdbcTemplate simpleSplitJdbcTemplate = (SimpleSplitJdbcTemplate) applicationContext
				.getBean("simpleSplitJdbcTemplate");

		IdService idService = (IdService) applicationContext
				.getBean("idService");

		// Make sure the id generated is not align multiple of 1000
		Random random = new Random(new Date().getTime());
		for (int i = 0; i < random.nextInt(16); i++)
			idService.genId();

		long id = idService.genId();
		System.out.println("id:" + id);

		int r = simpleSplitJdbcTemplate
				.update(id,
						"insert into test_db.TEST_TABLE(ID, NAME, GENDER, LST_UPD_USER, LST_UPD_TIME) values (?, ?, ?, ?, ?)",
						new Object[] { id, "test" + id, 0, "test", new Date() });
		AssertJUnit.assertEquals(1, r);

		r = simpleSplitJdbcTemplate.update(id,
				"update test_db.TEST_TABLE set name = ? where id = ?",
				new Object[] { "test1" + id, id });
		AssertJUnit.assertEquals(1, r);

		TestTable tt = simpleSplitJdbcTemplate.queryForObject(id,
				"select * from test_db.TEST_TABLE where id = ?",
				new Object[] { id }, new RowMapper<TestTable>() {
					public TestTable mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						TestTable tt = new TestTable();
						tt.setId(rs.getLong("ID"));
						tt.setName(rs.getString("NAME"));
						tt.setLstUpdUser(rs.getString("LST_UPD_USER"));
						tt.setLstUpdTime(rs.getDate("LST_UPD_TIME"));
						return tt;
					}
				});
		AssertJUnit.assertNotNull(tt);
		AssertJUnit.assertEquals(id, tt.getId());
		AssertJUnit.assertEquals("test1" + id, tt.getName());
		AssertJUnit.assertEquals("test", tt.getLstUpdUser());

		r = simpleSplitJdbcTemplate.update(id,
				"delete from test_db.TEST_TABLE where id = ?",
				new Object[] { id });
		AssertJUnit.assertEquals(1, r);
	}
}
