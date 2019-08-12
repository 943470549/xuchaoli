package com.spring.boot.servlet;

import com.spring.boot.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/20 16:47.
 **/
@WebServlet(name = "DispatcherServlet", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> beans = new HashMap<>();

    private Map<String, Method> urlMapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        scanBasePackage("com.spring.boot");

        doInstances();

        doIOC();

        doUrlMapping();

    }

    private void doInstances() {
        if (classNames.size() <= 0) {
            System.out.println("包扫描失败!!!");
            return;
        }

        for (String className : classNames) {
            String cn = className.replace(".class", "");
            try {
                Class<?> clazz = Class.forName(cn);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    Object bean = clazz.newInstance();
                    RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
                    beans.put(mapping.value(), bean);
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    Object instance = clazz.newInstance();
                    beans.put(service.value(), instance);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void doIOC() {
        if (beans.size() <= 0) {
            System.out.println("bean实例化失败!!!");
            return;
        }

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    field.setAccessible(true);
                    try {
                        field.set(instance, beans.get(autowired.value()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void doUrlMapping() {

        if (beans.size() <= 0) {
            System.out.println("bean实例化失败!!!");
            return;
        }

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String beanMapping = entry.getKey();
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                    String mapping = beanMapping + methodMapping.value();
                    urlMapping.put(mapping, method);
                }
            }
        }
    }

    private void scanBasePackage(String basePackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + basePackage.replaceAll("\\.", "/") + "/");
        String rootPath = url.getFile();
        File root = new File(rootPath);
        String[] files = root.list();
        for (String path : files) {
            File file = new File(rootPath + path);
            if (file.isDirectory()) {
                scanBasePackage(basePackage + "." + path);
            } else {
                classNames.add(basePackage + "." + file.getName());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String context = req.getContextPath();
        String path = uri.replace(context, "");
        Method method = urlMapping.get(path);
        Objects.requireNonNull(method, "url地址不存在!!!");
        Object bean = beans.get("/" + path.split("/")[1]);
        Object[] args = handleArgs(req, resp, method);
        try {
            method.invoke(bean, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Object[] handleArgs(ServletRequest req, ServletResponse resp, Method method) {

        Class<?>[] classes = method.getParameterTypes();

        Object[] args = new Object[classes.length];

        int argIndex = 0;

        int index = 0;

        for (Class<?> clazz : classes) {
            if (ServletRequest.class.isAssignableFrom(clazz)) {
                args[argIndex++] = req;
            }

            if (ServletResponse.class.isAssignableFrom(clazz)) {
                args[argIndex++] = resp;
            }

            Annotation[][] paramAnAns = method.getParameterAnnotations();

            Annotation[] paramAns = method.getParameterAnnotations()[index];
            if (paramAns.length > 0) {
                for (Annotation annotation : paramAns) {
                    if (RequestParam.class.isAssignableFrom(annotation.getClass())) {
                        RequestParam requestParam = (RequestParam) annotation;
                        args[argIndex++] = req.getParameter(requestParam.value());
                    }
                }
            }
            index++;
        }
        return args;
    }
}
