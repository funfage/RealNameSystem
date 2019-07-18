package com.real.name.device.netty.session;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    /**
     * 在线会话
     */
    private static final ConcurrentHashMap<Integer, Channel> onlineSessions = new ConcurrentHashMap<>();

    /**
     * 加入会话管理器
     */
    public static void putSession(Integer deviceId, Channel channel) {
        if (onlineSessions.containsKey(deviceId)) {
            removeSession(deviceId);
        }
        onlineSessions.put(deviceId, channel);
    }

    /**
     * 移除会话
     */
    public static Channel removeSession(Integer deviceId) {
        Channel channel = getSession(deviceId);
        if (channel != null) {
            channel.close();
        }
        return onlineSessions.remove(deviceId);
    }

    /**
     * 获取会话
     */
    public static Channel getSession(Integer deviceId) {
        return onlineSessions.get(deviceId);
    }
    /**
     * 发送消息[自定义协议]
     */
    /*public static <T extends Serializer> void sendMessage(Long deviceId, byte type, byte functionId, T message) {
        Session session = onlineSessions.get(deviceId);
        if (session != null && session.isConnected()) {
            AccessResponse response = new AccessResponse(type, functionId, message.getBytes())
        }
    }*/




}
















