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

    public static Object defaultValue(Class<?> returnType) {
        if (returnType.equals(Void.TYPE)) {
            return null;
        }
        if (returnType.equals(Boolean.TYPE)) {
            return false;
        }
        if (returnType.equals(Byte.TYPE)) {
            return (byte) 0;
        }
        if (returnType.equals(Short.TYPE)) {
            return (short) 0;
        }
        if (returnType.equals(Integer.TYPE)) {
            return 0;
        }
        if (returnType.equals(Long.TYPE)) {
            return 0L;
        }
        if (returnType.equals(Float.TYPE)) {
            return 0F;
        }
        if (returnType.equals(Double.TYPE)) {
            return 0D;
        }
        if (returnType.equals(Character.TYPE)) {
            return '\0';
        }
        return null;
    }
}
