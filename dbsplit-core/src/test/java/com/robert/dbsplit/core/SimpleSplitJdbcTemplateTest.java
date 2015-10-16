package com.robert.dbsplit.core;

import java.util.Date;
import java.util.Random;

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
		/*
		 * int r = simpleSplitJdbcTemplate.update(10,
		 * "update test_db.test_table set name = ? where id = ?", new Object[] {
		 * "test", 1 });
		 */
	}
}
