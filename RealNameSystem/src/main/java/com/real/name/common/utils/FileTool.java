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
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FileTool {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();
    private static Logger logger = LoggerFactory.getLogger(FileTool.class);

    /**
     * 生成文件
     *
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
        } finally {
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
    public static void deleteFile(String fileBasePath, String fileName) {
        String basePath = fileBasePath + fileName;
        File file = new File(basePath);
        if (file.exists()) {
            if (file.isFile()) {
                //判断是否为文件，是，则删除
                if (!file.delete()) {
                    logger.info("需要删除的图片路径为:" + basePath);
                    throw new AttendanceException(ResultError.DELETE_FILE_ERROR);
                }
            } else { //不为文件，则为文件夹
                String[] childFilePath = file.list();//获取文件夹下所有文件相对路径
                if (childFilePath != null) {
                    for (String path : childFilePath) {
                        String childFileBasePath = file.getAbsolutePath() + "/";
                        deleteFile(childFileBasePath, path);//递归，对每个都进行判断
                    }
                    file.delete();
                }
            }

        }
    }

    /**
     * 获取下载文件的目录路径
     *
     * @param downLoadType 0,人员文件路径
     *                     1,薪资文件路径
     *                     2,合同文件路径
     */
    public static String getDownLoadFilePath(Integer downLoadType) {
        if (downLoadType == 0) {
            return PathUtil.getImgBasePath();
        } else if (downLoadType == 1) {
            return PathUtil.getPayFileBasePath();
        } else if (downLoadType == 2) {
            return PathUtil.getContractFilePath();
        } else if (downLoadType == 3) {
            return PathUtil.getExcelFilePath();
        }
        return null;
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     */
    public static String getRandomPrefixName() {
        // 获取随机的五位数
        int rannum = random.nextInt(89999) + 10000;
        String nowTimeStr = dateFormat.format(new Date());
        return nowTimeStr + rannum;
    }

    /**
     * 获取文件的后缀名
     */
    public static String getSuffixName(MultipartFile file) {
        String WholeFileName = file.getOriginalFilename();
        if (!StringUtils.hasText(WholeFileName)) {
            throw new AttendanceException(ResultError.EMPTY_NAME);
        }
        String suffixName = WholeFileName.substring(WholeFileName.lastIndexOf("."));
        if (StringUtils.isEmpty(suffixName)) {
            throw AttendanceException.emptyMessage("文件后缀名");
        }
        return suffixName;
    }

}
