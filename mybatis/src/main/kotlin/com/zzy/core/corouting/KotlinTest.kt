package com.zzy.core.corouting

import com.mybatis.core.config.Configuration
import com.mybatis.core.executor.Executor
import com.mybatis.core.executor.Handler
import com.mybatis.core.executor.Handler2
import com.mybatis.core.executor.MyDruidDataSource
import com.mybatis.core.mapping.MappedStatement
import com.mybatis.core.reflection.ReflectionUtil
import kotlinx.coroutines.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.ArrayList
import java.util.concurrent.Future
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resumeWithException


class KotlinTest : Executor {
//    private var configuration: Configuration = config
//    public constructor(configuration: Configuration){
//        this.configuration = configuration
//    }

    override fun <E : Any?> query(ms: MappedStatement, parameter: Any): MutableList<E>  {
        var result: MutableList<E> = mutableListOf()
        val deferred = doQuery<E>(ms, parameter)
        runBlocking {
            result = doQuery<E>(ms, parameter).await()
            println("1111   "+Thread.currentThread().name)
        }
        println("2222   "+Thread.currentThread().name)

        return result
    }

    fun test(): String{
        var a  = String()
        runBlocking(Dispatchers.Unconfined){
            a = awaitEvent<String> {

                h-> h.handle("eeee")
            }
//            try {
//                a = awaitBlocking { throw java.lang.Exception("ss");  "aaaa" }
//            }catch (e: java.lang.Exception){
//                println(e.message)
//            }

            println("---1---"+Thread.currentThread().name)
        }
        println("---2---"+Thread.currentThread().name)
        return a
    }

    suspend fun <T> awaitEvent(block: (h: Handler<T>) -> Unit): T {
        return suspendCancellableCoroutine{ cont: CancellableContinuation<T> ->
            try {
                block.invoke(Handler { t ->
                    println("-----"+t)
                    cont.resume(t, {e->throw e})
                })

//                block(Handler{})
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }
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

    fun <E : Any?> doQuery(ms: MappedStatement, parameter: Any) = GlobalScope.async{
        val ret : MutableList<E> = mutableListOf()
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        try {
            launch() {  }

            connection = MyDruidDataSource.getConnection()
            preparedStatement = connection!!.prepareStatement(ms.sql)
            // 构造形参
            parameterized(preparedStatement, parameter)
            resultSet = preparedStatement!!.executeQuery()
            handlerResultSet(resultSet, ret, ms.resultMap)
//            print(ret.size)
        } catch (e: SQLException) {
            print(e.message)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            print(e.message)
        } finally {
            try {
                resultSet!!.close()
                preparedStatement!!.close()
                connection!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
        ret
    }


    private fun <E> handlerResultSet(resultSet: ResultSet, ret: MutableList<E>, resultMap: String) {
        var clazz: Class<E>? = null
        try {
            clazz = Class.forName(resultMap) as Class<E>
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            while (resultSet.next()) {
                val entity = clazz!!.newInstance()
                ReflectionUtil.setProToBeanFromResult(entity, resultSet)
                ret.add(entity as E)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Throws(SQLException::class)
    private fun parameterized(preparedStatement: PreparedStatement, parameter: Any) {
        if (parameter is Int) {
            preparedStatement.setInt(1, parameter)
        } else if (parameter is Long) {
            preparedStatement.setLong(1, parameter)
        } else if (parameter is String) {
            preparedStatement.setString(1, parameter)
            println(preparedStatement)
        }
    }

}
