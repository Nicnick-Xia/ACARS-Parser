package com.csair.soc.disp.monitor.data.util;

public class AcarsUtil {

	//想前补充0到共4位
	public static String addZero(String sourceStr) {
		sourceStr = "0" + sourceStr;
		if (sourceStr.length() == 4) {
			return sourceStr;
		} else {
			return addZero(sourceStr);
		}
	}

	// 转换为16进制
	public static String toHexString(String msg) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < msg.length(); i++) {
			int ch = (int) msg.charAt(i);
			StringBuffer temp = new StringBuffer(Integer.toHexString(ch));
			if (temp.length() == 1) {
				temp.insert(0, "0");
			}
			sb.append(temp);
			sb.append(" ");
		}
		return sb.toString();
	}
}
