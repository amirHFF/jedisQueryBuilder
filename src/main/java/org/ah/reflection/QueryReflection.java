package org.ah.reflection;

import org.ah.jedisQueryBuilder.ObjectBinder;
import org.ah.jedisQueryBuilder.QueryObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class QueryReflection {
	private Class C;

	public QueryReflection(Class c) {
		C = c;
	}

	public Map<String, String> getResultQueryParameterMap() {
		HashMap<String, String> queryMap = new HashMap<>();

		Field[] attributeFields = C.getDeclaredFields();
		for (Field attributeField : attributeFields) {
			if (attributeField.isAnnotationPresent(ObjectBinder.class)) {
				if (!attributeField.getAnnotation(ObjectBinder.class).alias().equals(""))
					queryMap.put(attributeField.getName(), attributeField.getAnnotation(ObjectBinder.class).alias());
				else if (!attributeField.getAnnotation(ObjectBinder.class).attribute().equals(""))
					queryMap.put(attributeField.getName(), attributeField.getAnnotation(ObjectBinder.class).attribute());
				else
					queryMap.put(attributeField.getName(), attributeField.getName());

			}
		}
		return queryMap;
	}

	public Map<String, Object> bindObjectToRedis(QueryObject obj) throws IllegalAccessException {
		Map<String, Object> searchQueryMap = new HashMap<>();
		for (Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (field.get(obj) != null) {
				if (field.isAnnotationPresent(ObjectBinder.class)) {
					if (!field.getAnnotation(ObjectBinder.class).alias().equals("")) {
						searchQueryMap.put(field.getAnnotation(ObjectBinder.class).alias(), field.get(obj));
					} else if (!field.getAnnotation(ObjectBinder.class).attribute().equals(""))
						searchQueryMap.put(field.getAnnotation(ObjectBinder.class).attribute(), field.get(obj));
					else
						searchQueryMap.put(field.getName(), field.get(obj));
				}
			}
		}
		return searchQueryMap;
	}

}
