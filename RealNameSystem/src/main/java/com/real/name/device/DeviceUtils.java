package com.real.name.device;




import com.alibaba.fastjson.JSONObject;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.HTTPTool;
import com.real.name.device.entity.Device;
import com.real.name.person.entity.Person;
import com.real.name.issue.entity.FaceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceUtils {

    public final static String deviceHeartBeat = "setDeviceHeartBeat";

    private static Logger logger = LoggerFactory.getLogger(DeviceUtils.class);

    //判断设备是否在线
    public static boolean isOnLine(String ip, Integer outPort) {
        return false;
    }

    /**
     * 注册心跳
     * @param device
     * @return
     */
    public static boolean registerHeartBeat(Device device) {
        Map<String, Object> map = new HashMap<>();
        //设备注册心跳地址
        String url = getWholeUrl(deviceHeartBeat, device);
        map.put("pass", device.getPass());
        map.put("url", url);
        String response = HTTPTool.postUrlForParam(url, map);
        if (StringUtils.hasText(response)) {
            FaceResult faceResult = JSONObject.parseObject(response, FaceResult.class);
            return faceResult.getSuccess();
        }
        return false;
    }

    /**
     * 得到完整的URL
     * @param baseUrl
     * @param device
     * @return
     */
    public static String getWholeUrl(String baseUrl, Device device) {
        return "http://" + device.getIp() + ":" + device.getOutPort() + "/" + baseUrl;
    }

    /**
     * ===========================================================================================
     */

    /**
     * 根据workRole向人脸识别设备发送人员信息
     */
    public static Map<String, Object> sendPersonInfo(Person person, List<Device> projectDevice, List<Device> allDevices) {
        Integer workRole = person.getWorkRole();
        // 给设备发送人员信息
        Map<String, Object> map = new HashMap<>();
        map.put("person", person.toJSON());
        String url = "person/create";
        Map<String, Object> resultMap;
        if (workRole == 20) { // 如果是建筑工人，发送给该工人所属项目的设备
            resultMap = HTTPTool.sendDataToFaceDeviceByProjectCode(url, map, DeviceConstant.postMethod, projectDevice);
        } else if (workRole == 10){ // 如果是管理工人，发送给全部设备
            resultMap = HTTPTool.sendDataToFaceDevice(url, map, DeviceConstant.postMethod, allDevices);
        } else {
            throw new AttendanceException(ResultError.PERSON_EMPTY);
        }
        return resultMap;
    }

    /**
     * 查询指定设备人员信息
     * @param device 设备信息
     * @param person 人员信息
     * @return 是否查询人员信息成功
     */
    public static Boolean queryPersonByDeviceId(Device device, Person person) {
        try {
            //查询设备信息是否有人员信息
            Map<String, Object> queryMap = sendQueryPerson(person.getPersonId().toString(),  device);
            String queryResponse = (String) queryMap.get(device.getDeviceId());
            //接收参数为空或者请求超时
            if (!StringUtils.hasText(queryResponse) || queryResponse.equals(DeviceConstant.connectTimeOut)) {
                logger.error("获取设备响应queryResponse信息：{}失败", queryResponse);
                //人员下发失败
                return false;
            } else {
                FaceResult queryResult = JSONObject.parseObject(queryResponse, FaceResult.class);
                Boolean isSuccess = queryResult.getSuccess();
                return isSuccess != null && isSuccess;
            }
        } catch (Exception e) {
            logger.error("queryPersonByDeviceId error e:{}", e);
            return false;
        }
    }

    /**
     * 下发人员信息到指定设备
     * @param device
     * @param person
     * @return
     */
    public static Boolean issuePersonByDeviceId(Device device, Person person) {
        try {
            Map<String, Object> sendMap = sendPersonByDeviceId(device, person);
            String sendResponse = (String) sendMap.get(device.getDeviceId());
            //接收参数为空或者请求超时
            if (!StringUtils.hasText(sendResponse) || sendResponse.equals(DeviceConstant.connectTimeOut)) {
                logger.error("获取设备响应sendResponse信息：{}失败", sendResponse);
                //人员下发失败
                return false;
            } else {
                FaceResult sendResult = JSONObject.parseObject(sendResponse, FaceResult.class);
                Boolean isSuccess = sendResult.getSuccess();
                return isSuccess != null && isSuccess;
            }
        } catch (Exception e) {
            logger.error("issuePersonByDeviceId error e:{}", e);
            return false;
        }
    }

    /**
     * 下发照片并查询是否成功下发照片
     * @return true则下发成功
     */
    public static Boolean issueAndQueryImageByDeviceId(Device device, Person person) {
        try {
            Boolean isSuccess;
            Map<String, Object> imgMap = sendHeadImgByDevice(device, person);
            String imgResponse = (String) imgMap.get(device.getDeviceId());
            //接收参数为空或者请求超时
            if (!StringUtils.hasText(imgResponse) || imgResponse.equals(DeviceConstant.connectTimeOut)) {
                logger.warn("sendHeadImgByDevice获取设备响应信息失败, e:{}", imgResponse);
                return false;
            }else {
                FaceResult imgResult = JSONObject.parseObject(imgResponse, FaceResult.class);
                isSuccess = imgResult.getSuccess();
                if(isSuccess != null && isSuccess){
                    return true;
                } else {
                    return queryImageByDeviceId(device, person);
                }
            }
        } catch (Exception e) {
            logger.error("issueAndQueryImageByDeviceId error e:{}", e);
            return false;
        }
    }

    /**
     * 想指定设备下发人脸信息
     * @param device
     * @param person
     * @return
     */
    public static Boolean issueImageByDeviceId(Device device, Person person) {
        try {
            Map<String, Object> sendMap = sendHeadImgByDevice(device, person);
            String sendResponse = (String) sendMap.get(device.getDeviceId());
            if (!StringUtils.hasText(sendResponse) || sendResponse.equals(DeviceConstant.connectTimeOut)) {
                logger.warn("sendHeadImgByDevice获取设备响应信息失败, e:{}", sendResponse);
                return false;
            } else {
                FaceResult sendResult = JSONObject.parseObject(sendResponse, FaceResult.class);
                Boolean isSuccess = sendResult.getSuccess();
                //返回是否查询成功
                return isSuccess != null && isSuccess;
            }
        } catch (Exception e) {
            logger.error("sendImageByDeviceId error e:{}", e);
            return false;
        }
    }


    /**
     * 查询指定设备照片信息
     * @param device 设备信息
     * @param person 下发的人员信息
     * @return 是否查询成功
     */
    public static Boolean queryImageByDeviceId(Device device, Person person) {
        try {
            //调用设备查询照片接口
            Map<String, Object> queryMap = sendQueryImage(person.getPersonId().toString(), device);
            String queryResponse = (String) queryMap.get(device.getDeviceId());
            if (!StringUtils.hasText(queryResponse) || queryResponse.equals(DeviceConstant.connectTimeOut)) {
                logger.warn("sendQueryImage获取设备响应信息失败, e:{}", queryResponse);
                return false;
            }
            FaceResult queryResult = JSONObject.parseObject(queryResponse, FaceResult.class);
            Boolean isSuccess = queryResult.getSuccess();
            //返回是否查询成功
            return isSuccess != null && isSuccess;
        } catch (Exception e) {
            logger.error("queryImageByDeviceId error, e{}", e);
            return false;
        }
    }

    /**
     * ==========================================================================================
     */

    /**
     * 下发人员照片到指定设备
     * @param device 指定下发的设备
     * @param person 下发的人员
     */
    public static Map<String, Object> sendHeadImgByDevice(Device device, Person person) {
        String url = "face/create";
        Map<String, Object> map = new HashMap<>();
        Integer personId = person.getPersonId();
        String imgBase64 = person.getHeadImage();
        map.put("personId", personId);
        map.put("imgBase64", imgBase64);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }

    /**
     * 向指定设备发送人员信息
     * @param device
     * @param person
     * @return
     */
    public static Map<String, Object> sendPersonByDeviceId(Device device, Person person) {
        String url = "person/create";
        Map<String, Object> map = new HashMap<>();
        JSONObject personJSON = new JSONObject();
        personJSON.put("name", person.getPersonName());
        personJSON.put("id", person.getPersonId());
        map.put("person", personJSON.toJSONString());
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }

    /**
     * 查询指定 id 的人员信息后者
     * 若id为-1则根据页数查询
     * 若peronId 传入-1 表示不局限于 personId 查询人员
     * @param personId 人员id
     * @param length 每页最大数量
     * @param index 页码
     */
    public static Map<String, Object> sendQueryPagesPerson(String personId, Integer length, Integer index, Device device) {
        String url = "/person/find?pass={pass}&personId={personId}&length={length}&index={index}";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", personId);
        map.put("length", length);
        map.put("index", index);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.getMethod, device);
    }

    /**
     * 根据id查询人员信息
     * @param id
     * @param device
     * @return
     */
    public static Map<String, Object> sendQueryPerson(String id, Device device) {
        String url = "/person/find?pass={pass}&id={id}";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.getMethod, device);
    }



    /**
     * 查询指定设备照片信息
     * @param personId 人员id
     * @param device 设备
     */
    public static Map<String, Object> sendQueryImage(String personId, Device device) {
        String url = "/face/find";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", personId);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }


}
