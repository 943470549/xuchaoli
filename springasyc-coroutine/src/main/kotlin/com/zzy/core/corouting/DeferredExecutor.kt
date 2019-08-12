package com.zzy.core.corouting

import org.springframework.core.task.AsyncTaskExecutor
import java.util.concurrent.Callable
import java.util.concurrent.Future

open class DeferredExecutor: AsyncTaskExecutor {
    override fun submit(p0: Runnable): Future<*> {
        return DeferredFutureTask(p0, null).start()
    }

    override fun <T : Any?> submit(p0: Callable<T>): Future<T> {
        return DeferredFutureTask(p0).start()
    }

    override fun execute(p0: Runnable, p1: Long) {
        DeferredFutureTask(p0, null, p1).start()
    }

    override fun execute(p0: Runnable) {
        DeferredFutureTask(p0, null).start()
    }
}