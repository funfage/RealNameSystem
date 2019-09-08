package com.real.name.common.controller;

import com.real.name.auth.entity.User;
import com.real.name.auth.service.UserService;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CommonUtils;
import com.real.name.common.utils.FileTool;
import com.real.name.common.utils.JedisService;
import com.real.name.common.utils.SendSms;
import com.real.name.device.service.DeviceService;
import com.real.name.person.service.PersonService;
import com.real.name.project.service.ProjectService;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/common")
public class CommonController {

    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PersonService personService;

    @Autowired
    private UserService userService;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    /**
     * 文件下载
     */
    @GetMapping("/downLoadFile")
    public void downLoadFile(@RequestParam("downLoadType") Integer downLoadType,
                             @RequestParam("fileName") String fileName,
                             HttpServletResponse response) {
        FileInputStream fis = null;
        String downLoadPath = FileTool.getDownLoadFilePath(downLoadType);
        if (downLoadPath != null) {
            File file = new File(downLoadPath + fileName);
            if (file.exists()) {
                try {
                    fis = new FileInputStream(file);
                    setFileDownloadHeader(response, fileName);
                    IOUtils.copy(fis, response.getOutputStream());
                    response.flushBuffer();
                } catch (IOException e) {
                    logger.error("文件下载异常,e:{}", e);
                }finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            logger.error("文件流关闭异常,e:{}", e);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取首页数据
     */
    @GetMapping("/getMainPageInfo")
    public ResultVo getMainPageInfo() {
        Map<String, Object> map = new HashMap<>();
        //项目相关的首页信息
        map.put("projectInfo", projectService.getMainPageProjectInfo());
        //获取设备相关首页信息
        map.put("deviceInfo", deviceService.getMainPageDeviceInfo());
        //获取人员相关首页信息
        map.put("personInfo", personService.getPersonMainPageInfo());
        //获取用户相关首页信息
        map.put("userInfo", userService.getUserMainPageinfo());
        return ResultVo.success(map);
    }

    /**
     * 发送短信验证码
     */
    @GetMapping("/sendAuthCode")
    public ResultVo sendAuthCode(@RequestParam("phone") String phone) {
        if (!CommonUtils.isRightPhone(phone)) {
            return ResultVo.failure(ResultError.PHONE_ERROR);
        }
            String code = SendSms.sendMessage(phone);
        if (StringUtils.isEmpty(phone)) {
            return ResultVo.failure(ResultError.SEND_MESSAGE_ERROR);
        }
        //设置验证码的有效期为5分钟
        jedisStrings.set(phone, code, 10, TimeUnit.MINUTES);
        return ResultVo.success();
    }

    /**
     * 判断输入的验证码是否正确
     */
    @GetMapping("/judgeAuthCode")
    public ResultVo judgeAuthCode(@RequestParam("phone") String phone,
                                  @RequestParam("authCode") String authCode) {
        String sendCode = (String) jedisStrings.get(phone);
        if (StringUtils.isEmpty(sendCode) || !sendCode.equals(authCode)) {
            return ResultVo.failure(ResultError.AUTH_CODE_ERROR);
        }
        //判断用户是否存在
        User user = userService.getUserByPhone(phone);
        if (user == null) {
            return ResultVo.failure(ResultError.USER_NOT_EXIST);
        }
        return ResultVo.success();
    }


    /**
     * <pre>
     * 浏览器下载文件时需要在服务端给出下载的文件名，当文件名是ASCII字符时没有问题
     * 当文件名有非ASCII字符时就有可能出现乱码
     *
     * 这里的实现方式参考这篇文章
     * http://blog.robotshell.org/2012/deal-with-http-header-encoding-for-file-download/
     *
     * 最终设置的response header是这样:
     *
     * Content-Disposition: attachment;
     *                      filename="encoded_text";
     *                      filename*=utf-8''encoded_text
     *
     * 其中encoded_text是经过RFC 3986的“百分号URL编码”规则处理过的文件名
     * </pre>
     */
    private static void setFileDownloadHeader(HttpServletResponse response, String filename) {
        String headerValue = "attachment;";
        headerValue += " filename=\"" + encodeURIComponent(filename) +"\";";
        headerValue += " filename*=utf-8''" + encodeURIComponent(filename);
        response.setHeader("Content-Disposition", headerValue);
    }

    /**
     * <pre>
     * 符合 RFC 3986 标准的“百分号URL编码”
     * 在这个方法里，空格会被编码成%20，而不是+
     * 和浏览器的encodeURIComponent行为一致
     * </pre>
     */
    private static String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
