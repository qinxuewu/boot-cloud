package com.github;

/**
 * 测试验证字符串连接处理时，使用StringBuilder方式是否比直接相加好。
 *
 * @author qinxuewu
 * @create 19/9/2下午12:35
 * @since 1.0.0
 */


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.Runner;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)  // 测试结果的时间单位
@BenchmarkMode(Mode.Throughput)     // 吞吐量模式，即单位时间内方法的吞吐量
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.MILLISECONDS)  // 预热参数 预热5次，每次1毫秒）
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)  // 默认状态，每个线程分配一个独享的实例；
public class JmhTest2 {

    public static void main(String[] args) throws Exception{
        Options options = new OptionsBuilder()
                .include(JmhTest2.class.getName())
                .build();
        new Runner(options).run();
    }
    /**
     * 字符串个数  参值依次为
     */
    @Param({"10", "100", "1000"})
    private int number;


    /**
     * 字符串直接相加方式
     */
    @Benchmark
    public void StringAddMode(){
        String str = "";
        for(int i=0;i<number;i++){
            str = str + i;
        }
    }
    /**
     * 字符串通过StringBuilder的append方式
     */
    @Benchmark
    public void StringBuilderMode(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<number;i++){
            sb.append(i);
        }
    }
}
