package com.lijun.rpc.client;

import com.lijun.rpc.protocol.RpcDecoder;
import com.lijun.rpc.protocol.RpcEncoder;
import com.lijun.rpc.protocol.RpcRequest;
import com.lijun.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Class Name RpcClientInitializer ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:51
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new RpcEncoder(RpcRequest.class));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0));
        pipeline.addLast(new RpcDecoder(RpcResponse.class));
        pipeline.addLast(new RpcClientHandler());
    }
}
