package com.qxw.webmvc.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qxw.webmvc.annotaion.QAutowired;
import com.qxw.webmvc.annotaion.QController;
import com.qxw.webmvc.annotaion.QRequestMapping;
import com.qxw.webmvc.annotaion.QRequestParam;
import com.qxw.webmvc.annotaion.QServices;
import com.qxw.webmvc.bean.Handler;
import com.qxw.webmvc.bean.View;


/**
 * 手写spring  中央控制器类
 *
 * @author qxw
 * @data 2018年6月29日下午12:48:56
 */
public class QDispatcherServlet extends HttpServlet {
    private static final String fmt = "%24s:   %s\n";
    /**
     * 跳转方式 请求转发/重定向
     */
    public final static String FORWARD = "forward";
    public final static String REDIRECT = "redirect";

    private static final long serialVersionUID = 8611565670636763991L;
    /**
     * 配置文件初始化
     */
    private Properties contexConfig = new Properties();
    /**
     * classNames集合
     */
    private List<String> classNames = new ArrayList<String>();

    /**
     * ioc容器
     */
    private Map<String, Object> ioc = new HashMap<String, Object>();

    /**
     * HandlerMapping处理器映射器
     */
    private Map<String, Handler> handlerMapping = new HashMap<String, Handler>();


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("doPost:  请求执行");
        //6  等待请求
        doDispatch(req, resp);

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURI();
        //处理成相对路径
        String contextPath = req.getContextPath();
        System.out.println("getRequestURI " + req.getRequestURI());
        System.out.println("getRemoteAddr " + req.getRemoteAddr());
        System.out.println("url:  " + url);
        System.out.println("contextPath:  " + contextPath);


        Map<String, String[]> param = req.getParameterMap();
//		  Set<Entry<String, String[]>> itermap=param.entrySet();
//		  for (Entry<String, String[]> entry : itermap) {
//	            String key = entry.getKey();
//	            String[] value = entry.getValue();
//	            for (int i = 0; i < value.length; i++) {
//				      System.out.println(value[i]);
//			     }
//
//	        }
        System.out.println("param:  " + param.toString());

