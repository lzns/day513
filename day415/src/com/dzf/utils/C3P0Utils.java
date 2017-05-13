package com.dzf.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Utils {
	private static ComboPooledDataSource cpd = new ComboPooledDataSource();

	private static ThreadLocal<Connection> tl = new ThreadLocal<>();

	/**
	 * 返回的是一个DataSource实例
	 * 
	 * @return dateSource
	 */
	public static DataSource GetDataSource() {
		return cpd;
	}

	/**
	 * 从当前线程上获取连接 返回时的是一个Connection实例
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = tl.get();
		if (conn == null) {
			// 第一次获取，创建一个连接，和当前的线程绑定
			conn = cpd.getConnection();
			// 和当前的线程绑定
			tl.set(conn);
		}
		return conn;
	}

	/**
	 * 开启事务
	 * 
	 * @throws SQLException
	 */
	public static void startTransAction() throws SQLException {
		// 开启事务之前先获取连接
		getConnection().setAutoCommit(false);
	}

	/**
	 * 事务提交和关闭连接(Connection的实例)
	 */
	public static void commitAndClose() {
		Connection conn = null;
		try {
			// 获取连接
			conn = getConnection();
			// 提交
			conn.commit();
			// 关闭资源
			closeConnection(conn, null, null);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/**
	 * 事务回滚和关闭连接(Connection的实例)
	 */
	public static void rollBackAndClose() {
		Connection conn = null;
		// 获取连接
		try {
			conn = getConnection();
			conn.rollback();
			closeConnection(conn, null, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 释放资源
	 * 
	 * @param conn
	 *            Connection
	 * @param state
	 *            Statement
	 * @param rs
	 *            ResultSet
	 */
	public static void closeConnection(Connection conn, Statement state, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
				// 和当前的线程解绑
				tl.remove();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				conn = null;
			}
		}
		if (state != null) {
			try {
				state.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				state = null;
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				rs = null;
			}
		}
	}

}
