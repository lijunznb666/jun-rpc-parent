package com.lijun.rpc.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Class Name RegistryProperties ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:59
 */
@ConfigurationProperties(prefix = "jun.rpc.registry")
public class RegistryProperties {

    private String address = "127.0.0.1:2181";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
