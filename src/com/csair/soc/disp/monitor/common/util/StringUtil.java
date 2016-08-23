package com.csair.soc.disp.monitor.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

//import com.csair.soc.disp.monitor.common.entity.DynamicFlt;
//import com.csair.soc.disp.monitor.common.model.Adsb;

public abstract class StringUtil {
	private static Logger LOGGER = LogManager.getLogger(StringUtil.class);

	/**
	 * 根据adsb Vo生成航班号
	 * 
	 * @param fiv
	 * @return
	 */
/*	public static String getAlnFltNumKey(Adsb fiv) {
		String alnCd = fiv.getAlncd();
		String fltNum = getFltNum(fiv.getFltnr());
		String opSuffix = fiv.getOpsuffix();
		StringBuilder sb = new StringBuilder();
		if (StringUtil.isEmpty(alnCd) || StringUtil.isEmpty(fltNum)) {
			return null;
		}
		sb.append(alnCd.trim()).append(fltNum.trim());
		if (StringUtil.isNotEmpty(opSuffix)) {
			sb.append(opSuffix.trim());
		}
		return sb.toString();
	}*/

	/**
	 * 输入327 返回0327 输入327A 返回0327A 输入3270 返回3270
	 * 
	 * @param fltNum
	 * @return
	 */
	public static String getFltNum(String fltNum) {
		if (isEmpty(fltNum)) {
			return "";
		}
		String opSuffix = fltNum;
		opSuffix = opSuffix.substring(opSuffix.length() - 1, opSuffix.length());
		if (StringUtil.isInteger(opSuffix)) {
			while (fltNum.length() < 4) {
				fltNum = "0" + fltNum;
			}
		} else {
			String str = fltNum.substring(0, fltNum.length() - 1);
			while (str.length() < 4) {
				str = "0" + str;
			}
			fltNum = str + opSuffix;
		}
		return fltNum;
	}

	/**
	 * 那空格和换行符删掉
	 * 
	 * @param str
	 * @return
	 */
	public static String deleteSpaceAndNewLine(String str) {
		return str.replaceAll(" ", "").replaceAll("\r\n", "");
	}

	/**
	 * 根据航班动态Vo生成航班号
	 * 
	 * @param fiv
	 * @return
	 */
/*	public static String getAlnFltNumKey(DynamicFlt fiv) {
		String alnCd = fiv.getAlnCd();
		String fltNum = fiv.getFltNr();
		String opSuffix = fiv.getOpSuffix();
		StringBuilder sb = new StringBuilder();
		sb.append(alnCd.trim()).append(fltNum.trim());
		if (StringUtil.isNotEmpty(opSuffix)) {
			sb.append(opSuffix.trim());
		}
		return sb.toString();
	}*/

	/**
	 * 根据航班动态Vo生成航班号
	 * 
	 * @param fiv
	 * @return
	 */
/*	public static String getFltNumKey(DynamicFlt fiv) {
		String fltNum = fiv.getFltNr();
		String opSuffix = fiv.getOpSuffix();
		StringBuilder sb = new StringBuilder();
		sb.append(fltNum.trim());
		if (StringUtil.isNotEmpty(opSuffix)) {
			sb.append(opSuffix.trim());
		}
		return sb.toString();
	}
*/
	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否非空
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String getString(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	/**
	 * 对查询的字段进行模糊处理和统一大写
	 * 
	 * @param dbStr
	 * @return
	 */
	public static String fuzzyString(String dbStr) {
		return "%" + dbStr.toUpperCase() + "%";
	}

	/**
	 * 判断是否是数字(含小数、负数)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (str == null || "".equals(str)) {
			return false;
		}
		Boolean strResult = str
				.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
		if (strResult == true) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否是正整数 modifiedby zhongyu
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		boolean flag = true;
		if (isEmpty(str)) {
			flag = false;
		} else {
			for (int i = str.length(); --i >= 0;) {
				if (!Character.isDigit(str.charAt(i))) {
					flag = false;
				}
			}
		}
		return flag;
	}

	/**
	 * 字符串编码转换
	 * 
	 * @param sourceStr
	 * @return
	 * @throws Exception
	 */
	public static String stringISOToGBK(String sourceStr) {
		if (isEmpty(sourceStr)) {
			return "";
		}
		String rs = "";
		try {
			rs = new String(sourceStr.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("转码错误:" + sourceStr, e);
		}
		return rs;
	}

	/**
	 * 字符串编码转换
	 * 
	 * @param sourceStr
	 * @return
	 * @throws Exception
	 */
	public static String stringGBKToISO(String sourceStr) {
		if (isEmpty(sourceStr)) {
			return "";
		}
		String rs = "";
		try {
			rs = new String(sourceStr.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("转码错误:" + sourceStr, e);
		}
		return rs;
	}

	/**
	 * 字符串的压缩
	 * 
	 * @param str
	 *            待压缩的字符串
	 * @return 返回压缩后的字符串
	 * @throws IOException
	 */
	public static byte[] compressZip(byte[] str) throws IOException {
		if (null == str || str.length <= 0) {
			return null;
		}
		// 创建一个新的 byte 数组输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 使用默认缓冲区大小创建新的输出流
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		// 将 b.length 个字节写入此输出流
		gzip.write(str);
		gzip.close();
		// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
		return out.toByteArray();
	}

	/**
	 * 字符串的解压
	 * 
	 * @param str
	 *            对字符串解压
	 * @return 返回解压缩后的字符串
	 * @throws IOException
	 */
	public static byte[] decompressZip(byte[] str) throws IOException {
		if (null == str || str.length <= 0) {
			return str;
		}
		// 创建一个新的 byte 数组输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
		ByteArrayInputStream in = new ByteArrayInputStream(str);
		// 使用默认缓冲区大小创建新的输入流
		GZIPInputStream gzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n = 0;
		while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
			// 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
			out.write(buffer, 0, n);
		}
		// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
		return out.toByteArray();
	}

	/**
	 * 压缩
	 * 
	 * @param data
	 *            待压缩数据
	 * @return byte[] 压缩后的数据
	 */
	public static byte[] compress(byte[] data) {
		byte[] output = new byte[0];

		Deflater compresser = new Deflater();

		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		compresser.end();
		return output;
	}

	/**
	 * 解压缩
	 * 
	 * @param data
	 *            待压缩的数据
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[4096];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}

	/**
	 * 把LF换成CRLF
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceLfToCrLf(String str) {
		if (str.contains("\r\n")) {
			str = str.replaceAll("\r\n", "\n");
		}
		if (str.contains("\n")) {
			str = str.replaceAll("\n", "\r\n");
		}
		return str;
	}

	/**
	 * trim
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		if (str == null) {
			return null;
		} else {
			return str.trim();
		}
	}

	/**
	 *
	 * @param str
	 * @param seperater
	 * @return
	 */
	public static List<String> getStringList(String str, String seperater) {
		if (str == null || seperater == null) {
			return null;
		}
		String[] stringArray = str.split(seperater);
		return Arrays.asList(stringArray);
	}

}
