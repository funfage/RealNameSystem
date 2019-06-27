package com.real.name.labor;


import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws UnsupportedEncodingException
    {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "stdout");

        String url = "http://182.148.48.165:8090/open.api";
        String appid = "44532120190310001";
        String method = "Corp.Query";
        String version = "1.0";
        String appsecret = "8910fbdb4ae8d90c814c6a753f76e082";
        String data = "{\"pageIndex\":0,\"pageSize\":10,\"corpName\":\"???????????????????\"}";
//
//        Scanner scan = new Scanner(System.in);
//        System.out.println("PLEASE ENTER THE??URL??:");
//        url = scan.nextLine();
//        if (StringUtils.isEmpty(url)) {
//            System.out.println("????URL??????????");
//        }
//
//        System.out.println("PLEASE ENTER THE ??APPID??:");
//        appid = scan.nextLine();
//
//        System.out.println("PLEASE ENTER THE ??APPSECRET??:");
//        appsecret = scan.nextLine();
//
//        System.out.println("PLEASE ENTER THE ??METHOD??:");
//        method = scan.nextLine();
//
//        System.out.println("PLEASE ENTER THE ??VERSION??:(IF EMPTY:1.0)");
//        String tempVersion = scan.nextLine();
//        version = StringUtils.isEmpty(tempVersion) ? "1.0" : tempVersion;
//
//
//        System.out.println("PLEASE ENTER THE ??DATA??:");
//        data = acceptmultiLineChars2(scan).replaceAll("\"","\\\"");
//        System.out.println(data);
//        if (data.length() > 2) {
//            data = data.substring(0,data.length() - 2);
//        }
        System.out.println(data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        String sortString = "appid=" + appid;
        String guid = UUID.randomUUID().toString().replace("-","");
        String timestamp = sdf.format(new Date());

        sortString += "&data=" + data;
        sortString += "&format=json";
        sortString += "&method=" + method;
        sortString += "&nonce=" + guid;
        sortString += "&timestamp=" + timestamp;
        sortString += "&version=" + version;
        sortString += "&appsecret=" + appsecret;

        String sign = EncriptionHelper.getSHA256StrJava(sortString.toLowerCase());
        System.out.println(String.format("?????????????%s",sign));

        BaseRequest request = new BaseRequest(){};
        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("appid",appid);
        dataMap.put("data", data);

        dataMap.put("format","json");
        dataMap.put("method",method);
        dataMap.put("nonce",guid);
        dataMap.put("timestamp",timestamp);
        dataMap.put("version",version);
        dataMap.put("sign",sign);
        try {
            String res = request.postData(url,null,dataMap);
            dataMap.put("appsecret",appsecret);
            WriteLogInfo(dataMap);
            System.out.println(String.format("????????%s",res));
        } catch (Exception ex) {
            String errorMsg = "";
            if (ex.getMessage().contains("ClientProtocolException")) {
                errorMsg = "?????????URL??งน";
            } else if (ex.getMessage().contains("UnknownHostException") || ex.getMessage().contains("unreachable")) {
                errorMsg = "?????????URL?????";
            }
            System.out.println(errorMsg);
        }
    }


    private static void WriteLogInfo(Map<String,String> map)
    {
        String paraString = "";
        StringBuilder sb = new StringBuilder();
        sb.append("appid=" + map.get("appid") + "&");
        sb.append("data=" + map.get("data") + "&");
        sb.append("format=" + map.get("format") + "&");
        sb.append("method=" + map.get("method") + "&");
        sb.append("nonce=" + map.get("nonce") + "&");
        sb.append("timestamp=" + map.get("timestamp") + "&");
        sb.append("version=" + map.get("version") + "&");
        sb.append("appsecret=" + map.get("appsecret") + "&");
        sb.append("sign=" + map.get("sign"));
        paraString = sb.toString();
        paraString = paraString.replace("\r", "").replace("\n", "").replace(" ","");
        System.out.println(String.format("????FormData:%s \r\n",paraString));
    }

    private static String acceptmultiLineChars2(Scanner sc)
    {
        ArrayList<String> ns = new ArrayList<>();
        do {
            String str = sc.nextLine();
            if (str.equals("")) {
                break;
            }
            ns.add(str);
        } while (true);
        StringBuilder sb = new StringBuilder();
        if (ns.size() > 0) {
            ns.forEach(item -> sb.append(item + "\r\n"));
        }
        return sb.toString();
    }
}
