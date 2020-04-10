package com.lijun.rpc.core.tookit;

import com.lijun.rpc.core.exceptions.RpcException;

/**
 * Class Name ExceptionUtils ...
 * 统一异常管理
 *
 * @author LiJun
 * Created on 2020/4/6 11:10
 */
public final class ExceptionUtils {

    /**
     * 统一异常管理
     *
     * @param msg
     * @return
     */
    public static RpcException mpe(String msg, Object... params) {
        return new RpcException(String.format(msg, params));
    }

    public static RpcException mpe(String message, Throwable cause) {
        return new RpcException(message, cause);
    }

    public static RpcException mpe(Throwable cause) {
        return new RpcException(cause);
    }


    private ExceptionUtils() {
    }
}
