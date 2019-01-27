package com.oneandone.cdi.weldstarter.spi;

import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.inject.spi.Extension;

import com.oneandone.cdi.weldstarter.WeldSetup;

/**
 * @author aschoerk
 */
public interface WeldStarter {
    void start(WeldSetup weldSetup);

    <T> T get(Class<T> clazz);

    String getVersion();

    void tearDown();

    boolean startInterceptionDecorationContext();

    void endInterceptorContext();

    Extension createExtension(String className);
}
