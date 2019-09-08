package com.real.name.common.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class SendSms {

    private static Logger logger = LoggerFactory.getLogger(SendSms.class);

    private static DecimalFormat df = new DecimalFormat("000000");//数字格式化

    //无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    private static String url;

    private static String appKey;

    private static String appSecret;

    private static String sender;

    private static String signature;

    private static String templateId;

    @Value("${huawei.message.url}")
    public void setUrl(String url) {
        SendSms.url = url;
    }

    @Value("${huawei.message.appKey}")
    public void setAppKey(String appKey) {
        SendSms.appKey = appKey;
    }

    @Value("${huawei.message.appSecret}")
    public void setAppSecret(String appSecret) {
        SendSms.appSecret = appSecret;
    }

    @Value("${huawei.message.sender}")
    public void setSender(String sender) {
        SendSms.sender = sender;
    }

    @Value("${huawei.message.signature}")
    public void setSignature(String signature) {
        SendSms.signature = signature;
    }

    @Value("${huawei.message.templateId}")
    public void setTemplateId(String templateId) {
        SendSms.templateId = templateId;
    }

    /**
     * 发送短息验证码
     * @param phone 手机号码
     */
    public static String sendMessage(String phone) {
        //全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
        String receiver = "+86" + phone;
        //获取随机验证码
        String code = getRandomCode();
        //参考华为短信文档
        String templateParas = "[\"" + code + "\"]"; //模板变量
        //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
        String statusCallBack = "";
        //请求Body,不携带签名名称时,signature请填null
        String body = buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack, signature);
        if (null == body || body.isEmpty()) {
            logger.error("请求体为空");
            return null;
        }
        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(appKey, appSecret);
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            logger.error("wsseHeader为空");
            return null;
        }
        try {
            //如果JDK版本是1.8,可使用如下代码
            //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
            CloseableHttpClient client = HttpClients.custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                            (x509CertChain, authType) -> true).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            HttpResponse response = client.execute(RequestBuilder.create("POST")//请求方法POST
                    .setUri(url)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                    .addHeader("X-WSSE", wsseHeader)
                    .setEntity(new StringEntity(body)).build());
            String result = EntityUtils.toString(response.getEntity());
            //返回结果不成功
            if(!result.contains("Success")) {
                logger.error("发送短信出现错误！发送手机号：" + phone + ",错误信息:" + result);
                return null;
            }
            return code;
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | IOException e) {
            logger.error("短信验证码发送失败，错误信息为:", e);
            return null;
        }
    }


    /**
     * 构造请求Body体
     * @param signature 签名名称,使用国内短信通用模板时填写
     */
    private static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                   String statusCallbackUrl, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            logger.error("buildRequestBody(): sender, receiver or templateId is null.");
            return null;
        }
        List<NameValuePair> keyValues = new ArrayList<NameValuePair>();
        keyValues.add(new BasicNameValuePair("from", sender));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        if (null != templateParas && !templateParas.isEmpty()) {
            keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        }
        if (null != statusCallbackUrl && !statusCallbackUrl.isEmpty()) {
            keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));
        }
        if (null != signature && !signature.isEmpty()) {
            keyValues.add(new BasicNameValuePair("signature", signature));
        }
        return URLEncodedUtils.format(keyValues, Charset.forName("UTF-8"));
    }

    /**
     * 构造X-WSSE参数值
     */
    private static String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            logger.error("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); //Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce
        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);
        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes()); //PasswordDigest
        //若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
        passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }

    /**
     * 获得随机验证码
     * @return 6位的随机验证码
     */
    private static String getRandomCode() {
        //并发随机工具类
        ThreadLocalRandom ran = ThreadLocalRandom.current();
        return df.format(ran.nextInt(999999));
    }
}
