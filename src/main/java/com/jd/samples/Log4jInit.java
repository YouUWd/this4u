package com.jd.samples;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class Log4jInit extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3929524553768072652L;

	@Override
	public void init() throws ServletException {
		System.out.println("log4j init start...");
		// String realPath = this.getServletContext().getRealPath("/");
		// System.out.println(realPath);
		// String filePath = getInitParameter("log4j");
		// PropertyConfigurator.configure(realPath + "/WEB-INF/" + filePath);
		try {
			Class.forName("org.apache.log4j.PropertyConfigurator");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("log4j init finish...");
	}
}
