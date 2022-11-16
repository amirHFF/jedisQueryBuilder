package org.ah.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RedisQuery {
	private Map<String,QueryResult> queryParameterList=new HashMap<>();

	public Map<String, QueryResult> getQueryParameterList() {
		return queryParameterList;
	}

	public void setQueryParameterList(Map<String, QueryResult> queryParameterList) {
		this.queryParameterList = queryParameterList;
	}
}
