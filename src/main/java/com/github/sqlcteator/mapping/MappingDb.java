package com.github.sqlcteator.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class MappingDb {

	private Class<?> clazz;
	private Object obj;
	private boolean mapUnderscoreToCamelCase = true;

	private MappingDb() {
	}

	public MappingDb(Class<?> clazz) {
		this();
		this.clazz = clazz;
	}

	public MappingDb(Object obj) {
		this();
		this.clazz = obj.getClass();
		this.obj = obj;
	}

	/**
	 * 对象获取字段时加下划线，反之，数据表映射对象时去掉下划线（驼峰）
	 * 
	 * @param mapUnderscoreToCamelCase
	 *            void
	 * @Author 杨健/YangJian
	 * @Date 2015年5月7日 上午11:10:25
	 * @Version 1.0.0
	 */
	public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
		this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
	}

	public boolean isMapUnderscoreToCamelCase() {
		return mapUnderscoreToCamelCase;
	}

	/**
	 * 将对象属性转换对应的数据库字段
	 * 
	 * @param clazz
	 * @return List<String>
	 * @Author 杨健/YangJian
	 * @Date 2015年5月7日 上午11:48:07
	 * @Version 1.0.0
	 */
	public List<String> getColumns() {
		List<String> columns = new ArrayList<String>();
		Map<String, MappingField> map = Mapping.m.getFieldMap(this.clazz);
		Set<String> keySet = map.keySet();
		String keyName = null;
		MappingField objField = null;
		for (String key : keySet) {
			objField = map.get(map.get(key).getKeyName());
			if (!objField.isMapping()) {
				continue;
			}
			keyName = objField.getKeyName();
			if (mapUnderscoreToCamelCase) {
				keyName = camelToUnderscore(keyName);
				if (null == keyName || "".equals(keyName)) {
					continue;
				}
			}
			columns.add(keyName);
		}
		return columns;
	}

	/**
	 * 将对象属性转换对应的数据库字段
	 * 
	 * @param clazz
	 * @return List<String>
	 * @Author 杨健/YangJian
	 * @Date 2015年5月7日 上午11:48:07
	 * @Version 1.0.0
	 */
	public List<MappingField> getFields() {
		List<MappingField> columns = new ArrayList<MappingField>();
		Map<String, MappingField> map = Mapping.m.getFieldMap(this.clazz);
		Set<String> keySet = map.keySet();
		MappingField objField = null;
		for (String key : keySet) {
			objField = map.get(map.get(key).getKeyName());
			if (!objField.isMapping()) {
				continue;
			}
			columns.add(objField);
		}
		return columns;
	}

	public Object[] getValues() {

		// 被转换对象的field Map，获取属性对应的值
		this.setMapUnderscoreToCamelCase(false);

		Map<String, MappingField> map = Mapping.m.getFieldMap(this.clazz);
		List<String> columns = getColumns();
		Object[] values = new Object[columns.size()];
		Object fieldValue = null;
		MappingField objField = null;
		try {
			int i = 0;
			for (String key : columns) {
				// 被转化对象的field信息。
				objField = map.get(map.get(key).getKeyName());
				if (!objField.isMapping()) {
					continue;
				}
				if (objField != null) {
					// 获得被转化对象的该字段的值。
					fieldValue = objField.getFieldValue(this.obj);
					if ((fieldValue == null || "".equals(fieldValue))
							&& (objField.isPrimaryKey() || "id".equalsIgnoreCase(key))) {
						fieldValue = StringUtils.join("nextval ('", objField.getSequenceName(), "')");
					}
				}
				values[i++] = fieldValue;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return values;
	}

	/**
	 * 将驼峰字段转换为下划线字段
	 * 
	 * @param param
	 * @return String
	 * @Author 杨健/YangJian
	 * @Date 2015年5月7日 上午11:49:06
	 * @Version 1.0.0
	 */
	public static String camelToUnderscore(String param) {
		Pattern pattern = Pattern.compile("[A-Z]");
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(param);
		Matcher matcher = pattern.matcher(param);
		int i = 0;
		while (matcher.find()) {
			builder.replace(matcher.start() + i, matcher.end() + i,
					StringUtils.join("_", matcher.group().toLowerCase()));
			i++;
		}
		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}
}
