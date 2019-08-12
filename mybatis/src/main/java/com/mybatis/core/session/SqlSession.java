package com.mybatis.core.session;
import java.util.List;

/**
 * Mapper代理类函数的实际执行类,通过Executor间接与mysql通信
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
public interface SqlSession {

    /**
     * 根据传入条件，进行单一查询
     *
     * @param statement menthod 对应的source+id
     * @param parameter 传入sql语句中的查询参数
     * @return 返回指定结果集
     */
    <T> T selectOne(String statement, Object parameter);


    /**
     * 根据传入条件，进行多个查询
     *
     * @param statement
     * @param parameter
     * @return
     */
    <E> List<E> selectList(String statement, Object parameter);

    Long delete(int id);

    /**
     * 获取Mapper
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    <T> T getMapper(Class<T> clazz) throws IllegalArgumentException, InstantiationException, IllegalAccessException;
}
