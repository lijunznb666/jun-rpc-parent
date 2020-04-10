package com.lijun.rpc.client;


import com.lijun.rpc.protocol.RpcRequest;
import com.lijun.rpc.protocol.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Class Name RpcClientHandler ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:21
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger log = LoggerFactory.getLogger(RpcClientHandler.class);


    private ConcurrentHashMap<String, RpcFuture> pendingRpc = new ConcurrentHashMap<>();

    private volatile Channel channel;

    private SocketAddress remotePeer;

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
        remotePeer = channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext context) throws Exception {
        super.channelRegistered(context);
        channel = context.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        log.error("client caught exception:{}", ExceptionUtils.getStackTrace(cause));
        context.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, RpcResponse response) throws Exception {
        String requestId = response.getRequestId();
        RpcFuture future = pendingRpc.get(requestId);
        if (future != null) {
            pendingRpc.remove(requestId);
            future.done(response);
        }
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public RpcFuture sendRequest(RpcRequest request) {
        final CountDownLatch latch = new CountDownLatch(1);
        RpcFuture future = new RpcFuture(request);
        pendingRpc.put(request.getRequestId(), future);
        channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> latch.countDown());
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("send request error:{}", ExceptionUtils.getStackTrace(e));
        }

        return future;
    }
}
