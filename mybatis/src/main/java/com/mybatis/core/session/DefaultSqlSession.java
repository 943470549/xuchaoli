package com.mybatis.core.session;

import com.mybatis.core.binding.MapperProxy;
import com.mybatis.core.config.Configuration;
import com.mybatis.core.mapping.MappedStatement;
import com.mybatis.core.executor.DefaultExecutor;
import com.mybatis.core.executor.Executor;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Mapper代理类函数的实际执行类,通过Executor间接与mysql通信
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor excutor;

    public Configuration getConfiguration() {
        return configuration;
    }

    public DefaultSqlSession(Configuration configuration) {
        super();
        this.configuration = configuration;
        excutor = new DefaultExecutor(configuration);
    }

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        super();
        this.configuration = configuration;
        excutor = executor == null ?  new DefaultExecutor(configuration):executor;
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
        List<E> list =  excutor.query(ms, parameter);
        return list;
    }

    @Override
    public Long delete(int id) {
        return 0L;
    }

    @SuppressWarnings("unchecked")

    @Override
    public <T> T getMapper(Class<T> type) {
        MapperProxy mapperProxy = new MapperProxy(this);
        //动态的实现类
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, mapperProxy);
    }

}