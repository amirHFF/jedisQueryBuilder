package org.ah.jedisQueryBuilder;

import org.ah.factory.QueryUnit;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Queryable {

	List<String> getIndexAttributes();

	Queryable addFilter(String attribute, Object value);

	Queryable addOperation(QueryOperator operator);

	void build();

	SearchResult search();

	Map<String, Object> info();
}
