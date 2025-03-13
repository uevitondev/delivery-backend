package com.uevitondev.deliverybackend.config.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AwsS3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3Service.class);

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucket.name}")
    private String awsS3BucketName;

    public AwsS3Service(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadFileAndReturnUrl(String fileName, MultipartFile multipartFile) throws IOException {
        var objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        var keyName = fileName.replace(" ", "_").toLowerCase();
        amazonS3Client.putObject(awsS3BucketName, keyName, multipartFile.getInputStream(), new ObjectMetadata());
        LOGGER.info("Upload File AWS-S3 Success!");
        return "https://" + awsS3BucketName + ".s3.sa-east-1.amazonaws.com/" + keyName;

    }


}
