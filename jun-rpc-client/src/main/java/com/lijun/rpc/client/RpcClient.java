package com.lijun.rpc.client;

import com.lijun.rpc.registry.IRegistry;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

/**
 * Class Name RpcClient ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:51
 */
public class RpcClient implements IRpcClient {

    private IRegistry registry;
    private Map<String, Object> proxyByClass = new LinkedHashMap<>();
    ConnectManager connectManager;

    public RpcClient(IRegistry registry) {
        this.registry = registry;
        this.connectManager = new ConnectManager(registry);
        //this.registry.watch();
    }

    @Override
    public <T> T create(Class<T> clazz) throws Exception {
        if (!proxyByClass.containsKey(clazz.getName())) {
            T proxy = new ByteBuddy()
                    .subclass(clazz)
                    .method(isDeclaredBy(clazz)).intercept(MethodDelegation.to(new RpcInvokeInterceptor(connectManager)))
                    .make()
                    .load(getClass().getClassLoader())
                    .getLoaded()
                    .newInstance();

            proxyByClass.put(clazz.getName(), proxy);
        }

        return (T) proxyByClass.get(clazz.getName());
    }
}
