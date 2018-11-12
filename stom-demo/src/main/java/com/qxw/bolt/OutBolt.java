package com.qxw.bolt;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
/**
 * 数据库流处理组件
 * 
 * 打印出输处理的bolt
 * 实现方式：继承BaseBasicBolt类  或实现IBasicBolt
 * @author qxw
 * @data 2018年9月17日上午11:36:07
 */
public class OutBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;
 
	/**
	 * 接受一个tuple进行处理，也可发送数据到下一级组件
	 */
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		////获取上一个组件所声明的Field
		String value=input.getStringByField("data");
		System.out.println("数据源发送的data: "+value);
		//发送到下一个组件
		collector.emit(new Values(value));
	}

	/**
	 * 声明发送数据的名称
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//可同时发送多个Field
		declarer.declare(new Fields("outdata"));
	}
}
