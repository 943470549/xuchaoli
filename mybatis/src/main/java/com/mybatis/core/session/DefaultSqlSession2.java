package com.mybatis.core.session;

import com.mybatis.core.binding.MapperProxy;
import com.mybatis.core.config.Configuration;
import com.mybatis.core.executor.DefaultFiberExecutor;
import com.mybatis.core.executor.Executor;
import com.mybatis.core.mapping.MappedStatement;
import com.mybatis.core.reflection.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mapper代理类函数的实际执行类,通过Executor间接与mysql通信
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
public class DefaultSqlSession2 implements SqlSession {

    private Configuration configuration;

    private Executor excutor;
    private static final ThreadLocal<ConcurrentHashMap<Class, Object>> mapperCache = new ThreadLocal<>();

//    private ConcurrentHashMap<Class, Object> mapperCache = new ConcurrentHashMap<>();

    MapperProxy mapperProxy = new MapperProxy(this);

    public Configuration getConfiguration() {
        return configuration;
    }

    public DefaultSqlSession2(Configuration configuration) {
        super();
        this.configuration = configuration;
        excutor = new DefaultFiberExecutor(configuration);
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<Object> selectList = this.selectList(statement, parameter);
        if (selectList == null || selectList.size() == 0) {
            return null;
        }
        if (selectList.size() == 1) {
            return (T) selectList.get(0);
        } else {
            throw new RuntimeException("to many result!");
        }
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        MappedStatement ms = configuration.getMappedStatements().get(statement);
        return excutor.query(ms, parameter);
    }

    @Override
    public Long delete(int id) {
        return 0L;
    }

    @SuppressWarnings("unchecked")

    @Override
    public <T> T getMapper(Class<T> type) {
        T mapper;
//        System.out.println(Thread.currentThread().getName());
        ConcurrentHashMap<Class, Object> mapperMap = mapperCache.get();
        if (mapperMap == null) {
            mapperCache.set(new ConcurrentHashMap<>());
        } else {
            mapper = (T) mapperMap.get(type);
            if (mapper != null) {
                return mapper;
            }
        }

        //动态的实现类
        String className = configuration.getBasePackagePath() + "." + "$" + type.getSimpleName();
        Class<T> tClass = getMapper(className);

        if (tClass != null) {
            System.out.println("从文件读取 mapper代理");
            try {
                Constructor constructor = tClass.getConstructor(InvocationHandler.class);
                mapper = (T) constructor.newInstance(mapperProxy);
                mapperCache.get().put(type, mapper);
                return mapper;
            } catch (Exception e) {

            }
        }
        mapper = (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, mapperProxy);
        ReflectionUtil.createProxyClassFile(configuration.getBasePackagePath(), type, "$" + type.getSimpleName());
        mapperCache.get().put(type, mapper);
        return mapper;
    }

    private <T> Class<T> getMapper(String name) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class $myProxy0 = loader.loadClass(name);
            return $myProxy0;
        } catch (Exception e) {
        }
        return null;
    }

}