package com.lijun.rpc.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Class Name ClientProperties ...
 *
 * @author LiJun
 * Created on 2020/4/8 21:04
 */
@ConfigurationProperties(prefix = "jun.rpc.client")
public class ClientProperties {

    private String address;

    private String connectManagerFactoryName = "ConnectManager";

    private String clientFactoryName = "Client";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConnectManagerFactoryName() {
        return connectManagerFactoryName;
    }

    public String getClientFactoryName() {
        return clientFactoryName;
    }

    public void setConnectManagerFactoryName(String connectManagerFactoryName) {
        this.connectManagerFactoryName = connectManagerFactoryName;
    }

    public void setClientFactoryName(String clientFactoryName) {
        this.clientFactoryName = clientFactoryName;
    }
}
