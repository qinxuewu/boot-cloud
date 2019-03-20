package com.example.config;
import com.example.directive.MyTagDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 * 配置类
 * @author qinxuewu
 * @version 1.00
 * @time  19/3/2019 下午 6:39
 * @email 870439570@qq.com
 */
@Component
public class FreeMarkerConfig {
    @Autowired
    private freemarker.template.Configuration configuration;

    @Autowired
    private MyTagDirective myTagDirective;

    /**
     * 添加自定义标签
     */
    @PostConstruct
    public void setSharedVariable() {
        /*
         * 向freemarker配置中添加共享变量;
         * 它使用Configurable.getObjectWrapper()来包装值，因此在此之前设置对象包装器是很重要的。（即上一步的builder.build().wrap操作）
         * 这种方法不是线程安全的;使用它的限制与那些修改设置值的限制相同。
         * 如果使用这种配置从多个线程运行模板，那么附加的值应该是线程安全的。
         */
        configuration.setSharedVariable("myTag", myTagDirective);
    }
}
