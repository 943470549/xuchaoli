package com.proxy.my;
import org.springframework.util.FileCopyUtils;
import sun.misc.ProxyGenerator;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/3
 **/

public class MyProxy {

    private static final String rt = "\r";
    private static final AtomicLong number = new AtomicLong();

    public static Object newProxyInstance2(MyClassLoader loader, Class<?> interfaces, InvocationHandler handler) {
        String className = "$MyProxy"+number.addAndGet(1);
        Object o = null;
        createProxyClassFile(loader, interfaces, className);
        try{
            Class $myProxy0 = loader.findClass(className);
            System.out.println(Subject2.class.getClassLoader().toString());
//            Subject2.class.getClassLoader().loadClass();
            System.out.println(loader.getParent().toString());
//            Class $myProxy0 = ClassLoader.getSystemClassLoader().loadClass("$MyProxy0");
//            Class $myProxy0 = Thread.currentThread().getContextClassLoader().loadClass("$MyProxy0");
            // $MyProxy0初始化
            Constructor constructor = $myProxy0.getConstructor(InvocationHandler.class);
            o = constructor.newInstance(handler);
        }catch (Exception e){
            e.printStackTrace();
        }
        return o;
    }

    public static Object newProxyInstance(MyClassLoader loader, Class<?> interfaces, MyInvocationHandler handler) {
        if (handler == null) {
            throw new NullPointerException();
        }
        // 根据接口构造代理类:$MyProxy0
        Method[] methods = interfaces.getMethods();
        StringBuffer proxyClassString = new StringBuffer();
        proxyClassString.append("package ")
                .append(loader.getProxyClassPkg()).append(";").append(rt)
                .append("import java.lang.reflect.Method;").append(rt)
                .append("public class $MyProxy0 implements ").append(interfaces.getName()).append("{").append(rt)
                .append("MyInvocationHandler handler;").append(rt)
                .append("public $MyProxy0(MyInvocationHandler handler) {").append(rt).append("this.handler = handler;").append(rt).append("}").append(rt)
                .append(getMethodString(methods, interfaces))
                .append("}");
        // 写入JAVA文件并编译
        String fileName = loader.getDir() + File.separator + "$MyProxy0.java";
        File myProxyFile = new File(fileName);
        Object o = null;
        try {
            compile(proxyClassString, myProxyFile);
            // 利用自定义的classloader加载
            Class $myProxy0 = loader.findClass("$MyProxy0");
            System.out.println(Subject2.class.getClassLoader().toString());
//            Subject2.class.getClassLoader().loadClass();
            System.out.println(loader.getParent().toString());
//            Class $myProxy0 = ClassLoader.getSystemClassLoader().loadClass("$MyProxy0");
//            Class $myProxy0 = Thread.currentThread().getContextClassLoader().loadClass("$MyProxy0");
            // $MyProxy0初始化
            Constructor constructor = $myProxy0.getConstructor(MyInvocationHandler.class);
            o = constructor.newInstance(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    private static void compile(StringBuffer proxyClassString, File myProxyFile) throws IOException {
        if(!myProxyFile.exists()){
            Files.createFile(Paths.get(myProxyFile.getPath()));
        }
        FileCopyUtils.copy(proxyClassString.toString().getBytes(), myProxyFile);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(null, null, null);
        Iterable javaFileObjects = standardJavaFileManager.getJavaFileObjects(myProxyFile);
        JavaCompiler.CompilationTask task = compiler.getTask(null, standardJavaFileManager, null, null, null, javaFileObjects);
        task.call();
        standardJavaFileManager.close();
    }


    private static String getMethodString(Method[] methods, Class interfaces) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Method method : methods) {
            stringBuffer.append("public void ").append(method.getName())
                    .append("()").append(" throws Throwable {").append(rt)
                    .append("Method method1 = ").append(interfaces.getName())
                    .append(".class.getMethod(\"").append(method.getName())
                    .append("\", new Class[]{});")
                    .append("this.handler.invoke(this, method1, null);").append(rt)
                    .append("}").append(rt);
        }
        return stringBuffer.toString();
    }

    private static void createProxyClassFile(MyClassLoader loader, Class c, String name) {

        byte[] data = ProxyGenerator.generateProxyClass(loader.getProxyClassPkg()+"."+name, new Class[]{c});
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(
                    loader.getDir()+"\\"+name+".class");
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createProxyClassFile(Class c, Long number) {
        String className = "$MyProxy"+number;
        byte[] data = ProxyGenerator.generateProxyClass(className, new Class[]{c});
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(
                    getRealRuntimePath("com.proxy.my")+"\\"+className+".class");
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

    public static void main(String[] args) throws Throwable {
//        URL uri = Thread.currentThread().getContextClassLoader().getResource("");
//        String path = Paths.get(uri.toURI()).resolve("com.proxy.my".replaceAll("\\.", "\\\\")).toString();
//        System.out.println(path);
//
//        MyInvocationHandler handler = new MyHandler(new MySubject2());
//
//        MyClassLoader myClassLoader = new MyClassLoader(
//                path, "com.proxy.my");
//
//        Subject2 subject = (Subject2) MyProxy.newProxyInstance2(myClassLoader,Subject2.class, handler);
//
//        subject.say();
//
//        Subject2 subject2 = (Subject2) MyProxy.newProxyInstance2(myClassLoader,Subject2.class, handler);
//
//        subject2.say();

//        createProxyClassFile(Subject2.class,1l);

    }
}
