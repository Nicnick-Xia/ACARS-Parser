package com.csair.newparse.pattern.beans;

public class RegexBean {

	private String field;
	private String regex;
	private int type = 1;

	public RegexBean(String field, String regex, int type) {
		this.field = field;
		this.regex = regex;
		this.type = type;
	}	

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
