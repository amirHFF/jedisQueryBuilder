package org.ah.factory;

public class NumericQuery extends QueryUnit<String,Number> {
	public NumericQuery(String parameter, Number value) {
		super(parameter, value);
	}

	@Override
	public String getQueryHandlerObject() {
		StringBuilder query = new StringBuilder("@");
		query.append(parameter);
		query.append(":[");
		query.append(value);
		query.append(" ");
		query.append(value);
		query.append("]");
		return query.toString();
	}
}
