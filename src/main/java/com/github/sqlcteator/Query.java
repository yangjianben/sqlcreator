package com.github.sqlcteator;

import static com.github.sqlcteator.util.StrUtil.isEmpty;
import static com.github.sqlcteator.util.StrUtil.isNotEmpty;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import com.github.sqlcteator.condition.OrderBy;
import com.github.sqlcteator.mapping.MappingDb;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * SQL SELECT查询器
 *
 * @Author 杨健/YangJian
 * @Date 2015年7月16日 下午3:21:32
 */
public class Query {

	private final StringBuilder sql;
	private final List<Object> parameters;
	private final List<OrderBy> orders;
	private boolean isDubeg = true;
	private Class<?> clazz;

	public void setDubeg(boolean isDubeg) {
		this.isDubeg = isDubeg;
	}

	public Query() {
		this.sql = new StringBuilder();
		this.orders = new ArrayList<OrderBy>();
		this.parameters = new ArrayList<Object>();
	}

	public Query(Class<?> clazz) {
		this.clazz = clazz;
		String table = MappingDb.camelToUnderscore(clazz.getSimpleName());
		this.sql = new StringBuilder(" select * from ").append(table).append(" where 1=1 ");
		this.orders = new ArrayList<OrderBy>();
		this.parameters = new ArrayList<Object>();
	}

	public Query(String sql) {
		this.sql = new StringBuilder(sql);
		this.orders = new ArrayList<OrderBy>();
		this.parameters = new ArrayList<Object>();
	}

	public static Query selectAll(String table) {
		return new Query(" select * from ").append(table).append(" where 1=1 ");
	}

	public static Query select(String table, String... columnNames) {
		Query sql = new Query(" select ");
		sql.append(StringUtils.join(columnNames, ","));
		sql.append(" from ");
		sql.append(table);
		sql.append(" where 1=1 ");
		return sql;
	}

	/**
	 * 直接拼接sql片段
	 * 
	 * @param segment
	 *            sql片段
	 * @return
	 * @return Query
	 * @Author 杨健/YangJian
	 * @Date 2015年7月16日 上午10:52:11
	 * @Version 1.0.0
	 */
	public Query append(Object segment) {
		if (isEmpty(segment))
			return this;

		sql.append(segment);
		return this;
	}

	@Override
	public String toString() {
		return sql.toString();
	}

	public List<Object> getParameters() {
		return parameters;
	}

	public Query setParameters(Object parameter) {
		if (isEmpty(parameter))
			return this;
		parameters.add(parameter);
		return this;
	}

