package com.hospital;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public final class TestProxies {
    private TestProxies() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }
}
