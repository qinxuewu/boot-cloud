package com.example.handler;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.entity.SysUserOnlineLog;
import com.example.entity.SystemClientInfo;
import com.example.repository.SysUserOnlineLogRepository;
import com.example.repository.SystemClientInfoRepository;
import com.example.vo.MessageInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import java.util.*;


/**
 *  用户事件处理
 * @author qinxuewu
 * @version 1.00
 * @time  29/3/2019 下午 4:23
 * @email 870439570@qq.com
 */
@Component
public class SocketEventHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private SystemClientInfoRepository systemClientInfoRepository;

    @Autowired
    private SysUserOnlineLogRepository sysUserOnlineLogRepository;



    /**
     * 上线事件处理
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        //用户的账号
        String account = client.getHandshakeData().getSingleUrlParam("account");
        //记录用户的页面来源
        String source= client.getHandshakeData().getSingleUrlParam("source");

        //查询该用户未建立连接的记录
        SystemClientInfo query = new SystemClientInfo();
        query.setAccount(account);
        query.setConnected((short)0);
        List<SystemClientInfo> clientList = systemClientInfoRepository.findAll(Example.of(query));
        //如果已存在就从未连接中随机获取一个，无未连接就新建一个
        SystemClientInfo clientInfo = null;
        if(clientList.size() > 0) {
            clientInfo = clientList.get(RandomUtil.randomInt(clientList.size()));
        }else {
            clientInfo = new SystemClientInfo();
            clientInfo.setAccount(account);
        }

        Date nowTime = new Date(System.currentTimeMillis());
        clientInfo.setConnected((short)1);
        clientInfo.setMostsignbits(client.getSessionId().getMostSignificantBits());
        clientInfo.setLeastsignbits(client.getSessionId().getLeastSignificantBits());
        clientInfo.setLastconnecteddate(nowTime);
        systemClientInfoRepository.save(clientInfo);

        //日志添加
        SysUserOnlineLog onlineLog=new SysUserOnlineLog();
        onlineLog.setAccount(account);
        onlineLog.setStartTime(nowTime);
        onlineLog.setSessionId(client.getSessionId().toString());
        onlineLog.setMostsignbits(client.getSessionId().getMostSignificantBits());
        onlineLog.setLeastsignbits(client.getSessionId().getLeastSignificantBits());
        onlineLog.setSource(source);
        sysUserOnlineLogRepository.save(onlineLog);

    }

    /**
     * 下线事件处理
     * @param client
     */
    @OnDisconnect
    public void onDisConnect(SocketIOClient client) {
        String account = client.getHandshakeData().getSingleUrlParam("account");
        //查询该用户是否存在
        SystemClientInfo query = new SystemClientInfo();
        query.setAccount(account);
        query.setMostsignbits(client.getSessionId().getMostSignificantBits());
        query.setLeastsignbits(client.getSessionId().getLeastSignificantBits());
        Optional<SystemClientInfo> systemClientInfoOptional = systemClientInfoRepository.findOne(Example.of(query));
        //存在就
        if (systemClientInfoOptional.isPresent()){
            SystemClientInfo clientInfo = systemClientInfoOptional.get();
            clientInfo.setConnected((short)0);
            clientInfo.setMostsignbits(null);
            clientInfo.setLeastsignbits(null);
            //存在即更新
            systemClientInfoRepository.save(clientInfo);

            //离线   更新当前会话ID的在线时长
            SysUserOnlineLog queryLog=new SysUserOnlineLog();
            queryLog.setAccount(account);
            queryLog.setSessionId(client.getSessionId().toString());
            Optional<SysUserOnlineLog> onlinelLog = sysUserOnlineLogRepository.findOne(Example.of(queryLog));
            if (onlinelLog.isPresent()){
                SysUserOnlineLog  onlinel=onlinelLog.get();
                onlinel.setEndTime(new Date());
                onlinel.setCountTime(DateUtil.between(onlinel.getEndTime(),onlinel.getStartTime(), DateUnit.SECOND)+"");
                sysUserOnlineLogRepository.save(onlinel);
            }

        }
    }



    /**
     * 消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
     * 此处未考虑同一个账号多人同时登陆的情况，故注释掉，以后改进
     * @param client
     * @param request
     * @param data
     */
    @OnEvent(value = "messageevent")
    public void onEvent(SocketIOClient client, AckRequest request, MessageInfoVo data) {
        //目标客户端ID
        String targetClientId = data.getTargetClientId();
        Optional<SystemClientInfo> systemClientInfoOptional = systemClientInfoRepository.findById(Long.parseLong(targetClientId));
        if (systemClientInfoOptional.isPresent()) {
            SystemClientInfo clientInfo = systemClientInfoOptional.get();
            ////0:断开连接1:连接成功
            if(clientInfo.getConnected() != 0) {
                UUID uuid = new UUID(clientInfo.getMostsignbits(), clientInfo.getLeastsignbits());
                //通过uuid从默认命名空间获取客户端, 然后给这客户端发消息
                client.sendEvent("messageevent","发送数据成功");
                socketIOServer.getClient(uuid).sendEvent("messageevent", data);
            }
        }

    }





    /**
     *  给所有监听pflm_msg 推送系统消息
     *
     * @param result
     * @param namespace
     * @throws Exception
     */
    public void sendSuperMsgEvent(String result,String namespace) throws Exception{
        SocketIONamespace pflmNamespace = socketIOServer.getNamespace(namespace);
        BroadcastOperations broadcastOperations = pflmNamespace.getBroadcastOperations();
        broadcastOperations.sendEvent("pflm_msg", result);
    }

    /**
     * 指定用户推送消息
     * @param clientInfoList
     * @param result
     * @throws Exception
     */
    public void sendMsgEvent(List<SystemClientInfo> clientInfoList,String result) throws Exception{
        SocketIONamespace pflmNamespace = socketIOServer.getNamespace(result);
        clientInfoList.forEach(clientInfo -> {
            UUID uuid = new UUID(clientInfo.getMostsignbits(), clientInfo.getLeastsignbits());
            SocketIOClient client = pflmNamespace.getClient(uuid);
            Optional<SocketIOClient> clientOptional = Optional.ofNullable(client);
            //判断用户是否已连接
            if(clientOptional.isPresent()) {
                client.sendEvent("pflm_msg", result);
            }else {
                clientInfo.setConnected((short)0);
                clientInfo.setMostsignbits(null);
                clientInfo.setLeastsignbits(null);
                systemClientInfoRepository.save(clientInfo);
            }
        });
    }


}
