package com.github.cache.rebuild;


import com.github.cache.model.ProductInfo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 功能描述: 重建缓存的内存队列
 * @author: qinxuewu
 * @date: 2019/11/7 16:31
 * @since 1.0.0
 */
public class RebuildCacheQueue {
    private ArrayBlockingQueue<ProductInfo> queue=new ArrayBlockingQueue<>(1000);

    /**
     * 添加商品更新任务到队列
     * @param info
     */
    public  void  putProductInfo(ProductInfo info){
        try{
            queue.put(info);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public ProductInfo takeProductInfo() {
        try {
            return queue.take();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 静态内部类单例模式
     */
    private  static  class Singleton{
        private  static  RebuildCacheQueue instance;
        static {
            instance=new RebuildCacheQueue();
        }
        public  static  RebuildCacheQueue getInstance(){
            return instance;
        }
    }

    public static RebuildCacheQueue getInstance(){
        return Singleton.getInstance();
    }

    public static void init() {
        getInstance();
    }
}
