package org.ah.factory;

public abstract class QueryUnit<Q,T> {
	protected Q parameter;
	protected T value;

	public QueryUnit(Q parameter, T value) {
		this.parameter = parameter;
		this.value = value;
	}

	abstract String getQueryHandlerObject();
}
