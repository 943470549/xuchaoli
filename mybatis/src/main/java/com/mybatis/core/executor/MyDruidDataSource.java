package com.mybatis.core.executor;



import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MyDruidDataSource {
    private static com.alibaba.druid.pool.DruidDataSource dataSource;
    static {
        try {
            InputStream inputStream = MyDruidDataSource.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            dataSource = new com.alibaba.druid.pool.DruidDataSource();
            String jdbcUrl = properties.getProperty("jdbc-url");
            String driverClass = properties.getProperty("driver-class");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String basePkgPath = properties.getProperty("base-package-path");
            dataSource.setDriverClassName(driverClass);
            dataSource.setUrl(jdbcUrl);

            dataSource.setUsername(username);

            dataSource.setPassword(password);

            dataSource.setMaxActive(Integer.parseInt("1000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void release(Connection conn, Statement st, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


