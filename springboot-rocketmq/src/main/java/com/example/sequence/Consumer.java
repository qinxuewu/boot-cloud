package com.example.sequence;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * 顺序消息消费，带事务方式（应用可控制Offset什么时候提交）
 * @author qinxuewu
 * @version 1.00
 * @time 27/8/2018下午 2:33
 */
public class Consumer {

    private  static DefaultMQPushConsumer consumer  =null;
    static {
        consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_3");
        consumer.setNamesrvAddr("127.0.0.1:9876");
    }

    public static void main(String[] args) {

        try {
            /**
             * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
             * 如果非第一次启动，那么按照上次消费的位置继续消费
             */
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //订阅SequenceTopicTest下Tag为TagA || TagC || TagD的消息
            consumer.subscribe("SequenceTopicTest", "TagA || TagC || TagD");
            consumer.registerMessageListener(new MessageListenerOrderly() {
                Random random = new Random();
                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext context) {
                    context.setAutoCommit(true);
                    System.out.print(Thread.currentThread().getName() + " Receive New Messages: " );
                    for (MessageExt msg: list) {
                        System.out.println(msg + ", content:" + new String(msg.getBody()));
                    }
                    try {
                        //模拟业务逻辑处理中...
                        TimeUnit.SECONDS.sleep(random.nextInt(10));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //返回消费状态
                    //CONSUME_SUCCESS 消费成功
                    //RECONSUME_LATER 消费失败，需要稍后重新消费
                    return ConsumeOrderlyStatus.SUCCESS;

                }
            });
            consumer.start();
            System.out.println("Consumer Started.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
