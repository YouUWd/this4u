package com.jd.samples.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class PushManage {

	public static String pushManageXml(InputStream is) throws JDOMException {

		String returnStr = ""; // 反回Servlet字符串
		String toName = ""; // 开发者微信号
		String fromName = ""; // 发送方帐号（一个OpenID）
		String type = ""; // 请求类型

		String content = ""; // 消息内容(接收)
		String picUrl = "";// 图片链接

		String lc_x = "";
		String lc_y = "";
		String address = "";

		try {

			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(is);
			// 获得文件的根元素
			Element root = doc.getRootElement();

			// 获得根元素的第一级子节点
			List<?> list = root.getChildren();
			for (int j = 0; j < list.size(); j++) {
				// 获得结点
				Element element = (Element) list.get(j);

				if (element.getName().equals("ToUserName")) {
					toName = element.getValue().trim();
				} else if (element.getName().equals("FromUserName")) {
					fromName = element.getValue().trim();
				} else if (element.getName().equals("MsgType")) {
					type = element.getValue().trim();
				} else if (element.getName().equals("Content")) {
					content = element.getValue().trim();
				} else if (element.getName().equals("PicUrl")) {
					picUrl = element.getValue().trim();
				} else if (element.getName().equals("Location_X")) {
					lc_x = element.getValue().trim();
				} else if (element.getName().equals("Location_Y")) {
					lc_y = element.getValue().trim();
				} else if (element.getName().equals("Label")) {
					address = element.getValue().trim();
				}
			}
		} catch (IOException e) {
			// 异常
		}

		if (type.equals("text")) { // 此为 文本信息
			returnStr = getBackXMLTypeText(toName, fromName,
					"平台建设中，你输入了(文本信息):" + content);
		} else if ("image".equals(type)) {
			returnStr = getBackXMLTypeImg(toName, fromName, "图片", content,
					picUrl);
		} else if ("voice".equals(type)) {
			returnStr = getBackXMLTypeText(toName, fromName,
					"平台建设中，你输入了(声音信息):" + content);
		} else if ("video".equals(type)) {
			returnStr = getBackXMLTypeText(toName, fromName,
					"平台建设中，你输入了(视频信息):" + content);
		} else if ("location".equals(type)) {
			returnStr = getBackXMLTypeText(toName, fromName,
					"平台建设中，你输入了(位置信息):X=" + lc_x + ",Y=" + lc_y + " " + address);
		} else if ("link".equals(type)) {
			returnStr = getBackXMLTypeText(toName, fromName,
					"平台建设中，你输入了(链接信息):" + content);
		} else {
			returnStr = getBackXMLTypeText(toName, fromName,
					"平台建设中，你输入了(其他信息):" + content);
		}

		return returnStr;
	}

	/**
	 * 编译文本信息
	 * 
	 * @author xiaowu
	 * @since 2013-9-27
	 * @param toName
	 * @param FromName
	 * @param content
	 * @return
	 */
	public static String getBackXMLTypeText(String toName, String fromName,
			String content) {

		String returnStr = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = format.format(new Date());

		Element rootXML = new Element("xml");

		rootXML.addContent(new Element("ToUserName").setText(fromName));
		rootXML.addContent(new Element("FromUserName").setText(toName));
		rootXML.addContent(new Element("CreateTime").setText(times));
		rootXML.addContent(new Element("MsgType").setText("text"));
		rootXML.addContent(new Element("Content").setText(content));

		Document doc = new Document(rootXML);

		XMLOutputter XMLOut = new XMLOutputter();
		returnStr = XMLOut.outputString(doc);

		return returnStr;
	}

	/**
	 * 编译图片信息(单图模式)
	 * 
	 * @author xiaowu
	 * @since 2013-9-27
	 * @param toName
	 * @param FromName
	 * @param content
	 * @return
	 */
	public static String getBackXMLTypeImg(String toName, String fromName,
			String title, String content, String url, String pUrl) {

		String returnStr = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = format.format(new Date());

		Element rootXML = new Element("xml");

		rootXML.addContent(new Element("ToUserName").setText(fromName));
		rootXML.addContent(new Element("FromUserName").setText(toName));
		rootXML.addContent(new Element("CreateTime").setText(times));
		rootXML.addContent(new Element("MsgType").setText("news"));
		rootXML.addContent(new Element("ArticleCount").setText("1"));

		Element fXML = new Element("Articles");
		Element mXML = null;

		mXML = new Element("item");
		mXML.addContent(new Element("Title").setText(title));
		mXML.addContent(new Element("Description").setText(content));
		mXML.addContent(new Element("PicUrl").setText(pUrl));
		mXML.addContent(new Element("Url").setText(url));
		fXML.addContent(mXML);
		rootXML.addContent(fXML);

		Document doc = new Document(rootXML);

		XMLOutputter XMLOut = new XMLOutputter();
		returnStr = XMLOut.outputString(doc);

		return returnStr;
	}

	/**
	 * 编译图片信息(无图模式)
	 * 
	 * @author xiaowu
	 * @since 2013-9-27
	 * @param toName
	 * @param FromName
	 * @param content
	 * @return
	 */
	public static String getBackXMLTypeImg(String toName, String fromName,
			String title, String content, String url) {

		String returnStr = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = format.format(new Date());

		Element rootXML = new Element("xml");

		rootXML.addContent(new Element("ToUserName").setText(fromName));
		rootXML.addContent(new Element("FromUserName").setText(toName));
		rootXML.addContent(new Element("CreateTime").setText(times));
		rootXML.addContent(new Element("MsgType").setText("news"));
		rootXML.addContent(new Element("ArticleCount").setText("1"));

		Element fXML = new Element("Articles");
		Element mXML = null;

		mXML = new Element("item");
		mXML.addContent(new Element("Title").setText(title));
		mXML.addContent(new Element("Description").setText(content));
		mXML.addContent(new Element("PicUrl").setText(""));// 无图体现在这里
		mXML.addContent(new Element("Url").setText(url));
		fXML.addContent(mXML);
		rootXML.addContent(fXML);

		Document doc = new Document(rootXML);

		XMLOutputter XMLOut = new XMLOutputter();
		returnStr = XMLOut.outputString(doc);

		return returnStr;
	}

	/**
	 * 编译音乐信息
	 * 
	 * @author xiaowu
	 * @since 2013-9-27
	 * @param toName
	 * @param FromName
	 * @param content
	 * @return
	 */
	public String getBackXMLTypeMusic(String toName, String fromName,
			String content) {

		String returnStr = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = format.format(new Date());

		Element rootXML = new Element("xml");

		rootXML.addContent(new Element("ToUserName").setText(fromName));
		rootXML.addContent(new Element("FromUserName").setText(toName));
		rootXML.addContent(new Element("CreateTime").setText(times));
		rootXML.addContent(new Element("MsgType").setText("music"));

		Element mXML = new Element("Music");

		mXML.addContent(new Element("Title").setText("音乐"));
		mXML.addContent(new Element("Description").setText("音乐让人心情舒畅！"));
		mXML.addContent(new Element("MusicUrl").setText(content));
		mXML.addContent(new Element("HQMusicUrl").setText(content));

		rootXML.addContent(mXML);

		Document doc = new Document(rootXML);

		XMLOutputter XMLOut = new XMLOutputter();
		returnStr = XMLOut.outputString(doc);

		return returnStr;
	}

}