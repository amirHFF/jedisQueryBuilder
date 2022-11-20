package org.ah.jedisQueryBuilder;

import redis.clients.jedis.search.SearchResult;

import java.util.List;
import java.util.Map;

public interface QueryBuilder {

	List<String> getIndexAttributes();

	void build();

	SearchResult search();

	Map<String, Object> info();
}
