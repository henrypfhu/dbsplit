package com.robert.dbsplit.core;

import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class SplitNode {
	private JdbcTemplate masterTemplate;
	private List<JdbcTemplate> slaveTemplates;

	private int iter = 0;

	public SplitNode() {
	}

	public SplitNode(JdbcTemplate masterTemplate,
			List<JdbcTemplate> slaveTemplates) {
		this.masterTemplate = masterTemplate;
		this.slaveTemplates = slaveTemplates;
	}

	public SplitNode(JdbcTemplate masterTemplate,
			JdbcTemplate... slaveTemplates) {
		this.masterTemplate = masterTemplate;
		this.slaveTemplates = Arrays.asList(slaveTemplates);
	}

	public JdbcTemplate getMasterTemplate() {
		return masterTemplate;
	}

	public void setMasterTemplate(JdbcTemplate masterTemplate) {
		this.masterTemplate = masterTemplate;
	}

	public List<JdbcTemplate> getSlaveTemplates() {
		return slaveTemplates;
	}

	public void setSlaveTemplates(List<JdbcTemplate> slaveTemplates) {
		this.slaveTemplates = slaveTemplates;
	}

	public void addSalveTemplate(JdbcTemplate jdbcTemplate) {
		this.slaveTemplates.add(jdbcTemplate);
	}

	public void removeSalveTemplate(JdbcTemplate jdbcTemplate) {
		this.slaveTemplates.remove(jdbcTemplate);
	}

	public JdbcTemplate getRandomSlaveTempate() {
		if (iter == Integer.MAX_VALUE)
			iter = 0;

		int index = iter++ % slaveTemplates.size();
		return slaveTemplates.get(index);
	}

}
