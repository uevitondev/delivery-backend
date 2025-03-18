package com.uevitondev.deliverybackend.config.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AwsS3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3Service.class);

    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String awsS3BucketName;

    public AwsS3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFileAndReturnUrl(String fileName, MultipartFile multipartFile) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {

            var keyName = fileName.replace(" ", "_").toLowerCase();
            var requestBody = RequestBody.fromInputStream(inputStream, multipartFile.getSize());
            var putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsS3BucketName)
                    .key(keyName)
                    .build();
            s3Client.putObject(putObjectRequest, requestBody);

            LOGGER.info("Upload File AWS-S3 Success!");
            return "https://" + awsS3BucketName + ".s3.sa-east-1.amazonaws.com/" + keyName;
        }

    }


}
