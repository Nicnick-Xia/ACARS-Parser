package com.csair.newparse.parser.arc;

import java.util.Set;

import org.apache.log4j.Logger;

import com.csair.newparse.config.TailNrConfig;
import com.csair.soc.disp.monitor.common.util.RegExpUtil;

public class AcrParser {

	private static Logger logger = Logger.getLogger("Acars");
	private static final String tailNrRegex = "AN\\s+B-(\\d{4})";
	/*
	 * 用于解析机型
	 * 
	 */
	public static String acTypeParse(String msgContent){
		String arc = null;
		Set<String> keyset = TailNrConfig.tailNrMap.keySet();	
		if (RegExpUtil.isMatched(tailNrRegex, msgContent)) {
			String tailNr = RegExpUtil.find(tailNrRegex, msgContent, 1);
			if(tailNr==null||tailNr.equals(" ")){
				logger.error("Error when parsing tailNr");
				return null;
			}
			for (String key : keyset) {
				if(key.contains(tailNr)){
					arc = TailNrConfig.tailNrMap.get(key);
				}
			}
		}else{
			logger.error("Error when parsing tailNr");
		}
		return arc;
	}

	public static String getTailNr(String msgContent){
		String tailNrRegex = "AN\\s+B-(\\d{4})";
		if (RegExpUtil.isMatched(tailNrRegex, msgContent)) {
			return RegExpUtil.find(tailNrRegex, msgContent, 1);
		}
		return null;
	}

}
