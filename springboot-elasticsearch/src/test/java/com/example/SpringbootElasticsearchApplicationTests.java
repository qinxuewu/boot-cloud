package com.example;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.EsDao;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootElasticsearchApplicationTests {
    private static final Logger LOG = LoggerFactory.getLogger(SpringbootElasticsearchApplicationTests.class);
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private EsDao esDao;


    /**
     * 创建索引
     */
    @Test
    public void createIndexOne() {
        try {
            String index="testdb";  //必须为小写
            String type="userinfo";
            JSONObject obj=new JSONObject();
            obj.put("name","qxw");
            obj.put("age",25);
            obj.put("sex","男");
            String [] tags={"标签1","标签2"};
            obj.put("tags",tags);
            esDao.createIndexOne(index,type,obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量创建索引
     */
    @Test
    public void bacthIndex(){
        String index="testdb";  //必须为小写
        String type="userinfo";
        List<JSONObject>  list=new ArrayList<>();
        JSONObject obj=null;
        for (int i = 0; i <10 ; i++) {
            obj=new JSONObject();
            obj.put("name","qxw"+i);
            obj.put("age",25+i);
            list.add(obj);
        }
        esDao.bacthIndex(index,type,list);
    }

    /**
     * 根据ID查询
     */
    @Test
    public void findById(){
        String index="testdb";  //必须为小写
        String type="userinfo";
        String id="NWrCg2UBU-HvVB1XZxe1";
        GetResponse res=esDao.findById(index,type,id);
        System.out.println("查询结果index："+res.getIndex());
        System.out.println("查询结果type："+res.getType());
        System.out.println("查询结果id："+res.getId());
        System.out.println("查询结果source："+res.getSource());
    }


    @Test
    public void update(){
        String index="testdb";  //必须为小写
        String type="userinfo";
        String id="NWrCg2UBU-HvVB1XZxe1";
        JSONObject obj=new JSONObject();
        obj.put("name","xiaoming");
        obj.put("time","2018-08-29 00:00:00");
        esDao.updateIndex(index,type,id,obj);
    }
    /**
     * 根据ID查询 指定过滤字段
     */
    @Test
    public void findByIdexcludes(){
        String index="testdb";  //必须为小写
        String type="userinfo";
        String id="NWrCg2UBU-HvVB1XZxe1";
        String [] includes={"name","sex","age"};//不过滤
        String [] excludes={"tags"}; //过滤字段

        System.out.println("查询结果："+esDao.findById(index,type,id,includes,excludes));
    }


    /**
     * 查询所有
     */
    @Test
    public  void  getAllIndex(){
        String index="testdb";  //必须为小写
        String type="userinfo";

        System.out.println("查询结果："+esDao.getAllIndex(index,type));

        String [] includes={"name","sex",};//不过滤
        String [] excludes={"tags","age"}; //过滤字段

        System.out.println("指定过滤字段查询结果："+esDao.getAllIndex(index,type,includes,excludes));

    }



    /**
     * 条件查询
     */
    @Test
    public  void  getAllIndexByFiled(){
        String index="testdb";
        String type="userinfo";
        /**
         * 使用QueryBuilder
         * termQuery("key", obj) 完全匹配
         * termsQuery("key", obj1, obj2..)   一次匹配多个值
         * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
         * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
         * matchAllQuery();         匹配所有文件
         */

        //匹配所有文件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        SearchResponse result=esDao.getAllIndex(index,type,searchSourceBuilder);
        //解析SearchHits
        SearchHits hits = result.getHits();
        long totalHits = hits.getTotalHits();
        float maxScore = hits.getMaxScore();

        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String indexs = hit.getIndex();
            String types = hit.getType();
            String ids = hit.getId();

            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println("id ："+ids+sourceAsMap.toString());
        }


    }

    /**
     * 条件查询
     * @throws IOException 
     */
    @Test
    public  void  getAllIndexByFiled2() throws IOException{
        String index="testdb";
        String type="userinfo";
        SearchSourceBuilder search2 = new SearchSourceBuilder();
         search2.query(QueryBuilders.termQuery("name", "qxw"));
        //设置from确定结果索引的选项以开始搜索。默认为0。
         search2.from(0);
        //设置size确定要返回的搜索命中数的选项。默认为10。
         search2.size(3);
         SearchRequest searchRequest = new SearchRequest(index);
         searchRequest.types(type);
         searchRequest.source(search2);
         SearchResponse response=client.search(searchRequest);
        System.out.println("完全匹配查询结果："+response.toString());
    }
    
  
    /**
     * 条件查询
     */
    @Test
    public  void  getAllIndexByFiled3(){
        String index="testdb";
        String type="userinfo";
   
        SearchSourceBuilder search3 = new SearchSourceBuilder();
//        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name","qxw");
         //在匹配查询上启用模糊匹配
//        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
//        //在匹配查询上设置前缀长度选项
//        matchQueryBuilder.prefixLength(3); 
//        //设置最大扩展选项以控制查询的模糊过程
//        matchQueryBuilder.maxExpansions(10); 
        
        
        //默认情况下，搜索请求会返回文档的内容,设置fasle不会返回窝
//        search3.fetchSource(false);
        
        //也接受一个或多个通配符模式的数组，以控制以更精细的方式包含或排除哪些字段
        String[] includeFields = new String[] {"name", "age", "tags"};
        String[] excludeFields = new String[] {"_type","_index"};
        search3.fetchSource(includeFields, excludeFields);
        
        //指定排序
        search3.sort(new FieldSortBuilder("age").order(SortOrder.DESC));




         //启用模糊查询 fuzziness(Fuzziness.AUTO)
//        search3.query(QueryBuilders.matchQuery("name","qxw").fuzziness(Fuzziness.AUTO));

        //模糊查询，?匹配单个字符，*匹配多个字符
//        search3.query(QueryBuilders.wildcardQuery("name","*qxw*"));

        //搜索name中或tags  中包含有qxw的文档（必须与music一致)
//        search3.query(QueryBuilders.multiMatchQuery("qxw","name","tags"));



        //多条件查询 相当于and
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询age=32
        TermQueryBuilder termQuery=QueryBuilders.termQuery("age",32);

        //匹配多个值  相当于sql 中in(....)操作
        TermsQueryBuilder termQuerys=QueryBuilders.termsQuery("_id","PWrIg2UBU-HvVB1XzRce","XWqYhGUBU-HvVB1Xahct");

        //模糊查询name中包含qxw
        WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name", "*qxw*");

        boolQueryBuilder.must(termQuery);
        boolQueryBuilder.must(queryBuilder);
        boolQueryBuilder.must(termQuerys);

//        //设置from确定结果索引的选项以开始搜索。默认为0。
//        search3.from(0);
//        //设置size确定要返回的搜索命中数的选项。默认为10。
//        search3.size(1);

        search3.query(boolQueryBuilder);

        SearchResponse result=esDao.getAllIndex(index,type,search3);
        //解析SearchHits
        SearchHits hits = result.getHits();
        long totalHits = hits.getTotalHits();
        float maxScore = hits.getMaxScore();

        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String indexs = hit.getIndex();
            String types = hit.getType();
            String ids = hit.getId();

            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println("id ："+ids+sourceAsMap.toString());
        }

        System.out.println("查询结果："+esDao.getAllIndex(index,type,search3));
    }

    /**
     * 请求突出显示
     * 通过设置HighlightBuilder打开， 可以突出显示搜索结果SearchSourceBuilder。
     * 通过向HighlightBuilder.Fielda 添加一个或多个实例，可以为每个字段定义不同的突出显示行为
     */
    @Test
    public void HighlightBuilderTest(){
        String index="testdb";
        String type="userinfo";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //设置突出显示的字段
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("name");
        //设置字段荧光笔类型
        highlightTitle.highlighterType("unified");
        //将字段突出显示添加到突出显示构建器
        highlightBuilder.field(highlightTitle);

   
        searchSourceBuilder.highlighter(highlightBuilder);
        SearchResponse result=esDao.getAllIndex(index,type,searchSourceBuilder);
        System.out.println("请求突出显示查询结果："+result.toString());



    }


    public void MultiSearchRequestTest(){
        //Create an empty MultiSearchRequest.
        MultiSearchRequest request = new MultiSearchRequest();
        SearchRequest firstSearchRequest = new SearchRequest();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("user", "kimchy"));

        firstSearchRequest.source(searchSourceBuilder);
        request.add(firstSearchRequest);



        SearchRequest secondSearchRequest = new SearchRequest();
        searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("user", "luca"));
        secondSearchRequest.source(searchSourceBuilder);

        request.add(secondSearchRequest);
    }



    /**
     * 聚合操作
     */
    @Test
    public  void AggregationsTest() throws IOException {
          String index="emptydb";
          String  type="empty";
//        List<JSONObject>  list=new ArrayList<>();
//        JSONObject obj=new JSONObject();
//        obj.put("name","小明"); obj.put("age",25); obj.put("salary",10000); obj.put("detpty","技术部");
//        list.add(obj);
//
//        JSONObject obj2=new JSONObject();
//        obj2.put("name","小蛋"); obj2.put("age",22); obj2.put("salary",5000); obj2.put("detpty","技术部");
//        list.add(obj2);
//
//        JSONObject obj3=new JSONObject();
//        obj3.put("name","张三"); obj3.put("age",24); obj3.put("salary",300); obj3.put("detpty","销售部");
//        list.add(obj3);
//
//        JSONObject obj4=new JSONObject();
//        obj4.put("name","李四"); obj4.put("age",22); obj4.put("salary",4000); obj4.put("detpty","采购部");
//        list.add(obj4);
//
//          //添加测试数据
//        esDao.bacthIndex(index,type,list);



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.source(searchSourceBuilder);

        //计算所有员工的平均年龄
        //terms(查询字段别名).field(分组字段)
        searchSourceBuilder.aggregation(AggregationBuilders.avg("average_age").field("age"));
        SearchResponse res=client.search(searchRequest);
        System.out.println("聚合操作查询结果："+res.toString());


        Aggregations aggregations = res.getAggregations();
        Map<String, Aggregation> aggregationMap = aggregations.getAsMap();
        System.out.println("聚合操作解析："+aggregationMap.toString());
    }

}
