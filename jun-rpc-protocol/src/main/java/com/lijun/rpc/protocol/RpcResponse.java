package com.lijun.rpc.protocol;

/**
 * Class Name RpcResponse ...
 *
 * @author LiJun
 * Created on 2020/4/6 12:24
 */
public class RpcResponse {

    private String requestId;
    private String error;
    private Object result;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
