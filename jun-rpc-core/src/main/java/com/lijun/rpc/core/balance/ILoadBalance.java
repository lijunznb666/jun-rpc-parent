package com.lijun.rpc.core.balance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Class Name ILoadBalance ...
 *
 * @author LiJun
 * Created on 2020/4/6 10:50
 */
public interface ILoadBalance {

    Logger log = LoggerFactory.getLogger(ILoadBalance.class);

    int select(Map<String, String> config, int amount) throws Exception;

}
