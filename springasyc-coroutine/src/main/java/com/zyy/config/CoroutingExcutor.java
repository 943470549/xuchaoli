package com.zyy.config;

import com.zzy.core.corouting.DeferredFutureTask;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CoroutingExcutor implements AsyncTaskExecutor {
    @Override
    public void execute(Runnable runnable, long l) {
        DeferredFutureTask future = new DeferredFutureTask(runnable, null, l);
        future.start();
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        DeferredFutureTask future = new DeferredFutureTask(runnable, null);
        future.start();
        return future;
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        DeferredFutureTask future = new DeferredFutureTask(callable);
        future.start();
        return future;
    }

    @Override
    public void execute(Runnable runnable) {
        DeferredFutureTask future = new DeferredFutureTask(runnable, null);
        future.start();
    }
}
