
1、pom.xml

```
<dependency>
    <groupId>com.netflix.hystrix</groupId>
    <artifactId>hystrix-core</artifactId>
    <version>1.5.12</version>
</dependency>
```


2、将商品服务接口调用的逻辑进行封装

hystrix进行资源隔离，其实是提供了一个抽象，叫做command，就是说，你如果要把对某一个依赖服务的所有调用请求，全部隔离在同一份资源池内

对这个依赖服务的所有调用请求，全部走这个资源池内的资源，不会去用其他的资源了，这个就叫做资源隔离

hystrix最最基本的资源隔离的技术，线程池隔离技术

对某一个依赖服务，商品服务，所有的调用请求，全部隔离到一个线程池内，对商品服务的每次调用请求都封装在一个command里面

每个command（每次服务调用请求）都是使用线程池内的一个线程去执行的

所以哪怕是对这个依赖服务，商品服务，现在同时发起的调用量已经到了1000了，但是线程池内就10个线程，最多就只会用这10个线程去执行

不会说，对商品服务的请求，因为接口调用延迟，将tomcat内部所有的线程资源全部耗尽，不会出现了

```java
public class CommandHelloWorld extends HystrixCommand<String> {
    private final String name;
    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }
    @Override
    protected String run() {
        return "Hello " + name + "!";
    }
}
```

不让超出这个量的请求去执行了，保护说，不要因为某一个依赖服务的故障，导致耗尽了缓存服务中的所有的线程资源去执行

3、开发一个支持批量商品变更的接口

HystrixCommand：是用来获取一条数据的
HystrixObservableCommand：是设计用来获取多条数据的


```java
public class ObservableCommandHelloWorld extends HystrixObservableCommand<String> {
    private final String name;
    public ObservableCommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }
    @Override
    protected Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        observer.onNext("Hello " + name + "!");
                        observer.onNext("Hi " + name + "!");
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
         } ).subscribeOn(Schedulers.io());
    }
}
```



4、command的四种调用方式

同步：new CommandHelloWorld("World").execute()，new ObservableCommandHelloWorld("World").toBlocking().toFuture().get()

如果你认为observable command只会返回一条数据，那么可以调用上面的模式，去同步执行，返回一条数据

异步：new CommandHelloWorld("World").queue()，new ObservableCommandHelloWorld("World").toBlocking().toFuture()

对command调用queue()，仅仅将command放入线程池的一个等待队列，就立即返回，拿到一个Future对象，后面可以做一些其他的事情，然后过一段时间对future调用get()方法获取数据

// observe()：hot，已经执行过了
// toObservable(): cold，还没执行过

```
Observable<String> fWorld = new CommandHelloWorld("World").observe();
assertEquals("Hello World!", fWorld.toBlocking().single());
fWorld.subscribe(new Observer<String>() {
    @Override
    public void onCompleted() {

    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onNext(String v) {
        System.out.println("onNext: " + v);
    }

});

Observable<String> fWorld = new ObservableCommandHelloWorld("World").toObservable();
assertEquals("Hello World!", fWorld.toBlocking().single());
fWorld.subscribe(new Observer<String>() {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(String v) {
        System.out.println("onNext: " + v);
    }

});
```

### Hystrix常用方法
- HystrixCommand：是用来获取一条数据的
- HystrixObservableCommand：是设计用来获取多条数据的
- 同步执行：`new CommandHelloWorld("World").execute()`，`new ObservableCommandHelloWorld("World").toBlocking().toFuture().get()`
- 异步执行：`new CommandHelloWorld("World").queue()`，`new ObservableCommandHelloWorld("World").toBlocking().toFuture()`
- 对command调用queue()，仅仅将command放入线程池的一个等待队列，就立即返回，拿到一个Future对象，后面可以做一些其他的事情，然后过一段时间对future调用get()方法获取数据

### Hystrix不同的执行方式
- execute()，获取一个 Future.get()，然后拿到单个结果。
- queue()，返回一个 Future。
- observe()，立即订阅 Observable，然后启动 8 大执行步骤，返回一个拷贝的 - Observable，订阅时立即回调给你结果。
- toObservable()，返回一个原始的 Observable，必须手动订阅才会去执行 8 大步骤。

### Hystrixz执行的整个工作流程
![hystrix执行流程.png](http://ww1.sinaimg.cn/large/0068QeGHgy1g8vcnody2jj30dc0he75g.jpg)

- 步骤一：创建 command,一个 HystrixCommand 或 HystrixObservableCommand 对象，代表了对某个依赖服务发起的一次请求或者调用。创建的时候，可以在构造函数中传入任何需要的参数。
- 步骤二：调用 command执行方法,执行command，就可以发起一次对依赖服务的调用。要执行 command，可以在4个方法中选择其中的一个：execute()、queue()、observe()、toObservable()。
- 步骤三：检查是否开启缓存，如果这个 command 开启了请求缓存 Request Cache，而且这个调用的结果在缓存中存在，那么直接从缓存中返回结果。否则，继续往后的步骤
- 步骤四：检查是否开启了断路器，检查这个command对应的依赖服务是否开启了断路器。如果断路器被打开了，那么Hystrix就不会执行这个command，而是直接去执行 fallback 降级机制，返回降级结果。
- 步骤五：检查线程池/队列/信号量是否已满，如果这个command线程池和队列已满，或者 semaphore 信号量已满，那么也不会执行 command，而是直接去调用 fallback 降级机制，同时发送 reject 信息给断路器统计。
- 步骤六：执行 command，调用 HystrixObservableCommand 对象的construct() 方法，或者 HystrixCommand 的 run() 方法来实际执行这个 command。
- 步骤七：断路健康检查，Hystrix会把每一个依赖服务的调用成功、失败、Reject、Timeout 等事件发送给 circuit breaker 断路器。断路器就会对这些事件的次数进行统计，根据异常事件发生的比例来决定是否要进行断路（熔断）。如果打开了断路器，那么在接下来一段时间内，会直接断路，返回降级结果。
- 步骤八：调用 fallback 降级机制，在以下几种情况中，Hystrix 会调用 fallback 降级机制。断路器处于打开状态；线程池/队列/semaphore满了；command 执行超时；run() 或者 construct() 抛出异常

