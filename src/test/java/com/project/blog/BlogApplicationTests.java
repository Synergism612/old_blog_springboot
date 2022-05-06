package com.project.blog;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {
        new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ").format(new Date());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ").format(new Date()));
    }


    @Test
    void testUpdateOss() {
        // Endpoint以北京为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "***";
        String accessKeySecret = "***";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = null;
        try {
        //文件地址
            inputStream = new FileInputStream("D:\\神和五律\\我的图片\\爱好类\\蓝白\\9df377b65c1c676ed5ca86cb741f7b97.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //参数（目标库名，文件名，文件）
        ossClient.putObject("synergism-images", "blogImages"+"/"+"2020-4-29-test-2.jpg", inputStream);

        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL，第一个参数为bucketName，第二个参数key为上传的文件路径名称，第三个为过期时间
        URL url = ossClient.generatePresignedUrl("synergism-images" ,"blogImages"+"2020-4-29-test-2.jpg", expiration);
        System.out.println(url);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
