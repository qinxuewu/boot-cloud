<!DOCTYPE>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>freemark学习</title>
    <link rel="stylesheet" href="/static/layui/css/layui.css">
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
    <legend>${name}基本组成部分</legend>
</fieldset>
<ul class="layui-timeline">
    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">ftl指令</h3>
            <p>在FreeMarker中,使用FTL标签来使用指令,FreeMarker有3种FTL标签,这和HTML标签是完全类似的.</p>
            <ul>
                 <#--  注释内容 不会显示 -->
                <li>文本：${name}</li>
                <li>int获取：${num}</li>
                <li>boolean获取：${falg?string("yes","no")}</li>
                <li>对 null对象进行取值，可以设置默认值：${str!'我是一个空字符串'} </li>
                <li>对象取值 name:${user.name},age:${user.age} </li>
                <li>转义HTML内容 转义获取: ${htmlStr?html} </li>
            </ul>
        </div>
    </li>

    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">定义变量，支持计算和复制</h3>
        <#--  定义变量 -->
            <#assign count = 100 />
            <ul>
                <li>获取定义的变量：${count}</li>
                <li>计算变量：${count+num},${count+1},${count*2},${count/2}</li>
            </ul>
        </div>
    </li>
    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">遍历List集合</h3>
            <ul>
                <#list  list  as  item>
                    <li>${item!}</li>
                </#list>
            </ul>
        </div>
    </li>
    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">遍历Map</h3>
            <ul>
                 <#list maps?keys as key>
                     <li>${key}:${maps[key]}</li>
                 </#list>
            </ul>
        </div>
    </li>
    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">条件判断指令(if,if else,if else if)</h3>
             <#assign age = 25 />
            <ul>
                <li>
                <#-- gt:大于,lt 小于-->
                      <#if age lte 18>
                          青年
                      <#elseif age == 25>
                          成年
                      <#else>
                          少年
                      </#if>
                </li>
            </ul>
        </div>
    </li>
    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">switch判断指令</h3>
            <ul>
                <li>
                <#switch num>
                  <#case 1>
                         1
                  <#case 2>
                         2
                  <#default>
                         d
                </#switch>
                </li>
            </ul>
        </div>
    </li>
    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">include指令</h3>
            <p>include指令用于导入文件,它可以在模版中插入其他的静态文件,或者是freemarker模版</p>
            <ul>
                <li>path：要包含文件的路径。可以使用相对路径和绝对路径</li>
                <li>options：一个或多个这样的选项(encoding：parse)</li>
                <li>ignore_missing: 算作是布尔值的表达式</li>
                <li> <#include "top.ftl"/>  </li>
            </ul>
        </div>
    </li>
    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">assign指令详解</h3>
            <p> Freemarker中assign指令为该模板页面创建或替换一个顶层变量。</p>

            <#assign h = {"name":"mouse", "price":50, "weight":30}>
            <#-- h?keys 调用了一个包装类的方法，将Map的key取出赋值 -->
            <#assign keys = h?keys>

            <ul>
                <#-- ${h[key]} 访问到了Map中的value属性 -->
                <#list keys as key>
                 <li>  ${key} = ${h[key]};  </li>
                </#list>
            </ul>
        </div>
    </li>
    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">assign指令详解</h3>
            <p> Freemarker中assign指令为该模板页面创建或替换一个顶层变量。</p>

            <#assign h = {"name":"mouse", "price":50, "weight":30}>
        <#-- h?keys 调用了一个包装类的方法，将Map的key取出赋值 -->
            <#assign keys = h?keys>

            <ul>
            <#-- ${h[key]} 访问到了Map中的value属性 -->
                <#list keys as key>
                 <li>  ${key} = ${h[key]};  </li>
                </#list>
            </ul>
        </div>
    </li>

    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">常用内建函数</h3>
            <ul>
                <li>substring ： 截取字符串，包头不包尾（下标</li>
                <li>cap_first :  第一个字母大写 </li>
                <li>end_with  :   以什么字母结尾    </li>
                <li>contains                      是否包含目标字符串</li>
                <li>date  datetime  time          转换成日期格式</li>
                <li>starts_with                   以什么字母开头</li>
                <li>index_of                      返回某个指定的字符串值在字符串中首次出现的位置（下标）</li>
                <li>last_index_of                 获取指定字符出现的最后位置（下标）</li>
                <li>split                         分隔</li>
                <li>trim                          去两端空格</li>
                <li>x?string("0.##")                  变成小数点后几位</li>
                <li>round                             四舍五入</li>
                <li>floor                              去掉小数点</li>
                <li>ceiling                            近1   变成整数</li>
                <li>first:                                取List值第一个值</li>
                <li>last:                                  取List值最后一个值</li>
                <li>seq_contains:                  是否包含指定字符</li>
                <li>seq_index_of:                 指定字符所在位置</li>
                <li>size:                                  集合大小</li>
                <li>reverse:                            集合倒序排列</li>
                <li>sort:                                  对集合进行排序</li>
                <li>sort_by:                           根据某一个属性排序</li>
                <li>chunk:                              分块处理</li>
                <li>is_string:                                      是否为字符类型</li>
                <li>is_number:                                    是否为整数类型</li>
                <li>is_method:                                   是否为方法</li>
                <li>():                                                  判断整个变量</li>
                <li>has_content:                                判断对象是否为空或不存在</li>
                <li>eval：                                           求值</li>
            </ul>
        </div>
    </li>

    <li class="layui-timeline-item">
        <i class="layui-icon layui-timeline-axis"></i>
        <div class="layui-timeline-content layui-text">
            <h3 class="layui-timeline-title">自定义标签使用</h3>

            <ul>
                <@myTag tagId=1 tagName='自定义指令'>
                      <#list data?keys as key>
                            <li>${key}:${data[key]}</li>
                      </#list>
                </@myTag>
            </ul>
        </div>
    </li>
</ul>
<script src="/static/layui/layui.js"></script>
<script>
    //一般直接写在一个js文件中
    layui.use(['layer', 'form'], function(){
        var layer = layui.layer,form = layui.form;

    });
</script>
</body>
</html>