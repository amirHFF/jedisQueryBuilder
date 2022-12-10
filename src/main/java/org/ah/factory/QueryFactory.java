package org.ah.factory;

import redis.clients.jedis.search.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryFactory {

	public static List<String> createTextQuery(Map<String, Object> searchObject) {
		List<String> queryList = new ArrayList<>();
		queryList.add("(");
		for (Map.Entry<String, Object> objectEntry : searchObject.entrySet()) {
			queryList.add((String) queryFactory(objectEntry.getKey(), objectEntry.getValue()).getQueryHandlerObject());
		}
		queryList.add(")");
		return queryList;
	}

	private static QueryUnit queryFactory(String parameter, Object value) {
		if (value instanceof String) {
			return new TextQuery(parameter, (String) value);
		} else if (value instanceof Number) {
			return new NumericQuery(parameter, (Number) value);
		} else if (value instanceof Boolean) {
			return new TagQuery(parameter, (Boolean) value);
		} else return null;
	}

	public static String createTextQuery(String parameter, Object value) {
		String query;
		query = (String) queryFactory(parameter, value).getQueryHandlerObject();

		return query;
	}

	public static Query createFinalQuery(List<String> textQuery) {
		return queryListToJedisQuery(textQuery);
	}

	private static Query queryListToJedisQuery(List<String> queryTextList) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Object text : queryTextList) {
			stringBuilder.append(text);
			stringBuilder.append(" ");
		}

		return new Query(stringBuilder.toString());
	}
}
