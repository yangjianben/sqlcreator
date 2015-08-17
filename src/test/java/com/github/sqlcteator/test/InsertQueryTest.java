package com.github.sqlcteator.test;

import java.util.Date;

import org.junit.Test;

import com.github.sqlcteator.Insert;
import com.github.sqlcteator.Query;

public class InsertQueryTest {
	
	@Test
	public void createQueryColumnsAndValues2() throws Exception {

		OrderTask orderTask = new OrderTask();
		orderTask.setBatchNumbers(System.currentTimeMillis()+"");
		orderTask.setDs(new Date());
		orderTask.setOrderAmount(1);
		Insert query = new Insert(orderTask,false);

		String sql = query.toString();
		Insert query2 = new Insert(orderTask,true);
		String sqlAndValue = query2.toString();
		System.out.println(sql);
		System.out.println("===============");
		System.out.println(sqlAndValue);

		/*System.out.println("===============");
		Object[] params = query.getColumns();
		for (Object param : params) {
			System.out.println(param.toString());
		}
		System.out.println("===============");
		Object[] values = query.getValues();
		for (Object value : values) {
			System.out.println(value);
		}*/
	}
	
	@Test
	public void queryColumnsAndValues2() throws Exception {

		Query sql = new Query(OrderTask.class);
		System.out.println("===============");
		System.out.println(sql.toString());
		
	}
}