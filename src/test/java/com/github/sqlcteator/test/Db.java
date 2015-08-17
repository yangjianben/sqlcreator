package com.github.sqlcteator.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

public class Db {

	private static String driveClassName = "org.postgresql.Driver";
	private static String url = "jdbc:postgresql://10.32.2.186:5432/dal_erp_test_1";

	private static String user = "postgres";
	private static String password = "pgsql";

	public static Connection getConn() {
		Connection conn = null;

		// load driver
		DbUtils.loadDriver(driveClassName);

		// connect db
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("connect failed!");
			e.printStackTrace();
		}
		return conn;
	}
	
	public static Connection getConn(String url,String user,String password) {
		Connection conn = null;

		// load driver
		DbUtils.loadDriver(driveClassName);

		// connect db
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("connect failed!");
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) throws SQLException {
		Connection conn = getConn();
		System.out.println(conn);
		conn.close();
	}
}
