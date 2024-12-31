package org.example.user.utils;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/*
* 配置参数注入
* 该工具类提供上传头像的方法
* 四个参数用于连接oss
* */
@ConfigurationProperties(prefix="aliyun.oss")
@Data
@Component
public class AliOSSUtils {

    //oss连接参数
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String imageUpLoad(MultipartFile image) throws IOException {
        OSS ossClient=new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        InputStream inputStream=image.getInputStream();
        String originName=image.getOriginalFilename();
        String uniqueName= UUID.randomUUID().toString()+originName.substring(originName.lastIndexOf("."));
        ossClient.putObject(bucketName,uniqueName,inputStream);
        String url=endpoint.split("//")[0]+"//"+bucketName+"."+endpoint.split("//")[1]+"/"+uniqueName;
        ossClient.shutdown();
        return url;
    }
}
