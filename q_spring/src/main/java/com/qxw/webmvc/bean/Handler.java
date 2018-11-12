package com.qxw.webmvc.bean;

import java.lang.reflect.Method;

public class Handler {

    protected Object object;
    protected String url;
    protected Method method;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Handler [object=" + object + ", url=" + url + ", method="
                + method + "]";
    }


}
