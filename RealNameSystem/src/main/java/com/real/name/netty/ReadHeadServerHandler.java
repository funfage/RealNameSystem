package com.real.name.netty;

import com.real.name.common.utils.ConvertCode;
import com.real.name.common.utils.SpringUtil;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class ReadHeadServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Logger logger = LoggerFactory.getLogger(ReadHeadServerHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        logger.info("DutouServerHandler start....");
        ByteBuf buf = packet.copy().content();
        String hostName = packet.sender().getHostName();
        logger.info("hostname", hostName);
        String ip = packet.sender().getAddress().getHostAddress();
        logger.info("ctx.channel().remoteAddress: ", ip);
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        //接收字节数据并转为16进制字符串
        StringBuffer receiveStr = new StringBuffer(ConvertCode.receiveHexToString(req));
        logger.info("channelRead0 receiveStr:{}", receiveStr);
        //获取功能号,占一个字节
        String functionID = receiveStr.substring(2, 4);
        String cardNo = "";
        //20为查询控制器状态命令，上传身份证信息
        if (functionID.equals("20")) {
            //获取卡号,占4个字节
            cardNo += receiveStr.substring(32, 40);
            logger.info("ReadHeadServerHandler.cardNo:", cardNo);
            //获取设备ID,占4个字节
            String deviceId = ConvertCode.HexString2IntString(receiveStr.substring(8, 16));
            logger.info("deviceId", deviceId);
            boolean flag = ControllerContainer.getInstance().addOnlineEquipment(deviceId, hostName, ctx, cardNo);
            //flag == true 是门禁读头，数据库需录入进出信息,同时返回给前端
            if (flag) {
                //插入数据库
                //根据身份证索引号在person表获得人员ID
                ApplicationContext appCtx = SpringUtil.getApplicationContext();
                PersonService personService = appCtx.getBean(PersonService.class);
                //根据身份证索引号查询人员信息
                Optional<Person> personOptional = personService.findByIdCardIndex(cardNo);
                int personId = 0;
                String personName = null;
                if (personOptional.isPresent()) {
                    Person person = personOptional.get();
                    personId = personOptional.get().getPersonId();
                    personName = person.getPersonName();
                    //map存储返回给前端的人员信息
                }
                //direction 1:in,2:out
                int direction = Integer.valueOf(receiveStr.substring(31, 32));
                //获得时间
                StringBuilder time = new StringBuilder(receiveStr.substring(40, 54));
                time.insert(4, "-");
                time.insert(7, "-");
                time.insert(10, " ");
                time.insert(13, ":");
                time.insert(16, ":");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ParsePosition pos = new ParsePosition(0);
                //获取刷卡时间
                Date swipeTime = format.parse(time.toString(), pos);
                // 获取设备信息
                DeviceService deviceService = appCtx.getBean(DeviceService.class);
                Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceId);
                Integer channel = null;
                if (deviceOptional.isPresent()) {
                    Device device = new Device();
                    channel = device.getChannel();
                }
                //保存记录
               /* Record recordSave = new Record(deviceId, DeviceConstant.AccessDeviceType, personId, personName, strtodate.getTime(), null, null, direction, channel);
                RecordMapper recordMapper = appCtx.getBean(RecordMapper.class);
                recordMapper.saveRecord(recordSave);*/
                /*WebSocket webSocket = appCtx.getBean(WebSocket.class);
                webSocket.sendMessageToAll(s);*/
            }
            logger.info("ReadHeadServerHandler stop....");
        }
        //System.out.println(functionID);

        // System.out.println("equipID:" +deviceId);
       /*Controller dutouController = new Controller();
        dutouController.setDevice_id(deviceId);
        dutouController.setCtx(ctx);
        dutouController.setIp(hostName);
        System.out.println(dutouController);

        //System.out.println("over");
        byte[] opengate = ReadHeadCmdSend.openGateCmd(deviceId,1);
        System.out.println("cmd:"+opengate);
        //byte[] bytes = opengate.getBytes("UTF-8");
        //ctx.writeAndFlush(opengate);
        InetSocketAddress send = new InetSocketAddress(packet.sender().getHostName(),60000);
        DatagramPacket data = new DatagramPacket(Unpooled.copiedBuffer(opengate), send);
        ctx.writeAndFlush(data);*/

        //            String body = new String(req, "UTF-8");
//            System.out.println(body);//打印收到的信息
//            //向客户端发送消息
//            String json = "服务端发送消息: 南无阿弥陀佛";
//            // 由于数据报的数据是以字符数组传的形式存储的，所以传转数据
//            byte[] bytes = json.getBytes("UTF-8");
//            DatagramPacket data = new DatagramPacket(Unpooled.copiedBuffer(bytes), packet.sender());
//            ctx.writeAndFlush(data);//向客户端发送消息
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx);
    }
}
