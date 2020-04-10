package com.lijun.rpc.client;

import com.lijun.rpc.client.proxy.AsyncObjectProxy;
import com.lijun.rpc.client.proxy.AsyncObjectProxyImpl;
import com.lijun.rpc.common.ServerThreadFactory;
import com.lijun.rpc.registry.ServiceDiscovery;

import java.lang.reflect.Proxy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class Name RpcClient ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:21
 */
public class RpcClient {

    private static String factoryName;

    private String serverAddress;

    private ServiceDiscovery serviceDiscovery;


    public RpcClient(String factoryName, String serverAddress, ServiceDiscovery serviceDiscovery) {
        RpcClient.factoryName = factoryName;
        this.serverAddress = serverAddress;
        this.serviceDiscovery = serviceDiscovery;
    }

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(65536), new ServerThreadFactory(factoryName));

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                new AsyncObjectProxyImpl<T>(interfaceClass));
    }

    public static <T> AsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new AsyncObjectProxyImpl<T>(interfaceClass);
    }

    public static void submit(Runnable task) {
        executor.submit(task);
    }

    public void  stop() {
        executor.shutdown();
        serviceDiscovery.stop();
        ConnectManager.getInstance().stop();
    }
}
