package org.ah.jedisQueryBuilder;

import org.ah.factory.QueryFactory;
import org.ah.reflection.QueryReflection;
import redis.clients.jedis.UnifiedJedis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AutomateQueryBuilder extends QueryBuilder {

	private QueryObject currentObject;

	public AutomateQueryBuilder(UnifiedJedis jedis, String index, QueryObject queryObject) {
		super(index, jedis);
		currentObject = queryObject;
		bindAndCheck(queryObject);
		createTextQuery();
		build();
	}

	private void bindAndCheck(QueryObject obj) {
		QueryReflection queryHandler = new QueryReflection(obj.getClass());
		try {
			searchQueryMap = queryHandler.bindObjectToRedis(obj);
			checkAttribute(searchQueryMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void createTextQuery() {
		queryList.add("( ");
		for (Map.Entry<String, Object> query : searchQueryMap.entrySet()) {
			queryList.add(QueryFactory.createTextQuery(query.getKey(), query.getValue()));
		}
		queryList.add(")");
//		queryList.addAll(QueryFactory.createTextQuery(searchQueryMap));
		queryList.add(" " + currentObject.getOperator().getValue() + " ");

	}

	private void checkAttribute(Map<String, Object> queryParam) {
		List<String> indexAttribute = getIndexAttributes();
		for (String attribute : indexAttribute) {
			Iterator<Map.Entry<String, Object>> iterator = queryParam.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> queryEntry = iterator.next();
				if (!attribute.equals(queryEntry.getKey())) {
					if (attribute.contains(queryEntry.getKey())) {
						queryParam.put(attribute, queryEntry.getValue());
						iterator.remove();
					}
				}
			}
		}
	}

	public AutomateQueryBuilder(UnifiedJedis jedis, String index, List<QueryObject> objectList) {
		super(index, jedis);

		if (objectList != null && !objectList.isEmpty()) {
			for (QueryObject queryObject : objectList) {
				bindAndCheck(queryObject);
				currentObject = queryObject;
				createTextQuery();
			}

		}
	}

	@Override
	public AutomateQueryBuilder addFilter(String attribute, Object value) {
		queryList.add(QueryFactory.createTextQuery(attribute,value));
		return this;
	}

	@Override
	public AutomateQueryBuilder addOperation(QueryOperator operator) {
		queryList.add(" " + operator.getValue() + " ");
		return this;
	}

	@Override
	public void build() {
		finalQuery = QueryFactory.createFinalQuery(queryList);
	}
}
