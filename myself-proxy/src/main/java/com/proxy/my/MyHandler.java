package com.proxy.my;

import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/3
 **/
public class MyHandler implements MyInvocationHandler {

    private Object obj;

    MyHandler(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before..............");
        method.invoke(obj, args);
        System.out.println("after...............");
        return null;
    }
}
