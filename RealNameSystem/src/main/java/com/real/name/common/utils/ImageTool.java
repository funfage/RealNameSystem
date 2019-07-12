package com.real.name.common.utils;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.person.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageTool {

    private static Logger logger = LoggerFactory.getLogger(ImageTool.class);

    public static final String imageBase = "/root/headImage/";

    /**
     * 图片base64编码
     * @param imgFile 图片路径
     * @return base64
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            // 加密
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(data);
        } catch (IOException e) {
            logger.error("getImageStr error e:{}", e);
            return null;
        }finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("getImageStr error e:{}", e);
            }
        }
    }


    /**
     * 生成照片
     */
    public static boolean generateImage(MultipartFile imageFile, Person person) {
        String WholeFileName = imageFile.getOriginalFilename();
        if (!StringUtils.hasText(WholeFileName)) {
            throw new AttendanceException(ResultError.EMPTY_NAME);
        }
        String suffixName = WholeFileName.substring(WholeFileName.lastIndexOf("."));
        logger.info("文件后缀名:" + suffixName);
        List<String> suffixList = new ArrayList<>();
        suffixList.add(".jpg");
        suffixList.add(".png");
        suffixList.add(".jpeg");
        if (!suffixList.contains(suffixName)) {
            throw new AttendanceException(ResultError.IMAGE_TYPE_ERROR);
        }
        //设置文件后缀名
        person.setSuffixName(suffixName);
        //生成图片
        String imgFilePath = PathUtil.getImgBasePath() + person.getPersonId() + suffixName;//新生成的图片
        File dest = new File(imgFilePath);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        //判断文件是否存在，若存在则删除
        if (dest.exists()) {
            dest.delete();
        }
        try {
            imageFile.transferTo(dest);
            logger.info("上传成功后的文件路径：" + imgFilePath);
            return true;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成base64编码
     */
    public static String imageToBase64(InputStream imageStream) {
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
     * @param personId 员工Id
     */
    public static boolean deleteImage(Integer personId, String suffixName){
        String imagePath = PathUtil.getImgBasePath() + personId + suffixName;
        File imageFile = new File(imagePath);
        if(imageFile.exists()){
            logger.info("需要删除的图片路径为:" + imagePath);
            return imageFile.delete();
        }
        return true;
    }

}
