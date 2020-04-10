package com.lijun.rpc.core.exceptions;

/**
 * Class Name RpcException ...
 *
 * @author LiJun
 * Created on 2020/4/6 11:09
 */
public class RpcException extends RuntimeException {
    private static final long serialVersionUID = 8260098415896211063L;

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
