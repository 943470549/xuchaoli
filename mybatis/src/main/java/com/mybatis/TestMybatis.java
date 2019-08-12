package com.mybatis;

import com.mybatis.core.executor.Executor;
import com.zzy.core.corouting.KotlinFiberExecutor;
import com.mybatis.core.session.SqlSession;
import com.mybatis.core.session.SqlSessionFactory;
import com.mybatis.entities.Course;
import com.mybatis.mapper.CourseMapper;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
public class TestMybatis {

    private static void createProxyClassFile(Class c) {
        byte[] data = ProxyGenerator.generateProxyClass("$CourseMapperProxy", new Class[]{c});
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(
                    "G:\\learn-workplace\\spring-source-analysis\\mybatis\\target\\classes\\com\\mybatis\\mapper\\$CourseMapperProxy.class");
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        SqlSessionFactory factory = new SqlSessionFactory();
//        SqlSession session = factory.openSqlSession();
//        CourseMapper mapper = session.getMapper(CourseMapper.class);
//        Course course = mapper.getByXml(111);
//        System.out.println("查询结果:" + course.getId() + "," + course.getName());
//        Course course2 = mapper.getByAnnotation(111);
//        System.out.println("查询结果:" + course2.getId() + "," + course2.getName());
//
//        createProxyClassFile(mapper.getClass());
//        Lock lock = new ReentrantLock();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1000);
        long t1 = System.nanoTime();
//        test1(executorService, latch, factory);

        test2(executorService, latch, factory);
        long t2 = System.nanoTime();
        latch.await();
        long t3 = System.nanoTime();
        System.out.println("ttttttttttt"+(t2-t1));
        System.out.println("ttttttttttt"+(t3-t1));
        System.out.println("it is over");

    }

    public static void test1(ExecutorService executorService, CountDownLatch latch, SqlSessionFactory factory){
        for(int i = 0;i<1000;i++){

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        SqlSession session = factory.openSqlSession();
                        CourseMapper mapper = session.getMapper(CourseMapper.class);
                        Course course = mapper.getByXml(111);
//                        System.out.println("查询结果:" + course.getId() + "," + course.getName() + " "+ Thread.currentThread().getId());
                        Course course2 = mapper.getByAnnotation(111);
//                        System.out.println("查询结果:" + course2.getId() + "," + course2.getName()+ " "+ Thread.currentThread().getId());
                        latch.countDown();
                    }catch (Exception e){}
                }
            });


        }

    }

    public static void test2(ExecutorService executorService, CountDownLatch latch, SqlSessionFactory factory){
        for(int i = 0;i<1000;i++){

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try{
//                        System.out.println("111");
                        SqlSession session = factory.openSqlSession( new KotlinFiberExecutor());
                        CourseMapper mapper = session.getMapper(CourseMapper.class);
                        Course course = mapper.getByXml(111);
//                        System.out.println("查询结果:" + course.getId() + "," + course.getName() + " "+ Thread.currentThread().getId());
                        Course course2 = mapper.getByAnnotation(111);
//                        System.out.println("查询结果:" + course2.getId() + "," + course2.getName()+ " "+ Thread.currentThread().getId());
                        latch.countDown();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            });


        }

    }
}
