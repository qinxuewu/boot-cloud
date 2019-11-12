
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
