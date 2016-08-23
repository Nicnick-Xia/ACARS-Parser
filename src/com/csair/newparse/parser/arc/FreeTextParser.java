package com.csair.newparse.parser.arc;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.csair.soc.disp.monitor.common.util.DateUtil;
import com.csair.soc.disp.monitor.common.util.RegExpUtil;
import com.csair.soc.disp.monitor.data.entity.AcarsVo;
import com.csair.soc.disp.monitor.data.util.AcarsUtil;

public class FreeTextParser implements AcarsParseI{
	
	private static Logger logger=LogManager.getLogger(FreeTextParser.class);

	public AcarsVo parseAcars(String msgContent, String regexType) {
		// TODO Auto-generated method stub
		AcarsVo acarsVo =new AcarsVo();
		acarsVo.setTelexMsg(msgContent);
		// 机尾号
		String tailNrRegex = "AN\\s+B-(\\d{4})";
		if (RegExpUtil.isMatched(tailNrRegex, msgContent)) {
			String tailNr = RegExpUtil.find(tailNrRegex, msgContent, 1);
			//logger.info("tailNr:" + tailNr);
			acarsVo.setTailNr("B"+tailNr);
		}else{
			logger.error("Parser goes wrong when parsing tailNr  -Message type: FreeText");
		}
		// 航班号
		String fltNrRegex = "FI\\s+([A-Z]{2})(\\d+)([A-Z]?)";
		if (RegExpUtil.isMatched(fltNrRegex, msgContent)) {
			String alnCd = RegExpUtil.find(fltNrRegex, msgContent, 1);
			String fltNr = RegExpUtil.find(fltNrRegex, msgContent, 2);
			String fltSuffix = RegExpUtil.find(fltNrRegex, msgContent, 3);
			if(null==fltSuffix){
				fltSuffix="";
			}
			if (fltNr.length() < 4) {
				fltNr = AcarsUtil.addZero(fltNr);
			}
			//logger.info("fltNr:" + alnCd + fltNr);
			acarsVo.setAlnCd(alnCd);
			acarsVo.setFltNr(fltNr);
			acarsVo.setFltSuffix(fltSuffix);
		}else{
			logger.error("Parser goes wrong when parsing fltNr  -Message type: FreeText");
		}	
		String dspDtRegex = "\\.(\\w{7})\\s+(\\d{6})";
		if (RegExpUtil.isMatched(dspDtRegex, msgContent)) {
			String lastArinc = RegExpUtil.find(dspDtRegex, msgContent, 1);
			String dspDtStr = RegExpUtil.find(dspDtRegex, msgContent, 2);
			acarsVo.setLastArinc(lastArinc);
			acarsVo.setDspDt(DateUtil.getAcarsDt(dspDtStr));
			acarsVo.setTelexDt(acarsVo.getDspDt());
		}else{
			logger.error("Parser goes wrong when parsing lastArinc  -Message type: FreeText");
			logger.error("Parser goes wrong when parsing dspDt  -Message type: FreeText");
		}
		return acarsVo;
	}

}
