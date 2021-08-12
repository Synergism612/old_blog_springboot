package com.project.blog.oss;

import lombok.Data;

@Data
public class ossInformation {
    // Endpoint以北京为例，其它Region请按实际情况填写。
    private String endpoint = "oss-cn-beijing.aliyuncs.com";
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
    private String accessKeyId = "LTAI5tNrMXna1v2i7DuudJAu";
    private String accessKeySecret = "7JDtOUw9Gp3b0cqLvlmZztSW8JSZKH";
    //目标仓库名
    private String bucketName = "synergism-images";
    //目标文件夹名
    private String directoryName = "blogUserImages";
}
