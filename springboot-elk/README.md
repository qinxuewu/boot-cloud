### docker安装elk
```
# 查询镜像
docker ssearch elk
#下载镜像
docker pull sebp/elk

#启动镜像 , 指定es的内存
docker run -e ES_JAVA_OPTS="-Xms256m -Xmx256m" -p 5601:5601 -p 5044:5044 -p 9200:9200 -p 9300:9300 -it --name elk 356hsf12(镜像ID)

#通过exec命令进入容器
docker exec -it elk /bin/bash

# 进入容器后，修改 /etc/logstash/conf.d/02-beats-input.conf
input {
    tcp {
        port => 5044
        codec => json_lines

    }

}
output{
    elasticsearch {
    hosts => ["localhost:9200"]

    }

}

# 重启
docker restart elk
访问http://127.0.0.1:5601
```