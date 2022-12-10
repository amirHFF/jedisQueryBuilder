package org.ah.jedisQueryBuilder;

public enum QueryOperator {
	AND("AND"," "),
	OR("UNION"," | "),
	OPEN_PARENTHESES("open"," ( "),
	CLOSE_PARENTHESES("close"," ) ");
//	EQUAL(),
//	BIGGER,
//	LOWER;

	private String name;
	private String value;

	QueryOperator(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
