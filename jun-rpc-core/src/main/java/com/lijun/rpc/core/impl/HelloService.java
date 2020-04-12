package com.lijun.rpc.core.impl;

import com.lijun.rpc.core.IHelloService;

/**
 * Class Name HelloService ...
 * helloService 实现类
 *
 * @author LiJun
 * Created on 2020/4/6 10:32
 */
public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        Thread.sleep(2 * 1000);
        return "Hello, " + name;
    }
}
