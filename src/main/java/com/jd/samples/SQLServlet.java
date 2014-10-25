package com.jd.samples;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

public class SQLServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		response.setStatus(200);
		PrintWriter writer = response.getWriter();

		String result;
		DbUtils.loadDriver("com.mysql.jdbc.Driver");
		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://10.0.16.16:4066/his4u_mysql_40wcj8vu", "ZlANddGd", "bx51lZC0AeRD");
			System.out.println(connection);
			QueryRunner qRunner = new QueryRunner();
			List<Map<String, Object>> query = qRunner.query(connection,
					"select * from myuser", new MapListHandler());
			result = "查询结果" + query;
			connection.close();
		} catch (SQLException e) {
			result = "异常";
			e.printStackTrace();
		}

		writer.println("Hello " + result);
		writer.close();
	}
}
