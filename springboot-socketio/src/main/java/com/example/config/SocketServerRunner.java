package com.example.config;
import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;

/**
 * SocketIO启动类
 * @author qinxuewu
 * @version 1.00
 * @time  29/3/2019 下午 4:15
 * @email 870439570@qq.com
 */
@Component
@Order(1)
public class SocketServerRunner  implements CommandLineRunner {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketIOServer socketIOServer;



    public void run(String... args) throws Exception {
        socketIOServer.start();
        logger.info("socket.io启动成功！");
    }

    @PreDestroy
    public void PreDestory(){
        socketIOServer.stop();
        logger.info("socket.io关闭成功！");
    }
}
