package com.github.inventory.service.impl;
import com.github.inventory.mapper.ProductInventoryMapper;
import com.github.inventory.model.ProductInventory;
import com.github.inventory.service.ProductInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;

/**
 * 功能描述:  商品库存Service实现类
 * @author: qinxuewu
 * @date: 2019/11/1 10:49
 * @since 1.0.0 
 */
@Service("productInventoryService")
public class ProductInventoryServiceImpl  implements ProductInventoryService {
    private static final Logger log = LoggerFactory.getLogger(ProductInventoryServiceImpl.class);

    @Resource
    private ProductInventoryMapper productInventoryMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
        log.info("已修改数据库中的库存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisTemplate.delete(key);
        log.info("已删除redis中的缓存，key=" + key);
    }

    /**
     * 根据商品id查询商品库存
     * @param productId 商品id
     * @return 商品库存
     */
    @Override
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    /**
     * 设置商品库存的缓存
     * @param productInventory 商品库存
     */
    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisTemplate.opsForValue().set(key, String.valueOf(productInventory.getInventoryCnt()));
        log.info("已更新商品库存的缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt() + ", key=" + key);
    }


    /**
     * 获取商品库存的缓存
     * @param productId
     * @return
     */
    @Override
    public ProductInventory getProductInventoryCache(Integer productId) {
        Long inventoryCnt = 0L;
        String key = "product:inventory:" + productId;
        String result = redisTemplate.opsForValue().get(key);
        if(!StringUtils.isEmpty(result)) {
            try {
                inventoryCnt = Long.valueOf(result);
                return new ProductInventory(productId, inventoryCnt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
