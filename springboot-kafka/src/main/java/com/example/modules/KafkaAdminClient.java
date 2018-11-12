package com.example.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

/**
 * KafkaAdminClient 集群管理工具
 * @author qxw
 * @data 2018年8月7日下午5:51:06
 * https://blog.csdn.net/u013256816/article/details/79996056
 * 
 * 创建Topic：createTopics(Collection<NewTopic> newTopics)
 * 删除Topic：deleteTopics(Collection<String> topics)
 * 罗列所有Topic：listTopics()
 * 查询Topic：describeTopics(Collection<String> topicNames)
 * 查询集群信息：describeCluster()
 * 查询ACL信息：describeAcls(AclBindingFilter filter)
 * 创建ACL信息：createAcls(Collection<AclBinding> acls)
 * 删除ACL信息：deleteAcls(Collection<AclBindingFilter> filters)
 * 查询配置信息：describeConfigs(Collection<ConfigResource> resources)
 * 修改配置信息：alterConfigs(Map<ConfigResource, Config> configs)
 * 修改副本的日志目录：alterReplicaLogDirs(Map<TopicPartitionReplica, String> replicaAssignment)
 * 查询节点的日志目录信息：describeLogDirs(Collection<Integer> brokers)
 * 查询副本的日志目录信息：describeReplicaLogDirs(Collection<TopicPartitionReplica> replicas)
 * 增加分区：createPartitions(Map<String, NewPartitions> newPartitions)
 */

@Component
public class KafkaAdminClient {

	private static final String NEW_TOPIC = "topic-test2";
	private static final String brokerUrl = "localhost:9092";
	private static AdminClient adminClient;

	
	public static void beforeClass(){
	    Properties properties = new Properties();
	    properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
	    adminClient = AdminClient.create(properties);
	}
	
	public static void afterClass(){
	    adminClient.close();
	}
	
	
	public static void createTopics() {
		try {
			System.out.println("=============");
		    NewTopic newTopic = new NewTopic(NEW_TOPIC,4, (short) 1);
		    Collection<NewTopic> newTopicList = new ArrayList<>();
		    newTopicList.add(newTopic);
		    adminClient.createTopics(newTopicList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	
	
	public static void main(String[] args) {
		beforeClass();
		createTopics();
		afterClass();
	}
}
