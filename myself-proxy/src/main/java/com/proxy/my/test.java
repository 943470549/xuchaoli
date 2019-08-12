package com.proxy.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {


    public static void main (String args[]){
        Long result = 707829217l;
        Map<Long, Long> map= new HashMap<>();
        for(long i = 1l;i<result;i++){
            if(result%i == 0){
                map.put(i, result/i);
            }
        }
        for(Map.Entry a: map.entrySet()){
            System.out.println(a.getKey()+":"+a.getValue());
        }
    }
}
