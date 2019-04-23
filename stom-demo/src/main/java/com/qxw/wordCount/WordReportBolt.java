package com.qxw.wordCount;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;


/**
 * 输出组件
 *
 * @author qxw
 * @data 2018年9月18日上午11:30:14
 */
public class WordReportBolt implements IRichBolt {


    private static final long serialVersionUID = 1L;


    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {


    }

    @Override
    public void execute(Tuple input) {
        String word = input.getStringByField("word");
        Long count = input.getLongByField("count");

        System.out.printf("实时统计单词出现次数  " + "%s\t%d\n", word, count);


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void cleanup() {

    }


    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
