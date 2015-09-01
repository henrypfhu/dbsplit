package com.robert.dbsplit.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SplitBizTemplateTest {
	public static void main(String[] args) throws Exception {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"spring/dbsplit-sample.xml");
		SplitBizTemplate splitBizTemplate = (SplitBizTemplate) ac
				.getBean("splitBizTemplate");

		/*
		 * TestTable tt = splitBizTemplate.queryForObject(0,
		 * "select * from test_db.test_table where id = ?", new Integer[] { 1 },
		 * TestTable.class);
		 */

		int r = splitBizTemplate.update(10,
				"update test_db.test_table set name = ? where id = ?",
				new Object[] { "test", 1 });

		System.out.println(r);
	}
}
