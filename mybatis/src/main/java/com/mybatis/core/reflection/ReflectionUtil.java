package com.mybatis.core.reflection;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
public class ReflectionUtil {

    public static void setProToBeanFromResult(Object entity, ResultSet resultSet) throws SQLException {
        Field[] decfileds = entity.getClass().getDeclaredFields();
        for (int i = 0; i < decfileds.length; i++) {
            if(decfileds[i].getType().getSimpleName().equals("String")) {
                setProToBean(entity,decfileds[i].getName(),resultSet.getString(decfileds[i].getName()));
            }else if(decfileds[i].getType().getSimpleName().equals("Long")) {
                setProToBean(entity,decfileds[i].getName(),resultSet.getLong(decfileds[i].getName()));
            }else if(decfileds[i].getType().getSimpleName().equals("int")) {
                setProToBean(entity,decfileds[i].getName(),resultSet.getInt(decfileds[i].getName()));
            }
        }
    }

    private static void setProToBean(Object bean, String name, Object value) {
        Field field;
        try {
            field = bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createProxyClassFile(String packageName, Class c, String name) {

        byte[] data = ProxyGenerator.generateProxyClass(packageName+"."+name, new Class[]{c});
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(
                    getRealRuntimePath(packageName)+"\\"+name+".class");
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRealRuntimePath(String packageName) {
        try{
            URL uri = Thread.currentThread().getContextClassLoader().getResource("");
            String path = Paths.get(uri.toURI()).resolve(packageName.replaceAll("\\.", "\\\\")).toString();
            return path;
        }catch (Exception e){
            return null;
        }

    }
}
