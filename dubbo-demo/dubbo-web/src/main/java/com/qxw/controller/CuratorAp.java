package com.qxw.controller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 *  Curator主要解决了三类问题
	1.封装ZooKeeper client与ZooKeeper server之间的连接处理
	2.提供了一套Fluent风格的操作API
	3.提供ZooKeeper各种应用场景(recipe, 比如共享锁服务, 集群领导选举机制)的抽象封装
 * @author qxw
 * @data 2018年8月14日下午2:08:51
 */
public class CuratorAp {
	/**
	 * Curator客户端
	 */
    public static CuratorFramework client = null;
    /**
     * 集群模式则是多个ip
     */
//    private static final String zkServerIps = "192.168.10.124:2182,192.168.10.124:2183,192.168.10.124:2184";
    private static final String zkServerIps = "127.0.0.1:2181";

	public static CuratorFramework getConnection(){
	        if(client==null){
	             synchronized (CuratorAp.class){
	               if(client==null){
	            		//通过工程创建连接
	           		client= CuratorFrameworkFactory.builder()
	           				.connectString(zkServerIps)
	           				.connectionTimeoutMs(5000) ///连接超时时间
	           				.sessionTimeoutMs(5000)  // 设定会话时间
	           				.retryPolicy(new ExponentialBackoffRetry(1000, 10))   // 重试策略：初试时间为1s 重试10次
//	           				.namespace("super")  // 设置命名空间以及开始建立连接
	           				.build();
	           		
	           		 //开启连接
	           		  client.start();
		        	  System.out.println(client.getState());
	                }
	            }
	        }
			return client;
	  }
	
