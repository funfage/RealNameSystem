package com.real.name.device.netty.codc;

import com.real.name.device.netty.model.AccessEvent;
import com.real.name.device.netty.scanner.Invoker;
import com.real.name.device.netty.scanner.InvokerHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessEventHandler extends SimpleChannelInboundHandler<AccessEvent> {

    private Logger logger = LoggerFactory.getLogger(AccessEvent.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("AccessEventHandler出现异常, 异常信息:{}", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AccessEvent event) throws Exception {
        logger.warn("AccessEventHandler中获取到的功能号为:{}", event.getFunctionId());
        //获取命令句执行器
        Invoker invoker = InvokerHolder.getInvoker(event.getType(), event.getFunctionId());
        if (invoker != null) {
            logger.warn("获取到了命令执行器");
            //通过反射执行
            invoker.invoke(event.getDeviceId(), event.getData(), event.getSequenceId(), event.getExternalData(), event.getAddress());
        } else {
            logger.warn("获取不到命令执行器");
        }
    }
}

















