package com.real.name.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.real.name.common.entity.forNational.NationalGroup;
import com.real.name.common.entity.forNational.SearchProject;
import com.real.name.common.info.BaseInfo;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.labor.BaseRequest;
import com.real.name.labor.EncriptionHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Desc TODO
 * @Author fxy
 * @Date 2019/5/12 20:53
 **/
public class NationalUtils {
    /**
     * @param method
     * @param data   这个是对象序列化之后的字符串
     * @return
     */
    public static String postDate(String method, String data) {
        BaseRequest request = new BaseRequest() {
        };
        Map<String, String> dataMap = getMap(data, method);
        return request.postData(BaseInfo.URL, null, dataMap);
    }

    static Map<String, String> getMap(String data, String method) {
        String guid = UUID.randomUUID().toString().replace("-", "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());

        String sortString = "appid=" + BaseInfo.APPID;
        sortString += "&data=" + data;
        sortString += "&format=" + BaseInfo.FORMART;
        sortString += "&method=" + method;
        sortString += "&nonce=" + guid;
        sortString += "&timestamp=" + timestamp;
        sortString += "&version=" + BaseInfo.VERSION;
        sortString += "&appsecret=" + BaseInfo.APPSCRECT;
        String sign = EncriptionHelper.getSHA256StrJava(sortString.toLowerCase());

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("appid", BaseInfo.APPID);
        dataMap.put("data", data);
        dataMap.put("format", BaseInfo.FORMART);
        dataMap.put("method", method);
        dataMap.put("nonce", guid);
        dataMap.put("timestamp", timestamp);
        dataMap.put("version", BaseInfo.VERSION);
        dataMap.put("sign", sign);
        return dataMap;
    }

    /**
     * 查询项目信息
     */
    public static Object queryProject(Integer pageIndex, Integer pageSize) {
        String method = "Project.Query";
        SearchProject sp = new SearchProject(pageIndex, pageSize);
        sp.setContractorCorpCode(BaseInfo.CORPCODE);
        sp.setProjectCode(BaseInfo.APPID);
        String str = JSON.toJSONString(sp);
        Map<String, String> dataMap = NationalUtils.getMap(str, method);
        BaseRequest request = new BaseRequest() {
        };
        String res = request.postData(BaseInfo.URL, null, dataMap);
        return JSON.parse(res);
    }

    /**
     * 创建班组
     */
    public static JSONObject uploadGroup(WorkerGroup group) {
        String method = "Team.Add";
        String appsecret = BaseInfo.APPSCRECT;
        WorkerGroup ng = new WorkerGroup();
        ng.setProjectCode(group.getProjectCode());
        ng.setCorpCode(group.getCorpCode());
        ng.setCorpName(group.getCorpName());
        ng.setTeamName(group.getTeamName());
        ng.setResponsiblePersonPhone(group.getResponsiblePersonPhone());
        ng.setResponsiblePersonName(group.getResponsiblePersonName());
        ng.setRemark(group.getRemark());
        String str = JSON.toJSONString(ng);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest(){};
        String res = request.postData(BaseInfo.URL,null,dataMap);
        JSONObject obj = JSONObject.parseObject(res);
        obj = obj.getJSONObject("data");
        return AsyncHandleResultQuery(obj.getString("requestSerialCode"), "AsyncHandleResult.Query");
    }

    /**
     * 异步调用接口查询
     */
    static JSONObject AsyncHandleResultQuery(String str,String method) {
        NationalGroup ng = new NationalGroup();
        ng.setRequestSerialCode(str);
        Map<String,String> dataMap = getMap(JSON.toJSONString(ng),method);
        BaseRequest request = new BaseRequest(){};
        String result = request.postData(BaseInfo.URL,null,dataMap);
        return JSONObject.parseObject(result);
    }
}