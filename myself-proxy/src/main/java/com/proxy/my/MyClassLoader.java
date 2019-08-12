package com.proxy.my;

import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/3
 **/
public class MyClassLoader extends ClassLoader {
    /**
     * 生成的代理类加载路径
     */
    private File dir;

    private String proxyClassPkg;

    public File getDir() {
        return dir;
    }

    public String getProxyClassPkg() {
        return proxyClassPkg;
    }

    public MyClassLoader(String path, String proxyClassPkg) {
        this.dir = new File(path);
        this.proxyClassPkg = proxyClassPkg;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (dir != null) {
            File classFile = new File(dir, name + ".class");
            if (classFile.exists()) {
                // 生成CLASS二进制字节流
                byte[] classBytes = new byte[0];
                try {
                    classBytes = FileCopyUtils.copyToByteArray(classFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return defineClass(proxyClassPkg + "." + name, classBytes, 0, classBytes.length);
            }
        }
        return super.findClass(name);
    }
}
