package com.example.pull;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 在RocketMQ中一般有两种获取消息的方式，一个是拉(pull，消费者主动去broker拉取)，一个是推(push，主动推送给消费者)
 *
 * push-优点：及时性、服务端统一处理实现方便
 * push-缺点：容易造成堆积、负载性能不可控
 *
 * pull-优点：获得消息状态方便、负载均衡性能可控
 * pull-缺点：及时性差
 *
 *
 * @author qinxuewu
 * @version 1.00
 * @time 27/8/2018下午 3:26
 */
public class Producer {

    public static void main(String[] args) {
        //声明并初始化一个producer
        //需要一个producer group名字作为构造方法的参数，这里为producer1
        DefaultMQProducer producer = new DefaultMQProducer("producer1");

        //设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
        //NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setVipChannelEnabled(false);
        producer.setRetryTimesWhenSendFailed(3);

        try {

            //调用start()方法启动一个producer实例
            producer.start();
            //发送10条消息到Topic为TopicTest，tag为TagA，消息内容为“Hello RocketMQ”拼接上i的值
            for (int i = 0; i < 10; i++) {
                try {
                    Message msg = new Message("TopicTest", "i"+i,("Hello RocketMQ " + i).getBytes("utf-8"));

                    //调用producer的send()方法发送消息
                    //这里调用的是同步的方式，所以会有返回结果
                    SendResult sendResult = producer.send(msg);
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+","+i);
//                    System.out.println(sendResult.getSendStatus()); //发送结果状态
                    //打印返回结果，可以看到消息发送的状态以及一些相关信息
                     System.out.println(sendResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(1000);
                }
            }

            //发送完消息之后，调用shutdown()方法关闭producer
            producer.shutdown();

        }catch (Exception e){

        }

    }
}
