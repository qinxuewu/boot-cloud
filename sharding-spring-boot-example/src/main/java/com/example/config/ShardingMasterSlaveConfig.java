package com.example.config;
import com.zaxxer.hikari.HikariDataSource;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.HashMap;
import java.util.Map;

/**
 *  存放数据源数据
 * @author qinxuewu
 * @version 1.00
 * @time  22/4/2019 下午 6:30
 * @email 870439570@qq.com
 */


@ConfigurationProperties(prefix = "sharding.jdbc")
public class ShardingMasterSlaveConfig {


    private Map<String, HikariDataSource> dataSources = new HashMap<>();

    private MasterSlaveRuleConfiguration masterSlaveRule;


    public Map<String, HikariDataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, HikariDataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public MasterSlaveRuleConfiguration getMasterSlaveRule() {
        return masterSlaveRule;
    }

    public void setMasterSlaveRule(MasterSlaveRuleConfiguration masterSlaveRule) {
        this.masterSlaveRule = masterSlaveRule;
    }
}
