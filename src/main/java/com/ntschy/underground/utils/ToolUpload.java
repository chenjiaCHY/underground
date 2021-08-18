package com.ntschy.underground.utils;

import com.ntschy.underground.entity.base.FileDec;
import com.ntschy.underground.entity.base.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ToolUpload {

    public static Result<FileDec> fileUpload(MultipartFile file, String tempPath) {
        Map<String, Object> resultMap = new HashMap<>();

        if (null == file) {
            return new Result(false, "获取上传文件失败，请检查file上传组件的名称是否正确");
        } else if (file.isEmpty()) {
            return new Result(false, "没有选择文件");
        } else {

            File fileDir = new File(tempPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            String originFileName = file.getOriginalFilename();

            String suffix = originFileName.substring(originFileName.lastIndexOf(".") + 1);

            String fileName = Utils.GenerateUUID(32);

            String uploadFilePath = tempPath + fileName + "." + suffix;

            File dest = new File(uploadFilePath);

            try {
                file.transferTo(dest);

                FileDec fileDec = new FileDec(fileName + "." + suffix, originFileName);

                return new Result(true, fileDec);
            } catch (IOException e) {
                e.printStackTrace();
                resultMap.put("result", false);
                resultMap.put("msg", "文件上传发生异常");
                return new Result(false, "文件上传发生异常");
            }
        }
    }
}
