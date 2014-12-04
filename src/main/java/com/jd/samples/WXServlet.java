package com.jd.samples;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom2.JDOMException;

import com.jd.samples.utils.PushManage;
import com.jd.samples.utils.SignUtil;
import com.jd.samples.utils.SimpleStringUtils;

public class WXServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(WXServlet.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戮
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		logger.info(SimpleStringUtils.join("|", "DOGET", signature, timestamp,
				nonce, echostr));

		PrintWriter out = response.getWriter();
		// 通过检验 signature 对请求进行校验，若校验成功则原样返回 echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}

		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<?, ?> parameterMap = req.getParameterMap();
		logger.info(SimpleStringUtils.join("|", "DOPOST", parameterMap));
		InputStream is = req.getInputStream();

		try {
			logger.info(SimpleStringUtils.join("|", "DOPOST",
					PushManage.pushManageXml(is)));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.doPost(req, resp);
	}
}
