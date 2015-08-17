package com.github.sqlcteator.mapping;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.omg.CORBA.portable.ApplicationException;

import com.github.sqlcteator.mapping.annotations.Fields;

/**
 * @comment 转换对象将Map转换成对象或者将对象转换成另外一个对象。可用于PO到VO的相互转换<br/>
 *          新对象的成员变量名称要与Map的key或者被转换对象的成员变量名称一样。 如果不一样可以通过 com.platform.kit.mapping.annotations.Fields
 *          标注来注释新对象的成员对应那些名称。 可以参看com.platform.kit.mapping.mapping.exam包中的 MapperObj2 和 OldObject_2.<br/>
 * <br/>
 *          使用方式：<br/>
 *          1、public static Mapping mapping = new Mapping(); //将该Mapping注册成为全局变量。<br/>
 *          2、public static Mapping mapping = new Mapping(new Class[]{MapperObj.class, MapperObj2.class, …………});
 *          为mapping提前注册各个需要转换的类。
 *
 * @author YangJian
 * @date 2013-7-10 下午5:11:47
 * @version 1.0.1
 */
public class Mapping {

	public final static Mapping m = new Mapping();

	private final Map<String, Map<String, MappingField>> mfMap = new TreeMap<String, Map<String, MappingField>>();

	private final Map<String, Constructor<?>> consMap = new TreeMap<String, Constructor<?>>();

	/**
	 * 无参构造方法。不设定初始化对象的class。
	 */
	public Mapping() {
	}

	/**
	 * 通过该构造方法可以提前注册各个转换对象及被转换对象的class。如果调用无参构造方法不进行提前注册那么将会在具体转换时对对象的class进行注册。
	 * 
	 * @param clazzs
	 */
	public Mapping(Class<?>... clazzs) {
		if (clazzs != null) {
			for (Class<?> clazz : clazzs) {
				getFieldMap(clazz);
			}
		}
	}

	/**
	 * 将Map对象转换成指定对象
	 * 
	 * @param clazz
	 *            指定的转换后的对象。
	 * @param map
	 *            被转换信息的map。
	 * @return clazz参数指定的对象。
	 * @exception ApplicationException
	 *                转换失败。
	 * @Author wangshuo
	 * @since 1.0.0
	 */
	public <E extends Serializable> E convertMap(Class<E> clazz, Map<String, Object> map) {
		if (clazz == null || map == null) {
			return null;
		}
		Map<String, MappingField> fieldMap = getFieldMap(clazz);
		if (fieldMap == null) {
			return null;
		}
		Constructor<?> c = consMap.get(clazz.getName());
		if (c == null) {
			return null;
		}
		Collection<MappingField> col = fieldMap.values();
		return this.convertObjectFromMap(col, c, map);
	}

	/**
	 * 将List转换成指定对象的List。
	 * 
	 * @param clazz
	 *            指定的转换后的对象。
	 * @param mapList
	 *            List集合每个对象为map。每个map包含具体的对象信息。
	 * @return List。list内每个对象元素为clazz参数指定的对象。
	 * @exception ApplicationException
	 *                转换失败。
	 * @Author wangshuo
	 * @since 1.0.1
	 */
	public <E extends Serializable> List<E> convertListMap(Class<E> clazz, List<Map<String, Object>> mapList) {
		if (clazz == null || mapList == null) {
			return null;
		}
		if (mapList.size() == 0) {
			return new ArrayList<E>();
		}

		Map<String, MappingField> fieldMap = getFieldMap(clazz);
		if (fieldMap == null) {
			return null;
		}
		Constructor<?> c = consMap.get(clazz.getName());
		if (c == null) {
			return null;
		}
		Collection<MappingField> col = fieldMap.values();
		List<E> ret = new ArrayList<E>();
		for (Map<String, Object> map : mapList) {
			E e = this.convertObjectFromMap(col, c, map);
			ret.add(e);
		}
		return ret;
	}

