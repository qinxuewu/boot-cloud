package com.qxw.drpc;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
public class ExclamationBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;

	@Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "return-info"));
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        Object arg = tuple.getValue(0);
        String retInfo = tuple.getString(1);
        System.out.println("v0: "+arg +" v1: "+retInfo);
        collector.emit(new Values(arg, retInfo + "!!!"));
    }
}
