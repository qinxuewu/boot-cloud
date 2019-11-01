package com.github.inventory.vo;


 /**
  * 功能描述: 请求的响应
  * @author: qinxuewu
  * @date: 2019/11/1 10:01
  * @since 1.0.0
  */
public class Response {

	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	
	private String status;
	private String message;
	
	public Response() {
		
	}
	
	public Response(String status) {
		this.status = status;
	}
	
	public Response(String status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