	/**
	 * 将对象转换成指定的对象。
	 * 
	 * @param clazz
	 *            指定转换成的对象。
	 * @param obj
	 *            被转换的对象。
	 * @return clazz参数指定转换成的对象。
	 * @exception ApplicationException
	 *                转换失败。
	 * @Author wangshuo
	 * @since 1.0.0
	 */
	public <E extends Serializable> E convertObject(Class<?> clazz, Object obj) {
		if (clazz == null || obj == null) {
			return null;
		}
		Map<String, MappingField> fieldMap = getFieldMap(clazz);
		// 被转换对象的field Map。
		Map<String, MappingField> objFieldMap = getFieldMap(obj.getClass());
		if (fieldMap == null || objFieldMap == null) {
			return null;
		}
		Constructor<?> c = consMap.get(clazz.getName());
		if (c == null) {
			return null;
		}
		Collection<MappingField> col = fieldMap.values();
		return this.convertObjectFromObject(col, c, objFieldMap, obj);
	}

	/**
	 * 将原始数据的List转换成指定对象的List。
	 * 
	 * @param clazz
	 *            指定转换成的对象。
	 * @param objList
	 *            原始数据List集合。
	 * @return List。list内每个对象元素为clazz参数指定的对象。
	 * @exception ApplicationException
	 *                转换失败。
	 * @Author wangshuo
	 * @since 1.0.1
	 */
	public <E extends Serializable> List<E> convertList(Class<E> clazz, List<?> objList) {
		if (clazz == null || objList == null) {
			return null;
		}

		if (objList.size() == 0) {
			return new ArrayList<E>();
		}

		Map<String, MappingField> fieldMap = getFieldMap(clazz);
		// 被转换对象的field Map。
		Map<String, MappingField> objFieldMap = getFieldMap(objList.get(0).getClass());
		if (fieldMap == null || objFieldMap == null) {
			return null;
		}
		Constructor<?> c = consMap.get(clazz.getName());
		if (c == null) {
			return null;
		}
		Collection<MappingField> col = fieldMap.values();
		List<E> ret = new ArrayList<E>();
		for (Object obj : objList) {
			E e = this.convertObjectFromObject(col, c, objFieldMap, obj);
			ret.add(e);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private <E extends Serializable> E convertObjectFromMap(Collection<MappingField> col, Constructor<?> c,
			Map<String, Object> map) {
		if (map == null) {
			return null;
		}
		try {
			E e = (E) c.newInstance();
			for (MappingField mf : col) {
				if (!mf.isMapping()) {
					continue;
				}
				Object obj = map.get(mf.getKeyName());
				if (obj != null) {
					mf.setFieldValue(e, obj);
				}
			}
			return e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private <E extends Serializable> E convertObjectFromObject(Collection<MappingField> col, Constructor<?> c,
			Map<String, MappingField> objFieldMap, Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			E e = (E) c.newInstance();
			for (MappingField mf : col) {
				if (!mf.isMapping()) {
					continue;
				}
				Object fieldValue = null;
				// 被转化对象的field信息。
				MappingField objField = objFieldMap.get(mf.getKeyName());
				if (objField != null) {
					// 获得被转化对象的该字段的值。
					fieldValue = objField.getFieldValue(obj);
				}
				if (fieldValue != null) {
					mf.setFieldValue(e, fieldValue);
				}
			}
			return e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, MappingField> getFieldMap(Class<?> clazz) {
		try {
			String className = clazz.getName();
			Map<String, MappingField> map = mfMap.get(className);
			if (map == null) {
				Field[] fields = clazz.getDeclaredFields();
				if (fields == null || fields.length == 0) {
					return null;
				}
				map = new TreeMap<String, MappingField>();
				for (Field f : fields) {
					if ("serialVersionUID".equals(f.getName())) {
						continue;
					}
					Fields ann = getAnnotation(f);
					MappingField mf = new MappingField(f, ann);
					map.put(mf.getKeyName(), mf);
				}
				mfMap.put(className, map);

				Constructor<?> c = clazz.getDeclaredConstructor();
				consMap.put(className, c);
			}
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Fields getAnnotation(Field f) {
		if (f.isAnnotationPresent(Fields.class)) {
			return f.getAnnotation(Fields.class);
		}
		return null;
	}

}
