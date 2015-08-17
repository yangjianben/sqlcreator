package com.github.sqlcteator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestSqlCteator {

	@Test
	public void main() {
		List<String> values = new ArrayList<String>();
		values.add("yang");
		values.add("yang2");
		Query query = Query.selectAll("nihao");
		query.eq("name", "yangjian");
		query.eq("password", "yj");
		query.notEq("id", "1");
		query.or("name", "yang");
		query.in("nickname", values);
		System.out.println(query.toString());
	}
}
