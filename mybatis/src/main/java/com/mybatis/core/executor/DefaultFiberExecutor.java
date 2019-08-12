package com.mybatis.core.executor;


import com.mybatis.core.config.Configuration;
import com.mybatis.core.mapping.MappedStatement;
import com.mybatis.core.reflection.ReflectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * sql语句实际执行类,直接与mysql通信
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
public class DefaultFiberExecutor implements Executor {

    private Configuration configuration;

    public DefaultFiberExecutor(Configuration configuration) {
        super();
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter) {
        List<E> ret = new ArrayList<E>();

//        List<E> finalRet = ret;
//        ret = execFiber(()-> {
//                PreparedStatement preparedStatement = null;
//                ResultSet resultSet = null;
//                Connection connection =null;
//                try {
//                    connection = MyDruidDataSource.getConnection();
//                    Thread.sleep(100);
////                    System.out.println(Thread.currentThread().getName());
//                    preparedStatement = connection.prepareStatement(ms.getSql());
//                    // 构造形参
//                    parameterized(preparedStatement, parameter);
//                    resultSet = preparedStatement.executeQuery();
//                    handlerResultSet(resultSet, finalRet,ms.getResultMap());
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }finally {
//                    try {
//                        resultSet.close();
//                        preparedStatement.close();
//                        connection.close();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return finalRet;
//        });
        return ret;
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(configuration.getJdbcDriver());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(configuration.getJdbcUrl(), configuration.getJdbcUsername(), configuration.getJdbcPassword());
        } catch (Exception e) {

        }
        return connection;
    }

//    private <T> T execFiber(SuspendableCallable callable) {
//        Fiber fiber = new Fiber<T>(callable);
//        fiber.start();
//        T resyult = null;
//        try {
//            resyult = (T) fiber.get();
//        } catch (Exception e) {
//        }
//        return resyult;
//    }


    @SuppressWarnings("unchecked")
    private <E> void handlerResultSet(ResultSet resultSet, List<E> ret, String resultMap) {
        Class<E> clazz = null;
        try {
            clazz = (Class<E>) Class.forName(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            while (resultSet.next()) {
                Object entity = clazz.newInstance();
                ReflectionUtil.setProToBeanFromResult(entity, resultSet);
                ret.add((E) entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parameterized(PreparedStatement preparedStatement, Object parameter) throws SQLException {
        if (parameter instanceof Integer) {
            preparedStatement.setInt(1, (Integer) parameter);
        } else if (parameter instanceof Long) {
            preparedStatement.setLong(1, (Long) parameter);
        } else if (parameter instanceof String) {
            preparedStatement.setString(1, (String) parameter);
            System.out.println(preparedStatement);
        }
    }

}
