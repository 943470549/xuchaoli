package com.zzy.core.corouting


import kotlinx.coroutines.*
import kotlin.coroutines.resumeWithException


class KotlinTest  {



    suspend fun <T> awaitBlocking(block: () -> T): T {
        return suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
            try {
                cont.resume(block(), {e->throw e})
                println("---3---"+Thread.currentThread().name)
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
            GlobalScope
        }
//       var a = get { "" }
//        a.await()
//        a.c
    }

    fun <T>get(block: ()-> T): Deferred<T?>{
       return GlobalScope.async {
            var t:T? = null
           t
       }
    }







}