        url = url.replace(contextPath, "").replaceAll("//", "/");
        if (!handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 not found ");
        }
        Handler m = handlerMapping.get(url);
        System.out.println("方法存在----Method:  " + m);
        if (m != null) {
            //执行url对应的方法,处理用户请求
            if (m.getMethod() != null) {
                Object retObject = null;
                //利用反射执行这个方法
                try {
                    Class<?>[] parameterTypes = m.getMethod().getParameterTypes();
                    //获取请求的参数
                    Map<String, String[]> parameterMap = req.getParameterMap();
                    //保存参数值
                    Object[] paramValues = new Object[parameterTypes.length];
                    //方法的参数列表
                    for (int i = 0; i < parameterTypes.length; i++) {
                        //根据参数名称，做某些处理
                        String requestParam = parameterTypes[i].getSimpleName();
                        if (requestParam.equals("HttpServletRequest")) {
                            //参数类型已明确，这边强转类型
                            paramValues[i] = req;
                            continue;
                        }
                        if (requestParam.equals("HttpServletResponse")) {
                            paramValues[i] = resp;
                            continue;
                        }
                        if (requestParam.equals("String")) {
                            for (Entry<String, String[]> param : parameterMap.entrySet()) {
                                String value = Arrays.toString(param.getValue()).replaceAll("[|]", "").replaceAll(",s", ",");
                                paramValues[i] = value;
                            }
                        }
                    }

//                    //获取方法上的的参数类型
//                    Object[] args = new Object[]{parameterTypes.length};
//                    for (int i = 0; i < parameterTypes.length; i++) {
//                        System.out.format(fmt, "ParameterType", parameterTypes[i]);
//                        if (parameterTypes[i].getName().equals("javax.servlet.http.HttpServletRequest")) {
//                            args[i] = req;
//                        } else if (parameterTypes[i].getName().equals("javax.servlet.http.HttpServletResponse")) {
//                            args[i] = resp;
//                        } else {
//                            System.out.println(parameterTypes[i].getName());
//                            System.out.println(parameterTypes[i].getTypeName());
//                        }
//                    }
                    //利用反射机制来调用
                    retObject = m.getMethod().invoke(m.getObject(), paramValues);
                    //如果有返回值,就代表用户需要返回视图
                    if (retObject != null) {
                        View view = (View) retObject;
                        //判断要使用的跳转方式
                        if (view.getDispathType().equals(FORWARD)) {
                            //使用服务器端跳转方式
                            req.getRequestDispatcher(view.getUrl()).forward(req, resp);

                        } else if (view.getDispathType().equals(REDIRECT)) {
                            //使用客户端跳转方式
                            resp.sendRedirect(req.getContextPath() + view.getUrl());
                        } else {
                            req.getRequestDispatcher(view.getUrl()).forward(req, resp);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("-----------手撸spring   QDispatcherServlet  init  初始化成功-------------------------------------------");

        try {
            //1 加载配置文件 web.xml中的contextConfigLocation 拿到配置文件名称
            doLoadConfig(config.getInitParameter("contextConfigLocation"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //2 扫描所有相关的类
        //通过contexConfig拿到配置文件的key 获取到包的名称
        doScanner(contexConfig.getProperty("scanPackage"));

        //3 初始化扫描到的所有类
        doInstance();

        //4 实现依赖注入
        doAutowired();


        //5  初始化HandlerMapping处理器映射器
        initHandlerMapping();

    }


    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        //对controller进行处理
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            //过滤没有加QController注解的类
            if (!clazz.isAnnotationPresent(QController.class)) {
                continue;
            }
            String baseUrl = "";
            if (clazz.isAnnotationPresent(QRequestMapping.class)) {
                QRequestMapping requestMapping = clazz.getAnnotation(QRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            Object action = null;
            try {
                //初始化
                action = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //拼接url
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(QRequestMapping.class)) {
                    QRequestMapping requestMapping = method.getAnnotation(QRequestMapping.class);
                    String url = requestMapping.value();
                    url = baseUrl + url;
                    Handler h = new Handler();
                    h.setUrl(url);
                    h.setMethod(method);
                    h.setObject(action);
                    handlerMapping.put(url, h);
                    System.out.println("Mapped ：" + url + ",  " + method);
                }
            }
        }
    }


    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();

            //遍历fields
            for (Field f : fields) {
                //授权,只要是加了QAutowied的字段
                if (f.isAnnotationPresent(QAutowired.class)) {
                    QAutowired autowired = f.getAnnotation(QAutowired.class);
                    String beanName = autowired.value().trim();
                    //如果注解上的值为空,使用默认的
                    if ("".equals(beanName.trim())) {
                        beanName = f.getType().getName();
                    }

                    //真正授权
                    f.setAccessible(true);

                    //赋值
                    try {
                        f.set(entry.getValue(), ioc.get(beanName));
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }

        }
    }


    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        try {
            //循环classNames
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                //初始所有有注解的类
                if (clazz.isAnnotationPresent(QController.class)) {
                    //初始化
                    Object obj = clazz.newInstance();

                    //key默认类名首字母小写
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    ioc.put(beanName, obj);

                } else if (clazz.isAnnotationPresent(QServices.class)) {
                    /**
                     * 1：默认类名首字母小写,
                     * 2：如果是接口要把实现类赋值给它
                     * 3：如果注解上有自定义的名字，优先使用自定义的名称
                     */
                    QServices service = clazz.getAnnotation(QServices.class);
                    //注解上的值
                    String beanName = service.value();
                    //如果注解上的值为空，优先只类名首字母小写
                    if ("".equals(beanName.trim())) {
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }
                    //初始化
                    Object instance = clazz.newInstance();
                    //放入ioc容器
                    ioc.put(beanName, instance);

                    //如果是接口,解决子类引用赋值给父类的问题
                    Class<?>[] instances = clazz.getInterfaces();
                    for (Class<?> i : instances) {
                        ioc.put(i.getCanonicalName(), instance);
                    }

                } else {

                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void doScanner(String scanPackage) {
        /**
         * 获取到配置文件定义的包名后，递归获取包下所有的文件
         * 然后把每个文件的.class文件 最后通过反射进行初始化
         */

        //把所有的.替换成一个/拿到文件路径的url
        URL url = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                //是一个文件夹 就进行递归
                doScanner(scanPackage + "." + file.getName());
            } else {
                //不是文件夹，拿到file名称把后缀.calss去掉  得到className
                String className = scanPackage + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }
    }


    private void doLoadConfig(String contextConfigLocation) throws IOException {
        //根据配置文件的名称 取得输入流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contexConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                is.close();
            }
        }
    }

    /**
     * 类名首字母小写
     *
     * @param str
     * @return
     */

    private String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }


}
