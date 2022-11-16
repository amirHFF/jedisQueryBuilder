package org.ah.basic;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class QueryHandler {
	private Class C;

	public QueryHandler(Class c) {
		C = c;
	}

	public Map<String, String> getResultQueryParameterMap() {
		HashMap<String, String> queryMap = new HashMap<>();

		Field[] attributeFields = C.getDeclaredFields();
		for (Field attributeField : attributeFields) {
			if (attributeField.isAnnotationPresent(RedisQueryFilter.class)) {
				if (!attributeField.getAnnotation(RedisQueryFilter.class).alias().equals(""))
					queryMap.put(attributeField.getName(), attributeField.getAnnotation(RedisQueryFilter.class).alias());
				else if (!attributeField.getAnnotation(RedisQueryFilter.class).attribute().equals(""))
					queryMap.put(attributeField.getName(), attributeField.getAnnotation(RedisQueryFilter.class).attribute());
				else
					queryMap.put(attributeField.getName(), attributeField.getName());

			}
		}
		return queryMap;
	}

}
