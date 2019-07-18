package com.real.name.device.netty.model;

public interface AccessFunction {

    /**
     * 查询控制器状态
     */
    byte QUERY_ACCESS_STATUS = 0x20;

    /**
     * 权限添加
     */
    byte ADD_AUTHORITY = 0x50;

    /**
     * 权限查询
     */
    byte QUERY_AUTHORITY = 0x5A;

    /**
     * 搜索控制器
     */
    byte SEARCH_ACCESS = (byte)0x94;

    /**
     * 权限清空
     */
    byte CLEAR_AUTHORITY = 0x54;

}
