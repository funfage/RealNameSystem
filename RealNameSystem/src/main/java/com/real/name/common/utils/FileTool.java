package com.real.name.common.utils;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.person.entity.Person;
import com.real.name.record.entity.Attendance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileTool {

    private static Logger logger = LoggerFactory.getLogger(FileTool.class);

    /**
     * 生成文件
     * @param basePath 文件所在的目录路径
     * @param fileName 文件名
     */
    public static void generateFile(MultipartFile file, String basePath, String fileName) {
        if (!file.isEmpty()) {
            String filePath = basePath + fileName;//新生成的图片
            File dest = new File(filePath);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            //判断文件是否存在，若存在则删除
            if (dest.exists()) {
                dest.delete();
            }
            try {
                file.transferTo(dest);
                logger.info("上传成功后的文件路径：{}", filePath);
                //将文件名返回
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                throw new AttendanceException(ResultError.GENERATE_FILE_ERROR);
            }
        }
    }

    /**
     * 生成base64编码
     */
    public static String fileToBase64(InputStream imageStream) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        //读取图片字节数组
        try {
            data = new byte[imageStream.available()];
            imageStream.read(data);
        } catch (IOException e) {
            logger.error("将照片转成字节数据失败");
            return null;
        }finally {
            try {
                if (imageStream != null) {
                    imageStream.close();
                }
            } catch (IOException e) {
                logger.error("图片流关闭异常");
            }
        }
        try {
            //对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(data);//返回Base64编码过的字节数组字符串
        } catch (Exception e) {
            logger.error("字节数组Base64编码失败");
            return null;
        }
    }

    /**
     * 删除头像
     */
    public static void deleteFile(String fileBasePath, String fileName){
        String imagePath = fileBasePath + fileName;
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            logger.info("需要删除的图片路径为:" + imagePath);
            if (!imageFile.delete()) {
                throw new AttendanceException(ResultError.DELETE_FILE_ERROR);
            }
        } else {
            throw new AttendanceException(ResultError.FILE_NOT_EXISTS);
        }
    }

}
