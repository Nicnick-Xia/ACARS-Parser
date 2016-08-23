package com.csair.newparse;

import java.io.File;
import java.util.List;

import org.apache.log4j.BasicConfigurator;

import com.csair.soc.disp.monitor.data.constants.EsbConst;
import com.csair.soc.disp.monitor.data.entity.AcarsVo;

public class Test {

	public static final String PATH1 = "ACARS-Raw-Data/B737NG(CMU)/OOOI(B-5596)/M11-OUT";
	public static final String PATH2 = "ACARS-Raw-Data/A32X-ACARS(ATSU)/OOOI(B-6308)/M12-OFF";
	public static final String PATH3 = "ACARS-Raw-Data/B787-8 ACARS/POS(B-2737)/A83-Downlink";
	public static final String PATH4 = "ACARS-Raw-Data/A330-ACARS(ATSU)/POS(B-6528)/M17-Downlink";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("file.encoding", "utf-8");
		BasicConfigurator.configure();
		AcarsFileClient afc = new AcarsFileClient();
		
		File[] acarsFiles = afc.readAcarsFile(EsbConst.ACARS_ALL_PATH);
		List<AcarsVo> newAcarsVoList = afc.acarsParse(acarsFiles);
	}

}
