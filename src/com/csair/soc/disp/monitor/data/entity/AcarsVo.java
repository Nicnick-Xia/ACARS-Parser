package com.csair.soc.disp.monitor.data.entity;

import java.io.Serializable;

public class AcarsVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3390575392098345154L;
	//add 
	private String curCas;//当前空速
	//add
	private String lastArinc;
	private String tailNr; // 机尾号，四位数字
	private String fltSuffix;//航班后缀
	private String fltNr; // 航班号，3/4位数字1位字母，不补0
	private String depArpCd; // 始发站，四字母
	private String arvArpCd; // 到达站，四字母
	private String dspDt; // 报文拍发时间ddhh
	private String telexDt; // 电报日期 ddmmmyyhhmm
	private String curLat; // 当前位置纬度
	private String curLon; // 当前位置经度
	private String curMh; // 当前位置磁航向
	private String curAlt; // 当前位置高度
	private String curMach; // 当前位置飞行马赫数
	private String curWd; // 风向
	private String curWs; // 风速
	private String curTemp; // 外界温度
	private String curFob; // 当前剩余油量
	private String curFf; // 燃油流量
	private String curZfw; // 当前零燃油重量
	private String curGw; // 飞机当前重量
	private String nextWp; // 下一航路点名字
	private String nextTime; // 预计到达下一航路点时间
	private String nextFuel; // 预计到达下一航路点剩余量
	private String estTime; // 预计到达目的机场时间
	private String estFob; // 预计到达目的机场剩余油量
	private String estDis; // 当前位置到目的机场距离
	private String telexMsg; // 位置报文原文
	private String type;//报文具体类型
	
	private String direction;//上传下行
	
	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getCurCas() {
		return curCas;
	}

	public void setCurCas(String curCas) {
		this.curCas = curCas;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTailNr() {
		return tailNr;
	}

	public void setTailNr(String tailNr) {
		this.tailNr = tailNr;
	}
	public String getFltSuffix() {
		return fltSuffix;
	}

	public void setFltSuffix(String fltSuffix) {
		this.fltSuffix = fltSuffix;
	}

	private String alnCd;//航空公司代码
	public String getAlnCd() {
		return alnCd;
	}

	/**
	 * @return the lastArinc
	 */
	public String getLastArinc() {
		return lastArinc;
	}

	/**
	 * @param lastArinc the lastArinc to set
	 */
	public void setLastArinc(String lastArinc) {
		this.lastArinc = lastArinc;
	}

	public void setAlnCd(String alnCd) {
		this.alnCd = alnCd;
	}

	public String getFltNr() {
		return fltNr;
	}

	public void setFltNr(String fltNr) {
		this.fltNr = fltNr;
	}

	public String getDepArpCd() {
		return depArpCd;
	}

	public void setDepArpCd(String depArpCd) {
		this.depArpCd = depArpCd;
	}

	public String getArvArpCd() {
		return arvArpCd;
	}

	public void setArvArpCd(String arvArpCd) {
		this.arvArpCd = arvArpCd;
	}

	public String getDspDt() {
		return dspDt;
	}

	public void setDspDt(String dspDt) {
		this.dspDt = dspDt;
	}

	public String getTelexDt() {
		return telexDt;
	}

	public void setTelexDt(String telexDt) {
		this.telexDt = telexDt;
	}

	public String getCurLat() {
		return curLat;
	}

	public void setCurLat(String curLat) {
		this.curLat = curLat;
	}

	public String getCurLon() {
		return curLon;
	}

	public void setCurLon(String curLon) {
		this.curLon = curLon;
	}

	public String getCurMh() {
		return curMh;
	}

	public void setCurMh(String curMh) {
		this.curMh = curMh;
	}

	public String getCurAlt() {
		return curAlt;
	}

	public void setCurAlt(String curAlt) {
		this.curAlt = curAlt;
	}

	public String getCurMach() {
		return curMach;
	}

	public void setCurMach(String curMach) {
		this.curMach = curMach;
	}

	public String getCurWd() {
		return curWd;
	}

	public void setCurWd(String curWd) {
		this.curWd = curWd;
	}

	public String getCurWs() {
		return curWs;
	}

	public void setCurWs(String curWs) {
		this.curWs = curWs;
	}

	public String getCurTemp() {
		return curTemp;
	}

	public void setCurTemp(String curTemp) {
		this.curTemp = curTemp;
	}

	public String getCurFob() {
		return curFob;
	}

	public void setCurFob(String curFob) {
		this.curFob = curFob;
	}

	public String getCurFf() {
		return curFf;
	}

	public void setCurFf(String curFf) {
		this.curFf = curFf;
	}

	public String getCurZfw() {
		return curZfw;
	}

	public void setCurZfw(String curZfw) {
		this.curZfw = curZfw;
	}

	public String getCurGw() {
		return curGw;
	}

	public void setCurGw(String curGw) {
		this.curGw = curGw;
	}

	public String getNextWp() {
		return nextWp;
	}

	public void setNextWp(String nextWp) {
		this.nextWp = nextWp;
	}

	public String getNextTime() {
		return nextTime;
	}

	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}

	public String getNextFuel() {
		return nextFuel;
	}

	public void setNextFuel(String nextFuel) {
		this.nextFuel = nextFuel;
	}

	public String getEstTime() {
		return estTime;
	}

	public void setEstTime(String estTime) {
		this.estTime = estTime;
	}

	public String getEstFob() {
		return estFob;
	}

	public void setEstFob(String estFob) {
		this.estFob = estFob;
	}

	public String getEstDis() {
		return estDis;
	}

	public void setEstDis(String estDis) {
		this.estDis = estDis;
	}

	public String getTelexMsg() {
		return telexMsg;
	}

	public void setTelexMsg(String telexMsg) {
		this.telexMsg = telexMsg;
	}

	@Override
	public String toString() {
		return "AcarsPosVo [type="+type+"\n"+"tailNr=" + tailNr + ", fltNr=" + fltNr
				+ ", depArpCd=" + depArpCd + ", arvArpCd=" + arvArpCd
				+ ", dspDt=" + dspDt + ", telexDt=" + telexDt + ", curLat="
				+ curLat + ", curLon=" + curLon + ", curMh=" + curMh
				+ ", curAlt=" + curAlt + ", curMach=" + curMach + ", curWd="
				+ curWd + ", curWs=" + curWs + ", curTemp=" + curTemp
				+ ", curFob=" + curFob + ", curFf=" + curFf + ", curZfw="
				+ curZfw + ", curGw=" + curGw + ", nextWp=" + nextWp
				+ ", nextTime=" + nextTime + ", nextFuel=" + nextFuel
				+ ", estTime=" + estTime + ", estFob=" + estFob + ", estDis="
				+ estDis+"]";
	}

	
	
}
