package com.csair.newparse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.csair.newparse.config.HexConfig;
import com.csair.newparse.parser.arc.AcrParser;
import com.csair.newparse.parser.arc.AcarsPaser;
import com.csair.newparse.parser.arc.FreeTextParser;
import com.csair.soc.disp.monitor.common.constants.FltConst;
import com.csair.soc.disp.monitor.common.util.ParserLonLatUtil;
import com.csair.soc.disp.monitor.common.util.StringUtil;
import com.csair.soc.disp.monitor.data.constants.EsbConst;
import com.csair.soc.disp.monitor.data.entity.AcarsVo;
import com.csair.soc.disp.monitor.data.util.AcarsUtil;


public class AcarsFileClient {

	/*
	 * @ClassName: AcarsFileClient 
	 * @Description: TEST
	 * @author: XiaZCH 
	 * @date: 2016.08.02
	 */
	
	private static Logger logger = Logger.getLogger(AcarsFileClient.class);
	
	//读取文件
	public File[] readAcarsFile(String path){
		File[] allFiles = new File(path).listFiles();
		if (allFiles.length == 0){
			logger.error("No file exists!");
			//System.err.println("No file exists!");
			return null;
		}
		return allFiles;
	}
	
	//解析文件
	public List<AcarsVo> acarsParse(File[] acarsFiles){
		List<AcarsVo> acarsVoList = new ArrayList<AcarsVo>();
		for(File acars:acarsFiles){
			String content = getContent(acars);
			if(content == null){
				logger.info("Wrong file. File name: "+acars.getName());
				continue;
			}
			AcarsVo ac = generate(content);
			if(ac!=null){
				if(checkLonLat(ac)){
					logger.info("Parse done succeessully");
					//System.out.println("Parse done successfully");
					//showData(ac);
				}else{
					logger.info("Parse done without position info");
					//System.out.println("Parse done without position info");
					//showData(ac);
				}
				acarsVoList.add(ac);
				//acarsCacheUtil.updateAcarsPoint(transToAcarsPoint(av));
			}else{
				logger.error("Parse failed - File: "+acars.getName());
				//System.err.println("Parse failed - File: "+acars.getName());
				System.err.println("---------------------------------------------\n\n");
				/*try {
					FileUtils.moveFile(acars,new File(EsbConst.ACARS_FAILED_PATH,acars.getName()));
				} catch (Exception e) {
					logger.error("File move fail");
					logger.error(e);
				}*/
			}
			//删除文件
			/*if(acars.exists()){
				acars.delete();
			}*/
		}
		return acarsVoList;
	}
	
	/**
	 * 从文件生成Content字符串
	 * @param file
	 * @return content
	 */
	public String getContent(File file) {
		if(file.getName().endsWith(".tsm")){
			logger.info("File: " + file.toString());
			//System.out.println("File: " + file.toString());	//print for test
			String content;
			try {
				content = FileUtils.readFileToString(file,"UTF-8");
				return content;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("generate IOException");
				e.printStackTrace();
			}	
		}
		return null;
	}
	
	/**
	 * 从文件content生成AcarsVo
	 * @param content
	 * @return acarsVo
	 */

	@Test
	public AcarsVo generate(String content){
		AcarsVo acarsVo = new AcarsVo();
		//通过16进制得到报文类型 (OOOI/POS)
		String hexContent = AcarsUtil.toHexString(content);
		String contentType = null;
		String type = null;
		Boolean flag = false;
		for (HexConfig hc : HexConfig.values()) {
			if(hexContent.contains(hc.getHexContent())){
				contentType = hc.getContentType();
				type = hc.name();
				flag = true;
				break;
			}
		}
		//如果未匹配到，则设置为FREE
		if(!flag){
			contentType = "FREE_DATA";
			type = "ACARS_FREE";
			FreeTextParser freeParser = new FreeTextParser();
			acarsVo = freeParser.parseAcars(content,contentType);
		}else{
			//通过机尾号得到机型
			String acr = AcrParser.acTypeParse(content);
			if (acr == null){
				logger.error("No matched ACR. TileNr: "+AcrParser.getTailNr(content));
				//System.err.println("No matched ACR. TileNr: "+AcrParser.getTailNr(content));
				return null;
			}
			//拼接机型和报文类型
			contentType = acr+"_"+contentType.split("_")[0];
			AcarsPaser acarsPaser = new AcarsPaser();
			//根据机型和类型进行解析
			acarsVo = acarsPaser.parseAcars(content, contentType);
		}
		
		if(acarsVo!=null){
			acarsVo.setType(type);
			
			//设置方向down	
			if(StringUtils.isEmpty(acarsVo.getTelexDt()) ||
					StringUtils.isEmpty(acarsVo.getTailNr())){
				//System.err.println("Error on content parsing (No telexDt or TailNr)\n\n");
				logger.error("Error on content parsing (No telexDt or TailNr)\n\n");
				logger.error(content);
				return null;
			}
			
			//经纬度转换
			if(StringUtil.isNotEmpty(acarsVo.getCurLat()) && 
					StringUtil.isNotEmpty(acarsVo.getCurLon()) ){
				acarsVo.setCurLon(ParserLonLatUtil.parseAcarsLatLon(
						acarsVo.getCurLon()));
				acarsVo.setCurLat(ParserLonLatUtil.parseAcarsLatLon(
						acarsVo.getCurLat()));
			}
		}
		return acarsVo;
	}
	
	public void showData(AcarsVo ac){
		System.out.println("---------------------------------------------");
		System.out.println("报文类型："+ac.getType());
		System.out.println("航班号："+ac.getFltNr());
		System.out.println("始发站："+ac.getDepArpCd());
		System.out.println("到达站："+ac.getArvArpCd());
		System.out.println("当前时间："+ac.getDspDt());
		System.out.println("电报时间："+ac.getTelexDt());
		System.out.println("磁航向："+ac.getCurMh());
		System.out.println("空速："+ac.getCurCas());
		System.out.println("位置： Lon:"+ac.getCurLon()+"  Lat:"+ac.getCurLat());
		System.out.println("风速："+ac.getCurWs()+"  风向："+ac.getCurWd());
		//System.out.println("零燃油重量："+ac.getCurZfw()+"  机身重量："+ac.getCurGw());
		System.out.println("---------------------------------------------\n");
	}

	/**
	 * 检查经纬度
	 */
	private boolean checkLonLat(AcarsVo acarsVo) {
		if(acarsVo.getCurLat()=="" ||acarsVo.getCurLon()==""
				|| acarsVo.getCurLat()==null ||acarsVo.getCurLon()==null){
			return false;
		}
		return true;
	}
	
}
