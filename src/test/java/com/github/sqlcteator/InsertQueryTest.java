package com.github.sqlcteator;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.github.sqlcteator.mapping.exam.MapperObj;
import com.github.sqlcteator.mapping.exam.MapperObj2;
import com.github.sqlcteator.test.po.WhPickingWall;

public class InsertQueryTest {
	private static final String NEW_LINE = System.getProperty("line.separator");

	@Test
	public void createInsert() {
		long start = System.currentTimeMillis();// Instant.now().toEpochMilli();
		Insert query = null;
		WhPickingWall whPickingWall = null;
		for (int i = 0; i < 10000; i++) {
			whPickingWall = new WhPickingWall();
			whPickingWall.setWallNo(System.currentTimeMillis() + "");
			query = new Insert(whPickingWall);
			query.toString();
		}
		long end = System.currentTimeMillis();// Instant.now().toEpochMilli();
		System.out.println(end - start);
	}

	@Test
	public void createQueryColumnsAndValues2() throws Exception {
		MapperObj2 mapperObj = new MapperObj2();
		mapperObj.setCreateDate(new Date());
		mapperObj.setDesc("你好中国");
		mapperObj.setId(1222);
		mapperObj.setName("中国");
		Insert query = new Insert(mapperObj);
		// When
		String sql = query.toString();
		String sqlAndValue = query.toString();
		System.out.println(sql);
		System.out.println("===============");
		System.out.println(sqlAndValue);

		System.out.println("===============");
		Object[] params = query.getColumns();
		for (Object param : params) {
			System.out.println(param.toString());
		}
		System.out.println("===============");
		Object[] values = query.getValues();
		for (Object value : values) {
			System.out.println(value);
		}
	}

	@Test
	public void createQueryColumnsAndValues() throws Exception {
		MapperObj mapperObj = new MapperObj();
		mapperObj.setCreateDate(new Date());
		mapperObj.setDesc("你好中国");
		mapperObj.setId(1222);
		mapperObj.setName("中国");
		// Given
		// Insert query = new Insert(MapperObj.class).values(1, "foo", 30,"");
		Insert query = new Insert(mapperObj);
		// When
		String sql = query.toString();
		String sqlAndValue = query.toString();
		System.out.println(sql);
		System.out.println("===============");
		System.out.println(sqlAndValue);

		System.out.println("===============");
		Object[] params = query.getColumns();
		for (Object param : params) {
			System.out.println(param.toString());
		}
		System.out.println("===============");
		Object[] values = query.getValues();
		for (Object value : values) {
			System.out.println(value);
		}
	}

	@Test
	public void shouldCreateQueryPassingColumnsAndValues() throws Exception {
		// Given
		Insert query = new Insert().insert("person").columns("id", "name", "age").values(1, "foo", 30);

		// When
		String actual = query.toString();

		// Then
		String expected = new StringBuilder("INSERT INTO person").append(NEW_LINE).append(" ( ")
				.append("id, name, age").append(" )").append(NEW_LINE).append("VALUES (").append("1, 'foo', 30")
				.append(")").toString();

		assertEquals(expected, actual);
	}

	@Test
	public void shouldCreateQueryPassingColumnsAndManyValues() throws Exception {
		// Given
		Insert query = new Insert().insert("person").columns("id", "name", "age").values(1, "foo", 30)
				.values(2, "bar", 23).values(3, "hello", 54).values(4, "world", 19);

		// When
		String actual = query.toString();

		// Then
		String expected = new StringBuilder("INSERT INTO person").append(NEW_LINE).append(" ( ")
				.append("id, name, age").append(" )").append(NEW_LINE)
				.append("VALUES (1, 'foo', 30), (2, 'bar', 23), (3, 'hello', 54), (4, 'world', 19)").toString();

		assertEquals(expected, actual);
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowErrorIfNoColumnsIsInformed() throws Exception {
		// Given
		Insert query = new Insert().insert("person").columns("id", "name", "age");

		// When
		query.toString();
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowErrorIfNoValuesIfPassed() throws Exception {
		// Given
		Insert query = new Insert().table("person").values(1, "foo", 30);

		// When
		query.toString();
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowErrorIfColumnsSizeIsDifferentOfValuesSize() throws Exception {
		// Given
		Insert query = new Insert().insert("person").columns("id", "name", "age").values(1, "bar", 20)
				.values(2, "foo", 30, "bar").values(3, "hello", 45);

		// When
		query.toString();
	}
}