## 商品详情页缓存全量更新问题以及缓存维度化解决方案
- 第一块，是做实时性比较高的那块数据，比如说库存，销量之类的这种数据，我们采取的实时的缓存+数据库双写的技术方案，双写一致性保障的方案
- 第二块，是做实时性要求不高的数据，比如说商品的基本信息，等等，我们采取的是三级缓存架构的技术方案，就是说由一个专门的数据生产的服务，去获取整个商品详情页需要的各种数据，经过处理后，将数据放入各级缓存中，每一级缓存都有自己的作用
- 缓存维度化解决方案:将每个维度的数据都存一份，比如说商品维度的数据存一份，商品分类的数据存一份，商品店铺的数据存一份;那么在不同的维度数据更新的时候，只要去更新对应的维度就可以了

## 测试
- 测试保存缓存： http://localhost:8081/testPutCache?id=1&name=afasfasfsa&price=100
- 测试获取缓存： http://localhost:8081/testGetCache?id=1

## 基于kafka+ehcache+redis完成缓存数据生产服务的开发与测试
- 两种服务会发送来数据变更消息：商品信息服务，商品店铺信息服务，每个消息都包含服务名以及商品id
- 接收到消息之后，根据商品id到对应的服务拉取数据，这一步，我们采取简化的模拟方式，就是在代码里面写死，会获取到什么数据，不去实际再写其他的服务去调用了
- 分别拉取到了数据之后，将数据组织成json串，然后分别存储到ehcache中，和redis缓存中

## 测试业务逻辑
- （1）创建一个kafka topic
- （2）在命令行启动一个kafka producer
- （3）启动系统，消费者开始监听kafka topic
- （4）在producer中，分别发送两条消息，一个是商品信息服务的消息，一个是商品店铺信息服务的消息
- （5）能否接收到两条消息，并模拟拉取到两条数据，同时将数据写入ehcache中，并写入redis缓存中
- （6）ehcache通过打印日志方式来观察，redis通过手工连接上去来查询

## W系统下启动Kafka
- 修改server.propertiespe `listeners=PLAINTEXT://localhost:9092`
- 启动zookeeper : `zookeeper-server-start.bat ../../config/zookeeper.properties`
- 启动kafka:  `kafka-server-start.bat ../../config/server.properties`
- 新建topic: `kafka-topics.bat –create –zookeeper 127.0.0.1:2181 –replication-factor 1 –partitions 1 –topic test`
- 验证: `kafka-topics.bat –list –zookeeper 127.0.0.1:2181 `
- 启动生产者: `kafka-console-producer.bat –broker-list 127.0.0.1:9092 –topic test`
- 启动消费者查看消费：`kafka-console-consumer.bat –bootstrap-server 127.0.0.1:9092 –topic test –from-beginning`


### 部署分发层nginx以及基于lua完成基于商品id的定向流量分发策略



## 分布式的缓存重建的并发问题
- 重建缓存：比如我们这里，数据在所有的缓存中都不存在了（LRU算法弄掉了），就需要重新查询数据写入缓存，重建缓存
- 分布式的重建缓存，在不同的机器上，不同的服务实例中，去做上面的事情，就会出现多个机器分布式重建去读取相同的数据，然后写入缓存中
- （1）变更缓存重建以及空缓存请求重建，更新redis之前，都需要先获取对应商品id的分布式锁
- （2）拿到分布式锁之后，需要根据时间版本去比较一下，如果自己的版本新于redis中的版本，那么就更新，否则就不更新
- （3）如果拿不到分布式锁，那么就等待，不断轮询等待，直到自己获取到分布式的锁