package com.jd.samples.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import com.jd.samples.bean.WxPcInfo;

public class DBManager {
	private static final Logger logger = Logger.getLogger(DBManager.class);
	static {
		DbUtils.loadDriver("com.mysql.jdbc.Driver");
	}

	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://10.0.16.16:4066/his4u_mysql_40wcj8vu",
					"ZlANddGd", "bx51lZC0AeRD");

		} catch (SQLException e) {
			logger.error("getConnection fail", e);
		}
		return connection;
	}

	public static void closeConnection(Connection connection) {
		if (connection != null)
			try {
				DbUtils.close(connection);
			} catch (SQLException e) {
				logger.error("closeConnection fail", e);
			}
	}

	public static List<Map<String, Object>> query() {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		List<Map<String, Object>> query = null;
		try {
			query = qRunner.query(connection, "select * from user",
					new MapListHandler());
		} catch (SQLException e) {
			logger.error("query fail", e);
		}
		closeConnection(connection);
		return query;
	}

	public static int updateLocation(String usid, float lc_x, float lc_y,
			String address) {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		int updated = 0;
		try {
			System.out.println("====start updateLocation====");
			updated = qRunner
					.update(connection,
							"update spcinfo set lc_x = ?,lc_y=?,address=?,createTime=NOW() where usid = ?",
							lc_x, lc_y, address, usid);
			if (updated == 0) {
				updated = qRunner
						.update(connection,
								"insert into spcinfo (usid,lc_x,lc_y,address) values (?,?,?,?)",
								usid, lc_x, lc_y, address);
			}
			System.out.println("====start updateLocation====" + updated);
		} catch (SQLException e) {
			logger.error("updateLocation fail", e);
			System.out.println("====SQLException====" + e);
		}
		closeConnection(connection);
		return updated;
	}

	public static List<WxPcInfo> queryInfos(float lc_x, float lc_y) {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		List<WxPcInfo> wxPcInfos = null;
		try {
			wxPcInfos = qRunner
					.query(connection,
							"select * from spcinfo where lc_x between ? AND ? and lc_y between ? AND ? and createTime > TIMESTAMPADD(MINUTE,-30,NOW())",
							new BeanListHandler<WxPcInfo>(WxPcInfo.class),
							lc_x - 1, lc_x + 1, lc_y - 1, lc_y + 1);
		} catch (SQLException e) {
			logger.error("queryInfos fail", e);
		}
		closeConnection(connection);
		return wxPcInfos;
	}

	public static WxPcInfo queryLocation(String usid) {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		WxPcInfo wxPcInfo = null;
		try {
			wxPcInfo = qRunner.query(connection,
					"select lc_x,lc_y from spcinfo where usid = ?",
					new BeanHandler<WxPcInfo>(WxPcInfo.class), usid);
		} catch (SQLException e) {
			logger.error("queryInfos fail", e);
		}
		closeConnection(connection);
		return wxPcInfo;
	}

	public static int updateDest(String dest, String usid) {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		int updated = 0;
		try {
			updated = qRunner
					.update(connection,
							"update spcinfo set destination = ? where usid = ? and createTime > TIMESTAMPADD(MINUTE,-30,NOW())",
							dest, usid);
		} catch (SQLException e) {
			logger.error("updateDest fail", e);
		}
		closeConnection(connection);
		return updated;
	}

}
