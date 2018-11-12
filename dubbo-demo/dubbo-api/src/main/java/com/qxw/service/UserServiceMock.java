package com.qxw.service;

/**
 * 服务降级处理
 * @author qxw
 * 2018年1月12日
 */
public class UserServiceMock implements UserService{

	/**
	 *降级业务处理逻辑
	 */
	public String getName(String name) {
		// TODO Auto-generated method stub
		return "服务降级业务处理返回值";
	}

}
