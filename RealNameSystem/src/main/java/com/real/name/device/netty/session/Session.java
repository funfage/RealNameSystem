package com.real.name.device.netty.session;

public interface Session {

    /**
     * 会话绑定对象
     */
    Object getAttachment();

    /**
     * 绑定对象
     */
    void setAttachment(Object attachment);

    /**
     * 移除对象
     */
    void removeAttachment();

    /**
     * 向会话中写入消息
     */
    void write(Object message);

    /**
     * 判断会话是否在连接中
     */
    boolean isConnected();

    /**
     * 关闭会话
     */
    void close();

}














