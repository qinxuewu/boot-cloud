package com.sun.shard.utils;

public class SnowFlakeGenerator {
	 public static class Factory {
	        /**
	         * 每一部分占用位数的默认值
	         */
	        private final static int DEFAULT_MACHINE_BIT_NUM = 5;   //机器标识占用的位数
	        private final static int DEFAULT_IDC_BIT_NUM = 5;//数据中心占用的位数

	        private int machineBitNum;
	        private int idcBitNum;

	        public Factory() {
	            this.idcBitNum = DEFAULT_IDC_BIT_NUM;
	            this.machineBitNum = DEFAULT_MACHINE_BIT_NUM;
	        }

	        public Factory(int machineBitNum, int idcBitNum) {
	            this.idcBitNum = idcBitNum;
	            this.machineBitNum = machineBitNum;
	        }

	        public SnowFlakeGenerator create(long idcId, long machineId) {
	            return new SnowFlakeGenerator(this.idcBitNum, this.machineBitNum, idcId, machineId);
	        }
	    }

	    /**
	     * 起始的时间戳
	     * 作者写代码时的时间戳
	     */
	    private final static long START_STAMP = 1508143349995L;

	    /**
	     * 可分配的位数
	     */
	    private final static int REMAIN_BIT_NUM = 22;

	    /**
	     * idc编号
	     */
	    private long idcId;

	    /**
	     * 机器编号
	     */
	    private long machineId;

	    /**
	     * 当前序列号
	     */
	    private long sequence = 0L;

	    /**
	     * 上次最新时间戳
	     */
	    private long lastStamp = -1L;

	    /**
	     * idc偏移量：一次计算出，避免重复计算
	     */
	    private int idcBitLeftOffset;

	    /**
	     * 机器id偏移量：一次计算出，避免重复计算
	     */
	    private int machineBitLeftOffset;

	    /**
	     * 时间戳偏移量：一次计算出，避免重复计算
	     */
	    private int timestampBitLeftOffset;

	    /**
	     * 最大序列值：一次计算出，避免重复计算
	     */
	    private int maxSequenceValue;

	    private SnowFlakeGenerator(int idcBitNum, int machineBitNum, long idcId, long machineId) {
	        int sequenceBitNum = REMAIN_BIT_NUM - idcBitNum - machineBitNum;

	        if (idcBitNum <= 0 || machineBitNum <= 0 || sequenceBitNum <= 0) {
	            throw new IllegalArgumentException("error bit number");
	        }

	        this.maxSequenceValue = ~(-1 << sequenceBitNum);

	        machineBitLeftOffset = sequenceBitNum;
	        idcBitLeftOffset = idcBitNum + sequenceBitNum;
	        timestampBitLeftOffset = idcBitNum + machineBitNum + sequenceBitNum;

	        this.idcId = idcId;
	        this.machineId = machineId;
	    }

	    /**
	     * 产生下一个ID
	     */
	    public synchronized long nextId() {
	        long currentStamp = getTimeMill();
	        if (currentStamp < lastStamp) {
	            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastStamp - currentStamp));
	        }

	        //新的毫秒，序列从0开始，否则序列自增
	        if (currentStamp == lastStamp) {
	            sequence = (sequence + 1) & this.maxSequenceValue;
	            if (sequence == 0L) {
	                //Twitter源代码中的逻辑是循环，直到下一个毫秒
	                lastStamp = tilNextMillis();
//	                throw new IllegalStateException("sequence over flow");
	            }
	        } else {
	            sequence = 0L;
	        }

	        lastStamp = currentStamp;

	        return (currentStamp - START_STAMP) << timestampBitLeftOffset | idcId << idcBitLeftOffset | machineId << machineBitLeftOffset | sequence;
	    }

	    private long getTimeMill() {
	        return System.currentTimeMillis();
	    }

	    private long tilNextMillis() {
	        long timestamp = getTimeMill();
	        while (timestamp <= lastStamp) {
	            timestamp = getTimeMill();
	        }
	        return timestamp;
	    }
	    
	    public static void main(String[] args) {
			System.out.println(new SnowFlakeGenerator.Factory().create(5, 5).nextId());
		}
}
