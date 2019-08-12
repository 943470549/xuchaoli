package com.proxy.my;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/3
 **/
public interface MyInvocationHandler extends InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
