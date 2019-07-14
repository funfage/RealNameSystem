package com.real.name.device.service.implement;

import com.real.name.common.info.DeviceConstant;
import com.real.name.common.utils.JedisService;
import com.real.name.device.entity.Device;
import com.real.name.device.entity.Record;
import com.real.name.device.netty.UDPClient;
import com.real.name.device.netty.utils.ConvertUtils;
import com.real.name.device.service.AccessType;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.RecordService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class AccessTypeImpl implements AccessType {

    private Logger logger = LoggerFactory.getLogger(AccessTypeImpl.class);

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final int HexRadix = 16;

    @Autowired
    private UDPClient udpClient;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    @Autowired
    private JedisService.JedisKeys jedisKeys;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PersonService personService;

    @Autowired
    private RecordService recordService;

    @Override
    public void queryAccessStatus(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address) {
        System.out.println("查询控制器转态获取到的数据:");
        String dataStr = ConvertUtils.bytesToHex(data);
        //获取卡号
        String cardNoStr = dataStr.substring(32, 40);
        byte[] cardNoBytes = ConvertUtils.hexToByteArray(cardNoStr);
        cardNoBytes = ConvertUtils.reverse(cardNoBytes);
        String cardNo = ConvertUtils.byte4ToInt(cardNoBytes, 0) + "";
        jedisStrings.set(deviceId + "", cardNo, 10, TimeUnit.MINUTES);
        //获取进出的有效性
        int verifyEnter = Integer.parseInt(dataStr.substring(26, 28), HexRadix);
        //如果为1标识有效, 保存进出记录
        if (verifyEnter == 1) {
            //获取记录类型
            int type = Integer.parseInt(dataStr.substring(24, 26), HexRadix);
            //获取时间字符串 格式为yyyy-MM-dd HH:mm:ss
            String timeStr = insertTime(dataStr.substring(40, 54));
            Date date = null;
            try {
                date =  dateFormat.parse(timeStr);
            } catch (ParseException e) {
                logger.error("时间转换错误, e:{}", e);
            }
            Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceId + "");
            if (deviceOptional.isPresent() && date != null) {
                Optional<Person> personOptional = personService.findByIdCardIndex(cardNo);
                if (personOptional.isPresent()) {
                    Device device = deviceOptional.get();
                    Person person = personOptional.get();
                    //保存记录
                    Record record = new Record(deviceId + "",  DeviceConstant.AccessDeviceType, person.getPersonId(),
                            person.getPersonName(), date.getTime(), date, type, null, device.getDirection(), device.getChannel());
                    recordService.saveRecord(record);
                    logger.info("成功保存一条控制器识别记录");
                }
            }
        }
    }

    private String insertTime(String timeStr) {
        StringBuilder timeSb = new StringBuilder(timeStr);
        timeSb.insert(4, "-");
        timeSb.insert(7, "-");
        timeSb.insert(10, " ");
        timeSb.insert(13, ":");
        timeSb.insert(16, ":");
        return timeSb.toString();
    }
}
