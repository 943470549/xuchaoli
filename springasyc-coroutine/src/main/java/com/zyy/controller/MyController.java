package com.zyy.controller;


import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;







import sun.net.www.http.HttpClient;

import java.io.Closeable;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/index")
public class MyController{


    /**
     * 把旧attrs结构转换成新结构
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Callable<String> convertToNewAttrs(){
        String a = "i am result ";
        System.out.println("主线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
//        Callable<String> callable = new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                System.out.println("副线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
//                Thread.sleep(2000);
//                System.out.println("副线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
//                return "Callable<String> async01()";
//            }
//        };
        return ()->{
            System.out.println("副线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
//            Thread.sleep(2000);
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(new HttpGet("https://www.baidu.com"));
            System.out.println("副线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
            return IOUtils.toString(response.getEntity().getContent());
        };
    }
}
