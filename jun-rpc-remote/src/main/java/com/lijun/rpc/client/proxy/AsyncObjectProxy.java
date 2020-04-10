package com.lijun.rpc.client.proxy;

import com.lijun.rpc.client.RpcFuture;

/**
 * Class Name AsyncObjectProxy ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:21
 */
public interface AsyncObjectProxy {

    /**
     * 异步调用方法
     *
     * @param methodName 方法名
     * @param args       参数列表
     * @return 执行结果
     */
    RpcFuture call(String methodName, Object... args);

}
