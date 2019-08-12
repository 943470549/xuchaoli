package com.proxy.jdk;

import sun.misc.ProxyGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/3
 **/
interface Subject {
    void say();
}

class MySubject implements Subject {

    public void say() {
        System.out.println("说话.........");
    }
}

class DynamicProxy implements InvocationHandler {

    private Object subject;

    DynamicProxy(Object obj) {
        this.subject = obj;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(subject, args);
        after();
        return result;
    }

    private void before() {
        System.out.println("before--------");
    }

    private void after() {
        System.out.println("after--------");
    }
}
public class JdkProxy {

    private static void createProxyClassFile(Class c) {
        byte[] data = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{c});
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("G:\\learn-workplace\\spring-source-analysis\\myself-proxy\\target\\classes\\com\\proxy\\jdk\\$Proxy0.class");
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        InvocationHandler handler = new DynamicProxy(new MySubject());

        Subject subject = (Subject) Proxy.newProxyInstance(Subject.class.getClassLoader(), new Class[]{Subject.class}, handler);

        subject.say();

        createProxyClassFile(Subject.class);

    }
}
