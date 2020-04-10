package com.lijun.rpc.core;

/**
 * Class Name IHelloService ...
 *
 * @author LiJun
 * Created on 2020/4/6 10:32
 */
public interface IHelloService {
    /**
     * hello
     *
     * @param name
     * @return
     * @throws Exception
     */
    String hello(String name) throws Exception;
}
