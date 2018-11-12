package com.qxw.drpc;
import java.util.Map;

import org.apache.storm.Config;
import org.apache.storm.utils.DRPCClient;
import org.apache.storm.utils.Utils;

public class DrpcTest {
	public static void main(String[] args) {
		try {
			Config conf = new Config();
	        conf.setDebug(false);
	        Map config = Utils.readDefaultConfig();
			DRPCClient	client = new DRPCClient(config,"192.168.1.191", 3772); //drpc服务
			String result = client.execute("exclamation", "hello");/// 调用drpcTest函数，传递参数为hello
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
