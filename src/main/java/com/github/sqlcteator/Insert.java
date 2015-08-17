package com.github.sqlcteator;

import static com.github.sqlcteator.util.StrUtil.isEmpty;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang3.StringUtils;

import com.github.sqlcteator.mapping.MappingDb;
import com.github.sqlcteator.mapping.MappingField;
import com.google.common.collect.Lists;

/**
 * 
 * @Comment SQL INSERT
 * @Author 杨健/YangJian
 * @Date 2015年7月2日 下午2:32:08
 * @Version 1.0.0
 */
public class Insert {

	private String table;
	private Class<?> clazz;
	private MappingDb mappingDb;
	private final List<MappingField> fields;
	private final List<String> columns;
	private final List<Object[]> values;
	private boolean seqBefore = false;
	private boolean terminated = false;
	private boolean isPrepareStatement = true;
	private final StringBuilder sql;
	private final static ColumnListHandler<Long> rsh = new ColumnListHandler<Long>("id");
	private boolean isDubeg = false;

	public Insert() {
		this.sql = new StringBuilder(" INSERT INTO ");
		this.fields = new LinkedList<>();
		this.columns = new LinkedList<>();
		this.values = new LinkedList<>();
	}

	public Insert(String table) {
		this();
		this.table = table;
	}

	public Insert(Class<?> clazz) {
		this();
		this.clazz = clazz;
		this.mappingDb = new MappingDb(this.clazz);
		this.table = MappingDb.camelToUnderscore(this.clazz.getSimpleName());
		if (this.mappingDb.isMapUnderscoreToCamelCase()) {
			this.table = MappingDb.camelToUnderscore(this.clazz.getSimpleName());
		} else {
			this.table = this.clazz.getSimpleName();
		}
		this.fields.addAll(this.mappingDb.getFields());
		this.columns.addAll(this.mappingDb.getColumns());
	}

	public Insert(Class<?> clazz, boolean mapUnderscoreToCamelCase) {
		this();
		this.clazz = clazz;
		this.mappingDb = new MappingDb(this.clazz);
		this.mappingDb.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
		if (mapUnderscoreToCamelCase) {
			this.table = MappingDb.camelToUnderscore(this.clazz.getSimpleName());
		} else {
			this.table = this.clazz.getSimpleName();
		}
		this.fields.addAll(this.mappingDb.getFields());
		this.columns.addAll(this.mappingDb.getColumns());
	}

	public Insert(Object obj) {
		this();
		this.clazz = obj.getClass();
		this.mappingDb = new MappingDb(obj);
		if (this.mappingDb.isMapUnderscoreToCamelCase()) {
			this.table = MappingDb.camelToUnderscore(this.clazz.getSimpleName());
		} else {
			this.table = this.clazz.getSimpleName();
		}
		this.fields.addAll(this.mappingDb.getFields());
		this.columns.addAll(this.mappingDb.getColumns());
		this.values.add(this.mappingDb.getValues());
	}

	public Insert(Object obj, boolean mapUnderscoreToCamelCase) {
		this();
		this.clazz = obj.getClass();
		this.mappingDb = new MappingDb(obj);
		this.mappingDb.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
		if (mapUnderscoreToCamelCase) {
			this.table = MappingDb.camelToUnderscore(this.clazz.getSimpleName());
		} else {
			this.table = this.clazz.getSimpleName();
		}
		this.fields.addAll(this.mappingDb.getFields());
		this.columns.addAll(this.mappingDb.getColumns());
		this.values.add(this.mappingDb.getValues());
	}

	public Insert insert(String table) {
		this.table = table;
		return this;
	}

	/**
	 * 设置序列seq前置还是后置，默认后置
	 * 
	 * @param seqBefore
	 * @return void
	 * @Author 杨健/YangJian
	 * @Date 2015年7月21日 上午11:38:17
	 * @Version 1.0.0
	 */
	public void setSeqBefore(boolean seqBefore) {
		this.seqBefore = seqBefore;
	}

	public String getSequenceName() {
		String sequenceName = StringUtils.join(this.table, "_id_seq");
		if (seqBefore) {
			sequenceName = StringUtils.join("seq_", this.table, "_id");
		}
		return sequenceName.toUpperCase();
	}

	public Insert append(String expression) {
		sql.append(expression);
		return this;
	}

	public Insert appendLine(String expression) {
		sql.append(expression);
		return this;
	}

	public Insert table(String table) {
		this.table = table;
		return this;
	}

	public Insert columns(String... columns) {
		Collections.addAll(this.columns, columns);
		return this;
	}

	public Insert values(Object... values) {
		this.values.add(values);
		return this;
	}

	public String toString() {
		toString(true);
		return sql.toString();
	}

