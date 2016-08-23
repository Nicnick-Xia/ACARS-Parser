package com.csair.newparse.pattern.beans;

import java.util.List;

public class RegexBeanConfig {
    private List<String> acarsFltTypes;
    private List<RegexBean> regexBeans;
    public List<String> getAcarsFltTypes() {
        return acarsFltTypes;
    }
    public void setAcarsFltTypes(List<String> acarsFltTypes) {
        this.acarsFltTypes = acarsFltTypes;
    }
    public List<RegexBean> getRegexBeans() {
        return regexBeans;
    }
    public void setRegexBeans(List<RegexBean> regexBeans) {
        this.regexBeans = regexBeans;
    }
}
