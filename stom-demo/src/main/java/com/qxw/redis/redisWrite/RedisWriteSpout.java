package com.qxw.redis.redisWrite;

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
 * @version 1.0.0
 * @date 2018年5月29日
 * @description:这是给的假的数据源
 */
public class RedisWriteSpout extends BaseRichSpout {
    private static final long serialVersionUID = 1L;
    private SpoutOutputCollector spoutOutputCollector;

    /**
     * 作为字段word输出
     */
    private static final Map<Integer, String> LASTNAME = new HashMap<Integer, String>();

    static {
        LASTNAME.put(0, "anderson");
        LASTNAME.put(1, "watson");
        LASTNAME.put(2, "ponting");
        LASTNAME.put(3, "dravid");
        LASTNAME.put(4, "lara");
    }

    /**
     * 作为字段myValues输出
     */
    private static final Map<Integer, String> COMPANYNAME = new HashMap<Integer, String>();

    static {
        COMPANYNAME.put(0, "abc");
        COMPANYNAME.put(1, "dfg");
        COMPANYNAME.put(2, "pqr");
        COMPANYNAME.put(3, "ecd");
        COMPANYNAME.put(4, "awe");
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
        spoutOutputCollector.emit(new Values(LASTNAME.get(randomNumber), COMPANYNAME.get(randomNumber)));
        System.out.println("数据来袭！！！！！！");
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "myValues"));
    }
}
