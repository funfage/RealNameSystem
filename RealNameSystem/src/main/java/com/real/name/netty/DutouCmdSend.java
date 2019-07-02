package com.real.name.netty;

import com.real.name.common.utils.ConvertCode;
import com.real.name.common.utils.TimeUtil;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 下发给读头的协议进行组装
 */
public class DutouCmdSend {

    private static Logger logger = LoggerFactory.getLogger(DutouCmdSend.class);

    /**
     * 0x20远程开门命令帧
     * @param equipmentID 设备ID
     * @param gateNo 通道号（1：进入的门，2：出去的门）
     * @return
     */
    public static byte[] openGateCmd(String equipmentID, int gateNo){
        byte[] result =new byte[64];

        ByteBuffer  frame = ByteBuffer.allocate(64) ;
        int number = Integer.valueOf(equipmentID);
        String equipmentIDofHEX = ConvertCode.intToHexString(number,4);
        String equipmentIDrev = ConvertCode.reverseStr(equipmentIDofHEX);
        System.out.println("DutouCmdSend:equipmentIDofHEX:" +equipmentIDofHEX);
        /*String cmd = "17" +"400000"+equipmentIDrev +"0"+String.valueOf(gateNo)+
                "000000000000000000000000000000000000000000" +
                "000000000000000000000000" +
                "00000000000000000000000000000000000000000000";*/
        /*result[0] =0x19;
        result[1] =0x40;
        result[2] = 0x00;
        result[3] = 0x00;*/
        frame.put((byte) 0x19);
        frame.put((byte)0x40);
        frame.put((byte)0x00);
        frame.put((byte)0x00);
        //frame.put((byte)0x00);
        ByteBuffer equip = ByteBuffer.allocate(4);
        equip.order(ByteOrder.LITTLE_ENDIAN);
        equip.putInt(number);
        byte[] equip2 = equip.array();
        frame.put(equip2);
        //frame.put(equip);
        //frame.put
        //frame.putInt(number);
        frame.put((byte) gateNo);
        //frame.order()
        result=frame.array();
        //frame.array()
        return result;
    }

    /**
     *0x50权限添加帧
     * @param equipmentID
     * @param cardNo
     * @return
     */
    public static byte[] addAuthority(String equipmentID, String cardNo){
        ByteBuffer  frame = ByteBuffer.allocate(64) ;
        int number = Integer.valueOf(equipmentID);
        String equipmentIDofHEX = ConvertCode.intToHexString(number,4);
        logger.info("equipmentID十六进制数", equipmentIDofHEX);
        frame.put((byte)0x19);
        frame.put((byte)0x50);
        frame.put((byte)0x00);
        frame.put((byte)0x00);
        ByteBuffer equip = ByteBuffer.allocate(8);
        equip.order(ByteOrder.LITTLE_ENDIAN);
        //设置设备序列号
        equip.putInt(number);
        //对字节数组进行逆序
        String cardNoRever = ConvertCode.reverseStr(cardNo);
        int cardNoInt = Integer.parseInt(cardNoRever,16);
        equip.putInt(cardNoInt);
        byte[] bytes = equip.array();
        frame.put(bytes);
        byte[] startTime = TimeUtil.getBCDTime();
        byte[] endTime = TimeUtil.getBCDTime2();
        frame.put(startTime);
        frame.put(endTime);
        frame.put((byte)0x01 );
        frame.put((byte)0x01 );
        frame.put((byte)0x01 );
        frame.put((byte)0x01 );
        byte [] result = frame.array();
        return result;
    }
}
