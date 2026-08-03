package com.hospital;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public final class TestProxies {
    private TestProxies() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            if (method.getDeclaringClass() == Object.class) {
                if ("toString".equals(method.getName())) {
                    return "TestProxy(" + type.getSimpleName() + ")";
                }
                if ("hashCode".equals(method.getName())) {
                    return System.identityHashCode(proxy);
                }
                if ("equals".equals(method.getName())) {
                    return proxy == args[0];
                }
            }
            return handler.invoke(proxy, method, args);
        });
    }
}
