package com.lijun.rpc.registry;


import com.lijun.rpc.core.Endpoint;

import java.util.List;

/**
 * Class Name IRegistry ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:53
 */
public interface IRegistry {

    /**
     * 注册服务
     *
     * @param serviceName
     * @param port
     * @throws Exception
     */
    void register(String serviceName, int port) throws Exception;

    /**
     * 服务发现
     *
     * @param serviceName
     * @return
     * @throws Exception
     */
    List<Endpoint> discover(String serviceName) throws Exception;

}
