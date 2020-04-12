package com.lijun.rpc.client;

import com.lijun.rpc.protocol.RpcRequest;
import io.netty.channel.Channel;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Class Name RpcInvokeInterceptor ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:51
 */
public class RpcInvokeInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RpcInvokeInterceptor.class);


    private IConnectManager connectManager;

    public RpcInvokeInterceptor(IConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] args, @Origin Method method) throws Exception {
        String name = method.getDeclaringClass().getName();
        System.out.println(name);
        // create rpc request
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        // get a connect from connect manager
        Channel channel = connectManager.getChannel(method.getDeclaringClass().getName());
        // send the rpc request via the connect

        RpcFuture future = new RpcFuture();
        RpcRequestHolder.put(request.getRequestId(), future);

        channel.writeAndFlush(request);

        Object result = null;
        try {
            result = future.get();
        } catch (Exception e) {
            log.error("RpcInvokeInterceptor intercept error :", e);
        }
        return result;
    }
}
