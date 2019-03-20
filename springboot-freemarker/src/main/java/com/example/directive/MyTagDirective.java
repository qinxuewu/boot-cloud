package com.example.directive;

import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义标签
 * @author qinxuewu
 * @version 1.00
 * @time  19/3/2019 下午 6:57
 * @email 870439570@qq.com
 */
@Component
public class MyTagDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        Integer tagId = -1;
        String tagName = "";
        //自己的逻辑 todo
        Map<String, Object> result = new HashMap<String, Object>(2);
        if (map.containsKey("tagId")){
            tagId = Integer.parseInt(map.get("tagId").toString());
        }

        if (map.containsKey("tagName")){
            tagName = (String)map.get("tagName").toString();
        }

        result.put("tagId", tagId);
        result.put("tagName", tagName);

        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        //此处的myTag就是返回给前端的数据
        environment.setVariable("data", builder.build().wrap(result));
        templateDirectiveBody.render(environment.getOut());
    }
}
