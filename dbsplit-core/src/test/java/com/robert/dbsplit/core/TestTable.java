package com.robert.dbsplit.core;

import java.util.Date;

import com.alibaba.fastjson.JSON;

public class TestTable {
	private long id;
	private String name;

	public enum Gender {
		MALE, FEMALE;

		public static Gender parse(int value) {
			for (Gender gender : Gender.values()) {
				if (value == gender.ordinal())
					return gender;
			}
			return null;
		}
	};

	private Gender gender;
	private String lstUpdUser;
	private Date lstUpdTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getLstUpdUser() {
		return lstUpdUser;
	}

	public void setLstUpdUser(String lstUpdUser) {
		this.lstUpdUser = lstUpdUser;
	}

	public Date getLstUpdTime() {
		return lstUpdTime;
	}

	public void setLstUpdTime(Date lstUpdTime) {
		this.lstUpdTime = lstUpdTime;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
