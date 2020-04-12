package com.lijun.rpc.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Class Name ServerProperties ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:59
 */
@ConfigurationProperties(prefix = "jun.rpc.server")
public class ServerProperties {

    private int port = 2020;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
