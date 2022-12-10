package org.ah.jedisQueryBuilder;

import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class QueryBuilder implements Queryable {
	protected final UnifiedJedis jedis;
	protected final String index;
	protected Map<String, Object> searchQueryMap = new HashMap<>();
	protected List<String> queryList = new ArrayList<>();
	protected Query finalQuery;

	public QueryBuilder(String index,UnifiedJedis jedis) {
		this.index = index;
		this.jedis=jedis;
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
	public SearchResult search() {
		return jedis.ftSearch(index, finalQuery);
	}

	@Override
	public Map<String, Object> info() {
		return jedis.ftInfo(index);
	}
}
