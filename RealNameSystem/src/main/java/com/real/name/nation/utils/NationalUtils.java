package com.real.name.nation.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.real.name.common.constant.NationConstant;
import com.real.name.common.utils.AesUtils;
import com.real.name.common.utils.CommonUtils;
import com.real.name.common.utils.HTTPTool;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.nation.entity.NationSubContractor;
import com.real.name.nation.entity.NationSubContractor.BankInfo;
import com.real.name.nation.entity.NationalGroup;
import com.real.name.nation.entity.NationalPerson;
import com.real.name.nation.entity.NationalProject;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.subcontractor.entity.SubContractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class NationalUtils {

    private static Logger logger = LoggerFactory.getLogger(NationalUtils.class);

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 上传项目信息
     */
    public static JSONObject uploadProject(Project project) {
        String method = "Project.Add";
        NationalProject nationalProject = new NationalProject();
        nationalProject.setProjectCode(project.getProjectCode());
        nationalProject.setContractorCorpCode(project.getContractorCorpCode());
        nationalProject.setContractorCorpName(project.getContractorCorpName());
        nationalProject.setName(project.getName());
        nationalProject.setDescription(project.getDescription());
        nationalProject.setCategory(project.getCategory());
        nationalProject.setBuildCorpName(project.getBuildCorpName());
        nationalProject.setBuildCorpCode(project.getBuildCorpCode());
        nationalProject.setBuilderLicenses(project.getBuilderLicenses());
        nationalProject.setBuildPlanNum(project.getBuildPlanNum());
        nationalProject.setAreaCode(project.getAreaCode());
        nationalProject.setInvest(project.getInvest());
        nationalProject.setBuildingArea(project.getBuildingArea());
        nationalProject.setBuildingLength(project.getBuildingLength());
        if (project.getStartDate() != null) {
            nationalProject.setStartDate(simpleDateFormat.format(project.getStartDate()));
        }
        if (project.getCompleteDate() != null) {
            nationalProject.setCompleteDate(simpleDateFormat.format(project.getCompleteDate()));
        }
        nationalProject.setLinkman(project.getLinkMan());
        nationalProject.setLinkPhone(project.getLinkPhone());
        nationalProject.setPrjStatus(getByInteger(project.getPrjStatus(), 3));
        nationalProject.setLat(project.getLat());
        nationalProject.setLng(project.getLng());
        nationalProject.setAddress(project.getAddress());
        nationalProject.setApprovalNum(project.getApprovalNum());
        nationalProject.setPrjSize(project.getPrjSize());
        nationalProject.setPropertyNum(project.getPropertyNum());
        nationalProject.setFunctionNum(project.getFunctionNum());
        nationalProject.setNationNum(getByInteger(project.getNationNum(), 3));
        String data = JSON.toJSONString(nationalProject);
        String result = postData(data, method);
        return HandleResultReturn(result);
    }

    /**
     * 修改项目信息
     */
    public static JSONObject uploadUpdateProject(Project project) {
        String method = "Project.PartUpdate";
        NationalProject nationalProject = new NationalProject();
        nationalProject.setProjectCode(project.getProjectCode());
        nationalProject.setDescription(project.getDescription());
        nationalProject.setBuildCorpName(project.getBuildCorpName());
        nationalProject.setBuildCorpCode(project.getBuildCorpCode());
        nationalProject.setBuildPlanNum(project.getBuildPlanNum());
        nationalProject.setPrjPlanNum(project.getPrjPlanNum());
        nationalProject.setInvest(project.getInvest());
        nationalProject.setBuildingArea(project.getBuildingArea());
        nationalProject.setBuildingLength(project.getBuildingLength());
        nationalProject.setLinkman(project.getLinkMan());
        nationalProject.setLinkPhone(project.getLinkPhone());
        nationalProject.setPrjStatus(getByInteger(project.getPrjStatus(), 3));
        nationalProject.setLat(project.getLat());
        nationalProject.setLng(project.getLng());
        nationalProject.setAddress(project.getAddress());
        nationalProject.setApprovalNum(project.getApprovalNum());
        nationalProject.setApprovalLevelNum(getByInteger(project.getApprovalLevelNum(), 3));
        nationalProject.setPrjSize(project.getPrjSize());
        nationalProject.setFunctionNum(project.getFunctionNum());
        nationalProject.setNationNum(getByInteger(project.getNationNum(), 3));
        String data = JSON.toJSONString(nationalProject);
        String result = postData(data, method);
        return HandleResultReturn(result);
    }

    /**
     * 上传参建单位信息
     */
    public static JSONObject uploadSubContractor(SubContractor subContractor) {
        String method = "ProjectSubContractor.Add";
        NationSubContractor nationSubContractor = new NationSubContractor();
        nationSubContractor.setProjectCode(subContractor.getProject().getProjectCode());
        nationSubContractor.setCorpCode(subContractor.getCorpCode());
        nationSubContractor.setCorpName(subContractor.getCorpName());
        nationSubContractor.setCorpType(subContractor.getCorpType());
        if (subContractor.getEntryTime() != null) {
            nationSubContractor.setEntryTime(simpleDateFormat.format(subContractor.getEntryTime()));
        }
        if (subContractor.getExitTime() != null) {
            nationSubContractor.setExitTime(simpleDateFormat.format(subContractor.getExitTime()));
        }
        nationSubContractor.setPmName(subContractor.getPmName());
        nationSubContractor.setPmIDCardNumber(AesUtils.encrypt(subContractor.getPmIdCardNumber(), NationConstant.appsecret));
        nationSubContractor.setPmPhone(subContractor.getPmPhone());
        BankInfo bankInfo = new NationSubContractor().new BankInfo();
        bankInfo.setBankCode(subContractor.getBankCode());
        bankInfo.setBankLinkNumber(subContractor.getBankLinkNumber());
        bankInfo.setBankName(subContractor.getBankName());
        bankInfo.setBankNumber(AesUtils.encrypt(subContractor.getBankNumber(), NationConstant.appsecret));
        List<BankInfo> bankInfos = new ArrayList<>();
        bankInfos.add(bankInfo);
        nationSubContractor.setBankInfos(bankInfos);
        String data = JSON.toJSONString(nationSubContractor);
        System.out.println(data);
        String result = postData(data, method);
        return HandleResultReturn(result);
    }

    /**
     * 修改参建单位信息
     */
    public static JSONObject uploadUpdateSubContractor(SubContractor subContractor) {
        String method = "ProjectSubContractor.Update";
        NationSubContractor nationSubContractor = new NationSubContractor();
        nationSubContractor.setProjectCode(subContractor.getProject().getProjectCode());
        nationSubContractor.setCorpCode(subContractor.getCorpCode());
        nationSubContractor.setCorpName(subContractor.getCorpName());
        nationSubContractor.setCorpType(subContractor.getCorpType());
        if (subContractor.getEntryTime() != null) {
            nationSubContractor.setEntryTime(simpleDateFormat.format(subContractor.getEntryTime()));
        }
        if (subContractor.getExitTime() != null) {
            nationSubContractor.setExitTime(simpleDateFormat.format(subContractor.getExitTime()));
        }
        nationSubContractor.setPmName(subContractor.getPmName());
        nationSubContractor.setPmIDCardNumber(AesUtils.encrypt(subContractor.getPmIdCardNumber(), NationConstant.appsecret));
        nationSubContractor.setPmPhone(subContractor.getPmPhone());
        BankInfo bankInfo = new NationSubContractor().new BankInfo();
        bankInfo.setBankCode(subContractor.getBankCode());
        bankInfo.setBankLinkNumber(subContractor.getBankLinkNumber());
        bankInfo.setBankName(subContractor.getBankName());
        bankInfo.setBankNumber(AesUtils.encrypt(subContractor.getBankNumber(), NationConstant.appsecret));
        List<BankInfo> bankInfos = new ArrayList<>();
        bankInfos.add(bankInfo);
        nationSubContractor.setBankInfos(bankInfos);
        String data = JSON.toJSONString(nationSubContractor);
        String result = postData(data, method);
        return HandleResultReturn(result);
    }

    /**
     * 上传班组班组
     */
    public static JSONObject uploadGroup(ProjectDetailQuery projectDetailQuery) {
        String method = "Team.Add";
        Project project = projectDetailQuery.getProject();
        WorkerGroup workerGroup = projectDetailQuery.getWorkerGroup();
        NationalGroup nationalGroup = new NationalGroup();
        nationalGroup.setProjectCode(project.getProjectCode());
        nationalGroup.setCorpCode(workerGroup.getCorpCode());
        nationalGroup.setCorpName(workerGroup.getCorpName());
        nationalGroup.setTeamName(workerGroup.getTeamName());
        nationalGroup.setResponsiblePersonIDCardType(getByInteger(workerGroup.getResponsiblePersonIdCardType(), 2));
        nationalGroup.setResponsiblePersonIDNumber(AesUtils.encrypt(workerGroup.getResponsiblePersonIdNumber(), NationConstant.appsecret));
        nationalGroup.setRemark(workerGroup.getRemark());
        if (workerGroup.getEntryTime() != null) {
            nationalGroup.setEntryTime(simpleDateFormat.format(workerGroup.getEntryTime()));
        }
        if (workerGroup.getExitTime() != null) {
            nationalGroup.setExitTime(simpleDateFormat.format(workerGroup.getExitTime()));
        }
        String data = JSON.toJSONString(nationalGroup);
        String result = postData(data, method);
        return HandleResultReturn(result);
    }

    /**
     * 修改班组信息
     */
    public static JSONObject uploadUpdateGroup(WorkerGroup workerGroup) {
        String method = "Team.Update";
        NationalGroup nationalGroup = new NationalGroup();
        nationalGroup.setTeamSysNo(workerGroup.getTeamSysNo());
        nationalGroup.setTeamName(workerGroup.getTeamName());
        nationalGroup.setResponsiblePersonName(workerGroup.getResponsiblePersonName());
        nationalGroup.setResponsiblePersonPhone(workerGroup.getResponsiblePersonPhone());
        nationalGroup.setResponsiblePersonIDCardType(getByInteger(workerGroup.getResponsiblePersonIdCardType(), 2));
        nationalGroup.setResponsiblePersonIDNumber(workerGroup.getResponsiblePersonIdNumber());
        nationalGroup.setRemark(workerGroup.getRemark());
        if (workerGroup.getEntryTime() != null) {
            nationalGroup.setEntryTime(simpleDateFormat.format(workerGroup.getEntryTime()));
        }
        if (workerGroup.getExitTime() != null) {
            nationalGroup.setExitTime(simpleDateFormat.format(workerGroup.getExitTime()));
        }
        String data = JSON.toJSONString(nationalGroup);
        String result = postData(data, method);
        return HandleResultReturn(result);
    }

    /**
     * 上传人员信息
     */
    public static JSONObject uploadPerson(ProjectDetailQuery projectDetailQuery) {
        String method = "ProjectWorker.Add";
        NationalPerson nationPerson = new NationalPerson();
        nationPerson.setProjectCode(projectDetailQuery.getProject().getProjectCode());
        WorkerGroup workerGroup = projectDetailQuery.getWorkerGroup();
        Person person = projectDetailQuery.getPerson();
        nationPerson.setCorpCode(person.getCorpCode());
        nationPerson.setCorpName(person.getSubordinateCompany());
        nationPerson.setTeamSysNo(workerGroup.getTeamSysNo());
        nationPerson.setTeamName(workerGroup.getTeamName());
        NationalPerson.Worker worker = new NationalPerson().new Worker();
        worker.setWorkerName(person.getPersonName());
        worker.setIsTeamLeader(person.getIsTeamLeader());
        worker.setIdCardType(getByInteger(person.getIdCardType(), 2));
        worker.setIdCardNumber(AesUtils.encrypt(person.getIdCardNumber(), NationConstant.appsecret));
        worker.setWorkType(person.getWorkType());
        if (person.getIssueCardDate() != null) {
            worker.setIssueCardDate(simpleDateFormat.format(person.getIssueCardDate()));
        }
        worker.setIssueCardPic(person.getIssueCardPic());
        worker.setCardNumber(person.getIdCardNumber());
        worker.setPayRollBankCardNumber(AesUtils.encrypt(person.getPayRollBankCardNumber(), NationConstant.appsecret));
        worker.setPayRollBankName(person.getPayRollBankName());
        worker.setBankLinkNumber(person.getBankLinkNumber());
        worker.setPayRollTopBankCode(getByInteger(person.getPayRollTopBankCode(), 3));
        worker.setHasBuyInsurance(person.getHasBuyInsurance());
        worker.setNation(person.getNation());
        worker.setAddress(person.getAddress());
        worker.setHeadImage(person.getHeadImage());
        worker.setPoliticsType(getByInteger(person.getPoliticsType(), 2));
        if (person.getJoinedTime() != null) {
            worker.setJoinedTime(simpleDateFormat.format(person.getJoinedTime()));
        }
        worker.setCellPhone(person.getCellPhone());
        worker.setCultureLevelType(getByInteger(person.getCultureLevelType(), 2));
        worker.setSpecialty(person.getSpecialty());
        worker.setHasBadMedicalHistory(person.getHasBadMedicalHistory());
        worker.setUrgentLinkMan(person.getUrgentLinkMan());
        worker.setUrgentLinkManPhone(person.getUrgentLinkManPhone());
        if (person.getWorkDate() != null) {
            worker.setWorkDate(simpleDateFormat.format(person.getWorkDate()));
        }
        worker.setMaritalStatus(getByInteger(person.getMaritalStatus(), 2));
        worker.setGrantOrg(person.getGrantOrg());
        worker.setPositiveIDCardImage(person.getPositiveIdCardImage());
        worker.setNegativeIDCardImage(person.getNegativeIdCardImage());
        if (person.getStartDate() != null) {
            worker.setStartDate(simpleDateFormat.format(person.getStartDate()));
        }
        if (person.getExpiryDate() != null) {
            worker.setExpiryDate(simpleDateFormat.format(person.getExpiryDate()));
        }
        List<NationalPerson.Worker> workerList = new ArrayList<>();
        workerList.add(worker);
        nationPerson.setWorkerList(workerList);
        String data = JSON.toJSONString(nationPerson);
        String result = postData(data, method);
        return HandleResultReturn(result);
    }

    /**
     * 修改人员信息
     */
    public static JSONObject uploadUpdatePerson(ProjectDetailQuery projectDetailQuery) {
        String method = "ProjectWorker.Update";
        NationalPerson nationPerson = new NationalPerson();
        nationPerson.setProjectCode(projectDetailQuery.getProject().getProjectCode());
        WorkerGroup workerGroup = projectDetailQuery.getWorkerGroup();
        Person person = projectDetailQuery.getPerson();
        nationPerson.setCorpCode(person.getCorpCode());
        nationPerson.setCorpName(person.getSubordinateCompany());
        nationPerson.setTeamSysNo(workerGroup.getTeamSysNo());
        nationPerson.setTeamName(workerGroup.getTeamName());
        NationalPerson.Worker worker = new NationalPerson().new Worker();
        worker.setWorkerName(person.getPersonName());
        worker.setIsTeamLeader(person.getIsTeamLeader());
        worker.setIdCardType(getByInteger(person.getIdCardType(), 2));
        worker.setIdCardNumber(AesUtils.encrypt(person.getIdCardNumber(), NationConstant.appsecret));
        worker.setWorkType(person.getWorkType());
        if (person.getIssueCardDate() != null) {
            worker.setIssueCardDate(simpleDateFormat.format(person.getIssueCardDate()));
        }
        worker.setIssueCardPic(person.getIssueCardPic());
        worker.setCardNumber(person.getIdCardNumber());
        worker.setPayRollBankCardNumber(AesUtils.encrypt(person.getPayRollBankCardNumber(), NationConstant.appsecret));
        worker.setPayRollBankName(person.getPayRollBankName());
        worker.setBankLinkNumber(person.getBankLinkNumber());
        worker.setPayRollTopBankCode(getByInteger(person.getPayRollTopBankCode(), 3));
        worker.setHasBuyInsurance(person.getHasBuyInsurance());
        worker.setNation(person.getNation());
        worker.setAddress(person.getAddress());
        worker.setHeadImage(person.getHeadImage());
        worker.setPoliticsType(getByInteger(person.getPoliticsType(), 2));
        if (person.getJoinedTime() != null) {
            worker.setJoinedTime(simpleDateFormat.format(person.getJoinedTime()));
        }
        worker.setCellPhone(person.getCellPhone());
        worker.setCultureLevelType(getByInteger(person.getCultureLevelType(), 2));
        worker.setSpecialty(person.getSpecialty());
        worker.setHasBadMedicalHistory(person.getHasBadMedicalHistory());
        worker.setUrgentLinkMan(person.getUrgentLinkMan());
        worker.setUrgentLinkManPhone(person.getUrgentLinkManPhone());
        if (person.getWorkDate() != null) {
            worker.setWorkDate(simpleDateFormat.format(person.getWorkDate()));
        }
        worker.setMaritalStatus(getByInteger(person.getMaritalStatus(), 2));
        worker.setGrantOrg(person.getGrantOrg());
        worker.setPositiveIDCardImage(person.getPositiveIdCardImage());
        worker.setNegativeIDCardImage(person.getNegativeIdCardImage());
        if (person.getStartDate() != null) {
            worker.setStartDate(simpleDateFormat.format(person.getStartDate()));
        }
        if (person.getExpiryDate() != null) {
            worker.setExpiryDate(simpleDateFormat.format(person.getExpiryDate()));
        }
        List<NationalPerson.Worker> workerList = new ArrayList<>();
        workerList.add(worker);
        nationPerson.setWorkerList(workerList);
        String data = JSON.toJSONString(nationPerson);
        String result = postData(data, method);
        return HandleResultReturn(result);
    }



    /**
     * 处理结果并返回
     *
     * @param result 异步通知结果
     * @return 为空则说明操作失败
     * data为空则说明所获取的数据为空
     * 否则成功获取数据并返回
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
        if (StringUtils.isEmpty(code)) {
            resultObj.put("data", "code解析为空");
            resultObj.put("error", true);
            logger.error("全国平台code解析为空");
            return resultObj;
        } else if (!code.equals("0")) {
            resultObj.put("error", true);
            resultObj.put("data", parseObject.getString("message"));
            logger.error("全国平台返回错误信息：{}", parseObject.getString("message"));
            return resultObj;
        } else if (parseObject.getJSONObject("data") == null) {
            //获取返回结果失败
            resultObj.put("data", "返回结果为空，处理失败");
            resultObj.put("error", true);
            logger.error("全国平台返回结果为空");
            return resultObj;
        } else {
            //获取正确结果
            JSONObject data = parseObject.getJSONObject("data");
            String requestSerialCode = data.getString("requestSerialCode");
            //requestSerialCode不为空则需要异步查询
            if (StringUtils.hasText(requestSerialCode)) {
                //异步查询结果
                data = AsyncHandleResultQuery(requestSerialCode);
                if (data == null) {
                    resultObj.put("error", true);
                    resultObj.put("data", "获取异步通知结果失败");
                    logger.error("异步通知返回结果为空");
                } else {
                    //判断是否调用成功
                    JSONObject queryData = data.getJSONObject("data");
                    String status = queryData.getString("status");
                    if (!StringUtils.hasText(status) || !status.equals("20")) { //处理失败
                        resultObj.put("error", true);
                        logger.error("异步通知处理失败，{}", queryData.getString("result"));
                    } else {   //处理成功
                        resultObj.put("error", false);
                    }
                    resultObj.put("data", queryData.getString("result"));
                }
                return resultObj;
            } else {
                //不需要异步查询，处理成功
                resultObj.put("error", false);
                resultObj.put("data", data);
                return resultObj;
            }
        }
    }

    /**
     * 获取请求的参数
     */
    private static Map<String, Object> getRequestParam(String data, String method) {
        //获取随机字符串
        String nonce = CommonUtils.getUniqueString(20);
        //获取时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        //生成企业签名
        String corpsign = AesUtils.encrypt(NationConstant.corpKey + timestamp, NationConstant.appsecret);
        //生成签名
        String sortString = "appid=" + NationConstant.appid;
        sortString += "&corpsign=" + corpsign;
        sortString += "&data=" + data;
        sortString += "&format=" + NationConstant.format;
        sortString += "&method=" + method;
        sortString += "&nonce=" + nonce;
        sortString += "&timestamp=" + timestamp;
        sortString += "&version=" + NationConstant.version;
        sortString += "&appsecret=" + NationConstant.appsecret;
        String sign = EncryptionHelper.getSHA256StrJava(sortString.toLowerCase());
        //请求数据封装
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("appid", NationConstant.appid);
        dataMap.put("format", NationConstant.format);
        dataMap.put("method", method);
        dataMap.put("nonce", nonce);
        dataMap.put("timestamp", timestamp);
        dataMap.put("version", NationConstant.version);
        dataMap.put("sign", sign);
        dataMap.put("corpsign", corpsign);
        dataMap.put("data", data);
        return dataMap;
    }

    /**
     * 发送post请求
     */
    private static String postData(String data, String method) {
        Map<String, Object> params = getRequestParam(data, method);
        return HTTPTool.postUrlForParam(NationConstant.url, params);
    }

    /**
     * 异步调用接口查询
     */
    private static JSONObject AsyncHandleResultQuery(String str) {
        try {
            String method = "AsyncHandleResult.Query";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestSerialCode", str);
            Map<String, Object> dataMap = getRequestParam(jsonObject.toJSONString(), method);
            String result = HTTPTool.postUrlForParam(NationConstant.url, dataMap);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            logger.error("全国平台异步查询错误, e{}", e);
            return null;
        }
    }

    /**
     * 获取人员的工种
     */
    public static String getWorkType(String workType) {
        StringBuilder workTypeBuilder = new StringBuilder(workType);
        while (workTypeBuilder.length() < 3) {
            workTypeBuilder.insert(0, "0");
        }
        workType = workTypeBuilder.toString();
        switch (workType) {
            case "010":
                return "砌砖工";
            case "020":
                return "钢筋工";
            case "030":
                return "架子工";
            case "040":
                return "混凝土工";
            case "050":
                return "模板工";
            case "060":
                return "机械设备安装工";
            case "070":
                return "通风工";
            case "080":
                return "安装起重工";
            case "090":
                return "安装钳工";
            case "100":
                return "电气设备安装调试工";
            case "110":
                return "管道工";
            case "120":
                return "变电安装工";
            case "130":
                return "建筑电工";
            case "140":
                return "司泵工";
            case "150":
                return "挖掘铲运和桩工机械司机";
            case "160":
                return "桩机操作工";
            case "170":
                return "起重信号工";
            case "180":
                return "建筑起重机安装拆卸工";
            case "200":
                return "室内成套设施安装工";
            case "210":
                return "建筑门窗幕墙安装工";
            case "220":
                return "幕墙制作工";
            case "230":
                return "防水工";
            case "240":
                return "木工";
            case "250":
                return "石工";
            case "270":
                return "电焊工";
            case "280":
                return "爆破工";
            case "290":
                return "除尘工";
            case "300":
                return "测量放线工";
            case "310":
                return "线路架设工";
            case "320":
                return "古建筑传统石工";
            case "330":
                return "古建筑传统瓦工";
            case "340":
                return "古建筑传统彩画工";
            case "350":
                return "古建筑传统木工";
            case "360":
                return "古建筑传统油工";
            case "380":
                return "金属工";
            case "900":
                return "管理人员";
            case "390":
                return "杂工";
            case "1000":
                return "其他";
            default:
                return "";
        }
    }

    private static String getByInteger(Integer data, Integer length) {
        if (data == null) {
            return null;
        }
        StringBuilder dataStr = new StringBuilder(data + "");
        while (dataStr.length() < length) {
            dataStr.insert(0, "0");
        }
        return dataStr.toString();
    }
}