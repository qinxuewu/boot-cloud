package com.company.shardingjdbc.common;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 单键数据库分片算法.
 *
 * 支持单键和多键策略
 * <ul>
 *     <li>单键 SingleKeyDatabaseShardingAlgorithm</li>
 *     <li>多键 MultipleKeysDatabaseShardingAlgorithm</li>
 * </ul>
 *
 * 支持的分片策略
 * <ul>
 *     <li> = doEqualSharding 例如 where order_id = 1 </li>
 *     <li> IN doInSharding 例如 where order_id in (1, 2)</li>
 *     <li> BETWEEN doBetweenSharding 例如 where order_id between 1 and 2 </li>
 * </ul>
 *
 * @author mengday
 */
public class ModuleDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Long> {

    /**
     * 分片策略 相等=
     * @param availableTargetNames 可用的目标名字(这里指数据名db0、db1)
     * @param shardingValue 分片值[logicTableName="t_order" 逻辑表名, columnName="user_id" 分片的列名, value="20" 分片的列名对应的值(user_id=20)]
     * @return
     */
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
        for (String each : availableTargetNames) {
            if (each.endsWith(shardingValue.getValue() % 2 + "")) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (Long value : shardingValue.getValues()) {
            for (String tableName : availableTargetNames) {
                if (tableName.endsWith(value % 2 + "")) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames,
                                                ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : availableTargetNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }

}
