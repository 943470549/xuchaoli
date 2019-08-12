package com.spring.boot.controller;

import com.spring.boot.annotation.Autowired;
import com.spring.boot.annotation.Controller;
import com.spring.boot.annotation.RequestMapping;
import com.spring.boot.annotation.RequestParam;
import com.spring.boot.service.MyEnjoyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/20 15:10.
 **/
@Controller
@RequestMapping("/enjoy")
public class MyEnjoyController {

    @Autowired
    private MyEnjoyService service;

    @RequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name) {
        OutputStream out = null;
        PrintWriter writer = null;
        try {
            out = response.getOutputStream();
            writer = new PrintWriter(out);
            writer.write(name);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                writer.close();
            }
            if( out!= null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
