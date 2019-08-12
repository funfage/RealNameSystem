package com.real.name.nation.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.real.name.nation.entity.NationalGroup;
import com.real.name.nation.entity.SearchProject;
import com.real.name.nation.entity.WorkerWrapper;
import com.real.name.common.constant.NationConstant;
import com.real.name.common.utils.AesUtils;
import com.real.name.common.utils.CommonUtils;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectPersonDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NationalUtils {

    private static Logger logger = LoggerFactory.getLogger(NationalUtils.class);

    /**
     * @param data   这个是对象序列化之后的字符串
     */
    public static String postDate(String method, String data) {
        BaseRequest request = new BaseRequest() {
        };
        Map<String, String> dataMap = getMap(data, method);
        return request.postData(NationConstant.url, null, dataMap);
    }

    private static Map<String, String> getMap(String data, String method) {
        String guid = UUID.randomUUID().toString().replace("-", "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        String sortString = "appid=" + NationConstant.appid;
        sortString += "&data=" + data;
        sortString += "&format=" + NationConstant.format;
        sortString += "&method=" + method;
        sortString += "&nonce=" + guid;
        sortString += "&timestamp=" + timestamp;
        sortString += "&version=" + NationConstant.version;
        sortString += "&appsecret=" + NationConstant.appsecret;
        String sign = EncryptionHelper.getSHA256StrJava(sortString.toLowerCase());
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("appid", NationConstant.appid);
        dataMap.put("data", data);
        dataMap.put("format", NationConstant.format);
        dataMap.put("method", method);
        dataMap.put("nonce", guid);
        dataMap.put("timestamp", timestamp);
        dataMap.put("version", NationConstant.version);
        dataMap.put("sign", sign);
        return dataMap;
    }
    /**
     * 上传项目信息
     */
    public static JSONObject uploadProject(Project project){
        return null;
    }

    /**
     * 查询项目信息
     */
    public static JSONObject queryProject(Integer pageIndex, Integer pageSize) {
        String method = "Project.Query";
        SearchProject sp = new SearchProject(pageIndex, pageSize);
//        sp.setContractorCorpCode(NationConstant.CORPCODE);
//        sp.setProjectCode(NationConstant.APPID);
        String str = JSON.toJSONString(sp);
        Map<String, String> dataMap = NationalUtils.getMap(str, method);
        BaseRequest request = new BaseRequest() {
        };
        String res = request.postData(NationConstant.url, null, dataMap);
        return HandleResultReturn(res);
    }

    /**
     * 创建班组
     */
    public static JSONObject uploadGroup(WorkerGroup group) {
        String method = "Team.Add";
        String appsecret = NationConstant.appsecret;
        WorkerGroup ng = new WorkerGroup();
        ng.setProjectCode(group.getProjectCode());
        ng.setCorpCode(group.getCorpCode());
        ng.setCorpName(group.getCorpName());
        ng.setTeamName(group.getTeamName());
        ng.setResponsiblePersonPhone(group.getResponsiblePersonPhone());
        ng.setResponsiblePersonName(group.getResponsiblePersonName());
        ng.setResponsiblePersonIdNumber(AesUtils.encrypt(group.getResponsiblePersonIdNumber(), appsecret));
        ng.setRemark(group.getRemark());
        String str = JSON.toJSONString(ng);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest(){};
        String res = request.postData(NationConstant.url,null,dataMap);
        System.out.println("返回结果:" + res);
        return HandleResultReturn(res);
    }

    /**
     * 修改班组
     */
    public static JSONObject updateGroup(WorkerGroup workerGroup) {
        String method = "Team.Update";
        String appsecret = NationConstant.appsecret;
        NationalGroup ng = new NationalGroup();
        ng.setTeamSysNo(workerGroup.getTeamSysNo());
        ng.setTeamName(workerGroup.getTeamName());
        ng.setResponsiblePersonName(workerGroup.getResponsiblePersonName());
        ng.setResponsiblePersonPhone(workerGroup.getResponsiblePersonPhone());
        if (workerGroup.getResponsiblePersonIdCardType() != null) {
            if (workerGroup.getResponsiblePersonIdCardType() < 10) {
                //若小于10，则要在前面补零
                ng.setResponsiblePersonIDCardType("0" + workerGroup.getResponsiblePersonIdCardType());
            } else {
                ng.setResponsiblePersonIDCardType(workerGroup.getResponsiblePersonIdCardType().toString());
            }
        }
        ng.setResponsiblePersonIDNumber(AesUtils.encrypt(workerGroup.getResponsiblePersonIdNumber(), appsecret));
        ng.setRemark(workerGroup.getRemark());
        ng.setEntryTime(CommonUtils.DateToString(workerGroup.getEntryTime()));
        ng.setExitTime(CommonUtils.DateToString(workerGroup.getExitTime()));
        String str = JSON.toJSONString(ng);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.url,null,dataMap);
        System.out.println("返回结果:" + res);
        return HandleResultReturn(res);
    }

    /**
     * 修改工人项目信息
     */
    public static JSONObject updateProjectPerson(ProjectPersonDetail projectPersonDetail){
        Person person = projectPersonDetail.getPerson();
        Project project = projectPersonDetail.getProject();
        WorkerGroup workerGroup = projectPersonDetail.getWorkerGroup();
        String method = "ProjectWorker.Update";
        String appsecret = NationConstant.appsecret;
        WorkerWrapper wp = new WorkerWrapper();
        wp.setProjectCode(project.getProjectCode());
        wp.setCorpCode(workerGroup.getCorpCode());
        wp.setCorpName(workerGroup.getCorpName());
        wp.setTeamName(workerGroup.getTeamName());
        wp.setTeamSysNo(workerGroup.getTeamSysNo());
        wp.setWorkerName(person.getPersonName());
        wp.setIsTeamLeader(person.getIsTeamLeader());
        if (person.getIdCardType() != null) {
            if (person.getIdCardType() < 10) {
                wp.setIdCardType("0" + person.getIdCardType());
            } else {
                wp.setIdCardType(person.getIdCardType().toString());
            }
        }
        wp.setIdCardNumber(AesUtils.encrypt(person.getIdCardNumber(),appsecret));
        wp.setWorkType(person.getWorkType());
        wp.setWorkRole(person.getWorkRole());
        wp.setNation(person.getNation());
        wp.setAddress(person.getAddress());
        wp.setHeadImage(person.getHeadImage());
        wp.setCellPhone(person.getCellPhone());
        wp.setGrantOrg(person.getGrantOrg());
        if (person.getPoliticsType() != null) {
            if (person.getPoliticsType() < 10) {
                wp.setPoliticsType("0" + person.getPoliticsType());
            } else {
                wp.setPoliticsType(person.getPoliticsType().toString());
            }
        }
        if (person.getCultureLevelType() != null) {
            wp.setCultureLevelType(person.getCultureLevelType().toString());
        }
        String str = JSON.toJSONString(wp);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.url,null,dataMap);
        System.out.println("返回结果：" + res);
        return HandleResultReturn(res);
    }

    /**
     * 异步调用接口查询
     */
    private static JSONObject AsyncHandleResultQuery(String str,String method) {
        try {
            NationalGroup ng = new NationalGroup();
            ng.setRequestSerialCode(str);
            Map<String,String> dataMap = getMap(JSON.toJSONString(ng),method);
            BaseRequest request = new BaseRequest(){};
            String result = request.postData(NationConstant.url,null, dataMap);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 处理结果并返回
     * @param result 异步通知结果
     * @return
     *        为空则说明操作失败
     *        data为空则说明所获取的数据为空
     *        否则成功获取数据并返回
     */
    private static JSONObject HandleResultReturn(String result) {
        JSONObject resultObj = new JSONObject();
        JSONObject parseObject;
        try {
            parseObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            resultObj.put("error", true);
            resultObj.put("message", "结果解析失败");
            logger.error("HandleResultReturn error, e:{}", e.getMessage());
            return resultObj;
        }
        //判断是否返回错误信息
        String code = parseObject.getString("code");
        //code不为空且不为零则说明返回了错误代码
        if(StringUtils.isEmpty(code)){
            resultObj.put("message", "code解析为空");
            resultObj.put("error", true);
            return resultObj;
        } else if (!code.equals("0")) {
            resultObj.put("error", true);
            resultObj.put("message", parseObject.getString("message"));
            return resultObj;
        } else {
            //获取正确结果
            JSONObject data = parseObject.getJSONObject("data");
            if (data != null) {
                String requestSerialCode = data.getString("requestSerialCode");
                //requestSerialCode不为空则需要异步查询
                if (StringUtils.hasText(requestSerialCode)) {
                    //异步查询结果
                    data = AsyncHandleResultQuery(requestSerialCode, "AsyncHandleResult.Query");
                    if (data == null) {
                        resultObj.put("error", true);
                        resultObj.put("message", "获取异步通知结果失败");
                        return resultObj;
                    } else {
                        //判断是否调用成功
                        JSONObject queryData =  data.getJSONObject("data");
                        String status =  queryData.getString("status");
                        if (!StringUtils.hasText(status) || !status.equals("20")) { //处理失败
                            resultObj.put("error", true);
                            resultObj.put("message", queryData.getString("result"));
                            return resultObj;
                        } else {   //处理成功
                            resultObj.put("error", false);
                            resultObj.put("data", queryData);
                            return resultObj;
                        }
                    }
                }
                resultObj.put("error", false);
                resultObj.put("data", data);
                return resultObj;
            } else {
                //获取返回结果失败
                resultObj.put("data", null);
                resultObj.put("error", true);
                return resultObj;
            }
        }
    }

    /**
     * 获取人员的工种
     */
    public static String getWorkType(String workType) {
        switch (workType) {
            case "010": return "砌砖工";
            case "020": return "钢筋工";
            case "030": return "架子工";
            case "040": return "混凝土工";
            case "050": return "模板工";
            case "060": return "机械设备安装工";
            case "070": return "通风工";
            case "080": return "安装起重工";
            case "090": return "安装钳工";
            case "100": return "电气设备安装调试工";
            case "110": return "管道工";
            case "120": return "变电安装工";
            case "130": return "建筑电工";
            case "140": return "司泵工";
            case "150": return "挖掘铲运和桩工机械司机";
            case "160": return "桩机操作工";
            case "170": return "起重信号工";
            case "180": return "建筑起重机安装拆卸工";
            case "200": return "室内成套设施安装工";
            case "210": return "建筑门窗幕墙安装工";
            case "220": return "幕墙制作工";
            case "230": return "防水工";
            case "240": return "木工";
            case "250": return "石工";
            case "270": return "电焊工";
            case "280": return "爆破工";
            case "290": return "除尘工";
            case "300": return "测量放线工";
            case "310": return "线路架设工";
            case "320": return "古建筑传统石工";
            case "330": return "古建筑传统瓦工";
            case "340": return "古建筑传统彩画工";
            case "350": return "古建筑传统木工";
            case "360": return "古建筑传统油工";
            case "380": return "金属工";
            case "900": return "管理人员";
            case "390": return "杂工";
            case "1000": return "其他";
            default: return "";
        }
    }
}