package com.qxw.redis.read;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
 
/**
 * @author cwc
 * @date 2018年5月30日  
 * @description:打印获取的数据
 * @version 1.0.0 
 */
public class RedisOutBolt extends BaseRichBolt{
 
	private OutputCollector collector;
	@Override
	public void execute(Tuple tuple) {

				String strs =tuple.getString(1);
				System.out.println(strs);
				
	}
 
	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector collector) {
		this.collector=collector;
	}
 
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("RedisOutBolt"));
	}		
}
