package com.real.name.common.controller;

import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.FileTool;
import com.real.name.common.utils.PathUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/common")
public class CommonController {

    private Logger logger = LoggerFactory.getLogger(CommonController.class);

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
                    response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
                    IOUtils.copy(fis,response.getOutputStream());
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

}
