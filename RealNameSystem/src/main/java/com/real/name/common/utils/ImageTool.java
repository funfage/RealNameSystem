package com.real.name.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Objects;

public class ImageTool {

    private static Logger logger = LoggerFactory.getLogger(ImageTool.class);

    public static final String imageBase = "/root/headImage/";

    public static final String imgSuffix = ".jpg";

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
     * base64字符串转化成图片,对字节数组字符串进行Base64解码并生成图片
     */
    public static boolean generateImage(String imgStr, String personId) {
        if (!StringUtils.hasText(imgStr)) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try
        {
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0) // 调整异常数据
                {
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            String imgFilePath = PathUtil.getImgBasePath() + personId + imgSuffix;//新生成的图片
            out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            return true;
        } catch (Exception e) {
            logger.error("generateImage error e:{}", e);
            return false;
        }finally{
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                logger.error("generateImage error e:{}", e);
            }
        }
    }

    /**
     * 生成照片
     * @param file
     * @param fileName
     * @param personId
     * @return
     */
    public static boolean generateImage(MultipartFile file, String fileName, String personId) {
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传的后缀名为：" + suffixName);
        //生成图片
        String imgFilePath = PathUtil.getImgBasePath() + personId + suffixName;//新生成的图片
        File dest = new File(imgFilePath);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            logger.info("上传成功后的文件路径：" + imgFilePath + fileName);
            return true;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String imageToBase64(InputStream imageStream) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "d://test.jpg";//待处理的图片
        byte[] data = null;
        //读取图片字节数组
        try {
            data = new byte[imageStream.available()];
            imageStream.read(data);
        } catch (IOException e) {
            logger.error("将照片转成base64编码失败");
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
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }

    /**
     * 删除头像
     * @param personId 员工Id
     */
    public static boolean deleteImage(String personId){
        File imageFile = new File(PathUtil.getImgBasePath() + personId + imgSuffix);
        if(imageFile.exists()){
            imageFile.delete();
        }
        return true;
    }

    public static void main(String[] args) {
//        System.out.println(getImageStr("/Users/terry-jri/Desktop/a.jpg"));/root/headImage
        String imageStr = getImageStr("/Users/terry-jri/Desktop/b.png");
        generateImage(imageStr, "1");
    }
}
