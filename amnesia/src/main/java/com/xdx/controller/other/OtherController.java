package com.xdx.controller.other;

import com.xdx.common.utils.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 附近上传和下载
 *
 * @author 小道仙
 * @date 2020年11月12日
 */
@RestController
public class OtherController {

    @PostMapping("/upload")
    public String upload(@RequestBody MultipartFile file){
        return FileUtils.upload(file);
    }

    @GetMapping("/download")
    public void download(@RequestParam String fileName, HttpServletResponse response){
        FileUtils.download(fileName,response);
    }
}
