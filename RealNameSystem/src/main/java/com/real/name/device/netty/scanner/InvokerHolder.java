package com.real.name.device.netty.scanner;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令执行器管理者
 */
public class InvokerHolder {

    private static Map<Byte, Map<Byte, Invoker>> invokers = new HashMap<>();

    /**
     * 添加命令调用器
     */
    static void addInvoker(byte type, byte functionId, Invoker invoker) {
        Map<Byte, Invoker> map = invokers.get(type);
        if (map == null) {
            map = new HashMap<>();
            invokers.put(type, map);
        }
        map.put(functionId, invoker);
    }

    /**
     * 获取命令执行器
     */
    public static Invoker getInvoker(Byte type, Byte functionId) {
        Map<Byte, Invoker> map = invokers.get(type);
        if (map != null) {
            return map.get(functionId);
        }
        return null;
    }

}
