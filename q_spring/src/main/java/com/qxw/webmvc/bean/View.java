package com.qxw.webmvc.bean;

import com.qxw.webmvc.servlet.QDispatcherServlet;

public class View {

    private String url;
    private String dispathType;


    public View(String url, String dispathType) {
        this.url = url;
        this.dispathType = dispathType;
    }

    public View(String url) {
        this.url = url;
        this.dispathType = QDispatcherServlet.FORWARD;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDispathType() {
        return dispathType;
    }


    public void setDispathType(String dispathType) {
        this.dispathType = dispathType;
    }


}
