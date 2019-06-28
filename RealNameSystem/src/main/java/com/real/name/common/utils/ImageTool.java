package com.real.name.common.utils;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class ImageTool {

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
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * base64字符串转化成图片,对字节数组字符串进行Base64解码并生成图片
     */
    public static boolean generateImage(String imgStr, String personId)
    {
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
//            String imgFilePath = "/Users/terry-jri/Desktop/" + personId + ".jpg";//新生成的图片
            String imgFilePath = PathUtil.getImgBasePath() + personId + imgSuffix;//新生成的图片
            out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        }finally{
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
