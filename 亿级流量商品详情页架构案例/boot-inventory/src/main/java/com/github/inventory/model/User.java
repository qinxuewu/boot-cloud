package com.github.inventory.model;

 /**
  * 功能描述: 测试用户Model类
  * @author: qinxuewu
  * @date: 2019/11/1 9:50
  * @since 1.0.0
  */
public class User {

	/**
	 * 测试用户姓名
	 */
	private String name;
	/**
	 * 测试用户年龄
	 */
	private Integer age;
	

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
}
