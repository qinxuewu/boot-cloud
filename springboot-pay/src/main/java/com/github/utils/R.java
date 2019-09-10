package com.github.utils;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * @author qinxuewu
 * @date 2016年12月21日 下午12:53:33
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static R error(String message) {
		return error(500, message);
	}
	
	public static R error(int code, String message) {
		R r = new R();
		r.put("code", code);
		r.put("msg", message);
		return r;
	}

	public static R ok(String message) {
		R r = new R();
		r.put("msg", message);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
