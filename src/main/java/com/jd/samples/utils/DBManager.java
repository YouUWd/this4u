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
import com.jd.samples.bean.WxSqInfo;
import com.jd.samples.bean.WxUserInfo;

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

	/**
	 * @param lc_x
	 *            纬度
	 * @param lc_y
	 *            经度
	 * @return 周边拼车信息
	 */
	public static List<WxPcInfo> queryInfos(float lc_x, float lc_y) {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		List<WxPcInfo> wxPcInfos = null;
		try {
			// 1km范围内的经纬度
			double[] around = DistanceUtil.getAround(lc_x, lc_y, 1000);
			wxPcInfos = qRunner
					.query(connection,
							"select * from spcinfo where lc_x between ? AND ? and lc_y between ? AND ? and createTime > TIMESTAMPADD(MINUTE,-30,NOW())",
							new BeanListHandler<WxPcInfo>(WxPcInfo.class),
							around[0], around[2], around[1], around[3]);
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

	// ###########################################社区信息模块###############################################
	/**
	 * @param lc_x
	 *            纬度
	 * @param lc_y
	 *            经度
	 * @return 周边社区服务信息
	 */
	public static List<WxSqInfo> querySqInfos(float lc_x, float lc_y) {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		List<WxSqInfo> wxPcInfos = null;
		try {
			// 1km范围内的经纬度
			double[] around = DistanceUtil.getAround(lc_x, lc_y, 1000);
			wxPcInfos = qRunner
					.query(connection,
							"select sq.* from sqinfo sq left join wuinfo wu on sq.usid = wu.usid where wu.lc_x between ? AND ? and wu.lc_y between ? AND ? and sq.createTime > TIMESTAMPADD(MONTH,-1,NOW())",
							new BeanListHandler<WxSqInfo>(WxSqInfo.class),
							around[0], around[2], around[1], around[3]);
		} catch (SQLException e) {
			logger.error("querySqInfos fail", e);
		}
		closeConnection(connection);
		return wxPcInfos;
	}

	/**
	 * @param usid
	 * @param lc_x
	 * @param lc_y
	 * @param address
	 * @return 更新用户信息
	 */
	public static int updateWuInfo(String usid, float lc_x, float lc_y,
			String address) {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		int updated = 0;
		try {
			updated = qRunner
					.update(connection,
							"update wuinfo set lc_x = ?,lc_y=?,address=? where usid = ?",
							lc_x, lc_y, address, usid);
			if (updated == 0) {
				updated = qRunner
						.update(connection,
								"insert into wuinfo (usid,lc_x,lc_y,address) values (?,?,?,?)",
								usid, lc_x, lc_y, address);
			}
		} catch (SQLException e) {
			logger.error("updateWuInfo fail", e);
		}
		closeConnection(connection);
		return updated;
	}

	/**
	 * @param usid
	 * @param serviceInfo
	 * @return 更新微信社区信息
	 */
	public static int updateWxSqInfo(String usid, String serviceInfo) {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		int updated = 0;
		try {
			updated = qRunner.update(connection,
					"update sqinfo set serviceInfo = ? where usid = ?",
					serviceInfo, usid);
			if (updated == 0) {
				updated = qRunner.update(connection,
						"insert into sqinfo (usid,serviceInfo) values (?,?)",
						usid, serviceInfo);
			}
		} catch (SQLException e) {
			logger.error("updateWxSqInfo fail", e);
		}
		closeConnection(connection);
		return updated;
	}

	/**
	 * @param usid
	 * @return 查询用户信息
	 */
	public static WxUserInfo queryWxUserInfo(String usid) {
		Connection connection = getConnection();
		QueryRunner qRunner = new QueryRunner();
		WxUserInfo wxUserInfo = null;
		try {
			wxUserInfo = qRunner.query(connection,
					"select * from wuinfo where usid = ? AND createTime > TIMESTAMPADD(MONTH,-1,NOW())",
					new BeanHandler<WxUserInfo>(WxUserInfo.class), usid);
		} catch (SQLException e) {
			logger.error("queryWxUserInfo fail", e);
		}
		closeConnection(connection);
		return wxUserInfo;
	}

}
