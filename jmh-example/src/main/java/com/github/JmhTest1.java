package com.github;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.util.concurrent.TimeUnit;

/**
 * 使用JMH进行基准性能测试
 *
 *  @OutputTimeUnit、@BenchmarkMode 这两个注解，表明这是一个JMH的测试类
 *  @Benchmark  注解标注需要benchmark(基准测试)的具体方法
 *
 *
 * @author qinxuewu
 * @create 19/9/2下午12:10
 * @since 1.0.0
 */


@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class JmhTest1 {

    public static void main(String[] args) throws Exception{
        Options options = new OptionsBuilder()
                .include(JmhTest1.class.getName())
//                .output("D:/JmhDemoOne.log")   //将测试结果输出到指定目录文件
                .build();
        new Runner(options).run();
    }
    /**
     * 测试sayHello的平局耗时
     * @throws Exception
     */
    @Benchmark
    public void sayHello() throws Exception{
        //TODO 业务方法 ，此处用休眠的方式模拟业务耗时10 ms
        TimeUnit.MILLISECONDS.sleep(10);
    }


}
