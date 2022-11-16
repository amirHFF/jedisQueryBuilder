package org.ah.jedisQueryBuilder;

import org.ah.basic.QueryResult;
import org.ah.basic.QueryHandler;
import org.ah.basic.RedisQuery;
import redis.clients.jedis.UnifiedJedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilder {

	private UnifiedJedis jedis;
	private String index;

	private List<String> queries = new ArrayList<>();
	private Class dtoClass;
	private RedisQuery obj;

	public QueryBuilder(UnifiedJedis jedis, String index, RedisQuery obj) {
		this.jedis = jedis;
		this.index = index;
		this.obj=obj;
		this.dtoClass = obj.getClass();
		initializeQueryParam();
	}

	private void initializeQueryParam() {
		QueryHandler queryHandler = new QueryHandler(dtoClass);
		Map<String, String> queryParam = queryHandler.getResultQueryParameterMap();
		List<String> indexAttribute = getIndexAttributes();
		for (String attribute : indexAttribute) {
			if (queryParam.get(attribute) != null) {
				obj.getQueryParameterList().put(attribute,new QueryResult(queryParam.get(attribute), attribute));
			} else {
				for (Map.Entry<String, String> entry : queryParam.entrySet()) {
					if (attribute.contains(entry.getValue())) {
						obj.getQueryParameterList().add(new QueryResult(attribute, attribute));
					} else {
						throw new RuntimeException("wrong alias name for search");
					}
				}
			}
		}
	}

	private List<String> getIndexAttributes() {
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

	public void setTextQuery(QueryResult textQuery) {
		StringBuilder query = new StringBuilder("@");
		query.append(textQuery.getParameter());
		query.append(":(");
		query.append(textQuery.getValue());
		query.append(")");
		queries.add(query.toString());
	}

	public void setNumericQuery(QueryResult numericQuery) {
		StringBuilder query = new StringBuilder("@");
		query.append(numericQuery.getParameter());
		query.append(":[");
		query.append(numericQuery.getValue());
		query.append(" ");
		query.append(numericQuery.getValue());
		query.append("]");
		queries.add(query.toString());
	}

	public void setBooleanQuery(boolean booleanQuery) {
	}
}
