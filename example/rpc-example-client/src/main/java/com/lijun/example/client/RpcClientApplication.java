package com.lijun.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication

@Component
public class RpcClientApplication {




    public static void main(String[] args) {
        SpringApplication.run(RpcClientApplication.class, args);
    }



}
