package com.real.name.common.annotion;

import java.lang.annotation.*;

//接口，类，枚举，注解上有效
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)//运行时有效
@Documented
public @interface SocketType {

    /**
     * 请求的类型
     */
    byte type();
}
