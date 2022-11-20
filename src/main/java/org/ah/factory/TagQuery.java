package org.ah.factory;

public class TagQuery extends QueryUnit<String, Boolean> {
	public TagQuery(String parameter, Object value) {
		super(parameter, value);
	}

	@Override
	public String getQueryHandlerObject() {
		StringBuilder query = new StringBuilder("@");
		query.append(parameter);
		query.append(":{");
		query.append(value);
		query.append("}");
		return query.toString();
	}
}
