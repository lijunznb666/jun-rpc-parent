package com.lijun.rpc.client;


import com.lijun.rpc.core.Endpoint;
import com.lijun.rpc.core.RpcConfig;
import com.lijun.rpc.core.balance.ILoadBalance;
import com.lijun.rpc.registry.IRegistry;
import com.lijun.rpc.spi.ExtensionLoader;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class Name ConnectManager ...
 * 连接管理类
 *
 * @author LiJun
 * Created on 2020/4/12 17:50
 */
public class ConnectManager implements IConnectManager {

    private IRegistry registry;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private AtomicInteger roundRobin = new AtomicInteger(0);
    private Map<String, List<ChannelWrapper>> channelsByService = new LinkedHashMap<>();

    public ConnectManager(IRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Channel getChannel(String serviceName) throws Exception {
        if (!channelsByService.containsKey(serviceName)) {
            List<Endpoint> endpoints = registry.discover(serviceName);
            List<ChannelWrapper> channels = new ArrayList<>();
            for (Endpoint endpoint : endpoints) {
                channels.add(connect(endpoint.getHost(), endpoint.getPort()));
            }
            channelsByService.put(serviceName, channels);
        }

        // select one channel from all available channels
        int size = channelsByService.get(serviceName).size();
        ILoadBalance loadBalance = ExtensionLoader.getExtensionLoader(ILoadBalance.class).getAdaptiveInstance();
        if (0 == size) {
            System.out.println("NO available providers for service: " + serviceName);
        }
        //int index = (roundRobin.getAndAdd(1) + size) % size;
        String loadbalance = RpcConfig.get("jun.rpc.loadbalance");
        Map<String, String> map = new LinkedHashMap<>();
        map.put("loadbalance", loadbalance);
        int index = loadBalance.select(map, size);
        ChannelWrapper channelWrapper = channelsByService.get(serviceName).get(index);
        System.out.println("Load balance:" + loadbalance + "; Selected endpoint: " + channelWrapper.toString());
        return channelWrapper.getChannel();
    }

    private ChannelWrapper connect(String host, int port) throws Exception {

        Bootstrap b = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());

        Channel channel = b.connect(host, port).sync().channel();
        ChannelWrapper channelWrapper = new ChannelWrapper(new Endpoint(host, port), channel);
        return channelWrapper;
    }


    private static class ChannelWrapper {
        private Endpoint endpoint;
        private Channel channel;

        public ChannelWrapper(Endpoint endpoint, Channel channel) {
            this.endpoint = endpoint;
            this.channel = channel;
        }

        public Endpoint getEndpoint() {
            return endpoint;
        }

        public Channel getChannel() {
            return channel;
        }

        @Override
        public String toString() {
            return endpoint.getHost() + ":" + endpoint.getPort();
        }
    }
}
