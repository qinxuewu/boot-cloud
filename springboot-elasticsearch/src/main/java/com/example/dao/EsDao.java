package com.example.dao;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;


/**
 * @author qinxuewu
 * @version 1.00
 * @time 28/8/2018下午 6:15
 */

@Component
public class EsDao {
    private static final Logger LOG = LoggerFactory.getLogger(EsDao.class);
    @Autowired
    private RestHighLevelClient client;


    /**
     * 判断索引是否存在
     * @param index 索引(关系型数据库)
     * @return
     */
    public boolean isIndexExist(String index){
        try {
            GetRequest getRequest=new GetRequest(index);
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            getRequest.storedFields("_none_");
            boolean exists = client.exists(getRequest);
            return exists;
        }catch (Exception e){
            LOG.error("判断索引是否存在是否存在异常",e);
        }
        return false;
    }

    /**
     * 判断索引是否存在
     * @param index  索引(关系型数据库)
     * @param type   类型(关系型数据表)
     * @param id     数据ID
     * @return
     */
    public boolean isIndexExist(String index,String type,String id){
        try {
            GetRequest getRequest=new GetRequest(index,type,id);
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            getRequest.storedFields("_none_");
            boolean exists = client.exists(getRequest);
            return exists;
        }catch (Exception e){
            LOG.error("判断索引是否存在是否存在异常",e);
        }
        return false;
    }

    /**
     * 创建索引
     * @param index  索引(关系型数据库)
     * @param type   类型(关系型数据表)
     * @param obj    数据源
     * @return
     */
    public void createIndexOne(String index, String type, JSONObject obj) {
        try {
            IndexRequest request = new IndexRequest(index, type);
            request.source(obj.toJSONString(), XContentType.JSON);
            client.index(request);

        } catch (Exception e) {
            LOG.error("创建索引异常", e);
        }
    }

        /**
         * 创建索引
         * @param index  索引(关系型数据库)
         * @param type   类型(关系型数据表)
         * @param id     数据ID
         * @param obj    数据源
         * @return
         */
        public void createIndexOne(String index, String type,String id, JSONObject obj){
            try {
                IndexRequest request=new IndexRequest(index,type,id);
                request.source(obj.toJSONString(),XContentType.JSON);
                client.index(request);
            }catch (Exception e){
                LOG.error("创建索引异常",e);
            }

        }

        /**
         * 批量创建索
         * @param index  索引(关系型数据库)
         * @param type   类型(关系型数据表)
         * @param list   数据源
         */
        public void bacthIndex(String index, String type,List<JSONObject> list){
            try {
                List<IndexRequest> requests = new ArrayList<>();
                list.forEach(i->{
                    requests.add(generateNewsRequest(index,type,i));
                });
                BulkRequest bulkRequest = new BulkRequest();
                for (IndexRequest indexRequest : requests) {
                    bulkRequest.add(indexRequest);
                }
                client.bulk(bulkRequest);
            }catch (Exception e){
                LOG.error("批量创建索引异常",e);
            }
        }
        public static IndexRequest generateNewsRequest(String index, String type,JSONObject obj){
            IndexRequest indexRequest = new IndexRequest(index, type);
            indexRequest.source(obj.toJSONString(),XContentType.JSON);
            return indexRequest;
        }

        /**
         * 删除索引
         * @param index  索引(关系型数据库)
         * @param type   类型(关系型数据表)
         * @param id     数据ID
         * @return
         */
        public boolean deleteIndex(String index,String type,String id){
            try {
                DeleteRequest request=new DeleteRequest(index,type,id);
                client.delete(request);
                return true;
            }catch (Exception e){
                LOG.error("删除索引异常",e);
            }
            return  false;
        }

        /**
         * 修改索引
         * @param index  索引(关系型数据库)
         * @param type   类型(关系型数据表)
         * @param id     数据ID
         * @param obj    数据源
         * @return
         */
        public boolean updateIndex(String index, String type,String id, JSONObject obj){
            try {
                UpdateRequest updateRequest = new UpdateRequest(index,type,id);
                updateRequest.doc(obj.toJSONString(),XContentType.JSON);
                client.update(updateRequest);
                return true;
            }catch (Exception e){
                LOG.error("修改索引异常",e);
            }
            return  false;
        }

        /**
         * 查询单条索引
         * @param index  索引(关系型数据库)
         * @param type   类型(关系型数据表)
         * @param id     数据ID
         */
        public GetResponse findById(String index, String type,String id){
            try {
                GetRequest getRequest=new GetRequest(index,type,id);

                GetResponse getResponse = client.get(getRequest);
                return getResponse;
            } catch (Exception e) {
                LOG.error("查询单条索引异常",e);
            }
            return null;
        }

        /**
         * 查询单条索引
         * @param index     索引(关系型数据库)
         * @param type      类型(关系型数据表)
         * @param id        数据ID
         * @param includes  显示字段
         * @param excludes  排除字段
         */
        public GetResponse findById(String index, String type,String id,String [] includes,String [] excludes){
            try {
                GetRequest getRequest=new GetRequest(index,type,id);
                FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
                getRequest.fetchSourceContext(fetchSourceContext);
                GetResponse getResponse = client.get(getRequest);
                return  getResponse;
            } catch (Exception e) {
                LOG.error("查询单条索引异常",e);
            }
            return null;
        }

        /**
         * 查询列表索引
         * @param index        索引(关系型数据库)
         * @param type         类型(关系型数据表)
         * @return
         */
        public SearchResponse getAllIndex(String index,String type){
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.types(type);
            searchRequest.source(sourceBuilder);
            try {
                SearchResponse response = client.search(searchRequest);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("查询列表索引异常",e);
            }
            return  null;
        }

    /**
     * 查询列表索引
     * @param index        索引(关系型数据库)
     * @param type         类型(关系型数据表)
     * @param includes     显示字段
     * @param excludes     排除字段
     * @return
     */
    public SearchResponse getAllIndex(String index, String type,String [] includes,String [] excludes){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.fetchSource(includes,excludes);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse response = client.search(searchRequest);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询列表索引异常",e);
        }
        return  null;
    }

        /**
         * 查询列表索引
         * @param index        索引(关系型数据库)
         * @param type         类型(关系型数据表)
         * @param sourceBuilder  查询条件
         * @return
         */
        public SearchResponse getAllIndex(String index, String type, SearchSourceBuilder sourceBuilder){
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.types(type);
            searchRequest.source(sourceBuilder);
            try {
                SearchResponse response = client.search(searchRequest);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("查询列表索引异常",e);
            }
            return  null;
        }
}