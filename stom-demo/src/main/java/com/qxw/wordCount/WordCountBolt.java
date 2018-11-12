package com.qxw.wordCount;
import java.util.HashMap;
import java.util.Map;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
/**
 * 统计组件
 * @author qxw
 * @data 2018年9月18日上午11:59:02
 */
public class WordCountBolt implements IRichBolt{

	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private HashMap<String, Long> counts=null;
	 
	/**
	 * 初始化放方法
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context,OutputCollector collector) {
		 this.collector = collector;
		 this.counts=new HashMap<String, Long>();
	}

	/**
	 * 统计单词出现的次数 一般是存储到数据库
	 */
	@Override
	public void execute(Tuple input) {
		String word=input.getStringByField("word");
		 Long count = 1L;
	     if(counts.containsKey(word)){
	    	 count = counts.get(word) + 1;
	      }
	     counts.put(word, count);
	     System.out.println("统计单词："+word+" 出现次数: "+count);
	     this.collector.emit(new Values(word, count));
		
	}


	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word","count"));
		
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
