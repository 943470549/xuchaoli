package com.zzy.core.corouting

import kotlinx.coroutines.*
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

open class DeferredFutureTask<T>(private val callable: Callable<T>, private var timeout: Long) : Future<T>, CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
//    override fun run() {
//        callable!!.call()
//    }

    override fun isDone(): Boolean {
        return deferred.isCompleted || deferred.isCancelled
    }

    fun isCompleted(): Boolean {
        return deferred.isCompleted
    }

    fun getCompleted(): T {
        return deferred.getCompleted()
    }

    override fun get(): T? {
        var result: T? = null
        runBlocking { result = deferred.await() }
        return result
    }

    override fun get(timeout: Long, unit: TimeUnit?): T? {
        var result: T? = null
        runBlocking {
            withTimeoutOrNull(timeout) {
                deferred.await()
            }
        }
        return result
    }

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        deferred.cancel()
        job.cancel()
        return true
    }

    override fun isCancelled(): Boolean {
        return deferred.isCancelled
    }

    suspend fun await(): T {
        return deferred.await()
    }

    fun start(): DeferredFutureTask<T> {
        deferred.start()
        return this
    }


    private var deferred: Deferred<T>

    init {
        println("init"+ timeout)
        deferred = when (timeout) {
            0L -> async(coroutineContext, start = CoroutineStart.LAZY) {
                callable.call()
            }
            else -> async(coroutineContext, start = CoroutineStart.LAZY) {
                withTimeout(timeout) {
                    GlobalScope.async(Dispatchers.IO) { callable.call() }.await()
                }
            }
        }

    }

    constructor(runnable: Runnable, result: T) : this(Callable {
        runnable.run()
        result
    }, 0L)

    constructor(callable: Callable<T>) : this(callable, 0L)

    constructor(runnable: Runnable, result: T, timeout: Long) : this(Callable {
        runnable.run()
        result
    }, timeout)


}