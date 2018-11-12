package com.qxw.trident;

import java.io.FileWriter;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;

public class OutFunction extends BaseFunction{
	private static final long serialVersionUID = 1L;
	private FileWriter writer ;
	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String value=tuple.getStringByField("spoutValue");
		try {
			if(writer == null){
				if(System.getProperty("os.name").equals("Windows 10")){
					writer = new FileWriter("F:\\099_test\\" + this);
				} else if(System.getProperty("os.name").equals("Windows 8.1")){
					writer = new FileWriter("F:\\099_test\\" + this);
				} else if(System.getProperty("os.name").equals("Windows 7")){
					writer = new FileWriter("F:\\099_test\\" + this);
				} else if(System.getProperty("os.name").equals("Linux")){
					System.out.println("----:" + System.getProperty("os.name"));
					writer = new FileWriter("/usr/local/temp/" + this);
				}
			}
			writer.write(value);
			writer.write("\n");
			writer.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
