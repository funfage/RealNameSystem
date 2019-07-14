package com.real.name.device.netty.utils;

import com.real.name.device.netty.model.AccessConstant;

public class ConvertUtils {

    /**
     * 若数据不够32位则填充零
     */
    public static byte[] fillData(byte[] data) {
        byte bytes[] = new byte[AccessConstant.DATA_LENGTH];
        if (data.length < AccessConstant.DATA_LENGTH) {
            System.arraycopy(data, 0, bytes, 0, data.length);
        }
        return data;
    }

    /**
     * 若扩展数据不够20位则填充零
     */
    public static byte[] fillExternalData(byte[] externalData) {
        byte bytes[] = new byte[AccessConstant.EXTERNAL_DATA_LENGTH];
        if (externalData.length < AccessConstant.EXTERNAL_DATA_LENGTH) {
            System.arraycopy(externalData, 0, bytes, 0, externalData.length);
        }
        return externalData;
    }

    /**
     * 将int转换为4个byte
     *
     * @param i
     * @return
     */
    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    /**
     * byte数组转换为int整数
     *
     * @param bytes byte数组
     * @param off   开始位置
     * @return int整数
     */
    public static int byte4ToInt(byte[] bytes, int off) {
        int b0 = bytes[off] & 0xFF;
        int b1 = bytes[off + 1] & 0xFF;
        int b2 = bytes[off + 2] & 0xFF;
        int b3 = bytes[off + 3] & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }

    /**
     * short整数转换为2字节的byte数组
     *
     * @param s short整数
     * @return byte数组
     */
    public static byte[] unsignedShortToByte2(int s) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (s >> 8 & 0xFF);
        targets[1] = (byte) (s & 0xFF);
        return targets;
    }

    /**
     * byte数组转换为无符号short整数
     *
     * @param bytes byte数组
     * @param off   开始位置
     * @return short整数
     */
    public static int byte2ToUnsignedShort(byte[] bytes, int off) {
        int high = bytes[off];
        int low = bytes[off + 1];
        return (high << 8 & 0xFF00) | (low & 0xFF);
    }

    /**
     * 转置字节数组
     *
     * @param bytes 字节数组
     * @return 转置后的字节数组
     */
    public static byte[] reverse(byte[] bytes) {
        int head = 0;
        int tail = bytes.length - 1;
        int center = bytes.length / 2;
        for (int i = 0; i < center; i++, head++, tail--) {
            byte temp = bytes[head];
            bytes[head] = bytes[tail];
            bytes[tail] = temp;
        }
        return bytes;
    }

    /**
     * 转置字节数组
     *
     * @param bytes 字节数组
     * @return 转置后的字节数组
     */
    public static byte[] reverse(byte[] bytes, int start, int end) {
        int head = start;
        int tail = end;
        int center = (end - start) / 2;
        for (int i = 0; i < center; i++, head++, tail--) {
            byte temp = bytes[head];
            bytes[head] = bytes[tail];
            bytes[tail] = temp;
        }
        return bytes;
    }


    /**
     * 字节转十六进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }


    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 指定字节数组始末转16进制
     *
     * @param bytes 需要转换的byte数组
     * @param start 起始
     * @param end   结束
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /**
     * Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte
     */
    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    /**
     * hex字符串转byte数组
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            //奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {
            //偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }

    /**
     * 将字符串转置
     */
    public static String reverseStr(String str) {
        String result = "";
        for (int i = 0; i < str.length() - 1; i += 2) {
            result = str.substring(i, i + 2) + result;
        }
        return result;
    }


}

