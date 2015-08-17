package com.github.sqlcteator.condition;

public class Null extends Condition {
	private Boolean isNull = false;

	public Null(String propertyName, Boolean isNull) {
		this.column = propertyName;
		this.isNull = isNull;
	}

	@Override
	public String getPrefix() {
		return " and ";
	}

	@Override
	public String getColumn() {
		return column;
	}

	@Override
	public Object getValue() {
		if (isNull) {
			return "is null";
		}else {
			return "is not null";
		}
	}

	@Override
	public String getOperator() {
		return this.operator;
	}

}
