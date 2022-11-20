package org.ah.factory;

public abstract class QueryUnit<Q,T> {
	protected String parameter;
	protected Object value;

	public QueryUnit(String parameter, Object value) {
		this.parameter = parameter;
		this.value = value;
	}

	abstract Q getQueryHandlerObject();
}
