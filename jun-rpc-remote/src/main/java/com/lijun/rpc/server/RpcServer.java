package com.lijun.rpc.server;

import com.lijun.rpc.common.ServerThreadFactory;
import com.lijun.rpc.core.tookit.ExceptionUtils;
import com.lijun.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class Name RpcServer ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:22
 */
public class RpcServer {

    private static final Logger log = LoggerFactory.getLogger(RpcServer.class);


    private ServiceRegistry registry;
    private int port = 2020;
    private static String factoryName;

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(65535), new ServerThreadFactory(factoryName));

    private Map<String, Object> handlerMap = new LinkedHashMap<>();

    public RpcServer(String factoryName, ServiceRegistry registry) {
        RpcServer.factoryName = factoryName;
        this.registry = registry;
    }

    public RpcServer exposeService(Class<?> clazz, Object handler) throws Exception {
        handlerMap.put(clazz.getName(), handler);
        return this;
    }

    public RpcServer port(int port) {
        this.port = port;
        return this;
    }

    public void run() throws Exception {

        executor.submit(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerInitializer(handlerMap))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = null;
            try {
                future = bootstrap.bind(port).sync();
                log.info("server started on port:{}", port);
            } catch (Exception e) {
                log.error("RpcServer run error :", e);
                throw ExceptionUtils.mpe(e);
            }

            for (String className : handlerMap.keySet()) {
                try {
                    registry.register(className);
                } catch (Exception e) {
                    log.error("RpcServer run error :", e);
                    throw ExceptionUtils.mpe(e);
                }
            }

            try {
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
               log.error("RpcServer run error :", e);
                throw ExceptionUtils.mpe(e);

            }

        });
    }
}
