package com.platform.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

    /**
     * Description:取得当前类所在的文件路径
     *
     * @param clazz
     * @return
     */
    public static File getClassFilePath(Class clazz) {
        URL path = clazz.getResource(clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1) + ".class");
        if (path == null) {
            String name = clazz.getName().replaceAll("[.]", "/");
            path = clazz.getResource("/" + name + ".class");
        }
        return new File(path.getFile());
    }

    /**
     * Description:同getClassFile 解决中文编码问题
     *
     * @param clazz
     * @return
     */
    public static String getClassFilePathChinese(Class clazz) {
        try {
            return java.net.URLDecoder.decode(getClassFilePath(clazz).getAbsolutePath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Description:取得当前类所在的ClassPath目录
     *
     * @param clazz
     * @return
     */
    public static File getClassPath(Class clazz) {
        File file = getClassFilePath(clazz);
        for (int i = 0, count = clazz.getName().split("[.]").length; i < count; i++) {
            file = file.getParentFile();
        }
        if (file.getName().toUpperCase().endsWith(".JAR!")) {
            file = file.getParentFile();
        }
        return file;
    }

    /**
     * Description: 同getClassPathFile 解决中文编码问题
     *
     * @param clazz
     * @return
     */
    public static String getClassPathChinese(Class clazz) {
        try {
            return java.net.URLDecoder.decode(getClassPath(clazz)
                    .getAbsolutePath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * getClassLoader获取bin根目录下文件 classPath路径
     *
     * @param clazz
     * @param fileRelativePath 相对路径（相对classpath路径）
     * @throws URISyntaxException
     */
    public static InputStream getFileByClassLoader(Class clazz,
                                                   String fileRelativePath) {
        try {
            // 获取classpath路径
            System.out.println(clazz.getClassLoader().getResource("").toURI().getPath());
            // System.out.println(clazz.getClassLoader().getResource("/").toURI().getPath());不可用
            // 抛异常
            System.out.println(clazz.getClassLoader().getResource(fileRelativePath).toURI().getPath());
        } catch (URISyntaxException ex) {

        }

        return clazz.getClassLoader().getResourceAsStream(fileRelativePath);

    }

    /**
     * 只能获取与当前class 文件同一个文件夹内的文件
     *
     * @param clazz
     * @param fileRelativePath 相对路径
     */
    public static InputStream getFileByResource(Class clazz, String fileRelativePath) {
        // 获取当前类所属文件夹路径
        System.out.println(clazz.getResource("").toString());
        // 获取classpath路径
        System.out.println(clazz.getResource("/").toString());
        // 获取相对当前类路径的文件路径
        System.out.println(clazz.getResource(fileRelativePath).getPath());

        return clazz.getResourceAsStream(fileRelativePath);
    }

    /**
     * 获取当前工程所在路径 G:\workplaces\eclipse_x64_workplace\DocumentHelper
     */
    public static String getSystemProperties() {

        System.out.println(System.getProperty("user.dir"));

        return System.getProperty("user.dir");
    }

    /**
     * 获取工程所有引用jar包的路径（中间以；隔开，包含classpath路径）
     *
     * @return
     */
    public static String getProjectJarPath() {

        System.out.println(System.getProperty("java.class.path"));

        return System.getProperty("java.class.path");

    }

    /**
     * 获取指定包下class列表
     *
     * @param pkgName     包名
     * @param isRecursive 标识是否要遍历该包路径下子包的类名
     * @param annotation  注释
     * @return
     */
    public static List<Class<?>> getClassList(String pkgName, boolean isRecursive, Class<? extends Annotation> annotation) {
        List<Class<?>> classList = new ArrayList<Class<?>>();

        //获取类加载器
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            // 按文件的形式去查找
            String strFile = pkgName.replaceAll("\\.", "/");
            Enumeration<URL> urls = loader.getResources(strFile);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    String pkgPath = url.getPath();
                    System.out.println("protocol:" + protocol + " pkgName:" + pkgName + " path:"
                            + pkgPath);
                    if ("file".equals(protocol)) {
                        // 本地自己可见的代码
                        findClassName(classList, pkgName, pkgPath, isRecursive,
                                annotation);
                    } else if ("jar".equals(protocol)) {
                        // 引用第三方jar的代码
                        findClassName(classList, pkgName, url, isRecursive,
                                annotation);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }

    /**
     * 本地class文件遍历
     *
     * @param clazzList
     * @param pkgName
     * @param pkgPath
     * @param isRecursive
     * @param annotation
     */
    public static void findClassName(List<Class<?>> clazzList, String pkgName,
                                     String pkgPath, boolean isRecursive,
                                     Class<? extends Annotation> annotation) {
        if (clazzList == null) {
            return;
        }
        // 过滤出.class文件及文件夹
        File[] files = filterClassFiles(pkgPath);
        System.out.println("package contain files:"
                + ((files == null) ? "null" : "length=" + files.length));
        if (files != null) {
            for (File f : files) {
                String fileName = f.getName();
                if (f.isFile()) {
                    // .class 文件的情况
                    String clazzName = getClassName(pkgName, fileName);
                    addClassName(clazzList, clazzName, annotation);
                } else {
                    // 文件夹的情况
                    if (isRecursive) {
                        // 需要继续查找该文件夹/包名下的类
                        String subPkgName = pkgName + "." + fileName;
                        String subPkgPath = pkgPath + "/" + fileName;
                        findClassName(clazzList, subPkgName, subPkgPath, true,
                                annotation);
                    }
                }
            }
        }
    }

    /**
     * 第三方Jar类库的引用。<br/>
     *
     * @throws IOException
     */
    public static void findClassName(List<Class<?>> clazzList, String pkgName,
                                     URL url, boolean isRecursive, Class<? extends Annotation> annotation)
            throws IOException {
        JarURLConnection jarURLConnection = (JarURLConnection) url
                .openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        System.out.println("jarFile:" + jarFile.getName());
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            // 类似：sun/security/internal/interfaces/TlsMasterSecret.class
            String jarEntryName = jarEntry.getName();
            String clazzName = jarEntryName.replace("/", ".");
            int endIndex = clazzName.lastIndexOf(".");
            String prefix = null;
            if (endIndex > 0) {
                String prefix_name = clazzName.substring(0, endIndex);
                endIndex = prefix_name.lastIndexOf(".");
                if (endIndex > 0) {
                    prefix = prefix_name.substring(0, endIndex);
                }
            }
            if (prefix != null && jarEntryName.endsWith(".class")) {
                // System.out.println("prefix:" + prefix +" pkgName:" +
                // pkgName);
                if (prefix.equals(pkgName)) {
                    System.out.println("jar entryName:" + jarEntryName);
                    addClassName(clazzList, clazzName, annotation);
                } else if (isRecursive && prefix.startsWith(pkgName)) {
                    // 遍历子包名：子类
                    System.out.println("jar entryName:" + jarEntryName
                            + " isRecursive:" + isRecursive);
                    addClassName(clazzList, clazzName, annotation);
                }
            }
        }
    }

    /**
     * 文件过滤，忽略指定package下非class文件或者非package文件
     *
     * @param pkgPath
     * @return
     */
    private static File[] filterClassFiles(String pkgPath) {
        if (pkgPath == null) {
            return null;
        }
        // 接收 .class 文件 或 类文件夹
        return new File(pkgPath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class"))
                        || file.isDirectory();
            }
        });
    }

    /**
     * 获取class类名
     *
     * @param pkgName
     * @param fileName
     * @return
     */
    private static String getClassName(String pkgName, String fileName) {
        int endIndex = fileName.lastIndexOf(".");
        String clazz = null;
        if (endIndex >= 0) {
            clazz = fileName.substring(0, endIndex);
        }
        String clazzName = null;
        if (clazz != null) {
            clazzName = pkgName + "." + clazz;
        }
        return clazzName;
    }

    /**
     * 根据类名插入列表
     *
     * @param clazzList
     * @param clazzName  类名
     * @param annotation
     */
    private static void addClassName(List<Class<?>> clazzList,
                                     String clazzName, Class<? extends Annotation> annotation) {
        if (clazzList != null && clazzName != null) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(clazzName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (clazz != null) {
                if (annotation == null) {
                    clazzList.add(clazz);
                    System.out.println("add:" + clazz);
                } else if (clazz.isAnnotationPresent(annotation)) {
                    //如果指定类型的注释存在于此元素上,方法返回true, 否则返回false
                    clazzList.add(clazz);
                    System.out.println("add annotation:" + clazz);
                }
            }
        }
    }

    public static Class<?> getClassByName(String clazzName) {
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
