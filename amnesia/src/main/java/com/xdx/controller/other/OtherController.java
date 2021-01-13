package com.xdx.controller.other;

import com.xdx.common.common.AjaxResult;
import com.xdx.common.utils.FileUtils;
import com.xdx.service.other.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
public class OtherController {

    @Autowired
    private OtherService otherService;

    @PostMapping("/upload")
    public String upload(@RequestBody MultipartFile file){
        return FileUtils.upload(file);
    }

    @GetMapping("/download")
    public void download(@RequestParam String fileName, HttpServletResponse response){
        FileUtils.download(fileName,response);
    }

    /**
     * 版本信息
     *
     * @author 小道仙
     * @date 2020年12月25日
     */
    @GetMapping("/other/editionInfo")
    public AjaxResult<?> editionInfo(){
        return otherService.editionInfo();
    }

}
