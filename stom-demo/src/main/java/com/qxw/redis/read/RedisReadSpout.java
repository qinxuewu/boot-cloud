package com.qxw.redis.read;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
 
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
 
/**
 * @author cwc
 * @date 2018年5月30日  
 * @description:
 * @version 1.0.0 
 */
public class RedisReadSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector spoutOutputCollector;  
    
    /**
     * 这是刚刚作为word写入的数据，要通过他获取我们存的值
     */
    private static final Map<Integer, String> LASTNAME = new HashMap<Integer, String>();  
    static {  
        LASTNAME.put(0, "anderson");  
        LASTNAME.put(1, "watson");  
        LASTNAME.put(2, "ponting");  
        LASTNAME.put(3, "dravid");  
        LASTNAME.put(4, "lara");  
    }  
      
    public void open(Map conf, TopologyContext context,  
            SpoutOutputCollector spoutOutputCollector) {  
        this.spoutOutputCollector = spoutOutputCollector;  
    }  
  
    public void nextTuple() {  
        final Random rand = new Random();  
        int randomNumber = rand.nextInt(5); 
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        spoutOutputCollector.emit (new Values(LASTNAME.get(randomNumber)));  
        System.out.println("读数据来袭！！！！！！");
    }  
  
    public void declareOutputFields(OutputFieldsDeclarer declarer) {  

        declarer.declare(new Fields("word"));  
    }  
}
