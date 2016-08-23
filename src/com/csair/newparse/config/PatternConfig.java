package com.csair.newparse.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csair.newparse.ReadYaml;
import com.csair.newparse.pattern.beans.RegexBean;

public class PatternConfig {
	
	public static final String filePath = "res/patternYaml";
	
	public static Map<String,List<RegexBean>> patternMap = new HashMap<String,List<RegexBean>>();
	
	static{
		ReadYaml.parseYamlToMap(filePath);
	}
}
