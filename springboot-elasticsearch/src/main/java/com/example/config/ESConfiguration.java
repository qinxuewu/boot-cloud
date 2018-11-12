package com.example.config;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *  elasticsearch spring-data 目前支持的最高版本为5.5 所以需要自己注入生成客户端
 *
 * 数据配置，进行初始化操作
 * @author qinxuewu
 * @version 1.00
 * @time 28/8/2018下午 5:54
 */
@Configuration
public class ESConfiguration implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(ESConfiguration.class);

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    private RestHighLevelClient restHighLevelClient;

    /**
     * 控制Bean的实例化过程
     *
     * @return
     * @throws Exception
     */
    @Override
    public RestHighLevelClient getObject() throws Exception {
        return restHighLevelClient;
    }
    /**
     * 获取接口返回的实例的class
     *
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public void destroy() throws Exception {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (final Exception e) {
            LOG.error("Error closing ElasticSearch client: ", e);
        }
    }

    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        restHighLevelClient = buildClient();
    }

    private RestHighLevelClient buildClient() {
        try {
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(clusterNodes.split(":")[0], Integer.parseInt(clusterNodes.split(":")[1]), "http")));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return restHighLevelClient;
    }


}
