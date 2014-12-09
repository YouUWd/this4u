package com.jd.samples.bean;

import java.util.Date;

/**
 * @author YouU
 * @date 2014年12月9日
 * @desc 用户位置信息
 */
public class WxUserInfo {
	private int id;

	/**
	 * 用户id
	 */
	private String usid;
	/**
	 * 纬度
	 */
	private float lc_x;
	/**
	 * 经度
	 */
	private float lc_y;
	/**
	 * 地名
	 */
	private String address;

	/**
	 * 用户信息更新时间
	 */
	private Date createTime;

	public WxUserInfo() {
	}

	public WxUserInfo(int id, String usid, float lc_x, float lc_y,
			String address, Date createTime) {
		this.id = id;
		this.usid = usid;
		this.lc_x = lc_x;
		this.lc_y = lc_y;
		this.address = address;
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsid() {
		return usid;
	}

	public void setUsid(String usid) {
		this.usid = usid;
	}

	public float getLc_x() {
		return lc_x;
	}

	public void setLc_x(float lc_x) {
		this.lc_x = lc_x;
	}

	public float getLc_y() {
		return lc_y;
	}

	public void setLc_y(float lc_y) {
		this.lc_y = lc_y;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "WxUserInfo [id=" + id + ", usid=" + usid + ", lc_x=" + lc_x
				+ ", lc_y=" + lc_y + ", address=" + address + ", createTime="
				+ createTime + "]";
	}

}
