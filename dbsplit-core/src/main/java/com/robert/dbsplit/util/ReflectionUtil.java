package com.robert.dbsplit.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public abstract class ReflectionUtil {
	private static final Logger log = LoggerFactory
			.getLogger(ReflectionUtil.class);

	public static List<Field> getClassEffectiveFields(
			Class<? extends Object> clazz) {
		List<Field> effectiveFields = new LinkedList<Field>();

		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (!field.isAccessible()) {
					try {
						Method method = clazz
								.getMethod(fieldName2GetterName(field.getName()));
						
						if (method.getReturnType() != field.getType()) {
							log.error(
									"The getter for field {} may not be correct.",
									field);
							continue;
						}
					} catch (NoSuchMethodException e) {
						log.error(
								"Fail to obtain getter method for non-accessible field {}.",
								field);
						log.error("Exception--->", e);

						continue;
					} catch (SecurityException e) {
						log.error(
								"Fail to obtain getter method for non-accessible field {}.",
								field);
						log.error("Exception--->", e);

						continue;
					}

				}
				effectiveFields.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		return effectiveFields;
	}

	public static String fieldName2GetterName(String fieldName) {
		return "get" + StringUtils.capitalize(fieldName);
	}
}
