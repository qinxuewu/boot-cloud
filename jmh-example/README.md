## JMH进行基准性能测试
- JMH，即Java Microbenchmark Harness，是专门用于代码微基准测试的工具套件。何谓Micro Benchmark呢？简单的来说就是基于方法层面的基准测试，精度可以达到微秒级
- 官方地址：http://hg.openjdk.java.net/code-tools/jmh

## JMH适用的典型场景
- 优化热点方法，准确的知道某个方法的执行耗时，以及不同入参与最终实际耗时的关系，从而针对性的进行优化；
- 寻找最佳方案，验证接口方法不同实现方式的实际吞吐量，从而确定最佳实现方式 。如：选择json转换工具时选fastjson还是gson、字符串连接使用StringBuilder方式还是直接相加;
- 分析性能损耗，在原接口方法业务逻辑中添加新的业务代码时，对整个业务方法的性能影响
- 分析百分比内的耗时，即测试方法多次调用时百分比区间内的耗时，如：测试调用某个方法，50%以内的调用耗时是8.2ms/op，90%以内是9.3ms/op，99.99%以内是10.2ms/op，等等

## JMH基本概念
- Mode ：表示JMH测试中的模式，默认有5种，分别是Throughput（吞吐量）、AverageTime（平均耗时）、SampleTime（随机采样）、SingleShotTime（单次执行）、All（以上4种都来一次）
- Fork：表示JMH将用来测试的进程数；
- Warmup : 表示预热，在HotSpot中，JVM的JIT编译器会对热点代码进行编译优化， 因此为了最接近真实的情况，需要先预热测试代码，使JIT编译器完成可能需要的优化，从而令JMH最终测试结果更加准确；
- Iteration ：表示JMH中的最小测试迭代单位，即测试次数，一般默认值是每次1s;
- Benchmark：用于标注JMH将进行测试的方法。（类似Junit中的@Test注解） 


## 测试输出参数说明
```
# 参数信息（1-10行）
# JMH version: 1.21    jmh版本
# VM version: JDK 1.8.0_45, Java HotSpot(TM) 64-Bit Server VM, 25.45-b02  jvm版本信息
# VM invoker: /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/bin/java  jvm程序（jdk安装路径）
# VM options: -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=50990:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8  jvm参数配置
# Warmup: 5 iterations, 10 s each  预热参数：预热次数、每次持续时间
# Measurement: 5 iterations, 10 s each  测试参数：测试次数、每次持续时间
# Timeout: 10 min per iteration  每次测试迭代超时时间
# Threads: 1 thread, will synchronize iterations   每个测试进程的测试线程数
# Benchmark mode: Average time, time/op   测试的模式
# Benchmark: com.github.JmhTest1.sayHello   测试的方法

# 测试过程

# Run progress: 0.00% complete, ETA 00:08:20  测试完成进度，预计剩余需要时间
# Fork: 1 of 5   当前第几次fork
objc[15927]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/bin/java and /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/lib/libinstrument.dylib. One of the two will be used. Which one is undefined.

# 预热执行，每次预热执行耗时
# Warmup Iteration   1: 11.327 ms/op
# Warmup Iteration   2: 11.396 ms/op
# Warmup Iteration   3: 11.403 ms/op
# Warmup Iteration   4: 11.305 ms/op
# Warmup Iteration   5: 11.219 ms/op

#  正式测试执行，每次测试执行耗时
Iteration   1: 11.306 ms/op
Iteration   2: 11.248 ms/op
Iteration   3: 11.397 ms/op
Iteration   4: 11.507 ms/op
Iteration   5: 11.279 ms/op

# 第2次fork测试
# Run progress: 20.00% complete, ETA 00:06:45
# Fork: 2 of 5
objc[15950]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/bin/java and /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/lib/libinstrument.dylib. One of the two will be used. Which one is undefined.
# Warmup Iteration   1: 11.356 ms/op
# Warmup Iteration   2: 11.305 ms/op
# Warmup Iteration   3: 11.507 ms/op
# Warmup Iteration   4: 11.401 ms/op
# Warmup Iteration   5: 11.564 ms/op
Iteration   1: 11.516 ms/op
Iteration   2: 11.415 ms/op
Iteration   3: 11.435 ms/op
Iteration   4: 11.564 ms/op
Iteration   5: 11.282 ms/op

# 第3次fork测试
# Run progress: 40.00% complete, ETA 00:05:03
# Fork: 3 of 5
objc[15977]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/bin/java and /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/lib/libinstrument.dylib. One of the two will be used. Which one is undefined.
# Warmup Iteration   1: 11.304 ms/op
# Warmup Iteration   2: 11.371 ms/op
# Warmup Iteration   3: 11.653 ms/op
# Warmup Iteration   4: 11.490 ms/op
# Warmup Iteration   5: 11.473 ms/op
Iteration   1: 11.479 ms/op
Iteration   2: 11.400 ms/op
Iteration   3: 11.419 ms/op
Iteration   4: 11.367 ms/op
Iteration   5: 11.335 ms/op

# 第4次fork测试
# Run progress: 60.00% complete, ETA 00:03:22
# Fork: 4 of 5
objc[15992]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/bin/java and /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/lib/libinstrument.dylib. One of the two will be used. Which one is undefined.
# Warmup Iteration   1: 11.422 ms/op
# Warmup Iteration   2: 11.338 ms/op
# Warmup Iteration   3: 11.354 ms/op
# Warmup Iteration   4: 11.633 ms/op
# Warmup Iteration   5: 11.399 ms/op
Iteration   1: 11.508 ms/op
Iteration   2: 11.359 ms/op
Iteration   3: 11.465 ms/op
Iteration   4: 11.471 ms/op
Iteration   5: 11.540 ms/op

# 第5次fork测试
# Run progress: 80.00% complete, ETA 00:01:41 
# Fork: 5 of 5
objc[16002]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/bin/java and /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/lib/libinstrument.dylib. One of the two will be used. Which one is undefined.
# Warmup Iteration   1: 11.572 ms/op
# Warmup Iteration   2: 11.763 ms/op
# Warmup Iteration   3: 11.473 ms/op
# Warmup Iteration   4: 

Result "com.github.JmhTest1.sayHello":
  11.437 ±(99.9%) 0.087 ms/op [Average]
  (min, avg, max) = (11.248, 11.437, 11.756), stdev = 0.117
  CI (99.9%): [11.349, 11.524] (assumes normal distribution)


#  测试结果，包括测试的方法、平均耗时[平局耗时的比例]、最大最小 耗时、测试结果数据离散度（stdev）等

# Run complete. Total time: 00:08:25  测试总耗时

# 对测试结果的解释
REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

# 测试结论{测试的方法、测试类型（Mode）、测试总次数(Cnt)、测试结果(Score)、误差(Error)、单位(Units)}
Benchmark          Mode  Cnt   Score   Error  Units
JmhTest1.sayHello  avgt   25  11.437 ± 0.087  ms/op

Process finished with exit code 0

```

