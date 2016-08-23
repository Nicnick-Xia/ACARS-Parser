package com.csair.newparse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.FormattableFlags;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.ho.yaml.Yaml;
import org.junit.Before;
import org.junit.Test;

import com.csair.newparse.config.PatternConfig;
import com.csair.newparse.pattern.beans.RegexBean;
import com.csair.newparse.pattern.beans.RegexBeanConfig;

public class ReadYaml {
	
	private static Logger logger = Logger.getLogger(ReadYaml.class);
	private static RegexBeanConfig config;
	
    public static void parseYamlToMap(String path) {
    	//读取文件
    	File[] files = new File(path).listFiles();
		if (files.length == 0){
			logger.error("No file exists!");
		}
		//解析YAML文件
    	int type;
    	String field;
    	String regex;
    	for (File file : files) {
    		if(!file.getName().endsWith(".yaml")){
    			continue;
    		}
    		List<RegexBean> regexBeanList = new ArrayList<>();
            try {
            	RegexBeanConfig config = Yaml.loadType(file,RegexBeanConfig.class);
                List<String> flyTypes = (List<String>)config.getAcarsFltTypes();
                List<HashMap<String, Object>> regexBeans = (List)config.getRegexBeans();
                for (HashMap<String, Object> hashMap : regexBeans) {
                	type = (Integer)hashMap.get("type");
                	regex = (String)hashMap.get("regex");
                	field = (String)hashMap.get("field");
    				RegexBean regexBean = new RegexBean(field, regex, type);
    				regexBeanList.add(regexBean);
                }
                for (String string : flyTypes) {
                	PatternConfig.patternMap.put(string, regexBeanList);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } 
		}
    }
}
