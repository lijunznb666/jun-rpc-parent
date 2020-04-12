package com.lijun.example;

import com.lijun.example.server.HelloService;
import com.lijun.rpc.core.annotation.Reference;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class RpcClientApplicationTests {

    @Reference(interfaceClass = HelloService.class)
    private HelloService helloService;

    @org.junit.Test
    public void test(){
        System.out.println(helloService.sayHello("test"));
    }

}
