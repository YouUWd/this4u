package com.jd.samples.bean;

import java.util.Date;

public class WxPcInfo {
	private int id;
	private String usid;
	private float lc_x;
	private float lc_y;
	private String address;
	private String destination;
	private Date createTime;

	public WxPcInfo() {

	}

	public WxPcInfo(int id, String usid, float lc_x, float lc_y,
			String address, String destination, Date createTime) {
		this.id = id;
		this.usid = usid;
		this.lc_x = lc_x;
		this.lc_y = lc_y;
		this.address = address;
		this.destination = destination;
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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "WxPcInfo [id=" + id + ", usid=" + usid + ", lc_x=" + lc_x
				+ ", lc_y=" + lc_y + ", address=" + address + ", destination="
				+ destination + ", createTime=" + createTime + "]";
	}

}
