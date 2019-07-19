package com.real.name.device.service.implement;

import com.real.name.common.info.DeviceConstant;
import com.real.name.common.utils.JedisService;
import com.real.name.device.entity.Device;
import com.real.name.issue.service.DeleteInfoService;
import com.real.name.record.entity.Record;
import com.real.name.device.netty.UDPClient;
import com.real.name.device.netty.utils.ConvertUtils;
import com.real.name.device.service.AccessService;
import com.real.name.device.service.AccessType;
import com.real.name.device.service.DeviceService;
import com.real.name.record.service.RecordService;
import com.real.name.issue.entity.IssueAccess;
import com.real.name.issue.service.IssueAccessService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

    @Autowired
    private IssueAccessService issueAccessService;

    @Autowired
    private AccessService accessService;

    @Autowired
    private DeleteInfoService deleteInfoService;

    /**
     * 查询设备状态
     */
    @Override
    public void queryAccessStatus(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address) {
        logger.info("查询控制器转态获取到的数据data:{}", Arrays.toString(data));
        String dataStr = ConvertUtils.bytesToHex(data);
        logger.info("十六进制数据为dataStr:{}", dataStr);
        //获取卡号
        String cardNoStr = dataStr.substring(16, 24);
        logger.info("十六进制卡号为cardNoStr:", cardNoStr);
        byte[] cardNoBytes = ConvertUtils.reverse(ConvertUtils.hexToByteArray(cardNoStr));
        String cardNo = ConvertUtils.byte4ToInt(cardNoBytes, 0) + "";
        logger.warn("接收到的身份证索引号为cardNo:{}", cardNo);
        jedisStrings.set(deviceId + "", cardNo, 10, TimeUnit.MINUTES);
        //获取进出的有效性
        int verifyEnter = Integer.parseInt(dataStr.substring(10, 12), HexRadix);
        //如果为1标识有效, 保存进出记录
        if (verifyEnter == 1) {
            //获取记录类型
            int type = Integer.parseInt(dataStr.substring(8, 10), HexRadix);
            //获取时间字符串 格式为yyyy-MM-dd HH:mm:ss
            String timeStr = insertTime(dataStr.substring(24, 38));
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

    /**
     * 查询用户权限
     */
    @Override
    public void queryAuthority(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address) {
        //获取查询的身份证索引号
        String cardIndexHex = ConvertUtils.bytesToHex(data, 0, 3);
        byte[] cardIndexBytes = ConvertUtils.reverse(ConvertUtils.hexToByteArray(cardIndexHex));
        int cardIndex = ConvertUtils.byte4ToInt(cardIndexBytes, 0);
        logger.info("查询的身份证索引号为:{}", cardIndex);
        Optional<Person> personOptional = personService.findByIdCardIndex(cardIndex + "");
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            if (cardIndex != 0) {//接收到卡号则说明用户有权限
                //修改下发标识
                IssueAccess issueAccess = new IssueAccess(person, new Device(deviceId + ""), 1);
                issueAccessService.updateIssueAccess(issueAccess);
            } else {
                //否则没有权限,删除标识删除失败的记录
                deleteInfoService.deleteByCondition(person.getPersonId(), deviceId + "");
            }
        }
    }

    /**
     * 接收搜索控制器的报文
     */
    @Override
    public void searchAuthority(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address) {
        String ipHexStr = ConvertUtils.bytesToHex(data, 0, 3);
        String ip = getIp(ipHexStr);
        //设置十分钟有效
        jedisStrings.set(deviceId.toString() + DeviceConstant.SEARCH_ACCESS, ip, 10, TimeUnit.MINUTES);
    }

    /**
     * 清空设备权限
     */
    @Override
    public void clearAuthority(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address) {
        if (data[0] == 1) {
            jedisStrings.set(deviceId + DeviceConstant.CLEAR_AUTHORITY, true, 10, TimeUnit.MINUTES);
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

    private String getIp(String ipHexStr) {
        String ip = "";
        ip += Integer.parseInt(ipHexStr.substring(0, 2), HexRadix) + ".";
        ip += Integer.parseInt(ipHexStr.substring(2, 4), HexRadix) + ".";
        ip += Integer.parseInt(ipHexStr.substring(4, 6), HexRadix) + ".";
        ip += Integer.parseInt(ipHexStr.substring(6, 8), HexRadix);
        return ip;
    }
}
