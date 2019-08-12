package com.zyy;

import java.util.concurrent.CountDownLatch;

public class test {
    public static void main(String args[]) throws InterruptedException {
        Object a = new Object();
        Runnable runnable = new MyRunable();
        CountDownLatch latch = new CountDownLatch(100);
       new Thread(runnable).start();
        a.wait();
        System.out.println("over");
        latch.await();
    }


}

class MyRunable implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            System.out.println("hh");

            Thread.currentThread().isAlive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
