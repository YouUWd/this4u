package com.jd.samples;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.setStatus(200);
		PrintWriter writer = response.getWriter();
		writer.println("Hello from JAE!我是中国人！");
		writer.close();
	}
}
