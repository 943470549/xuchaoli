package com.mybatis.core.binding;

import com.mybatis.core.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Mapper动态代理InvocationHandler
 */
public class MapperProxy implements InvocationHandler {
	
	private SqlSession sqlsession;

	public MapperProxy(SqlSession sqlsession) {
		super();
		this.sqlsession = sqlsession;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?>  returnType = method.getReturnType();
		//判断这个类是不是其子类
		if(Collection.class.isAssignableFrom(returnType)) {
			return sqlsession.selectList(method.getDeclaringClass().getName()+"."+method.getName(), args==null?null:args[0]);
		}else {
			Object a= sqlsession.selectOne(method.getDeclaringClass().getName()+"."+method.getName(), args==null?null:args[0]);
			return a;
		}
	}
}
