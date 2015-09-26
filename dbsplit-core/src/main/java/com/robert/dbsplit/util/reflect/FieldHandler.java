package com.robert.dbsplit.util.reflect;

import java.lang.reflect.Field;

public interface FieldHandler {
	public void handle(int index, Field field, Object value);
}
