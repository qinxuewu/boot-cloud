package com.sun.shard.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.InlineShardingStrategyConfiguration;

@Configuration
public class ShardingConfig {
	
	@Autowired
	private JdbcConfig jdbcConfig;
	
	/**
	 * shardingjdbc数据源
	 * @return
	 * @throws SQLException
	 */
	@Bean
    public DataSource dataSource() throws SQLException{
		// 配置真实数据源
	    Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>(2);
	    DruidDataSource dataSource0 = createDb0();
	    dataSourceMap.put("ds0", dataSource0);
	    DruidDataSource dataSource1 = createDb1();
	    dataSourceMap.put("ds1", dataSource1);
	    TableRuleConfiguration tableRuleConfig = userRuleConfig();
	    // 配置分片规则
	    ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
	    shardingRuleConfig.getTableRuleConfigs().add(tableRuleConfig);
	    shardingRuleConfig.getTableRuleConfigs().add(addressRuleConfig());
	    Properties p = new Properties();
	    p.setProperty("sql.show",Boolean.TRUE.toString());
	    // 获取数据源对象
	    DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), p);
	    return dataSource;
	}

	/**
	 * user表分片策略
	 * @return
	 */
	private TableRuleConfiguration userRuleConfig() {
		// 配置user表规则
	    TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration();
	    tableRuleConfig.setLogicTable("t_user");
	    tableRuleConfig.setActualDataNodes("ds${0..1}.t_user${0..1}");
	    tableRuleConfig.setKeyGeneratorColumnName("id");
	    // 配置分库 + 分表策略
	    tableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("city_id", "ds${city_id % 2}"));
	    tableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("sex", "t_user${sex % 2}"));
		return tableRuleConfig;
	}
	
	/**
	 * address表分片策略
	 * @return
	 */
	private TableRuleConfiguration addressRuleConfig() {
		// 配置address表规则
	    TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration();
	    tableRuleConfig.setLogicTable("t_address");
	    tableRuleConfig.setActualDataNodes("ds${0..1}.t_address");
	    tableRuleConfig.setKeyGeneratorColumnName("id");
	    // 配置分库 + 分表策略
	    tableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("lit", "ds${lit % 2}"));
		return tableRuleConfig;
	}
	
	/**
	 * 第二数据源
	 * @return
	 */
	private DruidDataSource createDb0() {
		// 配置第二个数据源
	    DruidDataSource dataSource = new DruidDataSource();
	    dataSource.setDriverClassName(jdbcConfig.getClassName1());
	    dataSource.setUrl(jdbcConfig.getUrl1());
	    dataSource.setUsername(jdbcConfig.getUser1());
	    dataSource.setPassword(jdbcConfig.getPassword1());
	    dataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        // 每个分区最大的连接数
	    dataSource.setMaxActive(20);
        // 每个分区最小的连接数
	    dataSource.setMinIdle(5);
		return dataSource;
	}

	/**
	 * 第一数据源
	 * @return
	 */
	private DruidDataSource createDb1() {
		// 配置第一个数据源
	    DruidDataSource dataSource = new DruidDataSource();
	    dataSource.setDriverClassName(jdbcConfig.getClassName2());
	    dataSource.setUrl(jdbcConfig.getUrl2());
	    dataSource.setUsername(jdbcConfig.getUser2());
	    dataSource.setPassword(jdbcConfig.getPassword2());
	    dataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        // 每个分区最大的连接数
	    dataSource.setMaxActive(20);
        // 每个分区最小的连接数
	    dataSource.setMinIdle(5);
		return dataSource;
	}
	
	
	@Bean
	public Filter statFilter(){
	    	StatFilter filter = new StatFilter();
	    	filter.setSlowSqlMillis(5000);
	    	filter.setLogSlowSql(true);
	    	filter.setMergeSql(true);
	    	return filter;
	}
	    
    @Bean
    public ServletRegistrationBean statViewServlet(){
        //创建servlet注册实体
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        //设置ip白名单
        servletRegistrationBean.addInitParameter("allow","127.0.0.1");
        //设置ip黑名单，如果allow与deny共同存在时,deny优先于allow
        //servletRegistrationBean.addInitParameter("deny","192.168.0.19");
        //设置控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        servletRegistrationBean.addInitParameter("loginPassword","123456");
        //是否可以重置数据
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }
    
}
