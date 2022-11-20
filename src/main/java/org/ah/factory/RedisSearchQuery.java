package org.ah.factory;

import org.ah.jedisQueryBuilder.QueryOperator;
import redis.clients.jedis.search.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedisSearchQuery {

	public List<String> createTextQuery(Map<String, Object> searchObject) {
		List<String> queryList = new ArrayList<>();
		queryList.add("(");
		for (Map.Entry<String, Object> objectEntry : searchObject.entrySet()) {
			queryList.add((String) queryFactory(objectEntry.getKey(), objectEntry.getValue()).getQueryHandlerObject());
		}
		queryList.add(")");

		return queryList;
	}
	public Query createFinalQuery(List<String> textQuery) {
		return queryListToJedisQuery(textQuery);
	}


	private QueryUnit queryFactory(String parameter, Object value) {
		if (value instanceof String) {
			return new TextQuery(parameter, value);
		} else if (value instanceof Number) {
			return new NumericQuery(parameter, value);
		} else if (value instanceof Boolean) {
			return new TagQuery(parameter, value);
		} else return null;
	}
	
	private Query queryListToJedisQuery(List<String> queryTextList){
		StringBuilder stringBuilder=new StringBuilder();
		for (Object text : queryTextList) {
			stringBuilder.append(text);
			stringBuilder.append(" ");
		}

		return new Query(stringBuilder.toString());
	}
}
