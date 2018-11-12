package com.qxw;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


public class JettyServer {

    public static void main(String[] args) {

        //指定端口号
        int port = 8080;
        Server server = new Server(port);
        WebAppContext webAppContext = new WebAppContext("webapp", "/web");   //访问项目名

        webAppContext.setDescriptor("webapp/WEB-INF/web.xml");
        webAppContext.setResourceBase("src/main/webapp");
        webAppContext.setDisplayName("web");   //项目名
        webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        webAppContext.setConfigurationDiscovered(true);
        webAppContext.setParentLoaderPriority(true);


        server.setHandler(webAppContext);
        System.out.println(webAppContext.getContextPath());
        System.out.println(webAppContext.getDescriptor());
        System.out.println(webAppContext.getResourceBase());
        System.out.println(webAppContext.getBaseResource());

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("server is  start, port is " + port + "............");
    }

}
