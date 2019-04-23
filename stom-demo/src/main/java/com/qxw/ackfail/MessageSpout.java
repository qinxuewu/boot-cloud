package com.qxw.ackfail;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/***
 * 模拟消息发送成功失败机制
 * @author qxw
 * @data 2018年9月18日下午3:51:47
 *
 * https://github.com/apache/storm/tree/master/docs
 */
public class MessageSpout implements IRichSpout {

    private static final long serialVersionUID = 1L;
    private SpoutOutputCollector collector;
    private final String[] lines = {"springBoot springCloud", "hadoop hbase", "html css", "java javaee"};

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;

    }


    @Override
    public void nextTuple() {
        for (int i = 0; i < lines.length; i++) {
            //发送消息时 附带数据的下标位置

            this.collector.emit(new Values(lines[i]), i);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送成功回调方法
     */
    @Override
    public void ack(Object msgId) {
        System.out.println("【消息发送成功 msgId:  】" + msgId);

    }

    /**
     * 发送失败回调方法
     */
    @Override
    public void fail(Object msgId) {
        System.out.println("【消息发送失败 msgId:  】" + msgId);
        System.out.println("【重新发送消息 msgId:】" + msgId + "  内容为： " + lines[Integer.parseInt(msgId.toString())]);
        this.collector.emit(new Values(lines[(Integer) msgId]), msgId);
        System.out.println("【重新发送消息成功】");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public void activate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
