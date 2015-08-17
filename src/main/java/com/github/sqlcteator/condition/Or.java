package com.github.sqlcteator.condition;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Or extends Condition {

	List<String> propertyName;

	public Or(List<String> propertyName, String operator, Object value) {
		this.propertyName = propertyName;
		this.operator = operator;
		this.value = value;
	}

	public Or(String propertyName, String operator, Object value) {
		this.column = propertyName;
		this.operator = operator;
		this.value = value;
	}

	@Override
	public String getColumn() {
		if (propertyName != null && propertyName.size() > 0) {
			return "";
		}
		return column;
	}

	@Override
	public String getPrefix() {
		return " OR ";
	}

	@Override
	public String getValue() {
		if (propertyName != null && propertyName.size() > 0) {
			StringBuilder sql = new StringBuilder();
			int size = propertyName.size();
			if (size > 1) {
				sql.append(" ( ");
			}
			for (int i = 0; i < size; i++) {
				sql.append(" ").append(propertyName.get(i)).append(" = ?");
				if (i < size - 1) {
					sql.append(" AND ");
				}
			}
			if (size > 1) {
				sql.append(" ) ");
			}
			return sql.toString();
		}
		return " ? ";
	}

	@Override
	public String getOperator() {
		if (propertyName != null && propertyName.size() > 0) {
			return "";
		}
		return StringUtils.join(" ", this.operator, " ");
	}
}