package com.xdx.common.utils;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件操作相关
 */
public class FileUtils {

    /**
     * 文件地址，按照年月日存储
     *
     * filePath 文件上传路径
     * uploadFolder 文件夹名称
     */
    private static String filePath;
    private static String fileFolder = "uploadFolder";
    static {
        try {
            filePath =  new File("").getCanonicalPath() + "/"+fileFolder+"/";
        }catch (Exception e){
            throw new RuntimeException("获取文件基本路径失败");
        }
    }

    /**
     * 文件上传 单个
     */
    public static String upload( MultipartFile file){
        try {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.split("\\.")[1];
            String newFileName = DateUtils.getYear() + "/" +
                    DateUtils.getMonth() + "/" +
                    DateUtils.getDay() + "/" +
                    UUID.randomUUID().toString() + "." + suffix;
            String newPath = filePath + newFileName;
            // 判断当前文件夹是否存在
            File oldFile = new File(newPath);
            if (!oldFile.getParentFile().exists()){
                oldFile.getParentFile().mkdirs();
            }
            File file2 = new File(newPath);
            file.transferTo(file2);
            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败");
        }
    }

    /**
     * 文件上传 多个
     */
    public static List<String> uploads( List<MultipartFile> files){
        List<String> result = new ArrayList<>();
        for (MultipartFile file : files){
            result.add(upload(file));
        }
        return result;
    }

    /**
     * 文件下载
     */
    public static void download(String fileName, HttpServletResponse response) {
        OutputStream toClient = null;
        try {
            // path是指欲下载的文件的路径。
            File file = new File(filePath + fileName);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(filePath + fileName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 如果是图片就预览，不是图片就下载
            if (fileName.endsWith("jpg") || fileName.endsWith("png") || fileName.endsWith("gif")){
                response.setHeader("Content-Type", "image/jpeg");
            }else {
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
                response.setContentType("application/octet-stream; charset=utf-8");
            }
            response.addHeader("Content-Length", "" + file.length());
            response.setHeader("Access-Control-Allow-Origin", "*");
            toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);

        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try {
                toClient.flush();
                toClient.close();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("文件上传关闭流失败！");
            }
        }
    }

    /**
     * 从网络中下载文件
     *
     * @param filePath 网络文件地址
     */
    public static void downloadByNetwork(String filePath,HttpServletResponse response){
        InputStream inputStream = null;
        try {
            URL url = new URL(filePath);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            int httpRequestCode = con.getResponseCode();
            if(httpRequestCode != 200){
                throw new Exception("文件下载错误，错误码："+httpRequestCode);
            }
            // 将文件读入文件流
            inputStream = con.getInputStream();
            // 下载文件的名称
            String finalFileName = UUID.randomUUID().toString();
            // 设置HTTP响应头
            // 重置 响应头
            response.reset();
            // 告知浏览器下载文件，而不是直接打开，浏览器默认为打开
            response.setContentType(URLConnection.getFileNameMap().getContentTypeFor(filePath));
            response.addHeader("Content-Disposition", "attachment;filename=" + finalFileName);
            // 循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }

        }catch (Exception e){
            throw new RuntimeException("从网络中下载文件失败");
        }finally {
            try {
                inputStream.close();
                response.getOutputStream().close();
            }catch (Exception e){
                throw new RuntimeException("文件流关闭失败");
            }
        }
    }
}

