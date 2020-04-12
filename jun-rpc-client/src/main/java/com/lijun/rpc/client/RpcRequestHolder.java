package com.lijun.rpc.client;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Class Name RpcRequestHolder ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:51
 */
public class RpcRequestHolder {

    /***
     * key: requestId     value: RpcFuture
     */
    private static ConcurrentHashMap<String, RpcFuture> processingRpc = new ConcurrentHashMap<>();

    public static void put(String requestId, RpcFuture rpcFuture) {
        processingRpc.put(requestId, rpcFuture);
    }

    public static RpcFuture get(String requestId) {
        return processingRpc.get(requestId);
    }

    public static void remove(String requestId) {
        processingRpc.remove(requestId);
    }
}
