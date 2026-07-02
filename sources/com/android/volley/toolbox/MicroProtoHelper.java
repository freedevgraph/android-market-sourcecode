package com.android.volley.toolbox;

import com.google.protobuf.micro.MessageMicro;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class MicroProtoHelper {
    private static final Map<Class<?>, Method> sGetMethodCache = new HashMap();
    private static final Map<Class<?>, Method> sSetMethodCache = new HashMap();

    public static <X extends MessageMicro, Y extends MessageMicro> Y getParsedResponseFromWrapper(X wrapper, Class<X> wrapperClass, Class<Y> responseClass) {
        try {
            Method method = findGetMethod(wrapperClass, responseClass);
            return (Y) method.invoke(wrapper, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Method findGetMethod(Class<?> outerClass, Class<?> innerClass) {
        Method cached = sGetMethodCache.get(innerClass);
        if (cached != null) {
            return cached;
        }
        Method[] methods = outerClass.getMethods();
        for (Method m : methods) {
            if (m.getReturnType().equals(innerClass) && m.getName().startsWith("get")) {
                sGetMethodCache.put(innerClass, m);
                return m;
            }
        }
        throw new IllegalArgumentException("No getter for " + innerClass + " in " + outerClass);
    }
}
