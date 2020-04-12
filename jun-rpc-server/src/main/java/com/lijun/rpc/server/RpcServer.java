package com.lijun.rpc.server;

import com.lijun.rpc.registry.IRegistry;
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
import java.util.concurrent.Executors;

/**
 * Class Name RpcServer ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:54
 */
public class RpcServer {

    private static final Logger log = LoggerFactory.getLogger(RpcServer.class);


    private String host = "127.0.0.1";
    private IRegistry registry;
    private int port = 2020;

    private Map<String, Object> handlerMap = new LinkedHashMap<>();

    public RpcServer(IRegistry registry) {
        this.registry = registry;
    }

    public RpcServer exposeService(Class<?> clazz, Object handler) throws Exception {
        handlerMap.put(clazz.getName(), handler);
//        registry.register(clazz.getName(),port);
//         registry.keepAlive();

        return this;
    }

    public RpcServer port(int port) {
        this.port = port;
        return this;
    }

    public void run() throws Exception {

        Executors.newSingleThreadExecutor().submit(() -> {
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
            } catch (InterruptedException e) {
                log.error("RpcServer run error :", e);
            }

            for (String className : handlerMap.keySet()) {
                try {
                    registry.register(className, port);
                } catch (Exception e) {
                    log.error("RpcServer run error :", e);
                }
            }

            try {
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("RpcServer run error :", e);
            }

        });
    }
}
