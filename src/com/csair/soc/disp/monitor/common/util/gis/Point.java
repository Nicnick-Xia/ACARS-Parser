/**
 * @author zhy
 * 20160105
 * 点的定义：一个点，座标为lat和lon
 */
package com.csair.soc.disp.monitor.common.util.gis;

public class Point implements Cloneable {
	
	private double lat; // 纬度

	private double lon; // 经度

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public Object clone() {
		Point o = null;
		try {
			o = (Point) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

}
