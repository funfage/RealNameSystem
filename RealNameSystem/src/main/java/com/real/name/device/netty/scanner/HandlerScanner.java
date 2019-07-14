package com.real.name.device.netty.scanner;

import com.real.name.device.netty.annotion.SocketFunction;
import com.real.name.device.netty.annotion.SocketType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class HandlerScanner implements BeanPostProcessor {

    private Logger logger = LoggerFactory.getLogger(HandlerScanner.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<? extends Object> clazz = bean.getClass();
        //获取所有的接口类
        Class<?>[] interfaces = clazz.getInterfaces();
        //扫描所有接口父类
        if (interfaces != null && interfaces.length > 0) {
            for (Class<?> interFace : interfaces) {
                //判断是否为handler接口类
                SocketType socketType = interFace.getAnnotation(SocketType.class);
                if (socketType == null) {
                    continue;
                }
                //找出功能号
                Method[] methods = interFace.getMethods();
                if (methods != null && methods.length > 0) {
                    for (Method method : methods) {
                        SocketFunction socketFunction = method.getAnnotation(SocketFunction.class);
                        if (socketFunction == null) {
                            continue;
                        }
                        //获取类型
                        final byte type = socketType.type();
                        //获取功能号
                        final byte functionId = socketFunction.function();
                        if (InvokerHolder.getInvoker(type, functionId) == null) {
                            InvokerHolder.addInvoker(type, functionId, Invoker.valueOf(method, bean));
                        } else {
                            logger.warn("重复命令, type:" + type + "functionId:" + functionId);
                        }
                    }
                }
            }
        }
        return bean;
    }
}
