	public Query setParameters(List<Object> parameter) {
		if (isEmpty(parameter))
			return this;
		parameters.addAll(parameter);
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
	 * @return Query
	 * @Author 杨健/YangJian
	 * @Date 2015年6月30日 上午11:46:48
	 * @Version 1.0.0
	 */
	public Query eq(String columnName, Object value) {

		if (isEmpty(value))
			return this;

		sql.append(" and ").append(columnName).append(" = ? ");
		setParameters(value);
		return this;
	}

	/** 不相等 */
	public Query notEq(String columnName, Object value) {
		if (isEmpty(value))
			return this;

		sql.append(" and ").append(columnName).append(" <> ? ");
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
	public Query in(String columnName, Object value) {
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
	public Query notIn(String columnName, Object value) {
		if (isEmpty(value))
			return this;
		in(columnName, value, " not in ");
		return this;
	}

	private Query in(String columnName, Object value, String opt) {

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
	public Query isNull(String columnName) {
		if (isEmpty(columnName))
			return this;

		sql.append(" and ").append(columnName).append(" is null ");
		return this;
	}

	/** 非空 */
	public Query isNotNull(String columnName) {
		if (isEmpty(columnName))
			return this;

		sql.append(" and ").append(columnName).append(" is not null ");
		return this;
	}

	public Query or(String columnName, Object value) {
		if (isEmpty(columnName))
			return this;
		if (isEmpty(value))
			return this;
		sql.append(" or ").append(columnName).append(" = ? ");
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
	public Query like(String columnName, String value) {
		if (isEmpty(value))
			return this;
		if (value.indexOf("%") < 0)
			value = StringUtils.join("%", value, "%");

		sql.append(" and ").append(columnName).append(" like ?");
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
	public Query notLike(String columnName, String value) {
		if (isEmpty(value))
			return this;
		if (value.indexOf("%") < 0)
			value = StringUtils.join("%", value, "%");

		sql.append(" and ").append(columnName).append(" not like ?");
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
	public Query between(String columnName, String lo, String go) {
		if (isNotEmpty(lo) && isNotEmpty(go)) {
			return this;
		}

		if (isNotEmpty(lo) && isEmpty(go)) {
			sql.append(" and ").append(columnName).append(" >= ? ");
			setParameters(lo);
			return this;
		}

		if (isEmpty(lo) && isNotEmpty(go)) {
			sql.append(" and ").append(columnName).append(" <= ? ");
			setParameters(go);
			return this;
		}

		sql.append(" between ? and ? ");
		setParameters(lo);
		setParameters(go);
		return this;
	}

	public Query between(String columnName, Number lo, Number go) {
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
	public Query le(String columnName, Number value) {
		if (isEmpty(value)) {
			return this;
		}
		sql.append(" and ").append(columnName).append(" <= ? ");
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
	public Query lt(String columnName, Number value) {
		if (isEmpty(value)) {
			return this;
		}
		sql.append(" and ").append(columnName).append(" < ? ");
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
	public Query ge(String columnName, Number value) {
		if (isEmpty(value)) {
			return this;
		}
		sql.append(" and ").append(columnName).append(" >= ? ");
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
	public Query gt(String columnName, Number value) {
		if (isEmpty(value)) {
			return this;
		}
		sql.append(" and ").append(columnName).append(" > ? ");
		setParameters(value);
		return this;
	}

	/**
	 * 排序
	 * 
	 * @param order
	 * @return Query
	 * @Author 杨健/YangJian
	 * @Date 2015年7月28日 下午3:41:43
	 * @Version 1.0.0
	 */
	public Query orderBy(OrderBy order) {
		if (isEmpty(order)) {
			return this;
		}
		this.orders.add(order);
		return this;
	}

	public Query limit(int start, int limit) {
		sql.append(" limit ? offset ? ");
		parameters.add(limit);
		parameters.add(start);
		return this;
	}

	/** 排序 */
	private void appendOrderBy() {
		if (isEmpty(orders)) {
			return;
		}
		sql.append(" order by ");

		int size = orders.size();

		for (int i = 0; i < size; i++) {
			sql.append(orders.get(i).toString());
			if (i < size - 1) {
				sql.append(",");
			}
		}
	}

	public static <T> BeanHandler<T> getBeanHandler(Class<T> clazz) {
		RowProcessor rowProcessor = new BasicRowProcessor(new GenerousBeanProcessor());
		BeanHandler<T> bh = new BeanHandler<T>(clazz, rowProcessor);
		return bh;
	}

	public static <T> BeanListHandler<T> getBeanListHandler(Class<T> clazz) {
		RowProcessor rowProcessor = new BasicRowProcessor(new GenerousBeanProcessor());
		BeanListHandler<T> bh = new BeanListHandler<T>(clazz, rowProcessor);
		return bh;
	}

	public Long getCount() throws SQLException {
		if (isEmpty(sql)) {
			return null;
		}
		ScalarHandler<Long> handler = new ScalarHandler<Long>(1);
		Long count = JdbcUtils.getRunnerWithDataSource().query(sql.toString(), handler, parameters.toArray());
		debug(count);
		return count;
	}

	public <T> T getCount(ScalarHandler<T> sh) throws SQLException {
		if (isEmpty(sql)) {
			return null;
		}
		T count = JdbcUtils.getRunnerWithDataSource().query(sql.toString(), sh, parameters.toArray());
		debug(count);
		return count;
	}

	public Map<String, Object> map() throws SQLException {
		if (isEmpty(sql)) {
			return null;
		}
		appendOrderBy();
		Map<String, Object> map = JdbcUtils.getRunnerWithDataSource().query(sql.toString(), new MapHandler(),
				parameters.toArray());
		debug(map);
		return map;
	}

	public List<Map<String, Object>> listmap() throws SQLException {
		if (isEmpty(sql)) {
			return null;
		}
		appendOrderBy();
		List<Map<String, Object>> map = JdbcUtils.getRunnerWithDataSource().query(sql.toString(),
				new MapListHandler(), parameters.toArray());
		debug(map);
		return map;
	}

	@SuppressWarnings("unchecked")
	public <T> T singleResult() throws SQLException {
		if (isEmpty(sql)) {
			return null;
		}

		if (isEmpty(clazz)) {
			throw new IllegalArgumentException("Not set clazz!");
		}

		BeanHandler<T> beanHandler = (BeanHandler<T>) getBeanHandler(clazz);
		T list = JdbcUtils.getRunnerWithDataSource().query(sql.toString(), beanHandler, parameters.toArray());
		debug(list);
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> list() throws SQLException {
		if (isEmpty(sql)) {
			return null;
		}

		if (isEmpty(clazz)) {
			throw new IllegalArgumentException("Not set clazz!");
		}

		appendOrderBy();

		BeanListHandler<T> beanListHandler = (BeanListHandler<T>) getBeanListHandler(clazz);
		List<T> list = JdbcUtils.getRunnerWithDataSource().query(sql.toString(), beanListHandler,
				parameters.toArray());

		debug(list);
		return list;
	}

	public <T> T singleResult(Class<T> clazz) throws SQLException {
		if (isEmpty(sql)) {
			return null;
		}
		BeanHandler<T> beanHandler = getBeanHandler(clazz);
		T list = JdbcUtils.getRunnerWithDataSource().query(sql.toString(), beanHandler, parameters.toArray());
		debug(list);
		return list;
	}

	public <T> List<T> list(Class<T> clazz) throws SQLException {
		if (isEmpty(sql)) {
			return null;
		}

		appendOrderBy();

		BeanListHandler<T> beanListHandler = getBeanListHandler(clazz);
		List<T> list = JdbcUtils.getRunnerWithDataSource().query(sql.toString(), beanListHandler,
				parameters.toArray());

		debug(list);
		return list;
	}

	public <T> List<T> list(ColumnListHandler<T> columnListHandler) throws SQLException {
		if (isEmpty(sql)) {
			return null;
		}

		appendOrderBy();

		List<T> list = JdbcUtils.getRunnerWithDataSource().query(sql.toString(), columnListHandler,
				parameters.toArray());
		debug(list);
		return list;
	}

	private void debug(Object result) {

		if (!isDubeg) {
			return;
		}

		System.out.println("SQL=" + sql.toString());
		System.out.println("SQL Parameters=" + parameters.toArray());
		System.out.println("SQL Value=" + result);
	}

}
