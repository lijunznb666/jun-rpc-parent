package com.lijun.rpc.spi.support;

/**
 * @author Jerry Lee(oldratlee AT gmail DOT com)
 * @since 0.3.0
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
