package com.zyy.config;

import com.zzy.core.corouting.DeferredFutureTask;
import com.zzy.core.corouting.KotlinUtil;
import kotlin.jvm.functions.Function0;

import java.util.concurrent.FutureTask;

public class KotlinHelper {

    public static <T> T sync(Function0<T> function0) {
        return KotlinUtil.INSTANCE.sync(function0);
    }

    public static <T> void async(Function0<T> function0) {
        KotlinUtil.INSTANCE.async(function0);
    }

    public static void async(DeferredFutureTask future) {
        future.start();
    }


}
