package com.example.task;

import com.alibaba.fastjson.JSONObject;
import com.example.dao.EsDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 定时任务
 *
 * @author qinxuewu
 * @version 1.00
 * @time 10/4/2019 下午 8:02
 * @email 870439570@qq.com
 */
@Component
public class SaticScheduleTask {
    private static final Logger LOG = LoggerFactory.getLogger(EsDao.class);
    @Autowired
    private EsDao esDao;


    /**
     * 定时任务 直接指定时间间隔
     */
    @Scheduled(fixedRate = 800)
    private void task() {
        LOG.info("开始爬取电影天堂.................");
        //设置需要下载多少页
        int page = 1000;
        try {
            for (int i = 1; i <= page; i++) {
                pachong_page("http://www.dytt8.net/html/gndy/dyzz/list_23_" + i + ".html");
            }
        } catch (IOException ex) {

        }

    }


    public void pachong_page(String url) throws IOException {
        //模拟火狐浏览器
        Document doc = Jsoup.connect(url).userAgent("Mozilla").get();

        //这里根据在网页中分析的类选择器来获取电影列表所在的节点
        Elements div = doc.getElementsByClass("co_content8");
        //获取电影列表  查找table标签
        Elements table = div.select("table");
        //获取电影列表总数
        int result = table.size();

        System.out.println("电影列表数为:" + result);
        for (Element tb : table) {
            try {
                //让线程操作不要太快 1秒一次 时间自己设置，主要是模拟人在点击
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //获取所有电影详情的链接所在的节点
            Elements tr = tb.select("tr");
            //获取电影列表链接和标题
            String videos = tr.get(1).select("a").attr("abs:href");

            //这里要跳过这个首页页面 否则会抛出异常
            if ("http://www.dytt8.net/html/gndy/jddy/index.html".equals(videos)) {
                continue;
            }


            String title = tr.get(1).select("a").text();
            //进如电影列表详情页面
            doc = Jsoup.connect(videos).userAgent("Mozilla").get();
            //获取到电影详情页面所在的节点
            Element div1 = doc.getElementById("Zoom");
            //获取电影描述
            String des = div1.select("p").text();
            //获取封面图地址
            Elements select = div1.select("img[src$=.jpg]");
            String imgUrl = select.get(0).attr("abs:src");
            //获取下载地址
            String downUrl = div1.select("td").text();

            String videoUrl = div1.select("td").text();
            //创建索引 必须为小写
            String index = "dytt8";
            String type = "movie_list";

            JSONObject obj = new JSONObject();
            obj.put("title", title);
            obj.put("imgUrl", imgUrl);

            obj.put("videoUrl", videoUrl);
            obj.put("describe", des);
            obj.put("downUrl", downUrl);
            esDao.createIndexOne(index, type, obj);
            LOG.info("标题={},imgUrl={},videoUrl={}", title, imgUrl, videoUrl);

        }

    }
}
