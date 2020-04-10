package com.lijun.rpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Class Name RpcDecoder ...
 *
 * @author LiJun
 * Created on 2020/4/6 12:24
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> clazz;

    public RpcDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        /*if (dataLength <= 0) {
            ctx.close();
        }*/
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        Object obj = SerializationUtil.deserialize(data, clazz);
        //Object obj = JsonUtil.deserialize(data,genericClass); // Not use this, have some bugs
        list.add(obj);
    }
}
