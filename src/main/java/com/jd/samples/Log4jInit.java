package com.jd.samples;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3929524553768072652L;

	@Override
	public void init() throws ServletException {
		System.out.println("log4j init start...");
		String filePath = getInitParameter("log4j");
		PropertyConfigurator.configure(filePath);
		System.out.println("log4j init finish...");
	}
}
