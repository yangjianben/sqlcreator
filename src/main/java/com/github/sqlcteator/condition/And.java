package com.github.sqlcteator.condition;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class And extends Condition {

	public And(String propertyName, String operator, Object value, List<Object> parameters) {
		this.column = propertyName;
		this.operator = operator;
		this.value = value;
		parameters.add(value);
	}

	@Override
	public String getPrefix() {
		return " AND ";
	}

	@Override
	public String getColumn() {
		return this.column;
	}

	@Override
	public String getOperator() {
		return StringUtils.join(" ", this.operator, " ");
	}

	@Override
	public Object getValue() {
		if ("like".equals(operator)) {
			return this.value;
		}
		return " ? ";
	}

}
