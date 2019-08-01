package com.real.name.device.netty.utils;


import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.real.name.common.info.CommConstant;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.schedule.entity.FaceRecordData;
import com.real.name.common.utils.HTTPTool;
import com.real.name.common.websocket.WebSocket;
import com.real.name.device.entity.Device;
import com.real.name.issue.entity.FaceResult;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.service.IssueFaceService;
import com.real.name.person.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FaceDeviceUtils {

    private static Logger logger = LoggerFactory.getLogger(FaceDeviceUtils.class);

    @Autowired
    private IssueFaceService issueFaceService;

    @Autowired
    private WebSocket webSocket;

    private static FaceDeviceUtils deviceUtils;

    @PostConstruct
    public void init() {
        deviceUtils = this;
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
     *重发下发失败的信息
     */


    /**
     * 下发人员信息到多个设备
     * @param times 表示下发的次数
     */
    public static void issuePersonToDevices(List<Device> deviceList, Person person, int times, String teamName) {
        for (Device device : deviceList) {
            issuePersonToOneDevice(device, person, times);
            if (teamName != null) {
                JSONObject map = new JSONObject();
                map.put("projectCode", device.getProjectCode());
                map.put("teamName", teamName);
                map.put("type", CommConstant.ENTER_TYPE);
                deviceUtils.webSocket.sendMessageToAll(map.toJSONString());
            }
        }
    }

    public static void updatePersonToDevices(List<Device> deviceList, Person person, int times) {
        for (Device device : deviceList) {
            updatePersonToOneDevice(device, person, times);
        }
    }

    /**
     * 下发照片信息到多个设备
     */
    public static void updateImageToDevices(List<Device> deviceList, Person person, int times) {
        for (Device device : deviceList) {
            updateImageToOneDevice(device, person, times);
        }
    }

    /**
     * 下发人员和照片信息到某个设备
     */
    public static void issuePersonToOneDevice(Device device, Person person, int times) {
        //只有当deviceId和personId都不为空才修改
        if (StringUtils.hasText(device.getDeviceId()) && person.getPersonId() != null) {
            //记录下发的次数
            int issuePerson = 0;
            //下发三次
            while (issuePerson < times) {
                Boolean isSuccess = issuePersonByDeviceId(device, person);
                if (isSuccess) { //成功则下发照片信息
                    IssueFace issueFace = new IssueFace();
                    issueFace.setDevice(device);
                    issueFace.setPerson(person);
                    issueFace.setIssuePersonStatus(1);
                    issueFace.setIssueImageStatus(0);
                    //防止前面设置过imageStatus的值
                    int issueImage = 0;
                    //下发三次照片信息
                    while (issueImage < times) {
                        Boolean isImageIssue = issueImageByDeviceId(device, person);
                        if (isImageIssue) {
                            issueFace.setIssueImageStatus(1);
                            break;
                        }
                        issueImage++;
                    }
                    //更新设备信息
                    deviceUtils.issueFaceService.updateByPersonIdAndDeviceId(issueFace);
                    break;
                }
                issuePerson++;
            }
        } else {
            logger.warn("issuePersonToOneDevice error 用户id或设备id为空");
        }
    }

    /**
     * 下发照片信息到某个设备
     */
    public static void issueImageToOneDevice(Device device, Person person, int times) {
        if (StringUtils.hasText(device.getDeviceId()) && person.getPersonId() != null) {
            int issueImage = 0;
            //下发三次照片信息
            while (issueImage < times) {
                Boolean isImageIssue = issueImageByDeviceId(device, person);
                if (isImageIssue) {
                    IssueFace issueFace = new IssueFace();
                    issueFace.setDevice(device);
                    issueFace.setPerson(person);
                    //设置下发照片信息成功标识
                    issueFace.setIssuePersonStatus(1);//防止前面设置过personStatus的值
                    issueFace.setIssueImageStatus(1);
                    //更新设备信息
                    deviceUtils.issueFaceService.updateByPersonIdAndDeviceId(issueFace);
                    break;
                }
                issueImage++;
            }
            //表明没有下发成功过
            if (issueImage >= times) {
                IssueFace issueFace = new IssueFace();
                issueFace.setDevice(device);
                issueFace.setPerson(person);
                //设置下发失败标识
                issueFace.setIssueImageStatus(0);
                deviceUtils.issueFaceService.updateByPersonIdAndDeviceId(issueFace);
            }
        } else {
            logger.warn("issueImageToOneDevice error 用户id或设备id为空");
        }
    }

    /**
     *
     * @param device
     * @param person
     * @param times
     * @return
     */
    public static boolean updatePersonToOneDevice(Device device, Person person, int times) {
        if (StringUtils.hasText(device.getDeviceId()) && person.getPersonId() != null) {
            int updatePerson = 0;
            while (updatePerson < times) {
                Boolean isSuccess = issueUpdatePersonInfo(device, person);
                if (isSuccess) {
                    IssueFace issueFace = new IssueFace();
                    issueFace.setDevice(device);
                    issueFace.setPerson(person);
                    //设置下发照片信息成功标识
                    issueFace.setIssuePersonStatus(1);
                    //更新设备信息
                    deviceUtils.issueFaceService.updateByPersonIdAndDeviceId(issueFace);
                    break;
                }
                updatePerson++;
            }
            //表明没有下发成功过
            if (updatePerson >= times) {
                IssueFace issueFace = new IssueFace();
                issueFace.setDevice(device);
                issueFace.setPerson(person);
                //设置失败标识, -1表示更新失败
                issueFace.setIssuePersonStatus(-1);
                deviceUtils.issueFaceService.updateByPersonIdAndDeviceId(issueFace);
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新某个设备的照片信息
     * @param device
     * @param person
     * @param times
     */
    public static boolean updateImageToOneDevice(Device device, Person person, int times) {
        if (StringUtils.hasText(device.getDeviceId()) && person.getPersonId() != null) {
            int updateImage = 0;
            //下发三次照片信息
            while (updateImage < times) {
                Boolean isImageIssue = issueUpdateImage(device, person);
                if (isImageIssue) {
                    IssueFace issueFace = new IssueFace();
                    issueFace.setDevice(device);
                    issueFace.setPerson(person);
                    //设置下发照片信息成功标识
                    issueFace.setIssueImageStatus(1);
                    //更新设备信息
                    deviceUtils.issueFaceService.updateByPersonIdAndDeviceId(issueFace);
                    break;
                }
                updateImage++;
            }
            //表明没有下发成功过
            if (updateImage >= times) {
                IssueFace issueFace = new IssueFace();
                issueFace.setDevice(device);
                issueFace.setPerson(person);
                //设置失败标识, -1表示更新失败
                issueFace.setIssueImageStatus(-1);
                deviceUtils.issueFaceService.updateByPersonIdAndDeviceId(issueFace);
            } else {
                return true;
            }
        } else {
            logger.warn("issueImageToOneDevice error 用户id或设备id为空");
        }
        return false;
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
            logger.error("queryPersonByDeviceId error e:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 下发人员信息到指定设备
     * @param device
     * @param person
     * @return
     */
    private static Boolean issuePersonByDeviceId(Device device, Person person) {
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
            logger.error("issuePersonByDeviceId error e:{}", e.getMessage());
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
            logger.error("issueAndQueryImageByDeviceId error e:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 想指定设备下发人脸信息
     * @param device
     * @param person
     * @return
     */
    private static Boolean issueImageByDeviceId(Device device, Person person) {
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
            logger.error("sendImageByDeviceId error e:{}", e.getMessage());
            return false;
        }
    }

    public static Boolean issueUpdatePersonInfo(Device device, Person person) {
        try {
            Map<String, Object> updateMap = sendUpdatePersonInfoByDevice(device, person);
            String updateResponse = (String) updateMap.get(device.getDeviceId());
            if (!StringUtils.hasText(updateResponse) || updateResponse.equals(DeviceConstant.connectTimeOut)) {
                logger.warn("sendUpdatePersonInfoByDevice获取设备响应信息失败, e:{}", updateResponse);
                return false;
            } else {
                FaceResult sendResult = JSONObject.parseObject(updateResponse, FaceResult.class);
                Boolean isSuccess = sendResult.getSuccess();
                //返回是否查询成功
                return isSuccess != null && isSuccess;
            }
        } catch (Exception e) {
            logger.error("issueUpdatePersonInfo error e:{}", e.getMessage());
            return false;
        }
    }
    /**
     *更新人员的照片信息
     */
    private static Boolean issueUpdateImage(Device device, Person person) {
        try {
            Map<String, Object> updateMap = sendUpdateImageByDevice(device, person);
            String updateResponse = (String) updateMap.get(device.getDeviceId());
            if (!StringUtils.hasText(updateResponse) || updateResponse.equals(DeviceConstant.connectTimeOut)) {
                logger.warn("sendHeadImgByDevice获取设备响应信息失败, e:{}", updateResponse);
                return false;
            } else {
                FaceResult sendResult = JSONObject.parseObject(updateResponse, FaceResult.class);
                Boolean isSuccess = sendResult.getSuccess();
                //返回是否查询成功
                return isSuccess != null && isSuccess;
            }
        } catch (Exception e) {
            logger.error("issueUpdateImage error, e:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 发送发送重置报文到设备
     * @param device
     * @return
     */
    public static Boolean issueResetDevice(Device device) {
        try {
            Map<String, Object> sendMap = sendResetDevice(device);
            String sendResponse = (String) sendMap.get(device.getDeviceId());
            if (!StringUtils.hasText(sendResponse) || sendResponse.equals(DeviceConstant.connectTimeOut)) {
                logger.warn("issueResetDevice获取设备响应信息失败, e:{}", sendResponse);
                return false;
            } else {
                FaceResult sendResult = JSONObject.parseObject(sendResponse, FaceResult.class);
                Boolean isSuccess = sendResult.getSuccess();
                //返回是否查询成功
                return isSuccess != null && isSuccess;
            }
        } catch (Exception e) {
            logger.error("issueResetDevice error,  e:", e.getMessage());
            return false;
        }
    }


    /**
     * 查询指定设备照片信息
     * @param device 设备信息
     * @param person 下发的人员信息
     * @return 是否查询成功
     */
    private static Boolean queryImageByDeviceId(Device device, Person person) {
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
            logger.error("queryImageByDeviceId error, e{}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取用户在某个识别的识别记录
     * @param device 需要查询的设备
     * @param personId 查询指定 id 的人员识别记录, 传入-1 可查询所有人员的识别记录，包括陌生人,
     *                 传入 STRANGERBABY，可查询所有陌生人/识别失败记录
     * @param length 传入-1 为不分页; 若不传-1，请务必大于 0
     * @param index 页码，从 0 开始
     * @param startTime 若需要按时间查询，请按照如下格式（年-月-日 时:分:秒）：
     * @param endTime 截止时间
     */
    public static FaceRecordData getPersonRecords(Device device, Integer personId, Integer length, Integer index, String startTime, String endTime) {
        try {
            Map<String, Object> queryMap = sendQueryPersonRecords(device, personId, length, index, startTime, endTime);
            String queryResponse = (String) queryMap.get(device.getDeviceId());
            if (!StringUtils.hasText(queryResponse) || queryResponse.equals(DeviceConstant.connectTimeOut)) {
                logger.warn("sendQueryPersonRecords响应信息失败, queryResponse:{}", queryResponse);
                return null;
            } else {
                FaceResult faceResult = JSONObject.parseObject(queryResponse, FaceResult.class);
                Boolean isSuccess = faceResult.getSuccess();
                if (isSuccess) {
                    Object data = faceResult.getData();
                    return JSONObject.parseObject(JSONUtils.toJSONString(data), FaceRecordData.class);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error("getPersonRecords error");
            return null;
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
    private static Map<String, Object> sendHeadImgByDevice(Device device, Person person) {
        String url = "face/create";
        Map<String, Object> map = new HashMap<>();
        Integer personId = person.getPersonId();
        String imgBase64 = person.getHeadImage();
        map.put("personId", personId);
        map.put("faceId", personId);
        map.put("imgBase64", imgBase64);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }

    /**
     * 向指定设备发送人员信息
     */
    private static Map<String, Object> sendPersonByDeviceId(Device device, Person person) {
        String url = "/person/create";
        Map<String, Object> map = new HashMap<>();
        JSONObject personJSON = new JSONObject();
        personJSON.put("id", person.getPersonId().toString());
        personJSON.put("name", person.getPersonName());
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
     */
    public static Map<String, Object> sendQueryPerson(String personId, Device device) {
        String url = "/person/find?pass={pass}&id={id}";
        Map<String, Object> map = new HashMap<>();
        map.put("id", personId);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.getMethod, device);
    }



    /**
     * 查询指定设备照片信息
     * @param personId 人员id
     * @param device 设备
     */
    private static Map<String, Object> sendQueryImage(String personId, Device device) {
        String url = "/face/find";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", personId);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }

    /**
     * 更新某个设备的照片信息
     */
    public static Map<String, Object> sendUpdateImageByDevice(Device device, Person person) {
        String url = "/face/update";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", person.getPersonId());
        map.put("faceId", person.getPersonId());
        map.put("imgBase64", person.getHeadImage());
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }

    public static Map<String, Object> sendUpdatePersonInfoByDevice(Device device, Person person) {
        String url = "/person/update";
        Map<String, Object> map = new HashMap<>();
        JSONObject personJSON = new JSONObject();
        personJSON.put("id", person.getPersonId());
        personJSON.put("name", person.getPersonName());
        map.put("person", personJSON.toJSONString());
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }

    /**
     *发送重置设备信息请求
     * @return true则重置成功
     */
    private static Map<String, Object> sendResetDevice(Device device) {
        String url = "/device/reset";
        Map<String, Object> map = new HashMap<>();
        map.put("delete", true);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }

    /**
     * 获取人脸设备的序列号
     */
    public static FaceResult getDeviceKey(Device device) {
        String url = "http://" + device.getIp() + ":" + device.getOutPort() + "/getDeviceKey";
        try {
            String result = HTTPTool.postUrlForParam(url, null);
            if (result != null) {
                return JSONObject.parseObject(result, FaceResult.class);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("getDeviceKey error e:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 刷脸记录查询
     */
    public static Map<String, Object> sendQueryPersonRecords(Device device, Integer personId, Integer length, Integer index, String startTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        String url = "/findRecords";
        map.put("personId", personId.toString());
        map.put("length", length);
        map.put("index", index);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }

    /**
     * 删除人员信息
     */
    public static Map<String, Object> deleteDevicePersonInfo(Device device, Integer personId) {
        Map<String, Object> map = new HashMap<>();
        String url = "/person/delete";
        map.put("id", personId.toString());
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.postMethod, device);
    }

}



















