package com.github.sqlcteator.mapping.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sqlcteator.mapping.Mapping;

/**
 * @comment mapping的使用例子。
 * @author wangshuo
 * @date 2013-7-12 上午11:39:43
 * @version 1.0.0
 */
public class MappingExample {

	// private static Mapping m = new Mapping(new Class[]{MapperObj.class});

	public static void main(String[] args) {
		//mapToObj();
		//objToObj();
		//mapToObjList();
		//objToObjList();
	}

	static void mapToObj() {
		Mapping m = new Mapping();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 10);
		map.put("name", "123");
		map.put("desc", "test");

		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			MapperObj obj = m.convertMap(MapperObj.class, map);
			System.out.println(obj);
		}
		long t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
	}

	static void objToObj() {
		Mapping m = new Mapping();
		OldObject_1 obj1 = new OldObject_1();
		obj1.setId(10);
		obj1.setName("tiantianwan");
		obj1.setDesc("tiantianwa.tiantianle.tiantianxiao...");

		OldObject_2 obj2 = new OldObject_2();
		obj2.setCid(100);
		obj2.setNickName("1234567890");
		obj2.setDesc(".....................");

		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			MapperObj mp1 = m.convertObject(MapperObj.class, obj1);
			System.out.println(mp1);

			MapperObj2 mp2 = m.convertObject(MapperObj2.class, obj2);
			System.out.println(mp2);

			OldObject_2 obj3 = m.convertObject(OldObject_2.class, mp2);
			System.out.println(obj3);
		}
		long t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
	}

	static void mapToObjList() {
		Mapping m = new Mapping();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", i);
			map.put("name", "123" + i);
			map.put("desc", "test" + i);
			mapList.add(map);
		}

		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			List<MapperObj> ret = m.convertListMap(MapperObj.class, mapList);
			for (MapperObj mo : ret) {
				System.out.println(mo);
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
	}

	static void objToObjList() {
		Mapping m = new Mapping();
		List<OldObject_1> list1 = new ArrayList<OldObject_1>();
		List<OldObject_2> list2 = new ArrayList<OldObject_2>();
		for (int i = 0; i < 10; i++) {
			OldObject_1 obj1 = new OldObject_1();
			obj1.setId(i);
			obj1.setName("tiantianwan" + i);
			obj1.setDesc("tiantianwa.tiantianle.tiantianxiao..." + i);
			list1.add(obj1);

			OldObject_2 obj2 = new OldObject_2();
			obj2.setCid(i);
			obj2.setNickName("1234567890" + i);
			obj2.setDesc("....................." + i);
			list2.add(obj2);
		}
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			List<MapperObj> moList = m.convertList(MapperObj.class, list1);
			for (MapperObj mo : moList) {
				System.out.println(mo);
			}

			List<MapperObj2> mo2List = m.convertList(MapperObj2.class, list2);
			for (MapperObj2 mo2 : mo2List) {
				System.out.println(mo2);
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);
	}
}
