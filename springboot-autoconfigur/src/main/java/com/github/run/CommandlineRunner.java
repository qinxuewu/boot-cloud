package com.github.run;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 功能描述: 自定义启动加载器
 * @author: qinxuewu
 * @date: 2020/3/19 14:17
 * @since 1.0.0
 *  callRunners实现流程
 *      1.添加ApplicationRunner实现至runners集合
 *      2.添加CommandLineRunner实现至runners集合
 *      3.对runners集合排序
 *      4. 遍历runners集合一次调用实现类的run方法
 *
 *
 *      // 源码如下
 *      private void callRunners(ApplicationContext context, ApplicationArguments args) {
 * 		List<Object> runners = new ArrayList<>();
 * 		runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
 * 		runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
 * 		AnnotationAwareOrderComparator.sort(runners);
 * 		for (Object runner : new LinkedHashSet<>(runners)) {
 * 			if (runner instanceof ApplicationRunner) {
 * 				callRunner((ApplicationRunner) runner, args);
 *                        }
 * 			if (runner instanceof CommandLineRunner) {
 * 				callRunner((CommandLineRunner) runner, args);
 *            }        * 		}
 * 	}
 *
 */
@Component
@Order(1)
public class CommandlineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\u001B[32m >>> 自定义启动加载 runner<<<");
    }
}
