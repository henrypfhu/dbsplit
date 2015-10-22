package com.robert.dbsplit.util.reflect;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldVisitor<T> {
	private static final Logger log = LoggerFactory
			.getLogger(FieldVisitor.class);

	private T bean;

	public FieldVisitor(T bean) {
		this.bean = bean;
	}

	public void visit(FieldHandler fieldHandler) {
		List<Field> fields = ReflectionUtil.getClassEffectiveFields(bean
				.getClass());

		int count = 0;
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);

			Object value = null;
			try {
				boolean access = field.isAccessible();

				field.setAccessible(true);
				value = field.get(bean);

				if (value != null) {
					if (value instanceof Number
							&& ((Number) value).doubleValue() == -1d)
						continue;

					if (value instanceof List)
						continue;

					fieldHandler.handle(count++, field, value);
				}

				field.setAccessible(access);
			} catch (IllegalArgumentException e) {
				log.error("Fail to obtain bean {} property {}.", bean, field);
				log.error("Exception--->", e);
			} catch (IllegalAccessException e) {
				log.error("Fail to obtain bean {} property {}.", bean, field);
				log.error("Exception--->", e);
			}
		}
	}
}
