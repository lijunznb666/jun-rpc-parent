package com.lijun.rpc.client;

import io.netty.channel.Channel;

/**
 * Class Name IConnectManager ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:50
 */
public interface IConnectManager {
    /**
     * 获取通道
     *
     * @param serviceName
     * @return
     * @throws Exception
     */
    Channel getChannel(String serviceName) throws Exception;
}
