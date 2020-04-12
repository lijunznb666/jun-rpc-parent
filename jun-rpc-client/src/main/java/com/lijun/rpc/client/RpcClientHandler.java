package com.lijun.rpc.client;

import com.lijun.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Class Name RpcClientHandler ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:51
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) throws Exception {
        String requestId = response.getRequestId();
        RpcFuture future = RpcRequestHolder.get(requestId);
        if (null != future) {
            RpcRequestHolder.remove(requestId);
            future.done(response);
        }
    }
}
