package com.example.datasources.aspect;
import com.example.datasources.DataSourceNames;
import com.example.datasources.DynamicDataSource;
import com.example.datasources.annotation.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 多数据源，切面处理类
 */
@Aspect
@Component
public class DataSourceAspect implements Ordered {
    protected Logger logger = LoggerFactory.getLogger(getClass());

        public static HashMap<String, Integer> serverWeightMap = new HashMap<String, Integer>();
        private static Integer pos=0;
        static {
            //第一个参数是数据库名,第二个是权重
            serverWeightMap.put(DataSourceNames.SLAVE1,1);
            serverWeightMap.put(DataSourceNames.SLAVE2,1);
        }


    @Pointcut("@annotation(com.example.datasources.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSource ds = method.getAnnotation(DataSource.class);

        //为空默认读库
        if(ds == null){
            DynamicDataSource.setDataSource(DataSourceNames.MASTAER);
            logger.debug("设置默认数据源： " + DataSourceNames.MASTAER);
        }
        String name=ds.name();

        //主库=写库
        if("master".equals(name)){
            DynamicDataSource.setDataSource(DataSourceNames.MASTAER);
            logger.debug("设置主库数据源： " + ds.name());
        }else{
            Set<String> soureceList = serverWeightMap.keySet();
            List<String> list = new ArrayList<String>();
            list.addAll(soureceList);
            //获取IP的策略
            Random random = new Random();
            int pos = random.nextInt(list.size());
            DynamicDataSource.setDataSource(  list.get(pos));
            logger.debug("设置默认数据源： " +   list.get(pos));
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            logger.debug("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }





}
