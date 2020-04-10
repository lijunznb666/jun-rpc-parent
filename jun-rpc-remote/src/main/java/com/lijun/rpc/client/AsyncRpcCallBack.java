package com.lijun.rpc.client;

/**
 * Class Name AsyncRpcCallBack ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:21
 */
public interface AsyncRpcCallBack {

    void success(Object result);

    void fail(Exception e);

}
