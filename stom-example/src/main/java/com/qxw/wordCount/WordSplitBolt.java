package com.qxw.wordCount;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * 切割组件
 *
 * @author qxw
 * @data 2018年9月18日上午11:58:49
 */
public class WordSplitBolt implements IRichBolt {

    private static final long serialVersionUID = 1L;
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;

    }

    /**
     * 这个函数也会被不断执行，但它的数据来自于上游。
     * 这里将文本行分割为单词，并发送
     *
     * @param tuple
     */
    @Override
    public void execute(Tuple input) {
        String line = input.getStringByField("line");
        String[] words = line.split(" ");
        for (String word : words) {
            this.collector.emit(new Values(word));
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));

    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }


    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
