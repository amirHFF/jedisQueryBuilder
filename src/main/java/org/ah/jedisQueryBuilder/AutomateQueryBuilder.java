package org.ah.jedisQueryBuilder;

import org.ah.factory.RedisSearchQuery;
import org.ah.reflection.QueryReflection;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

import java.util.*;

public class AutomateQueryBuilder implements QueryBuilder {

	Map<String, Object> searchQueryMap = new HashMap<>();
	private UnifiedJedis jedis;
	private String index;
	private List<String> queryList = new ArrayList<>();
	private Query finalQuery;
	private QueryObject currentObject;

	public AutomateQueryBuilder(UnifiedJedis jedis, String index, QueryObject queryObject) {
		this.jedis = jedis;
		this.index = index;
		currentObject = queryObject;
		bindAndCheck(queryObject);
		createTextQuery();
		build();
	}
	public AutomateQueryBuilder(UnifiedJedis jedis, String index, List<QueryObject> objectList) {
		this.jedis = jedis;
		this.index = index;
		if (objectList != null && !objectList.isEmpty()) {
			for (QueryObject queryObject : objectList) {
				bindAndCheck(queryObject);
				currentObject = queryObject;
				createTextQuery();
			}

		}
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

	@Override
	public List<String> getIndexAttributes() {
		List<String> indexAttribute = new ArrayList<String>();

		Map<String, Object> AllAttribute = jedis.ftInfo(index);
		ArrayList<Object> attributeList = ((ArrayList<Object>) AllAttribute.get("attributes"));

		for (Object info : attributeList) {
			ArrayList<String> infoList = ((ArrayList<String>) info);
			for (int i = 0; i < infoList.size(); i++) {
				if (infoList.get(i).equals("attribute")) {
					try {
						indexAttribute.add(infoList.get(i + 1));
					} catch (IndexOutOfBoundsException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return indexAttribute;

	}

	@Override
	public void build() {
		RedisSearchQuery redisSearchQuery = new RedisSearchQuery();
		finalQuery=redisSearchQuery.createFinalQuery(queryList);

	}

	public void createTextQuery() {
		RedisSearchQuery redisSearchQuery = new RedisSearchQuery();
		queryList.addAll(redisSearchQuery.createTextQuery(searchQueryMap));
		queryList.add(" "+currentObject.getOperator().getValue()+" ");

	}

	@Override
	public SearchResult search() {
		return jedis.ftSearch(index, finalQuery);
	}

	@Override
	public Map<String, Object> info() {
		return jedis.ftInfo(index);
	}

	private Query convertToQuery(List<String> queries) {
		StringBuilder queryString = new StringBuilder();
		for (String query : queries) {
			queryString.append(query);
			queryString.append(" ");
		}
		return new Query(queryString.toString());
	}

	public void setTextQuery(String parameter, String value) {
		StringBuilder query = new StringBuilder("@");
		query.append(parameter);
		query.append(":(");
		query.append(value);
		query.append(")");
//		queries.add(query.toString());
	}

	public void setNumericQuery(String parameter, Number value) {
		StringBuilder query = new StringBuilder("@");
		query.append(parameter);
		query.append(":[");
		query.append(value);
		query.append(" ");
		query.append(value);
		query.append("]");
//		queries.add(query.toString());
	}

	public void setBooleanQuery(String parameter, boolean value) {
		StringBuilder query = new StringBuilder("@");
		query.append(parameter);
		query.append(":{");
		query.append(value);
		query.append("}");
//		queries.add(query.toString());
	}
}
