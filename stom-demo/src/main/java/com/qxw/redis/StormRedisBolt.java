package com.qxw.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;


public class StormRedisBolt implements IBasicBolt {

    private static final long serialVersionUID = 2L;
    private RedisOperations redisOperations = null;
    private String redisIP = null;
    private int port;

    public StormRedisBolt(String redisIP, int port) {
        this.redisIP = redisIP;
        this.port = port;
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        Map<String, Object> record = new HashMap<String, Object>();

        record.put("firstName", input.getValueByField("firstName"));
        record.put("lastName", input.getValueByField("lastName"));
        record.put("companyName", input.getValueByField("companyName"));
        String id = UUID.randomUUID().toString();
        System.out.println("接收到消息record:   " + record.toString() + "   ID:" + id);
        redisOperations.insert(record, id);
    }

    //向下发送的名字
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public void prepare(Map stormConf, TopologyContext context) {
        redisOperations = new RedisOperations(this.redisIP, this.port);
    }

    public void cleanup() {

    }

}
