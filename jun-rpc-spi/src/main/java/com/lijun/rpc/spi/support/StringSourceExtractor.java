package com.lijun.rpc.spi.support;

/**
 * Class Name StringSourceExtractor ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:57
 */
public class StringSourceExtractor extends AbstractNameExtractor {
    @Override
    protected void doInit() {
    }

    @Override
    public String extract(Object argument) {
        return (String) argument;
    }
}
