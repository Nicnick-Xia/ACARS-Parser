package com.csair.soc.disp.monitor.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: RegExpTool
 * @Description: Java正则表达式工具类
 * @author: liuzhiheng
 * @date: 2013-3-8
 * 
 */
public class RegExpUtil {

	//用“.”作为通配符进行解析
	public static String find(String expressionStr, String matcherStr) {
		Pattern pattern = Pattern.compile(expressionStr, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(matcherStr);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	// 分括号解析
	public static String find(String expressionStr, String matcherStr,
			int groupIndex) {
		Pattern pattern = Pattern.compile(expressionStr);
		Matcher matcher = pattern.matcher(matcherStr);
		if (matcher.find()) {
			return matcher.group(groupIndex);
		}

		return null;
	}

	public static List<String> findAll(String expressionStr, String matcherStr) {
		Pattern pattern = Pattern.compile(expressionStr);
		Matcher matcher = pattern.matcher(matcherStr);
		List<String> matchList = new ArrayList<String>();
		while (matcher.find()) {
			matchList.add(matcher.group());
		}
		return matchList;
	}

	public static boolean isMatched(String expressionStr, String matcherStr) {
		Pattern pattern = Pattern.compile(expressionStr);
		Matcher matcher = pattern.matcher(matcherStr);
		return matcher.find();
	}

	public static boolean isMatched(String expressionStr, String matcherStr,
			int patternFlag) {
		Pattern pattern = Pattern.compile(expressionStr, patternFlag);
		Matcher matcher = pattern.matcher(matcherStr);
		return matcher.find();
	}

	public static int start(String expressionStr, String matcherStr) {
		Pattern pattern = Pattern.compile(expressionStr);
		Matcher matcher = pattern.matcher(matcherStr);
		if (matcher.find())
			return matcher.start();
		else
			return -1;
	}

	public static int start(String expressionStr, String matcherStr,
			int nBeginPos) {
		if (expressionStr == null || matcherStr == null)
			return -1;
		Pattern pattern = Pattern.compile(expressionStr);
		Matcher matcher = pattern.matcher(matcherStr);
		if (matcher.find(nBeginPos))
			return matcher.start();
		else
			return -1;
	}
//
//	public static void main(String[] args) {
//		 System.out.println(RegExpUtil.start("\\d{4,6}", "899  kjghgh"));
//		// String m= RegExpTool.find("((\\w{2}\\s){1}.+[/|\\r|\\n])++",
//		// "-  PRG/FNCSN3740/DTZGSD,05O,96,030515,291A69\r");
//		// System.out.println("result===" +m);
//	}
}
