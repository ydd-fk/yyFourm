package com.app.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    //D:\MyProjest\resources\icon\
    ///root/project/res/forumRes/

    public static final String filePath="/root/project/res/forumRes/";

    public static String fileUpload(MultipartFile file) throws IOException{
        if (file.isEmpty()) {
            throw new IOException("文件为空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        fileName = UUID.randomUUID().toString().replaceAll("-","") + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        return fileName;
    }

    public static void fileDelete(String fileName) {
        File file = new File(filePath + fileName);
        if (file.exists()) file.delete();
    }
}
