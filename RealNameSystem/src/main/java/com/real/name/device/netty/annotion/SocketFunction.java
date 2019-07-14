package com.real.name.device.netty.annotion;

import java.lang.annotation.*;

@Target(ElementType.METHOD)//在方法上有效
@Retention(RetentionPolicy.RUNTIME)//运行时有效
@Documented
public @interface SocketFunction {

    /**
     * 请求的功能号
     */
    byte function();

}
