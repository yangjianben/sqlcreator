package com.github.sqlcteator;

import static com.github.sqlcteator.util.StrUtil.isEmpty;
import static com.github.sqlcteator.util.StrUtil.isNotEmpty;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * SQL UPDATE 更新
 *
 * @Author 杨健/YangJian
 * @Date 2015年7月21日 上午10:15:32
 */
public class Update {

	private boolean terminated = false;

	private String table;

	private Collection<String> conditions;

	private final List<String> columns;

	private final List<Object> parameters;

	private final StringBuilder sql;

	public Update() {
		this.sql = new StringBuilder(" update ");
		this.columns = new LinkedList<>();
		this.parameters = new LinkedList<>();
		this.conditions = new LinkedList<>();
	}

	public Update(String table) {
		this();
		this.table = table;
	}

	public Update table(String table) {
		this.table = table;
		return this;
	}

	public Update set(String column, Object value) {
		columns.add(column);
		if (value != null && value instanceof Date) {
			Date date = (Date) value;
			value = new Timestamp(date.getTime());
		}
		parameters.add(value);
		return this;
	}

	/**
	 * 拼接SQL查询条件（and）
	 * 
	 * @param sql
	 * @param params
	 *            保存参数值
	 * @param columnName
	 *            字段
	 * @param value
	 *            参数值
	 * @return Update
	 * @Author 杨健/YangJian
	 * @Date 2015年6月30日 上午11:46:48
	 * @Version 1.0.0
	 */
	public Update eq(String columnName, Object value) {

		if (isEmpty(value))
			return this;

		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" = ? ").toString());
		setParameters(value);
		return this;
	}

	private Update setParameters(Object value) {
		parameters.add(value);
		return this;
	}

	/** 不相等 */
	public Update notEq(String columnName, Object value) {
		if (isEmpty(value))
			return this;

		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" <> ? ").toString());
		setParameters(value);
		return this;
	}

	/**
	 * in
	 * 
	 * @param sql
	 * @param columnName
	 * @param value
	 * @return Query
	 * @Author 杨健/YangJian
	 * @Date 2015年7月15日 下午5:22:49
	 * @Version 1.0.0
	 */
	public Update in(String columnName, Object value) {
		if (isEmpty(value))
			return this;

		in(columnName, value, " in ");
		return this;
	}

	/**
	 * not in
	 * 
	 * @param columnName
	 *            属性名称
	 * @param value
	 *            值集合
	 * @param isQuotationMark
	 *            是否需要引号
	 * @return
	 */
	public Update notIn(String columnName, Object value) {
		if (isEmpty(value))
			return this;
		in(columnName, value, " not in ");
		return this;
	}

	private Update in(String columnName, Object value, String opt) {

		List<String> params = Lists.newArrayList();
		if (value instanceof Collection<?>) {
			Collection<?> valueList = (Collection<?>) value;
			for (Object v : valueList) {
				setParameters(v);
				params.add("?");
			}
		} else {
			setParameters(value);
			params.add("?");
		}

		if (isEmpty(value) || "null".equals(value))
			return this;

		sql.append(" and ").append(columnName).append(opt).append("( ").append(Joiner.on(",").join(params))
				.append(" )");
		return this;
	}

	/** 空 */
	public Update isNull(String columnName) {
		if (isEmpty(columnName))
			return this;

		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" is null ").toString());
		return this;
	}

	/** 非空 */
	public Update isNotNull(String columnName) {
		if (isEmpty(columnName))
			return this;

		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" is not null ").toString());
		return this;
	}

	public Update or(String columnName, Object value) {
		if (isEmpty(columnName))
			return this;
		if (isEmpty(value))
			return this;
		conditions.add(new StringBuilder().append(" or ").append(columnName).append(" = ? ").toString());
		setParameters(value);
		return this;
	}

	/**
	 * 模糊匹配
	 * 
	 * @param columnName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public Update like(String columnName, String value) {
		if (isEmpty(value))
			return this;
		if (value.indexOf("%") < 0)
			value = StringUtils.join("%", value, "%");

		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" like ?").toString());
		setParameters(value);
		return this;
	}

	/**
	 * 模糊匹配
	 * 
	 * @param columnName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public Update notLike(String columnName, String value) {
		if (isEmpty(value))
			return this;
		if (value.indexOf("%") < 0)
			value = StringUtils.join("%", value, "%");

		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" not like ?").toString());
		setParameters(value);
		return this;
	}

	/**
	 * 时间区间查询
	 * 
	 * @param columnName
	 *            属性名称
	 * @param lo
	 *            日期属性起始值
	 * @param go
	 *            日期属性结束值
	 * @return
	 */
	public Update between(String columnName, String lo, String go) {
		if (isNotEmpty(lo) && isNotEmpty(go)) {
			return this;
		}

		if (isNotEmpty(lo) && isEmpty(go)) {
			conditions.add(new StringBuilder().append(" and ").append(columnName).append(" >= ? ").toString());
			setParameters(lo);
			return this;
		}

		if (isEmpty(lo) && isNotEmpty(go)) {
			conditions.add(new StringBuilder().append(" and ").append(columnName).append(" <= ? ").toString());
			setParameters(go);
			return this;
		}

		conditions.add(new StringBuilder().append(" between ? and ? ").toString());
		setParameters(lo);
		setParameters(go);
		return this;
	}

	public Update between(String columnName, Number lo, Number go) {
		if (isNotEmpty(lo))
			ge(columnName, lo);

		if (isNotEmpty(go))
			le(columnName, go);

		return this;
	}

	/**
	 * 小于等于
	 * 
	 * @param columnName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public Update le(String columnName, Number value) {
		if (isEmpty(value)) {
			return this;
		}
		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" <= ? ").toString());
		setParameters(value);
		return this;
	}

	/**
	 * 小于
	 * 
	 * @param columnName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public Update lt(String columnName, Number value) {
		if (isEmpty(value)) {
			return this;
		}
		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" < ? ").toString());
		setParameters(value);
		return this;
	}

	/**
	 * 大于等于
	 * 
	 * @param columnName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public Update ge(String columnName, Number value) {
		if (isEmpty(value)) {
			return this;
		}
		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" >= ? ").toString());
		setParameters(value);
		return this;
	}

	/**
	 * 大于
	 * 
	 * @param columnName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public Update gt(String columnName, Number value) {
		if (isEmpty(value)) {
			return this;
		}
		conditions.add(new StringBuilder().append(" and ").append(columnName).append(" > ? ").toString());
		setParameters(value);
		return this;
	}

	private void terminate() {
		if (columns.isEmpty() || parameters.isEmpty())
			throw new IllegalArgumentException("Not contains SET statements!");

		if (!terminated) {
			sql.append(table).append(" set ");
			int size = columns.size();
			for (int i = 0; i < size; i++) {
				sql.append(columns.get(i)).append(" = ? ");
				if (i < size - 1) {
					sql.append(",");
				}
			}
			if (!conditions.isEmpty()) {
				sql.append(" where 1=1 ");
				Iterator<String> conditionIter = conditions.iterator();
				while (conditionIter.hasNext()) {
					String condition = conditionIter.next();
					sql.append(condition);
				}
			}

			terminated = true;
		}
	}

	@Override
	public String toString() {
		terminate();
		return sql.toString();
	}

	public int doUpdate(Connection connection) throws SQLException {
		System.out.println("SQL=" + this.toString());
		int update = JdbcUtils.getRunner().update(connection, this.toString(), parameters.toArray());
		System.out.println("SQL Value=" + update);
		return update;
	}

}
