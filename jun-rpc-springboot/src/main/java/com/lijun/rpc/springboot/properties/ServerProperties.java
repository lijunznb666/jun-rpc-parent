package com.lijun.rpc.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Class Name ServerProperties ...
 *
 * @author LiJun
 * Created on 2020/4/7 19:47
 */
@ConfigurationProperties(prefix = "jun.rpc.server")
public class ServerProperties {

    private int port = 2020;

    private String serverFactoryName;


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerFactoryName() {
        return serverFactoryName;
    }

    public void setServerFactoryName(String serverFactoryName) {
        this.serverFactoryName = serverFactoryName;
    }
}
