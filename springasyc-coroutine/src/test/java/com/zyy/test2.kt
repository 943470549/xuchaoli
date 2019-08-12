package com.zyy

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.system.measureTimeMillis

fun CoroutineScope.produceNumber(): ReceiveChannel<Int> = produce{
    var x = 2
    while ( true) {
        send(x++)
        delay(100)}
}

fun main() = runBlocking{
    var time = measureTimeMillis {
        doSum()
    }
    println("time is $time")
}
suspend fun doSum():Int = coroutineScope{
    var one = do1Async()
    var two = do2Async()
    one.await()+two.await()
}

fun do1Async() = GlobalScope.async {
    do1()
}

fun do2Async() = GlobalScope.async {
    do2()
}

suspend fun do1():Int{
    delay(1000L)
    return 1
}
suspend fun do2():Int{
    delay(1000L)
    return 2
}
