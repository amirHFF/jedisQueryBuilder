package org.ah.factory;

public class TextQuery extends QueryUnit<String,String> {

	public TextQuery(String parameter, String value) {
		super(parameter, value);
	}

	@Override
	public String getQueryHandlerObject() {
		StringBuilder query = new StringBuilder("@");
		query.append(parameter);
		query.append(":(");
		query.append(value);
		query.append(")");
		return query.toString();
	}
}
