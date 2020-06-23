package com.leaves.controller;

import com.leaves.util.Result;
import com.leaves.util.ResultResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: leaves
 * @date: 2020年5月6日 上午09:37:15
 * @version: v1.0.0
 * @Description: 图片上传
 *
 */
@Controller
public class UploadController {
    @PostMapping("/upload.do")
    @ResponseBody
    public ResultResponse upload(MultipartFile file) {
        String fileName = "";
        try {
            fileName = file.getOriginalFilename();
            String destFileName = "D://User/" + File.separator + fileName;
            System.out.println(destFileName);
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Result.resuleError("上传失败," + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.resuleError("上传失败," + e.getMessage());
        }
        String url = "/image/" + fileName;
        return Result.resuleSuccess(url);
    }

    @PostMapping("/layeditUpload.do")
    @ResponseBody
    public Map<String, Object> layeditUpload(MultipartFile file) {
        String fileName = "";
        Map<String, Object> result = new HashMap<>();
        try {
            fileName = file.getOriginalFilename();
            String destFileName = "D://User/" + File.separator + fileName;
            System.out.println(destFileName);
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result.put("code", "10001");
            result.put("msg", e.getMessage());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", "10001");
            result.put("msg", e.getMessage());
            return result;
        }
        String url = "/image/" + fileName;
        result.put("code", "0");
        Map<String, String> data = new HashMap<>();
        data.put("src", url);
        data.put("title", fileName);
        result.put("data", data);
        return result;
    }
}
