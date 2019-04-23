package com.example.config;
import com.zaxxer.hikari.HikariDataSource;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;



/**
 * 配置数据源
 * @author qinxuewu
 * @version 1.00
 * @time  22/4/2019 下午 6:31
 * @email 870439570@qq.com
 */


@Configuration
@EnableConfigurationProperties(ShardingMasterSlaveConfig.class)
@ConditionalOnProperty({ "sharding.jdbc.data-sources.ds_master.jdbc-url", "sharding.jdbc.master-slave-rule.master-data-source-name" })
public class ShardingDataSourceConfig {
    private static final Logger log = LoggerFactory.getLogger(ShardingDataSourceConfig.class);
    @Autowired(required = false)
    private ShardingMasterSlaveConfig shardingMasterSlaveConfig;



    /**
     * 配置数据源
     * @return
     * @throws SQLException
     */
    @Bean("dataSource")
    public DataSource masterSlaveDataSource() throws SQLException {
        shardingMasterSlaveConfig.getDataSources().forEach((k, v) -> configDataSource(v));
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        dataSourceMap.putAll(shardingMasterSlaveConfig.getDataSources());
        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,shardingMasterSlaveConfig.getMasterSlaveRule(), new HashMap<>(2));
        log.info("masterSlaveDataSource config complete！！");
        return dataSource;
    }

    /**
     * 可添加数据源一些配置信息
     * @param dataSource
     */
    private void configDataSource(HikariDataSource dataSource) {
        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(5);
    }

}
