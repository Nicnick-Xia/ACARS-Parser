package com.csair.newparse.config;

public enum HexConfig {
	
	/*
	 * used for hex content configuration
	 * @Attention:
	 * 1.after addition, please update the constancy "FltConst.java"
	 * 2.the order can influent the efficiency
	 * 
	 */
	ACARS_OUT			("02 4d 31 31","OOOI_DATA"),
	ACARS_OFF			("02 4d 31 32","OOOI_DATA"),
	ACARS_ON			("02 4d 31 33","OOOI_DATA"),
	ACARS_IN			("02 4d 31 34","OOOI_DATA"),
	ACARS_POS_COMMON	("02 4d 31 37","POS_COMMON_DATA"),
	ACARS_POS_B777		("02 4d 32 37","POS_B777_DATA"),
	ACARS_POS_B787		("02 41 38 33","POS_B787_DATA");
	
	private String hexContent;
	private String contentType;
	
	private HexConfig(String hexContent, String contentType){
		this.hexContent = hexContent;
		this.contentType = contentType;
	}

	public String getHexContent() {
		return hexContent;
	}

	public String getContentType() {
		return contentType;
	}
}
