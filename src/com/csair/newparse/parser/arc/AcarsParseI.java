package com.csair.newparse.parser.arc;

import com.csair.soc.disp.monitor.data.entity.AcarsVo;

public interface AcarsParseI {
	
	public AcarsVo parseAcars(String msgContent, String regexType);
	
}
