package com.csair.newparse.parser.arc;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.csair.newparse.config.PatternConfig;
import com.csair.newparse.pattern.beans.RegexBean;
import com.csair.soc.disp.monitor.common.util.DateUtil;
import com.csair.soc.disp.monitor.common.util.RegExpUtil;
import com.csair.soc.disp.monitor.data.entity.AcarsVo;
import com.csair.soc.disp.monitor.data.util.AcarsUtil;

public class AcarsPaser implements AcarsParseI{

	private static Logger logger = Logger.getLogger(AcarsPaser.class);

	public AcarsVo parseAcars(String msgContent, String regexType) {
		// TODO Auto-generated method stub
		String regex = null;
		AcarsVo acarsVo = new AcarsVo();

		//从patternMap中获取改机型对应的正则表达式List
		List<RegexBean> patterns = PatternConfig.patternMap.get(regexType); 
		if(patterns == null){
			logger.error("Regextype not found, please check PatterConfig.java -regexType: "+regexType);
			return null;
		}
		try {
			Class<?> ac = Class.forName("com.csair.soc.disp.monitor.data.entity.AcarsVo");
			for (RegexBean pattern : patterns) {
				regex = pattern.getRegex();
				if(RegExpUtil.isMatched(regex, msgContent)){
					String result = RegExpUtil.find(regex, msgContent,pattern.getType());
					Field field = ac.getDeclaredField(pattern.getField());
					field.setAccessible(true);
					field.set(acarsVo,result);
					//System.out.println(pattern.getField()+": "+result);
				}else{
					System.out.println("Regex:"+regex);
					logger.error("Parser goes wrong when parsing "+pattern.getField()+"  -Message type: "+regexType);
					System.err.println("Parser goes wrong when parsing "+pattern.getField()+"  -Message type: "+regexType);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//System.err.println("Entity AcarsVo not found");
			logger.error("Entity AcarsVo not found");
			e.printStackTrace();
		} catch (ReflectiveOperationException e) {
			// TODO Auto-generated catch block
			//System.err.println("Reflective operation error");
			logger.error("Reflective operation error");
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			//System.err.println("Data not Accessible");
			logger.error("Data not Accessible");
			e.printStackTrace();
		}
		//后续处理相应的数据
		acarsVo = postProcessing(acarsVo,msgContent,regexType);		
		return acarsVo;
	}

	/*
	 * 后续处理函数
	 * 如果有特殊报文字段需要另行处理，则在此处添加代码
	 * @param:acarsVo,msgContent,regexType
	 * @return:acarsVo
	 */
	public AcarsVo postProcessing(AcarsVo acarsVo,String msgContent,String regexType){
		//后续处理机尾号
		if(acarsVo.getTailNr()!=null){
			acarsVo.setTailNr("B"+acarsVo.getTailNr().replace(" ", ""));
		}
		//后续处理航班号
		String rawFltNr = acarsVo.getFltNr();
		if(rawFltNr!=null){
			String fltNrRegex = "([A-Z]{2})(\\d+)([A-Z]?)";
			String alnCd = RegExpUtil.find(fltNrRegex, rawFltNr, 1);
			String fltNr = RegExpUtil.find(fltNrRegex, rawFltNr, 2);
			String fltSuffix = RegExpUtil.find(fltNrRegex, rawFltNr, 3);
			if (fltNr.length() < 4) {
				fltNr = AcarsUtil.addZero(fltNr);
			}
			if(null==fltSuffix){
				fltSuffix="";
			}
			acarsVo.setAlnCd(alnCd);
			acarsVo.setFltNr(fltNr);
			acarsVo.setFltSuffix(fltSuffix);
		}
		//后续处理报文发送时间
		String rawDspDt = acarsVo.getDspDt();
		String rawTelexDt = acarsVo.getTelexDt();
		if(rawTelexDt!=null&&rawDspDt!=null&&rawTelexDt!="----"){
			acarsVo.setDspDt(DateUtil.getAcarsDt(rawDspDt));
			if(regexType.contains("777")||regexType.contains("787")){
				acarsVo.setTelexDt(DateUtil.getAcarsDt(rawTelexDt));
			}else{
				acarsVo.setTelexDt(DateUtil.getAcarsTime(rawTelexDt));
			}
		}else{
			acarsVo.setTelexDt(acarsVo.getDspDt());
		}
		//处理OOOI报文的特殊情况
		if(regexType.contains("OOOI")){
			if(acarsVo.getCurLat()!=null&&acarsVo.getCurLon()!=null){
				acarsVo.setCurLat(acarsVo.getCurLat().replace(" ", ""));
				acarsVo.setCurLon(acarsVo.getCurLon().replace(" ", ""));
			}
		}
		//处理POS报文的特殊情况
		if(regexType.contains("POS")){
			//调整预计到达时间
			if(acarsVo.getEstTime()!=null&&acarsVo.getEstTime()!="----"){
				acarsVo.setEstTime(DateUtil.getAcarsTime(acarsVo.getEstTime()));
			}
			//B777特殊情况，空速单位转换
			if(regexType.contains("777")){
				if(acarsVo.getCurCas()!=null){
					Double mach = Double.valueOf(acarsVo.getCurCas());
					Double airSpeed = (mach/100)*340.3/0.5144; //马赫转换成节
					acarsVo.setCurCas(airSpeed.toString());
				}
			}
			//B787特殊情况
			if(regexType.contains("787")){
				String latStr = acarsVo.getCurLat();
				String lonStr = acarsVo.getCurLon();
				if(latStr!=null&&lonStr!=null){
					String[] latLon = redoLonLat(latStr, lonStr);
					acarsVo.setCurLat(latLon[0]);
					acarsVo.setCurLon(latLon[1]);
				}
				if(acarsVo.getTelexDt()!=null){
					acarsVo.setEstTime(redoEstTime(msgContent,acarsVo.getTelexDt()));
				}
			}else{
				//一般POS报文经纬度处理
				if(acarsVo.getCurLat()!=null&&acarsVo.getCurLon()!=null){
					acarsVo.setCurLat(redoLonLat(acarsVo.getCurLat(), "N", "S"));
					acarsVo.setCurLon(redoLonLat(acarsVo.getCurLon(), "E", "W"));
				}
			}
		}
		return acarsVo;
	}

	//后续处理经纬度函数
	public String redoLonLat(String rawContent,String ne,String sw){
		String Regex = "((E|W|S|N){0,1}\\s*\\-{0,1})\\s*(\\d*\\.\\d+)";
		String FlagStr = RegExpUtil.find(Regex, rawContent, 1);
		String NumStr = RegExpUtil.find(Regex, rawContent, 3);
		if(FlagStr == null || NumStr == null){
			return rawContent;
		}

		if(FlagStr.indexOf("-")==-1){
			if(FlagStr.startsWith(ne)){//E
				FlagStr=ne;
			}else if(FlagStr.startsWith(sw)){//W
				FlagStr=sw;
			}else{//
				FlagStr=ne;
			}
		}else if(FlagStr.indexOf("-")!=-1){
			if(FlagStr.startsWith(ne)){//E -
				FlagStr=sw;
			}else if(FlagStr.startsWith(sw)){//W -
				FlagStr=ne;
			}else{//
				FlagStr=sw;
			}
		}
		if(FlagStr.startsWith(".")){
			FlagStr="0"+FlagStr;
		}
		String lon=(FlagStr + NumStr).replace(" ","");
		return lon;
	}

	//后续处理经纬度函数
	public String[] redoLonLat(String latStr,String lonStr){
		if(latStr.indexOf("-")!=-1){
			latStr=latStr.replace('-', 'S');
		}else{
			latStr="N"+latStr;
		}
		if(lonStr.indexOf("-")!=-1){
			lonStr=lonStr.replace('-', 'W');
		}else{
			lonStr="E"+lonStr;
		}
		String[] result = {latStr,lonStr};
		return result;
	}

	//后续处理B787的预计时间问题
	public String redoEstTime(String msgContent, String telex){
		if(msgContent.split("\r\n").length>7){
			String etaLine = msgContent.trim().split("\r\n")[6];
			String etaStr = etaLine.split("\\.")[4].substring(2,8);
			long etaTime=1000*(3600*Integer.valueOf(etaStr.substring(0, 2))
					+60*Integer.valueOf(etaStr.substring(2,4))
					+Integer.valueOf(etaStr.substring(4)));
			Date telexDt=DateUtil.parse(telex,DateUtil.DEFAULT_FORMAT);
			String estTime=DateUtil.format(new Date(telexDt.getTime()+etaTime)
					, DateUtil.DEFAULT_FORMAT);
			return estTime;
		}
		logger.error("Parser goes wrong when parsing estTime  -Message type: 787_POS");
		//System.err.println("Parser goes wrong when parsing estTime  -Message type: 787_POS");
		return null;
	}
}