## 注解介绍

### @BenchmarkMode

```
基准测试模式。一共有5种可选值：（其实是4种）
Mode.Throughput：吞吐量模式，即单位时间内方法的吞吐量
Mode.AverageTime：平均耗时模式，即一定测试次数内方法执行的平均耗时
Mode.SampleTime：随机采样模式，即最终结果为取样结果分布比例
Mode.SingleShotTime：单次执行模式，即只会执行一次（以上的模式通常会有预热、会迭代执行多次，这个模式可用于测试某些特定场景，如冷启动时的性能）
Mode.All：即以上模式都执行一遍

-----------------------------------
用法示例：（benchmark模式为平均耗时模式）
@BenchmarkMode(Mode.AverageTime)

```

### @OutputTimeUnit

```$xslt
测试结果的时间单位。其值为java.util.concurrent.TimeUnit 枚举中的值，通常用的值是秒、毫秒、微妙（需要注意的是，在不同测试模式下，需要选择合适的时间单位，从而获取更精确的测试结果。）

------------------------------------
用法示例：（benchmark结果时间单位为毫秒）
@OutputTimeUnit(TimeUnit.MILLISECONDS)
————————————————

```
### @Benchmark
```
基准测试，方法级注解（配置在方法名上）。用于标注需要进行benchmark （基准测试）的方法

------------------------------------
用法示例：（方法需要benchmark）
@Benchmark
```

### @Warmup
```$xslt
预热参数。配置预热的相关参数，参数含义是：iterations（预热次数）、time （预热时间）、timeUnit （时间单位）

------------------------------------
用法示例：（预热10次，每次20s）
@Warmup(iterations = 10, time = 20, timeUnit = TimeUnit.SECONDS)
————————————————

```
### @Measurement
```$xslt
度量，即benchmark基本参数。参数含义是：iterations（测试次数）、time （每次测试时间）、timeUnit （时间单位）

------------------------------------
用法示例：（测试5次，每次30s）
@Measurement(iterations = 5, time = 30, timeUnit = TimeUnit.SECONDS)
————————————————

```

### @Fork
```$xslt
分叉，即进程数。用于配置将使用多少个进程进行测试

------------------------------------
用法示例：（使用3个进程）
@Fork(3)
```
### @Threads
```$xslt
线程数。每个Fork（进程）中的线程数，一般可设为测试机器cpu核心数。

------------------------------------
用法示例：（使用4个线程）
@Threads(4)
```

### @Param
```$xslt
成员参数，属性级注解。用于测试方法在不同入参情况下的性能表现。

------------------------------------
用法示例：（入参值依次为1 、10、100）
@Param({“1”, “10”, “100”})
```

### @Setup
```$xslt
设置，方法级注解。用于标注benchmark前的操作，通常用于测试前初始化参数资源，如初始化数据库连接等。

------------------------------------
用法示例：（初始化方法）
@Setup
```

### @TearDow
```$xslt
拆卸，方法级注解。用于标注benchmark后的操作，通常用于测试后回收资源，如关闭数据库连接等。

------------------------------------
用法示例：（回收方法）
@TearDown
```

### @State
```$xslt
状态，表示一个类/方法的可用范围，其值有3个：
Scope.Thread：默认状态，每个线程分配一个独享的实例；
Scope.Benchmark：测试中的所有线程共享实例；（多线程测试情况下）
Scope.Group：同一个组的线程共享实例；

用法示例：（默认值，每个线程分配一个实例）
@State(Scope.Thread)
```

###  @Group
```$xslt
测试组，方法级注解。适用分组测试，每组线程数不一样的场景。

------------------------------------
用法示例：（组名为“group_name”的一个组）
@Group（“group_name”）
```
### @GroupThreads
```$xslt
组线程数，方法级注解。通常和@Group搭配使用

------------------------------------
用法示例：（组线程数为10）
@GroupThreads（10）
```

### @Timeout
```$xslt
超时时间。每次测试迭代超时时间

------------------------------------
用法示例：（每次测试超时时间为20min）
@Timeout(time = 20, timeUnit = TimeUnit.MINUTES)

```

## 参考链接
- https://blog.csdn.net/cndmss/article/details/93771981