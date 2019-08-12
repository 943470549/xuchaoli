package com.spring.boot.service;

import com.spring.boot.annotation.Service;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/20 15:10.
 **/
@Service("enjoyService")
public class MyEnjoyService {

    public String query(String name) {
        return name;
    }

}
