package org.ah.jedisQueryBuilder;

public class QueryObject {
	private QueryOperator operator=QueryOperator.AND;

	public QueryOperator getOperator() {
		return operator;
	}

	public void setOperator(QueryOperator operator) {
		this.operator = operator;
	}

}
