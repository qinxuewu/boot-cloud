package com.qxw.wordCount;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * 数据源
 *
 * @author qxw
 * @data 2018年9月18日上午11:58:35
 */
public class WordSpout implements IRichSpout {

    private static final long serialVersionUID = 1L;
    private SpoutOutputCollector collector;

    private int index = 0;
    private final String[] lines = {
            "long long ago I like playing with cat",
            "playing with cat make me happy",
            "I feel happy to be with you",
            "you give me courage",
            "I like to be together with you",
            "long long ago I like you"
    };

    //初始化
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;

    }

    //发送数据
    @Override
    public void nextTuple() {
        this.collector.emit(new Values(lines[index]));
        index++;
        if (index >= lines.length) {
            index = 0;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("line"));

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


    /**
     * 发送成功的回调方法
     */
    @Override
    public void ack(Object msgId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fail(Object msgId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
