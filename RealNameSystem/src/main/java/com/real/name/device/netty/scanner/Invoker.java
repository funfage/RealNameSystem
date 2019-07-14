package com.real.name.device.netty.scanner;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Setter
@Getter
public class Invoker {

    /**
     * 方法
     */
    private Method method;

    /**
     * 目标对象
     */
    private Object target;

    public static Invoker valueOf(Method method, Object target) {
        Invoker invoker = new Invoker();
        invoker.setMethod(method);
        invoker.setTarget(target);
        return invoker;
    }

    /**
     * 执行
     */
    public Object invoke(Object... paramValues) {
        try {
            return method.invoke(target, paramValues);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}

















