package com.example.esapi;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author qinxuewu
 * @version 1.00
 * @time 27/8/2018下午 5:29
 */
public class RestClientTest{

    public static void main(String[] args) {
        index();
//        bacthIndex();
//         queryTest();
    }

    /**
     * 插入数据
     */
    public  static  void  index(){
        try {
            //RestHighLevelClient实例需要低级客户端构建器来构建
            RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.1.191", 9200, "http")));
            IndexRequest indexRequest = new IndexRequest("demo", "demo");
            JSONObject obj=new JSONObject();
            obj.put("title","标题图表题大法师飞洒发顺丰三");
            obj.put("time","2018-08-21 17:43:50");
            indexRequest.source(obj.toJSONString(),XContentType.JSON);
            //添加索引
            client.index(indexRequest);
            client.close();


            //http://localhost:9200/demo/demo/_search  浏览器运行查询数据
        }catch (Exception e){
                e.printStackTrace();
        }
    }

    /**
     * 批量插入数据
     */
   public static  void  bacthIndex(){
       RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
       List<IndexRequest> requests = new ArrayList<>();
       requests.add(generateNewsRequest("中印边防军于拉达克举行会晤 强调维护边境和平", "2018-01-27T08:34:00Z"));
       BulkRequest bulkRequest = new BulkRequest();
       for (IndexRequest indexRequest : requests) {
           bulkRequest.add(indexRequest);
       }
       try {
           client.bulk(bulkRequest);
           client.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    public static IndexRequest generateNewsRequest(String title,String publishTime){
        IndexRequest indexRequest = new IndexRequest("demo", "demo");
        JSONObject obj=new JSONObject();
        obj.put("title",title);
        obj.put("time",publishTime);
        indexRequest.source(obj.toJSONString(),XContentType.JSON);
        return indexRequest;
    }

    /**
     * 查询操作
     * https://blog.csdn.net/paditang/article/details/78802799
     * https://blog.csdn.net/A_Story_Donkey/article/details/79667670
     * https://www.cnblogs.com/wenbronk/p/6432990.html
     */
    public static  void  queryTest(){
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        // 这个sourcebuilder就类似于查询语句中最外层的部分。包括查询分页的起始，
        // 查询语句的核心，查询结果的排序，查询结果截取部分返回等一系列配置
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        try {
            // 结果开始处
            sourceBuilder.from(0);
            // 查询结果终止处
            sourceBuilder.size(2);
            // 查询的等待时间
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            /**
             * 使用QueryBuilder
             * termQuery("key", obj) 完全匹配
             * termsQuery("key", obj1, obj2..)   一次匹配多个值
             * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
             * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
             * matchAllQuery();         匹配所有文件
             */
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "费德勒");

            //分词精确查询
//            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tag", "体育");


//            // 查询在时间区间范围内的结果  范围查询
//            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
//            rangeQueryBuilder.gte("2018-01-26T08:00:00Z");
//            rangeQueryBuilder.lte("2018-01-26T20:00:00Z");

            // 等同于bool，将两个查询合并
            BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
            boolBuilder.must(matchQueryBuilder);
//            boolBuilder.must(termQueryBuilder);
//            boolBuilder.must(rangeQueryBuilder);
            sourceBuilder.query(boolBuilder);


            // 排序
//            FieldSortBuilder fsb = SortBuilders.fieldSort("date");
//            fsb.order(SortOrder.DESC);
//            sourceBuilder.sort(fsb);


            SearchRequest searchRequest = new SearchRequest("demo");
            searchRequest.types("demo");
            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest);
            System.out.println(response);
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 修改操作
     * @param id
     */
    public void updateTest(String id){
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        UpdateRequest updateRequest = new UpdateRequest("demo", "demo", id);
        Map<String, String> map = new HashMap<>();
        map.put("title", "网球");
        updateRequest.doc(map);
        try {
        	client.update(updateRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
