package com.jd.samples.bean;

import java.util.Date;

/**
 * @author YouU
 * @date 2014年12月9日
 * @desc 微信社区信息
 */
public class WxSqInfo {
	/**
	 * 用户id
	 */
	private String usid;

	/**
	 * 服务信息
	 */
	private String serviceInfo;
	/**
	 * 创建时间
	 */
	private Date createTime;

	public WxSqInfo() {
	}

	public WxSqInfo(String usid, String serviceInfo, Date createTime) {
		this.usid = usid;
		this.serviceInfo = serviceInfo;
		this.createTime = createTime;
	}

	public String getUsid() {
		return usid;
	}

	public void setUsid(String usid) {
		this.usid = usid;
	}

	public String getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(String serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "WxSqInfo [usid=" + usid + ", serviceInfo=" + serviceInfo
				+ ", createTime=" + createTime + "]";
	}

}
