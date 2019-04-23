package com.qxw.spout;

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
 * 数据源 spout
 *
 * @author qxw
 * @data 2018年9月17日上午11:21:00
 * <p>
 * 申明数据源的方式：继承BaseRichSpout类 ， 重写需要的方法。实现IRichSpout接口 重写所有的方法
 */
public class DataSource extends BaseRichSpout {

    private static final long serialVersionUID = 1L;
    private SpoutOutputCollector collector;

    private static final Map<Integer, String> map = new HashMap<Integer, String>();

    static {
        map.put(0, "java");
        map.put(1, "php");
        map.put(2, "groovy");
        map.put(3, "python");
        map.put(4, "ruby");
    }

    /**
     * 初始化方法
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;

    }

    /**
     * 轮询tuple 发送数据
     */
    @Override
    public void nextTuple() {
        //这里可以查询数据库 或者读取消息队列中的数据、测试使用map替代
        final Random r = new Random();
        int num = r.nextInt(5);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //发送单词到下一个拓扑节点
        this.collector.emit(new Values(map.get(num)));


    }


    /**
     * 声明发送数据的名称
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //指定名称 用于下一个节店取值时使用
        declarer.declare(new Fields("data"));

    }


    /**
     * 在该spout关闭前执行，但是并不能得到保证其一定被执行
     */
    @Override
    public void close() {
        System.out.println("spout关闭前执行");

    }

    /**
     * 当Spout已经从失效模式中激活时被调用。该Spout的nextTuple()方法很快就会被调用。
     */
    @Override
    public void activate() {
        System.out.println("当Spout已经从失效模式中激活时被调用");

    }

    /**
     * 当Spout已经失效时被调用。在Spout失效期间，nextTuple不会被调用。Spout将来可能会也可能不会被重新激活。
     */
    @Override
    public void deactivate() {
        System.out.println("当Spout已经失效时被调用");

    }


    /**
     * 成功处理tuple回调方法
     */
    @Override
    public void ack(Object paramObject) {
        System.out.println("成功处理tuple回调方法");

    }

    /**
     * 处理失败tuple回调方法
     */
    @Override
    public void fail(Object paramObject) {
        System.out.println("paramObject");

    }


}
