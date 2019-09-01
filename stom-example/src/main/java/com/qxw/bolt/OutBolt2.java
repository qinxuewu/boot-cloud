package com.qxw.bolt;

import java.io.FileWriter;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * 数据库流处理组件
 * <p>
 * 打印出输处理的bolt
 * 实现方式：继承BaseBasicBolt类  或实现IBasicBolt
 *
 * @author qxw
 * @data 2018年9月17日上午11:36:07
 */
public class OutBolt2 extends BaseBasicBolt {


    private static final long serialVersionUID = 1L;
    private FileWriter writer;

    /**
     * 接受一个tuple进行处理，也可发送数据到下一级组件
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        ////获取上一个组件所声明的Field
        String value = input.getStringByField("outdata");
        try {
            if (writer == null) {
                if (System.getProperty("os.name").equals("Windows 10")) {
                    writer = new FileWriter("F:\\099_test\\" + this);
                } else if (System.getProperty("os.name").equals("Windows 8.1")) {
                    writer = new FileWriter("F:\\099_test\\" + this);
                } else if (System.getProperty("os.name").equals("Windows 7")) {
                    writer = new FileWriter("F:\\099_test\\" + this);
                } else if (System.getProperty("os.name").equals("Linux")) {
                    System.out.println("----:" + System.getProperty("os.name"));
                    writer = new FileWriter("/usr/local/temp/" + this);
                }
            }
            writer.write(value);
            writer.write("\n");
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 声明发送数据的名称
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

}
