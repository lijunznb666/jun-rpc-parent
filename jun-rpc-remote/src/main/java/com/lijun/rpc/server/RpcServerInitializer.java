package com.lijun.rpc.server;


import com.lijun.rpc.protocol.RpcDecoder;
import com.lijun.rpc.protocol.RpcEncoder;
import com.lijun.rpc.protocol.RpcRequest;
import com.lijun.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Map;

/**
 * Class Name RpcServerInitializer ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:22
 */
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {

    private final Map<String, Object> handlerMap;

    public RpcServerInitializer(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        pipeline.addLast(new RpcDecoder(RpcRequest.class));
        pipeline.addLast(new RpcEncoder(RpcResponse.class));
        pipeline.addLast(new RpcServerHandler(handlerMap));
    }
}
