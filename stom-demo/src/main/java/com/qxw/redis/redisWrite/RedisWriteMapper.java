package com.qxw.redis.redisWrite;

import org.apache.storm.redis.common.mapper.RedisDataTypeDescription;
import org.apache.storm.redis.common.mapper.RedisStoreMapper;
import org.apache.storm.tuple.ITuple;
 
/**
 * @author cwc
 * @date 2018年5月30日  
 * @description:
 * @version 1.0.0 
 */
public class RedisWriteMapper implements RedisStoreMapper{
	private static final long serialVersionUID = 1L;
	private RedisDataTypeDescription description;
	//这里的key是redis中的key
    private final String hashKey = "mykey";
 
    public RedisWriteMapper() {
        description = new RedisDataTypeDescription(RedisDataTypeDescription.RedisDataType.HASH, hashKey);
    }
    
	@Override
	public String getKeyFromTuple(ITuple ituple) {
		//这个代表redis中，hash中的字段名
		return ituple.getStringByField("word");
	}
 
	@Override
	public String getValueFromTuple(ITuple ituple) {
		//这个代表redis中，hash中的字段名对应的值
		return ituple.getStringByField("myValues");
	}
 
	@Override
	public RedisDataTypeDescription getDataTypeDescription() {
		return description;
	}
 
	
}
