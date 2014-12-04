package com.jd.samples;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jd.samples.utils.SignUtil;

public class WXServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ΢�ż���ǩ��
        String signature = request.getParameter("signature");
        // ʱ��¾
        String timestamp = request.getParameter("timestamp");
        // �����
        String nonce = request.getParameter("nonce");
        // ����ַ���
        String echostr = request.getParameter("echostr"); 
          
        PrintWriter out = response.getWriter();
        // ͨ������ signature ���������У�飬��У��ɹ���ԭ������ echostr����ʾ����ɹ����������ʧ��
       if(SignUtil.checkSignature(signature, timestamp, nonce)){
           out.print(echostr);
       }
  
       out.close();
	}
}
