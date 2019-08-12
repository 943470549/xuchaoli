package com.mybatis.core.session;


import com.mybatis.core.annotation.Mapper;
import com.mybatis.core.annotation.Select;
import com.mybatis.core.config.Configuration;
import com.mybatis.core.executor.Executor;
import com.mybatis.core.mapping.MappedStatement;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * 1：把配置文件加载到内存，即：把他们放到configuration类对象中
 * 2:生产sqlsession
 */
public class SqlSessionFactory {

    private Configuration configuration = new Configuration();

    public SqlSessionFactory() {
        //1、加载数据库信息
        loadConfig();
        //2、加载xml mapper信息
        loadXmlMapper();
        // 3、加载annotation mapper信息
        loadAnnotationMapper();
    }

    /**
     * 加载数据库配置信息
     */
    private void loadConfig() {
        InputStream inputStream = SqlSessionFactory.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            String jdbcUrl = properties.getProperty("jdbc-url");
            String driverClass = properties.getProperty("driver-class");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String basePkgPath = properties.getProperty("base-package-path");
            configuration.setJdbcDriver(driverClass);
            configuration.setJdbcUrl(jdbcUrl);
            configuration.setJdbcUsername(username);
            configuration.setJdbcPassword(password);
            configuration.setBasePackagePath(basePkgPath);
        } catch (Exception e) {
        }
    }

    /**
     * 加载xml配置文件信息
     */
    private void loadXmlMapper() {
        URL resources = SqlSessionFactory.class.getClassLoader().getResource("mapper");
        File mappers = new File(resources.getFile());
        if (mappers.isDirectory()) {
            File[] files = mappers.listFiles();
            for (File file : files) {
                loadMethodStatement(file);
            }
        }
    }

    private void loadAnnotationMapper() {
        String targetPath = configuration.getBasePackagePath().replaceAll("\\.", "/") + "/";
        URL classes = this.getClass().getClassLoader().getResource(targetPath);
        File rootMappers = new File(classes.getFile());
        if(rootMappers.isDirectory()) {
            File[] files = rootMappers.listFiles((dir, filename) -> !filename.startsWith("$") && filename.endsWith(".class"));
            for (File file: files) {
                String targetName = configuration.getBasePackagePath() + "." + file.getName().replace(".class", "");

                try {
                    Class clazz = Class.forName(targetName);
                    if(clazz.isAnnotationPresent(Mapper.class)) {
                        Method[] methods = clazz.getDeclaredMethods();
                        for(Method method: methods) {
                            if(method.isAnnotationPresent(Select.class)) {
                                Select select = method.getAnnotation(Select.class);
                                MappedStatement mappedStatement = new MappedStatement();
                                String resultMap = method.getReturnType().getName();
                                String sql = select.value();
                                String sourceId = targetName + "." + method.getName();
                                mappedStatement.setSourceId(sourceId);
                                mappedStatement.setResultMap(resultMap);
                                mappedStatement.setSql(sql);
                                mappedStatement.setNamespace(targetName);
                                configuration.getMappedStatements().put(sourceId, mappedStatement);
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 加载xml,mapper映射关系
     *
     * @param file
     */
    private void loadMethodStatement(File file) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(file);
            System.out.println(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element node = document.getRootElement();
        String namespace = node.attribute("namespace").getData().toString();
        List<Element> selects = node.elements("select");
        for (Element element : selects) {
            MappedStatement mappedStatement = new MappedStatement();
            String id = element.attribute("id").getData().toString();
            String resultMap = element.attribute("resultMap").getData().toString();
            String sql = element.getData().toString();
            String sourceId = namespace + "." + id;
            mappedStatement.setSourceId(sourceId);
            mappedStatement.setResultMap(resultMap);
            mappedStatement.setSql(sql);
            mappedStatement.setNamespace(namespace);
            configuration.getMappedStatements().put(sourceId, mappedStatement);
        }
    }

    public SqlSession openSqlSession() {
        return new DefaultSqlSession(configuration);
    }
    public SqlSession openSqlSession(Executor executor) {
        return new DefaultSqlSession(configuration, executor);
    }
}
