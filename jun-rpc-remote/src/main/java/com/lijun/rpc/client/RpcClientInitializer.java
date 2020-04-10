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
 * Created on 2020/4/8 20:22
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline cp = channel.pipeline();
        cp.addLast(new RpcEncoder(RpcRequest.class));
        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        cp.addLast(new RpcDecoder(RpcResponse.class));
        cp.addLast(new RpcClientHandler());
    }
}
