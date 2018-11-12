package com.qxw.ackfail;
import java.util.Map;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class PringBolt implements IRichBolt{
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	@Override
	public void prepare(Map stormConf, TopologyContext context,OutputCollector collector) {
		 this.collector = collector;
		
	}
	private boolean falg=false;
	
	@Override
	public void execute(Tuple input) {
		try {
			String line=input.getStringByField("message");
			if(!falg && line.equals("html css")){
				falg=true;
				//模拟触发异常
				int a=1/0;
			}
			
			String[] value = line.split(" ");
			for(String v : value){
	            this.collector.emit(new Values(v));
	       }
			//标记消息已发送成功
			collector.ack(input);
		} catch (Exception e) {
			e.printStackTrace();
			//标记消息发送失败，spout处理重新发送
			collector.fail(input);
		}
		
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(""));
	}
	@Override
	public void cleanup() {
	}
	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
