package com.lijun.example.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lijun.example")
public class RpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcServerApplication.class, args);
    }

}
