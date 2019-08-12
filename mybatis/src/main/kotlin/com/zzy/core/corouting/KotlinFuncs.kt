package com.zzy.core.corouting

import com.mybatis.core.executor.Handler
import kotlinx.coroutines.*
import java.util.concurrent.Future
import kotlin.coroutines.resumeWithException

object KotlinUtil{
    suspend fun <T> awaitEvent(block: (h: Handler<T>) -> Unit): T {
        return suspendCancellableCoroutine{ cont: CancellableContinuation<T> ->
            try {
                    block.invoke(Handler { t ->
                        println("-----"+t)
                        cont.resume(t, {e->throw e})
                    })

            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }
    }

    fun <T> sync(block: () -> T):T?{
        var result: T? = null
        runBlocking(Dispatchers.IO){
            result = awaitBlocking(block)
        }
        return result
    }

    suspend fun <T> awaitBlocking(block: () -> T): T {
        return suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
            try {
                    cont.resume(block(), {e->throw e})
                    println("---3---"+Thread.currentThread().name)
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }
    }

    suspend fun <T> awaitResult(block: (h: Handler<Future<T>>) -> Unit): T {
        val future : Future<T> = awaitEvent(block)
        return future.get()
    }
}
