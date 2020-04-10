package com.lijun.rpc.client;


import com.lijun.rpc.core.tookit.ExceptionUtils;
import com.lijun.rpc.protocol.RpcRequest;
import com.lijun.rpc.protocol.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class Name RpcFuture ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:22
 */
public class RpcFuture implements Future<Object> {

    private static final Logger log = LoggerFactory.getLogger(RpcFuture.class);


    private Sync sync;

    private RpcRequest request;

    private RpcResponse response;

    private long startTime;

    private long responseTimeout = 5000L;

    private List<AsyncRpcCallBack> pendingCallbacks = new ArrayList<>();

    private ReentrantLock lock = new ReentrantLock();

    public RpcFuture(RpcRequest request) {
        this.sync = new Sync();
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        if (response != null) {
            return response.getResult();
        } else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (request != null) {
                return response.getResult();
            } else {
                return null;
            }
        } else {
            throw ExceptionUtils.mpe("未得到服务 requestId:%s, className:%s, methodName:%s", request.getRequestId(), request.getClassName(), request.getMethodName());
        }
    }

    public void done(RpcResponse response) {
        this.response = response;
        sync.release(1);
        invokeCallbacks();
        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > responseTimeout) {
            log.warn("service response time is too slow. requestId:{}, responseTime:{}ms", response.getRequestId(),
                    responseTime);
        }
    }

    private void invokeCallbacks() {
        lock.lock();
        try {
            for (final AsyncRpcCallBack callback : pendingCallbacks) {
                runCallback(callback);
            }
        } catch (Exception e) {
            log.error("invokeCallbacks error:{}", org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
        } finally {
            lock.unlock();
        }
    }


    public RpcFuture addCallback(AsyncRpcCallBack callBack) {
        lock.lock();
        try {
            if (isDone()) {
                runCallback(callBack);
            } else {
                pendingCallbacks.add(callBack);
            }
        } catch (Exception e) {
            log.error("addCallback error:{}", org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
        } finally {
            lock.unlock();
        }

        return this;
    }

    private void runCallback(final AsyncRpcCallBack callback) {
        final RpcResponse res = this.response;
        RpcClient.submit(() -> callback.success(res.getResult()));
    }

    static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4267186114619934899L;

        /**
         * future status
         */
        private final int done = 1;
        private final int pending = 0;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == pending) {
                return compareAndSetState(pending, done);
            } else {
                return false;
            }
        }

        public boolean isDone() {
            getState();
            return getState() == done;
        }
    }
}
