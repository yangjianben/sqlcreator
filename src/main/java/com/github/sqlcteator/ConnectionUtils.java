package com.github.sqlcteator;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

/**
 * 
 * @Comment Connection管理器
 * @Author 杨健/YangJian
 * @Date 2015年6月29日 下午5:30:30
 * @Version 1.0.0
 */
public class ConnectionUtils {

	// public static Connection getConnection() throws SQLException {
	// return getDataSource().getConnection();
	// }

	public static void beginTransaction(Connection conn) throws SQLException {
		conn.setAutoCommit(false);
	}

	public static void commitTransaction(Connection conn) {
		DbUtils.commitAndCloseQuietly(conn);
	}

	public static void rollbackTransaction(Connection conn) {
		DbUtils.rollbackAndCloseQuietly(conn);
	}

	// public static DataSource getDataSource() {
	// return DataSourceManager.getInstance().getDataSource();
	// }

	public static QueryRunner getRunner() {
		return new QueryRunner();
	}

	public static QueryRunner getRunnerWithDataSource() {
		return new QueryRunner();//new QueryRunner(getDataSource());
	}

}
