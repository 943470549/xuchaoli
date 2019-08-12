package com.mybatis.core.executor;

import com.mybatis.core.mapping.MappedStatement;

import java.util.List;

/**
 * sql语句实际执行类,直接与mysql通信
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
public interface Executor {

    /**
     * query
     * @param ms
     * @param parameter
     * @param <E>
     * @return
     */
    <E> List<E> query(MappedStatement ms, Object parameter);
}
