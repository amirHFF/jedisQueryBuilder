package org.ah.jedisQueryBuilder;

import org.ah.factory.QueryFactory;
import redis.clients.jedis.UnifiedJedis;

import java.util.ArrayList;
import java.util.List;

public class CustomQueryBuilder extends QueryBuilder {
	List<String> redisAttributeList=new ArrayList<>();
	List<String> userAttributeList=new ArrayList<>();

	public CustomQueryBuilder(UnifiedJedis jedis , String index) {
		super(index,jedis);
		super.queryList.add("( ");
	}

	@Override
	public CustomQueryBuilder addFilter(String attribute, Object value) {
		userAttributeList.add(attribute);
		queryList .add( QueryFactory.createTextQuery(attribute,value));
		return this;
	}

	@Override
	public CustomQueryBuilder addOperation(QueryOperator operator) {
		queryList.add(operator.getValue());
		return this;
	}

	@Override
	public void build() {
		redisAttributeList .addAll(getIndexAttributes());
		checkAttribute();
		queryList.add(" )");
		finalQuery = QueryFactory.createFinalQuery(queryList);
	}

	private void checkAttribute(){
		for (String userAttribute : userAttributeList) {
			if (!redisAttributeList.contains(userAttribute))
				throw new RuntimeException("there is no attribute for attribute : "+userAttribute);
		}
	}
}
