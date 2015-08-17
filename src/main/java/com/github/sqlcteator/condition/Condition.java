package com.github.sqlcteator.condition;

public abstract class Condition {

	/** 字段 */
	protected String column;

	/** 值 */
	protected Object value;

	/** 运算符（大于号，小于号，等于号 like 等） */
	protected String operator;

	/** 是否使用条件值占位符 */
	private boolean isPlaceHolder = true;

	public abstract String getColumn();
	public abstract String getPrefix();
	public abstract String getOperator();
	public abstract Object getValue();

	public boolean isPlaceHolder() {
		return isPlaceHolder;
	}

}
