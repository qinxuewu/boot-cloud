package com.github.inventory.request;


import com.github.inventory.model.ProductInventory;
import com.github.inventory.service.ProductInventoryService;
import com.github.inventory.service.impl.ProductInventoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述: 重新加载商品库存的缓存
 * @author: qinxuewu
 * @date: 2019/11/1 10:39
 * @since 1.0.0
 */
public class ProductInventoryCacheRefreshRequest  implements Request {

    private static final Logger log = LoggerFactory.getLogger(ProductInventoryCacheRefreshRequest.class);

    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 商品库存Service
     */
    private ProductInventoryService productInventoryService;

    /**
     * 是否强制刷新缓存
     */
    private boolean forceRefresh;
    public ProductInventoryCacheRefreshRequest(Integer productId,
                 ProductInventoryService productInventoryService,
                  boolean forceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process() {
        // 从数据库中查询最新的商品库存数量
        ProductInventory productInventory = productInventoryService.findProductInventory(productId);
        log.info("已查询到商品最新的库存数量，商品id=" + productId + ", 商品库存数量=" + productInventory.getInventoryCnt());
        // 将最新的商品库存数量，刷新到redis缓存中去
        productInventoryService.setProductInventoryCache(productInventory);
    }

    @Override
    public Integer getProductId() {
        return productId;
    }

    @Override
    public boolean isForceRefresh() {
        return forceRefresh;
    }
}
