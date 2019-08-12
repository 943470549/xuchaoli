package com.zyy

import com.zzy.core.corouting.DeferredFutureTask
import kotlinx.coroutines.runBlocking
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients


fun main(args: Array<String>) {
//    var latch : CountDownLatch = CountDownLatch(10000)
//    var time1: Long = System.nanoTime()
//    for (i in 1..1000) {
//        GlobalScope.launch(Dispatchers.IO){
//        var client: CloseableHttpClient = HttpClients.createDefault()
//        var httpGet: HttpGet = HttpGet("http://localhost:8080/index")
//        var httpRes = client.execute(httpGet)
//        latch.countDown()
//        }
//    }
//
//    latch.await()
//    print("end------------"+(System.nanoTime()-time1))

    var a = DeferredFutureTask(Runnable {
        for (i in 1..100) {

            var client: CloseableHttpClient = HttpClients.createDefault()
            var httpGet: HttpGet = HttpGet("https://www.baidu.com")
            var httpRes = client.execute(httpGet)

        }
        println(Thread.currentThread().name)
        println("hhhhhh")

    }, 1000, 2000).start()
    runBlocking {
        println(a.await())
    }
}