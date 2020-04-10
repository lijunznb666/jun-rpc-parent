package com.lijun.example.server.impl;

import com.lijun.example.server.HelloService;
import com.lijun.rpc.core.annotation.Service;

/**
 * Class Name HelloServiceImpl ...
 *
 * @author LiJun
 * Created on 2020/4/8 21:15
 */
@Service(interfaceClass = HelloService.class)
// @Component
public class HelloServiceImpl implements HelloService {


    @Override
    public String sayHello(String ip) {
        System.out.println(String.format("hello%s", ip));
        return String.format("hello%s", ip);
    }
}
