package com.mybatis;

import com.zzy.core.corouting.KotlinFiberExecutor;
import com.zzy.core.corouting.KotlinTest;
import com.zzy.core.corouting.KotlinUtil;
import jdk.nashorn.internal.objects.annotations.Function;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.GlobalScope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Test {
    public static void main(String args[]){
//        KotlinTest executor = new KotlinTest();
//        String resultw =executor.test();
//        System.out.println(" aaa = "+resultw);
        System.out.println("begin--"+ Thread.currentThread().getName());
        String result = KotlinHelper.sync(()->{
            System.out.println("call--"+Thread.currentThread().getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "333";
        });
        System.out.println("end--"+ Thread.currentThread().getName());
        System.out.println(result);
    }
}