	/**
	 * 创建节点   不加withMode默认为持久类型节点
	 * @param path  节点路径
	 * @param value  值
	 */
	public static String create(String path,String value){
		try {
			//若创建节点的父节点不存在会先创建父节点再创建子节点
			return getConnection().create().creatingParentsIfNeeded().forPath("/super"+path,value.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 创建节点 
	 * @param path  节点路径
	 * @param value  值
	 * @param modeType 节点类型
	 */
	public static String create(String path,String value,String modeType){
		try {
			if(StringUtils.isEmpty(modeType)){
				return null;
			}
			//持久型节点
			if(CreateMode.PERSISTENT.equals(modeType)){
				//若创建节点的父节点不存在会先创建父节点再创建子节点
				return getConnection().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/super"+path,value.getBytes());
			}
			//临时节点
			if(CreateMode.EPHEMERAL.equals(modeType)){
				//若创建节点的父节点不存在会先创建父节点再创建子节点
				return getConnection().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/super"+path,value.getBytes());
			}
			
			//持久类型顺序性节点
			if(CreateMode.PERSISTENT_SEQUENTIAL.equals(modeType)){
				//若创建节点的父节点不存在会先创建父节点再创建子节点
				return getConnection().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/super"+path,value.getBytes());
			}
			
			//临时类型顺序性节点
			if(CreateMode.EPHEMERAL_SEQUENTIAL.equals(modeType)){
				//若创建节点的父节点不存在会先创建父节点再创建子节点
				return getConnection().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/super"+path,value.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	/**
	 * 获取单个节点
	 * @param path
	 * @return
	 */
	public static String getData(String path){
		try {
			String str = new String(getConnection().getData().forPath("/super"+path));
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *获取字节点
	 * @param path
	 * @return
	 */
	public static List<String> getChildren(String path){
		try {
			List<String> list = getConnection().getChildren().forPath("/super"+path);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 修改节点值
	 * @param path
	 * @param valu
	 * @return
	 */
	public static String setData(String path,String valu){
		try {
			getConnection().setData().forPath("/super"+path,valu.getBytes());
			String str = new String(getConnection().getData().forPath("/super"+path));
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除节点
	 * @param path
	 */
	public static void  delete(String path){
		try {
			getConnection().delete().guaranteed().deletingChildrenIfNeeded().forPath("/super"+path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 检测节点是否存在
	 * @param path
	 * @return
	 */
	public static boolean  checkExists(String path){
		try {
			Stat s=getConnection().checkExists().forPath("/super"+path);
			return s==null? false:true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	/**
	 * 分布式锁 对象（可重入锁）
	 * @param path
	 * @return
	 */
	public static InterProcessMutex getLock(String path){
		InterProcessMutex lock=null;
		try {
			lock=new InterProcessMutex(getConnection(), "/super"+path);
			 return  lock;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 分布式锁 进程内部（可重入）读写锁  
	 * @param path
	 * @return
	 */
	public static InterProcessReadWriteLock  getReadWriteLock(String path){
		InterProcessReadWriteLock  lock=null;
		try {
			lock=new InterProcessReadWriteLock (getConnection(), "/super"+path);
			 return  lock;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	
	
	
	public static void main(String[] args) throws Exception {
//		if(checkExists("/qxw")){
//			delete("/qxw");
//		}
//		System.out.println("创建节点："+create("/qxw/q1", "苏打水法萨芬撒"));
//		System.out.println("创建节点："+create("/qxw/q2", "苏打水法萨芬撒"));
//		System.out.println("创建节点："+create("/qxw/q3", "苏打水法萨芬撒"));
//		
//
//		
//		ExecutorService pool = Executors.newCachedThreadPool();
//		getConnection().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).inBackground(new BackgroundCallback() {
//			public void processResult(CuratorFramework cf, CuratorEvent ce) throws Exception {
//				System.out.println("code:" + ce.getResultCode());
//				System.out.println("type:" + ce.getType());
//				System.out.println("线程为:" + Thread.currentThread().getName());
//			}
//		}, pool)
//		.forPath("/super/qxw/q4","q4内容".getBytes());
//		
//		System.out.println("读取节点： "+getData("/qxw"));
//		System.out.println("读取字节点："+getChildren("/qxw").toString());
		
		//test();
//		InterProcessReadWriteLockTest();
//		curatorAtomicInteger();
//		InterProcessSemaphoreV2Test();
//		DistributedBarrierTest();
		DistributedDoubleBarrierTest();
	}
	
	/***
	 * 分布锁演示（同JUC并发包下的可重入锁）
	 */
	private static int count=0;
	public  static void test() throws InterruptedException{
		final InterProcessMutex lock=getLock("/lock");
		final CountDownLatch c=new CountDownLatch(10);
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i <10; i++) {
			pool.execute(new Runnable() {
				public void run() {
					try {		
						c.countDown();
						Thread.sleep(1000);
						//加锁
						lock.acquire(5, TimeUnit.SECONDS);
//						lock.acquire();
						System.out.println(System.currentTimeMillis()+"___"+(++count));
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						try {
							lock.release();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}
			});
		}
		pool.shutdown();
		c.await();
		System.out.println("CountDownLatch执行完");
	}
	
	
	//分布锁读写锁（同JUC包下的读写锁）
	public static void InterProcessReadWriteLockTest(){
		//一个线程像List里面添加10个消息，添加消息的代码用写锁同步，另外三个线程不停的去取List中的最新的一条消息，取的操作用读锁保护。
		 InterProcessReadWriteLock lock=getReadWriteLock("/readWritelock");
			// 读锁  
		 InterProcessLock readLock=lock.readLock();  
		    // 写锁  
		 InterProcessLock writeLock=lock.writeLock();  
		 //共享数据存放
		 List<String> list=new ArrayList<String>(10);
		 
		 //读线程
		 for (int i = 0; i <3; i++) {
			 new Thread(()->{		
				 while(true){
					 try {
						readLock.acquire(5, TimeUnit.SECONDS);
						 if( list.size()>0){
								String pre = "";
								String s=list.get(list.size()-1);
								
								 if(!s.equals(pre)){
									  pre = s;
									  System.out.println(Thread.currentThread().getName() + " get the last news : " + s);
								 }
								 if(Integer.parseInt(s) == 9){
									 break;
								 }
						 }
					} catch (Exception e) {
						
						e.printStackTrace();
					}finally{
						try {
							readLock.release();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}
				 
			 },"读线程 thread ").start();
	 
		}
		 
		 
		//写的线程一个线程像List里面添加10个消息		
	 	new Thread(()->{
					for (int i = 0; i < 10; i++) {
						try {
							writeLock.acquire(5, TimeUnit.SECONDS);
//							Thread.sleep(100);
							list.add(i+"");
							System.out.println("写锁获取  写入 message:" + i);
						} catch (Exception e) {
							e.printStackTrace();
						}finally{
							try {
								writeLock.release();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					
					}
			},"写线程  ").start();
	}
	
	/**
	 * 分布式计数器 （同jdk的原子类AtomicInteger）
	 * 
	 * 	add​(Integer delta)：将delta添加到当前值并返回新值信息。
	 * 	compareAndSet​(Integer expectedValue, Integer newValue)：如果当前值为==预期值，则以原子方式将值设置为给定的更新值。
	 * 	decrement​()：从当前值中减去1并返回新值信息。
	 * 	forceSet​(Integer newValue)：强制设置计数器的值而不保证原子性。
	 *  get​()：返回计数器的当前值。
	 *  increment​()：将1添加到当前值并返回新值信息。
	 *  initialize​(Integer initialize)： 最初将原子值设置为NULL数据库中的等效值。
	 *  subtract​(Integer delta)： 从当前值中减去delta并返回新值信息
	 *  trySet​(Integer newValue)：尝试将值原子设置为给定值。
	 * @throws Exception 
	 */
	public static void curatorAtomicInteger() throws Exception{
		DistributedAtomicInteger atomicIntger = new DistributedAtomicInteger(getConnection(), "/super", new RetryNTimes(3, 1000));
		AtomicValue<Integer> value = atomicIntger.add(1);
		System.out.println(value.succeeded());
		System.out.println(value.postValue());	//最新值
		System.out.println(value.preValue());	//原始值		
	}
	
	/**
	 * 分布式 信号计数量（同JUC并发包下的Semaphonre）
	 * @throws Exception 
	 */
	public static void InterProcessSemaphoreV2Test() throws Exception{
			int MAX_LEASE = 10;
		    FakeLimitedResource resource = new FakeLimitedResource();
	        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
	        client.start();
	        InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(client,  "/super",MAX_LEASE);
	        Collection<Lease> leases = semaphore.acquire(5);
	        System.out.println("获取租约数量：" + leases.size());
	        Lease lease = semaphore.acquire();
	        System.out.println("获取单个租约");
	        resource.use();
	        Collection<Lease> leases2 = semaphore.acquire(5, 10, TimeUnit.SECONDS);
	        System.out.println("获取租约，如果为空则超时： " + leases2);
	        System.out.println("释放租约");
	        semaphore.returnLease(lease);
	        System.out.println("释放集合中的所有租约");
	        semaphore.returnAll(leases);
	        client.close();
	        System.out.println("OK!");
	}
	
	/**
	 * 分布式栅栏DistributedBarrier用于在分布式环境下，阻塞指定数量线程（不一定在同一台机器），当这些线程达到某一点时，
	 * 再放开阻塞。和多线程编程中的CyclicBarrier类似。
	 *  waitOnBarrier()方法用于阻塞，当所有线程都调用了该方法后，阻塞解除。
	 *  waitOnBarrier​(long maxWait, TimeUnit unit) 可设置超时
	 * 	removeBarrier​():用于删除屏障节点
	 * 	setBarrier​():设置屏障节点
	 * @throws Exception 
	 */
    public static void DistributedBarrierTest() throws Exception{
    	 //创建分布式栅栏
        DistributedBarrier distributedBarrier = new DistributedBarrier(getConnection(), "/super/CyclicBarrier");
    	  //生成线程池
        ExecutorService executor = Executors.newCachedThreadPool();
         for (int i = 0; i < 5; i++) {
        	 executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println(Thread.currentThread().getName() + "设置barrier!");
						distributedBarrier.setBarrier(); //设置
						distributedBarrier.waitOnBarrier();	//等待
					
					} catch (Exception e) {
						e.printStackTrace();
					}		
				}
			});
		}
        distributedBarrier.removeBarrier();
        executor.shutdown();
        System.out.println("---------开始执行程序----------");
    }
    
    /**
     * 分布式双重屏障。双屏障使客户端能够同步计算的开始和结束。当足够的进程加入屏障时，进程开始计算并在完成后离开屏障。
     * 	enter​() 输入屏障并阻止，直到所有成员都输入
     *  enter​(long maxWait, TimeUnit unit)
     *  leave​()  
     */
    public static void DistributedDoubleBarrierTest(){   
    	for(int i = 0; i < 5; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(getConnection(), "/super", 5);
						Thread.sleep(1000 * (new Random()).nextInt(3)); 
						System.out.println(Thread.currentThread().getName() + "已经准备");
						barrier.enter();
						System.out.println("同时开始运行...");
//						Thread.sleep(1000 * (new Random()).nextInt(3));
						System.out.println(Thread.currentThread().getName() + "运行完毕");
						barrier.leave();
						System.out.println("同时退出运行...");
						

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			},"t" + i).start();
		}
    }
    

}
