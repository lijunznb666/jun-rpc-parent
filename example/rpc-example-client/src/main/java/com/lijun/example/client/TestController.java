package com.lijun.example.client;

import com.lijun.example.server.HelloService;
import com.lijun.rpc.core.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class Name TestController ...
 *
 * @author LiJun
 * Created on 2020/4/9 16:27
 */
@RestController
public class TestController {

    @Reference(interfaceClass = HelloService.class)
    private HelloService helloService;

    @RequestMapping("/test")
    public String test(){
        return helloService.sayHello("test");
    }
}
