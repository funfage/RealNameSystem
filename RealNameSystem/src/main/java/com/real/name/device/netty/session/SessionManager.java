package com.real.name.device.netty.session;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    /**
     * 在线会话
     */
    private static final ConcurrentHashMap<Long, Session> onlineSessions = new ConcurrentHashMap<>();

    /**
     * 加入会话管理器
     */
    public static boolean putSession(Long deviceId, Session session) {
        if (!onlineSessions.containsKey(deviceId)) {
            return onlineSessions.putIfAbsent(deviceId, session) == null;
        }
        return false;
    }

    /**
     * 移除会话
     */
    public static Session removeSession(Long deviceId) {
        return onlineSessions.remove(deviceId);
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
















