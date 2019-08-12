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
public class DefaultExecutor implements Executor {

    private Configuration configuration;

    public DefaultExecutor(Configuration configuration) {
        super();
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter) {
        List<E> ret = new ArrayList<E>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = MyDruidDataSource.getConnection();
            preparedStatement = connection.prepareStatement(ms.getSql());
            // 构造形参
            parameterized(preparedStatement, parameter);
            resultSet = preparedStatement.executeQuery();
            handlerResultSet(resultSet,ret,ms.getResultMap());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    @SuppressWarnings("unchecked")
    private<E> void handlerResultSet(ResultSet resultSet, List<E> ret, String resultMap) {
        Class<E> clazz = null;
        try {
            clazz = (Class<E>) Class.forName(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            while(resultSet.next()) {
                Object entity = clazz.newInstance();
                ReflectionUtil.setProToBeanFromResult(entity,resultSet);
                ret.add((E)entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parameterized(PreparedStatement preparedStatement, Object parameter) throws SQLException {
        if(parameter instanceof Integer) {
            preparedStatement.setInt(1, (Integer) parameter);
        }else if(parameter instanceof Long) {
            preparedStatement.setLong(1, (Long) parameter);
        }else if(parameter instanceof String) {
            preparedStatement.setString(1, (String) parameter);
            System.out.println(preparedStatement);
        }
    }

}
