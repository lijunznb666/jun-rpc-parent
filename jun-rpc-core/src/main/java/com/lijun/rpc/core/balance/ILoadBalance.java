package com.lijun.rpc.core.balance;

import com.lijun.rpc.spi.Adaptive;
import com.lijun.rpc.spi.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Class Name ILoadBalance ...
 * 负载均衡
 *
 * @author LiJun
 * Created on 2020/4/6 10:50
 */
@Extension(defaultValue = "random")
public interface ILoadBalance {

    Logger log = LoggerFactory.getLogger(ILoadBalance.class);

    /**
     * 选择对应的负载配置
     *
     * @param config
     * @param amount
     * @return
     * @throws Exception
     */
    int select(@Adaptive("loadbalance") Map<String, String> config, int amount) throws Exception;

}
