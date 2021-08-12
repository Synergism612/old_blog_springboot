package com.project.blog.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class updateOss {
    private ossInformation information = new ossInformation();

    //保存图片
    public String saveOssImg(MultipartFile multipartFile, String fileType, String userphone) {
        //获取用户参数
        String endpoint = "http://" + information.getEndpoint();
        //用库路径+用户手机号+当前时间+文件后缀 拼接文件目录以及文件名
        String fileName = information.getDirectoryName() + "/" + userphone + "-" + new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss").format(new Date()) + fileType;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, information.getAccessKeyId(), information.getAccessKeySecret());

        // 上传文件流。
        InputStream inputStream = null;
        try {
            //文件地址
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //参数（目标库名，文件名，文件）
        ossClient.putObject(information.getBucketName(), fileName, inputStream);

        //拼接url
        String url = "https://" + information.getBucketName() + "." + information.getEndpoint() + "/" + fileName;
        ossClient.shutdown();
        return url;
    }

    //删除图片
    public void deleteOssImg(String url) {
        //获取用户参数
        String endpoint = "http://" + information.getEndpoint();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, information.getAccessKeyId(), information.getAccessKeySecret());

        // 删除文件 使用url截取文件名
        ossClient.deleteObject(information.getBucketName(), information.getDirectoryName()+url.substring(url.lastIndexOf("/")));

        // 关闭OSSClient。
        ossClient.shutdown();

    }
}
