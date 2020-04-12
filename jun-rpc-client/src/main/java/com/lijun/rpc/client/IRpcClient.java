package com.lijun.rpc.client;

/**
 * Class Name IRpcClient ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:50
 */
public interface IRpcClient {

    /***
     * 创建
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T create(Class<T> clazz) throws Exception;
}
