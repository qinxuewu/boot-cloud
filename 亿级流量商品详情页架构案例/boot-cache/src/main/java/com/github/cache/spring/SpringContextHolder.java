package com.github.cache.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 功能描述: spring上下文
 * @author: qinxuewu
 * @date: 2019/11/7 16:38
 * @since 1.0.0
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 得到Spring 上下文环境
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * 通过Spring Bean name 得到Bean 
	 * @param name bean 上下文定义名称
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}
	
	/**
	 * 通过类型得到Bean
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return (T) applicationContext.getBeansOfType(clazz);
	}

	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}
	
	/**
	 * 通过类型得到Object
	 * @param clazz
	 * @return
	 */
	public static Object getObject(String clazz) {
		checkApplicationContext();
		return applicationContext.getBean(clazz);
	}

	/**
	 * 通过类型得到Object
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> T getObject(Class<T> clazz) {
		checkApplicationContext();
		return applicationContext.getBean(clazz);
	}
	
}
