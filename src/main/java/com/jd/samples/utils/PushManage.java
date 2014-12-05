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

import com.jd.samples.bean.WxPcInfo;

public class PushManage {

	private static String SEPARATOR = System.getProperty("line.separator");

	public static String pushManageXml(InputStream is) throws JDOMException {

		String returnStr = ""; // 反回Servlet字符串
		String toName = ""; // 开发者微信号
		String fromName = ""; // 发送方帐号（一个OpenID）
		String type = ""; // 请求类型

		String content = ""; // 消息内容(接收)
		String picUrl = "";// 图片链接

		// Location_X 地理位置维度
		// Location_Y 地理位置经度
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
			// 更新目的地信息
			if (content.startsWith("GO/")) {
				String dest = content.substring(3).trim();
				int updateDest = DBManager.updateDest(dest, fromName);
				System.out.println("=======" + updateDest);
				if (updateDest > 0) {
					returnStr = PushManage.getBackXMLTypeText(toName, fromName,
							"您发布的到【" + dest + "】的拼车信息已经发布成功，有效期30分钟~");
				} else {
					returnStr = PushManage.getBackXMLTypeText(toName, fromName,
							"请发送您当前的位置信息~");
				}
			} else if (content.startsWith("TO/")) {
				WxPcInfo location = DBManager.queryLocation(fromName);
				if (location == null) {
					returnStr = PushManage.getBackXMLTypeText(toName, fromName,
							"您还没告诉我您的位置信息呢，请先发送位置~");
				} else {
					List<WxPcInfo> infos = DBManager.queryInfos(
							location.getLc_x(), location.getLc_y());
					StringBuilder builder = new StringBuilder("欢迎使用即刻拼车平台:")
							.append(SEPARATOR);
					if (infos == null) {
						builder.append("您周围1km还没有人发布拼车信息呢，快去邀请你认识的小伙伴吧~")
								.append(SEPARATOR);
					} else {
						String dest = content.substring(3);
						StringBuilder infosStr = new StringBuilder();
						StringBuilder infosFitStr = new StringBuilder();
						int index = 0;
						int indexFit = 0;
						for (WxPcInfo wxPcInfo : infos) {
							if (wxPcInfo.getDestination().contains(dest)) {
								indexFit++;
								infosFitStr.append(indexFit).append(":")
										.append(wxPcInfo.getAddress())
										.append("--->")
										.append(wxPcInfo.getDestination());
							}
							index++;
							infosStr.append(index).append(":")
									.append(wxPcInfo.getAddress())
									.append("--->")
									.append(wxPcInfo.getDestination());
						}
						if (infosFitStr.length() == 0) {
							builder.append(
									"没有找到你需要的拼车信息,您周围的人发布了以下拼车信息，看看是否符合您的要求~")
									.append(SEPARATOR);
							builder.append(infosStr).append(SEPARATOR);
						} else {
							builder.append("为您找到以下拼车信息，看看是否符合你的要求~").append(
									SEPARATOR);
							builder.append(infosFitStr).append(SEPARATOR);
						}
						returnStr = PushManage.getBackXMLTypeText(toName,
								fromName, builder.toString());
					}
				}
			} else {
				StringBuilder builder = new StringBuilder("欢迎使用即刻拼车平台:")
						.append(SEPARATOR);
				builder.append("首先请发送您的位置信息").append(SEPARATOR);
				builder.append("1：信息发布者发送GO/目的地/联系方式").append(SEPARATOR);
				builder.append("2：信息查询者发送TO/目的地").append(SEPARATOR);
				builder.append("如：\"GO/亚运村-某先生/13812345678\"")
						.append(SEPARATOR);
				returnStr = PushManage.getBackXMLTypeText(toName, fromName,
						builder.toString());
			}
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
			returnStr = getBackXMLTypeText(toName, fromName, "位置信息更新成功:您当前位置-"
					+ address);
			DBManager.updateLocation(fromName, Float.parseFloat(lc_x),
					Float.parseFloat(lc_y), address);// 更新用户地理位置信息
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