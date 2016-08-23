package com.csair.soc.disp.monitor.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.csair.soc.disp.monitor.common.util.gis.Point;

public class ParserLonLatUtil {

	private static final String W = "W";
	private static final String _4F = "%.4f";
	private static final String S = "S";

	/**
	 * 转换为Lido数据库的经纬度
	 * 
	 * @param lonOrLat
	 * @return
	 */
	public static BigDecimal toLidoLatLon(BigDecimal lonOrLat) {
		double b = 3600 * 100;
		double aa = lonOrLat.doubleValue();

		return BigDecimal.valueOf(aa * b);
	}

	/**
	 * Lido数据库的经纬度解析
	 * 
	 * @param lonOrLat
	 * @return
	 */
	public static BigDecimal dealLidoLatLon1(BigDecimal lonOrLat) {

		double b = 3600 * 100;
		double aa = lonOrLat.doubleValue();

		return new BigDecimal(aa / b);
	}

	/**
	 * Lido数据库的经纬度解析
	 * 
	 * @param lonOrLat
	 * @return
	 */
	public static String dealLidoLatLon(BigDecimal lonOrLat) {
		double b = 3600 * 100;
		double aa = lonOrLat.doubleValue();
		return String.valueOf(aa / b);
	}
	/**
     * Lido数据库的经纬度解析
     * 
     * @param lonOrLat
     * @return
     */
    public static double dealLidoLatLonValue(BigDecimal lonOrLat) {
        double b = 3600 * 100;
        double aa = lonOrLat.doubleValue();
        return aa / b;
    }
	/**
	 * 解析经度 类型1
	 * 
	 * @param longtitude
	 * @author Zhongyu
	 * @对于不合适的格式返回空值
	 * @example 纬度:N43482603,经度:E084072403
	 */
	public static String parseLon1(String lon) {

		if (!lon.contains("E") && !lon.contains("W")) {
			return "";
		}
		String lonType = lon.substring(0, 1);
		double lonDu = Double.parseDouble(lon.substring(1, 4));
		double lonMin = Double.parseDouble(lon.substring(4, 6));
		double lonSec = Double.parseDouble(lon.substring(6, 8) + "."
				+ lon.substring(8));
		double curLon = lonDu + lonMin / 60 + lonSec / 3600;
		if (W.equalsIgnoreCase(lonType)) {
			curLon = -curLon;
		}

		return String.format(_4F, curLon);
	}

	/**
	 * 处理ACARS经纬度
	 * 
	 * @param latOrLon
	 * @return
	 * @example 纬度:N43.48,经度:E36.51
	 */
	public static String parseAcarsLatLon(String latOrLon) {
		if (!latOrLon.contains("N") && !latOrLon.contains("S")
				&& !latOrLon.contains("W") && !latOrLon.contains("E")) {
			return "";
		}
		String latType = latOrLon.substring(0, 1);
		double latLonValue = Double.parseDouble(latOrLon.substring(1));
		if (S.equalsIgnoreCase(latType) || W.equalsIgnoreCase(latType)) {
			latLonValue = -latLonValue;
		}
		return String.format(_4F, latLonValue);
	}

	/**
	 * 解析纬度 类型1
	 * 
	 * @param latitude
	 * @author Zhongyu
	 * @对于不合适的格式返回空值
	 * @example 纬度:N43482603,经度:E084072403
	 */
	public static String parseLat1(String lat) {
		if (!lat.contains("N") && !lat.contains("S")) {
			return "";
		}
		String latType = lat.substring(0, 1);
		double latDu = Double.parseDouble(lat.substring(1, 3));
		double latMin = Double.parseDouble(lat.substring(3, 5));
		double latSec = Double.parseDouble(lat.substring(5, 7) + "."
				+ lat.substring(7));
		double curLat = latDu + latMin / 60 + latSec / 3600;
		if (S.equalsIgnoreCase(latType)) {
			curLat = -curLat;
		}

		return String.format(_4F, curLat);
	}

	/**
	 * 解析度 类型2
	 * 
	 * @param longtitude
	 * @author Zhongyu
	 * @对于不合适的格式返回空值
	 * @example"N43 54.4"
	 */
	public static String parsePlanLat(String lat) {
		if (lat == null || (!lat.contains("N") && !lat.contains("S"))) {
			return "";
		}
		String latType = lat.substring(0, 1);
		double latDu = Double.parseDouble(lat.substring(1, 3));
		double latMin = Double.parseDouble(lat.substring(4, 8));
		double curLat = latDu + latMin / 60;
		if ("S".equalsIgnoreCase(latType)) {
			curLat = -curLat;
		}

		return String.format(_4F, curLat);
	}

	/**
	 * 解析经度 类型2
	 * 
	 * @param longtitude
	 * @author Zhongyu
	 * @对于不合适的格式返回空值
	 * @example"N43 54.4"
	 */

	public static String parsePlanLon(String lon) {
		if (lon == null || (!lon.contains("W") && !lon.contains("E"))) {
			return "";
		}
		String lonType = lon.substring(0, 1);
		double lonDu = Double.parseDouble(lon.substring(1, 4));
		double lonMin = Double.parseDouble(lon.substring(5, 9));
		double curLon = lonDu + lonMin / 60;
		if ("W".equalsIgnoreCase(lonType)) {
			curLon = -curLon;
		}
		return String.format(_4F, curLon);
	}

	/**
	 * @description 将矢量点转换为经纬度
	 * @param point
	 * @return
	 */

	public static String formatePoint(Point point, String seperator) {
		DecimalFormat df = new java.text.DecimalFormat("0.00");
		if (point == null) {
			return null;
		}
		String lat = null;
		String lon = null;
		if (point.getLat() < 0) {
			lat = "S";
		} else {
			lat = "N";
		}
		if (point.getLon() < 0) {
			lon = "W";
		} else {
			lon = "E";
		}
		Double latitude = Math.abs(point.getLat());
		Double longtitude = Math.abs(point.getLon());

		String latValue[] = String.valueOf(latitude).split("\\.");// 小数点需要转义
		String lonValue[] = String.valueOf(longtitude).split("\\.");

		Double latMinute = Double.valueOf("0." + latValue[1]) * 60;
		Double lonMinute = Double.valueOf("0." + lonValue[1]) * 60;
		lat = lat + latValue[0] + " " + df.format(latMinute);
		lon = lon + lonValue[0] + " " + df.format(lonMinute);
		return lat + seperator + lon;

	}
}
