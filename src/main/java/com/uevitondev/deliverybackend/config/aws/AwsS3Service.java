package com.uevitondev.deliverybackend.config.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AwsS3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3Service.class);

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucket.name}")
    private String awsS3BucketName;

    public AwsS3Service(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadS3FileAndReturnUrl(MultipartFile multipartFile) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {

            var fileName  = (ThreadLocalRandom.current().nextInt(100000, 1000000) + " " + multipartFile.getOriginalFilename() );
            var keyName = fileName.replace(" ", "_").toLowerCase();
            PutObjectRequest putObjectRequest = new PutObjectRequest(awsS3BucketName,"nome-do-arquivo",  new File(Objects.requireNonNull(multipartFile.getOriginalFilename())));
            amazonS3Client.putObject(putObjectRequest);
            LOGGER.info("Upload File AWS-S3 Success!");
            return "https://" + awsS3BucketName + ".s3.sa-east-1.amazonaws.com/" + keyName;
        }

    }


}
