package com.example.pull;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.impl.consumer.PullResultExt;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author qinxuewu
 * @version 1.00
 * @time 27/8/2018下午 3:30
 */
public class PullConsumer {

    private static final Map<MessageQueue, Long> offsetTable = new HashMap<>();

    public static void main(String[] args) {
        try {
            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("pullConsumer");
            consumer.setNamesrvAddr("127.0.0.1:9876");
            consumer.start();
            Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("TopicTest");
            for(MessageQueue mq:mqs) {
                System.out.println("Consume from the queue: " + mq);


                PullResultExt pullResult =(PullResultExt)consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                switch (pullResult.getPullStatus()) {
                    case FOUND:
                        List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                        for (MessageExt m : messageExtList) {
                            System.out.println(new String(m.getBody()));
                        }
                        break;
                    case NO_MATCHED_MSG:
                        break;
                    case NO_NEW_MSG:
                        break ;
                    case OFFSET_ILLEGAL:
                        break;
                    default:
                        break;
                }
            }

        }catch (Exception e){

        }
    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        offsetTable.put(mq, offset);
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = offsetTable.get(mq);
        if (offset != null)
            return offset;
        return 0;
    }

}
