package com.lijun.rpc.core;

import java.util.Objects;

/**
 * Class Name Endpoint ...
 *
 * @author LiJun
 * Created on 2020/4/6 10:29
 */
public class Endpoint {

    /**
     * host
     */
    private final String host;

    /**
     * 端口
     */
    private final int port;

    public Endpoint(String hostPort) {
        final String[] split = hostPort.split(":");
        this.host = split[0];
        this.port = Integer.parseInt(split[1]);
    }

    public Endpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", host, port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Endpoint)) {
            return false;
        }
        Endpoint endpoint = (Endpoint) o;
        return getPort() == endpoint.getPort() &&
                Objects.equals(getHost(), endpoint.getHost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHost(), getPort());
    }
}