	public String toString(boolean isPrepareStatement) {
		this.isPrepareStatement = isPrepareStatement;
		if (isPrepareStatement) {
			terminatePrepareStatement();
		} else {
			terminate();
		}
		return sql.toString();
	}

	private void terminatePrepareStatement() {
		if (columns.isEmpty())
			throw new RuntimeException("No columns informed!");
		if (values.isEmpty())
			throw new RuntimeException("No values informed!");

		for (Object[] valueSet : values) {
			if (valueSet.length != columns.size()) {
				throw new RuntimeException("Value size different from column size!");
			}
		}

		if (!terminated) {
			this.appendLine(table).append(" ( ").append(StringUtils.join(columns, ", ")).appendLine(" )")
					.append(" VALUES (");

			for (MappingField field : fields) {
				if (field.isPrimaryKey() || "id".equalsIgnoreCase(field.getKeyName())) {
					this.append(StringUtils.join(" nextval ('",
							field.getSequenceName() == null ? this.getSequenceName() : field.getSequenceName(),
							"'), "));
				} else {
					this.append(StringUtils.join("?", ", "));
				}
			}
			sql.deleteCharAt(sql.length() - 2);
			sql.append(")");
		}
	}

	private void terminate() {
		if (columns.isEmpty())
			throw new RuntimeException("No columns informed!");
		if (values.isEmpty())
			throw new RuntimeException("No values informed!");

		for (Object[] valueSet : values) {
			if (valueSet.length != columns.size()) {
				throw new RuntimeException("Value size different from column size!");
			}
		}

		if (!terminated) {
			this.appendLine(table).append(" ( ").append(StringUtils.join(columns, ", ")).appendLine(" )")
					.append("VALUES ").append(StringUtils.join(getSqlValues(), ", "));
		}
	}

	public Object[] getColumns() {
		Object[] result = new Object[values.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = values.get(i);
		}
		return columns.toArray();
	}

	public Object[] getValues() {
		List<Object> v = Lists.newArrayList();
		for (Object[] v2 : values) {
			for (Object value : v2) {
				if (isPrepareStatement && value != null && value.toString().startsWith("nextval")) {
					continue;
				} else if (value != null && value instanceof Date) {
					Date date = (Date) value;
					v.add(new Timestamp(date.getTime()));
				} else {
					v.add(value);
				}
			}
		}
		return v.toArray();
	}

	private String[] getSqlValues() {
		String[] result = new String[values.size()];
		for (int i = 0; i < result.length; i++) {
			Object[] objs = values.get(i);
			result[i] = toValue(objs);
		}
		return result;
	}

	private String toValue(Object[] objs) {
		String[] result = new String[objs.length];

		for (int i = 0; i < result.length; i++) {
			if (objs[i] instanceof String) {
				if (objs[i].toString().startsWith("nextval")) {
					result[i] = objs[i].toString();
				} else {
					result[i] = StringUtils.join("'", objs[i].toString(), "'");
				}
			} else if (objs[i] instanceof Date) {
				Date date = (Date) objs[i];
				result[i] = "'" + new Timestamp(date.getTime()) + "'";
			} else {
				result[i] = objs[i] == null ? "null" : objs[i].toString();
			}
		}
		return "(" + StringUtils.join(result, ", ") + ")";
	}

	public List<Long> save(Connection connection) throws SQLException {
		List<Long> result = ConnectionUtils.getRunner().insert(connection, this.toString(), rsh, this.getValues());
		debug(result, null);
		return result;
	}

	public <T> List<T> save(Connection connection, ColumnListHandler<T> columnlisthandler) throws SQLException {
		List<T> result = ConnectionUtils.getRunner().insert(connection, this.toString(), columnlisthandler,
				this.getValues());
		debug(result, null);
		return result;
	}

	public List<Long> insertBatch(Connection connection, String sql, Object[][] batchParams) throws SQLException {
		List<Long> result = ConnectionUtils.getRunner().insertBatch(connection, sql, rsh, batchParams);
		debug(result, sql);
		return result;
	}

	public List<Long> save(Connection connection, String sql) throws SQLException {
		List<Long> result = ConnectionUtils.getRunner().insert(connection, sql, rsh, this.getValues());
		debug(result, sql);
		return result;
	}

	public List<Long> save(Connection connection, String sql, Object[] values) throws SQLException {
		List<Long> result = ConnectionUtils.getRunner().insert(connection, sql, rsh, values);
		debug(result, sql);
		return result;
	}

	public void setDubeg(boolean isDubeg) {
		this.isDubeg = isDubeg;
	}

	private void debug(Object result, String sql) {
		if (!isDubeg) {
			return;
		}
		if (isEmpty(sql)) {
			System.out.println("SQL=" + this.toString());
		} else {
			System.out.println("SQL=" + sql);
		}
		System.out.println("SQL Parameters=" + this.getValues());
		System.out.println("SQL Value=" + result);
	}
}