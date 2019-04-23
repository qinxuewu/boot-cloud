package com.example.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * https://github.com/mrniko/netty-socketio-demo
 * <p>
 * <p>
 * Socket.io配置
 *
 * @author qinxuewu
 * @version 1.00
 * @time 29/3/2019 下午 3:44
 * @email 870439570@qq.com
 */
@Configuration
public class SocketConfig {

    @Value("${socketio.server.hostName}")
    private String hostName;

    @Value("${socketio.server.port}")
    private String port;


    @Bean
    public SocketIOServer socketIOServer() {

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        //如果未设置，则绑定地址将为0.0.0.0或:: 0
        config.setHostname(hostName);
        //服务器将侦听的端口
        config.setPort(Integer.valueOf(port));

        // 协议升级超时时间（毫秒），默认10000。HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(10000);
        // Ping消息间隔（毫秒），默认25000。客户端向服务器发送一条心跳消息间隔
        config.setPingInterval(25000);
        // Ping消息超时时间（毫秒），默认60000，这个时间间隔内没有接收到心跳消息就会发送超时事件
        config.setPingTimeout(60000);

        // 握手协议参数,用于通讯身份认证。 参数验证,拦截
        config.setAuthorizationListener(data -> {
            String token = data.getSingleUrlParam("token");
            //执行判断逻辑，查询这个token是否存在数据库
            return true;
        });
        SocketIOServer server = new SocketIOServer(config);

        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }


}
