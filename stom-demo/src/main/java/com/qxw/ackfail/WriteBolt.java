package com.qxw.ackfail;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class WriteBolt implements IRichBolt {

    private static final long serialVersionUID = 1L;
    private OutputCollector collector;
    private FileWriter filewrite;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        try {
            filewrite = new FileWriter("C:\\Users\\admin\\Desktop\\message.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void execute(Tuple input) {
        String value = input.getString(0);
        try {
            filewrite.write(value);
            filewrite.write("\r\n");
            filewrite.flush();
        } catch (Exception e) {
            e.printStackTrace();
            //标记失败
            collector.fail(input);
        }
        //发送，目的是为了spoult中的成功和失败的回调方法监听使用
        collector.emit(input, new Values(value));
        //标记成功
        collector.ack(input);

    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
