package com.robert.dbsplit.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SplitBizTemplateTest {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"spring/dbsplit-sample.xml");
		SplitBizTemplate splitBizTemplate = (SplitBizTemplate)ac.getBean("splitBizTemplate");
		
		System.out.println(splitBizTemplate);
		
		splitBizTemplate.update(0, "update test_db.test_table set name ='test' where id = ?", 1);
	}
}
