package com.jd.samples.utils;

public class SimpleStringUtils {

	public static String joinArray(String separator, Object[] s) {
		if (s == null || s.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append(s[0]);
		for (int i = 1; i < s.length; ++i) {
			sb.append(separator);
			sb.append(s[i] == null ? "NULL" : s[i].toString());
		}
		return sb.toString();
	}

	public static String join(String separator, Object... c) {
		return joinArray(separator, c);
	}
}
